# Fixes Bookkeeping (review artifact — UNTRACKED, never `git add`)

Per-fix evidence: symptom, verbatim original error/stack, reproduce steps, pinned root cause, fix.
Kept out of commit messages by design.

---

## e2e82a2 — Guard `_dropTable` against a blank table name (UNDER REVIEW — root cause NOT yet proven)

**Commit:** `e2e82a22df1415ad39eba592aa268643375e7c32` (LPD-X, shuyangzhou, Wed Jul 29 2026)
Body claims: "deleteObjectDefinition calls _dropTable for the extension and localization tables, whose names are blank for object definitions that do not have them, producing the invalid SQL DROP_TABLE_IF_EXISTS()".

### Symptom (per memory commit-to-test-failure-map)

`deleteObjectDefinition` on a leftover object definition emits `DROP_TABLE_IF_EXISTS()` (empty parens) → `SQLSyntaxErrorException` → ERROR-log noise during DataGuard teardown → fails `PortalLogAssertorTest` ("zero ERROR" rule).

### Reproduced failure MODE (proves the crash path, NOT the origin) — 2026-08-01
- Un-guarded `_dropTable` redeployed; integration test published an approved custom object definition, forced `setDBTableName("")` in memory, called `deleteObjectDefinition`.
- Verbatim stack (MySQL):

```
com.liferay.portal.kernel.exception.SystemException: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'DROP_TABLE_IF_EXISTS()' at line 1
	at ...ObjectDefinitionLocalServiceBaseImpl.runSQL(...:571)
	at ...ObjectDefinitionLocalServiceImpl.runSQL(...:1417)
	at ...ObjectDefinitionLocalServiceImpl._dropTable(...:2267)   <-- un-guarded body
	at ...ObjectDefinitionLocalServiceImpl.deleteObjectDefinition(...:709)  <-- _dropTable(objectDefinition.getDBTableName())
Caused by: java.sql.SQLSyntaxErrorException: ... near 'DROP_TABLE_IF_EXISTS()' ...
	at com.mysql.cj.jdbc.StatementImpl.executeUpdate(StatementImpl.java:1253)
	at com.liferay.portal.dao.db.BaseDB.runSQL(BaseDB.java:766)
```

- **This repro is INVALID as a root-cause proof** (user's own words): it forces the blank value; it does not show how a persisted row gets a blank `dbTableName`.

### Commit-message claim is WRONG
- `ObjectDefinitionImpl.getExtensionDBTableName()` = `getDBTableName() + "_x"` (or unmodifiable-system `+ "_x_" + companyId`) — ALWAYS appends, never blank.
- `getLocalizationDBTableName()` = `getDBTableName() + "_l"` — ALWAYS appends.
- If `getDBTableName()` were blank, extension="_x", localization="_l" — both VALID identifiers, no empty parens. **Only** `_dropTable(getDBTableName())` at line 709 (approved branch) can emit empty parens.
- `_getDBTableName(...)` (impl :2320) can NEVER return blank: passthrough if caller-supplied non-null; else `name` for unmodifiable-system; else `prefix + companyId + "_" + shortName`.

### Creation path that DOES persist a blank dbTableName (FOUND 2026-08-01)
- `getOrAddEmptyObjectDefinition` (impl :980) → `_emptyModelManager.getOrAddEmptyModel(...)` → 6-arg `_addObjectDefinition` (impl :1446).
- That 6-arg overload does `objectDefinitionPersistence.create(...)`, sets `status = STATUS_EMPTY`, name/scope/etc., and **never calls setDBTableName** → row persists with blank `dbTableName`.
- Real callers of `getOrAddEmptyObjectDefinition`: `ObjectDefinitionResourceImpl:1256`, `ObjectRelationshipResourceImpl:341`, `ObjectActionLocalServiceImpl:910`.

### ROOT CAUSE — PROVEN 2026-08-01 (organic path)

**How the blank `dbTableName` column is created (the part not visible in normal creation code):**
`getOrAddEmptyObjectDefinition` (impl :980) → `EmptyModelManagerImpl.getOrAddEmptyModel` (export-import-report-service :48). That method only invokes the empty-model supplier (the 6-arg `_addObjectDefinition` :1446 → blank `dbTableName`, `STATUS_EMPTY`) when **`LazyReferencingThreadLocal.isEnabled()`** is true — i.e. during **LAR export/import lazy referencing**. Otherwise it does a throwing get. That's why the blank creation is invisible in the ordinary object-definition creation path — it lives in the export/import staged-model machinery. Trigger site: `ObjectDefinitionResourceImpl._addObjectRelationship` (:1233-1285) wraps `getOrAddEmptyObjectDefinition` in `LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)` when applying an imported definition whose relationship references another def by ERC.

**Does publishing a blank empty-model def leave it APPROVED+blank? — EMPIRICALLY: NO (repro4, un-guarded).**
Hypothesis was: `_publishObjectDefinition` sets `STATUS_APPROVED` (:2537) + update (:2539), then DDL (`_createLocalizationTable` :2567 / `_createTable` :2570); MySQL implicitly commits before a CREATE, so the approved write would survive the CREATE's failure. **Tested and refuted:** an empty-model def has blank names for BOTH its main table AND its PK object-field DB column, so the FIRST create-table DDL is itself malformed and fails — `_createLocalizationTable` emits `create table _l ( bigint not null, languageId ..., <field> varchar(280), prima...)` (blank leading PK column) → syntax error at the FIRST DDL, before any successful DDL commits the status. The transaction rolls back; the def stays `STATUS_EMPTY`. Verified: repro4 ran un-guarded; DataGuard's teardown deletion of the blank empty def (id 76754) produced **NO** `DROP_TABLE_IF_EXISTS()` — proving it was `STATUS_EMPTY` (approved branch never taken), and that an empty-model def deletes cleanly (skips :685 and :688). So the MySQL-implicit-commit path CANNOT be triggered by an empty-model def (all its names are blank; no successful DDL ever precedes the failing one). The workflow synthesizer's "unreachable via publish" holds.

**Net root-cause conclusion (VERIFIED — no PRODUCT path):** `approved + blank dbTableName` is **not organically reachable** through the object-service public API: (a) normal add/update always derive a non-blank name before approving; (b) empty-model defs are `STATUS_EMPTY` and delete cleanly; (c) publishing an empty-model def rolls back (first DDL malformed). So there is no product creation path for a blank table — the user's instinct was correct.

### THE ACTUAL REPRODUCE — CONFIRMED from an existing test (2026-08-01, un-guarded + instrumented full object-test run)

**Offender:** `com.liferay.object.internal.upgrade.v10_19_0.ObjectDefinitionUpgradeProcessTest.testUpgrade`.

It raw-SQL-inserts a **bare** ObjectDefinition row:

```java
int objectDefinitionId = RandomTestUtil.randomInt();          // e.g. 154088757
_db.runSQL(
    "insert into ObjectDefinition (objectDefinitionId, rootObjectDefinitionId) values (" +
        objectDefinitionId + ", " + rootObjectDefinitionId + ")");
_runUpgrade();
// ...asserts the upgrade migrated rootObjectDefinitionId -> setting & dropped the column...
// NEVER deletes the inserted row
```

Only `objectDefinitionId` + `rootObjectDefinitionId` are set, so every other column defaults: `companyId=0`, `name/externalReferenceCode/dbTableName` blank, and **`status=0` — the default int, which equals `WorkflowConstants.STATUS_APPROVED`**. The test leaves the row; **DataGuard's `@AfterClass` teardown deletes it**:
`DataGuardTestRule.afterClass:94 -> _autoDeleteAndAssert:281 -> _autoDeleteLeftovers:396 -> smartDelete:184 -> deleteObjectDefinition`. Because `isApproved()` is true (status==0==APPROVED, an accident of the default value), delete takes the `else if (isApproved())` branch at :688 and reaches `_dropTable(getDBTableName())` at :709 with a blank name -> `DROP_TABLE_IF_EXISTS()` -> SQLSyntaxErrorException -> ERROR log -> fails PortalLogAssertorTest.

**Verbatim marker (un-guarded diagnostic at :709 + DataGuard stack):**

```
ERROR [ObjectDefinitionLocalServiceImpl:710] ##BLANKTABLE## objectDefinitionId=154088757 name= erc= status=0 companyId=0
  ObjectDefinitionLocalServiceImpl.deleteObjectDefinition:717   (_dropTable call)
  DataGuardTestRuleUtil.smartDelete:184
  DataGuardTestRuleUtil._autoDeleteLeftovers:396
  DataGuardTestRuleUtil._autoDeleteAndAssert:281
  DataGuardTestRuleUtil.afterClass:94
```

Caught at test #~250 of the full object-test run; the monitor killed the run on first sight. Sibling upgrade tests that raw-insert a bare ObjectDefinition the same way (other `ObjectDefinitionUpgradeProcessTest` versions) are equally capable of triggering it.

**Fix options (the real bug is the TEST leaving a status=0 leftover, not production):**

1. Best: the upgrade test deletes its inserted row (`_db.runSQL("delete from ObjectDefinition where objectDefinitionId = " + objectDefinitionId)` in a finally), so DataGuard never deletes a bare row. Applies to every sibling with the same pattern.

1. The `e2e82a2` `_dropTable` `Validator.isNull` guard is then legitimate defense-in-depth (a `deleteObjectDefinition` should not emit `DROP_TABLE_IF_EXISTS()` for a row with no table), but its commit message must be corrected to the real cause (bare upgrade-test row, default status=0 reads as APPROVED) — NOT the extension/localization getters.

### VALID organic reproduction (no forced setDBTableName) — object-test, 2026-08-01

`EmptyModelPublishBlankTableReproTest` (public service APIs only):

1. `try (LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)) { getOrAddEmptyObjectDefinition(erc, companyId, userId, 0, true, SCOPE_COMPANY, false) }` → empty def, `STATUS_EMPTY`, blank dbTableName.

1. `addCustomObjectField(... localized=true ...)` → def still blank.

1. `publishCustomObjectDefinition(userId, id)` → throws.

Verbatim tomcat (catalina.out) output:

```
REPRO step1 empty def: id=69307 status=9 dbTableName=[]
REPRO step2 after localized field: dbTableName=[]
REPRO step3 publish threw:
com.liferay.portal.kernel.exception.SystemException: java.sql.SQLSyntaxErrorException: ... near 'bigint not null, languageId varchar(10) not null, a7uQJJIZK_ varchar(280), prima' at line 1
	at ...ObjectDefinitionLocalServiceImpl._createLocalizationTable(...:2191)
	at ...ObjectDefinitionLocalServiceImpl._publishObjectDefinition(...:2567)
	at ...ObjectDefinitionLocalServiceImpl.publishCustomObjectDefinition(...:1045)
	at ...CMSPermissionsObjectDefinitionLocalServiceWrapper.publishCustomObjectDefinition(...:44)
	at ...EmptyModelPublishBlankTableReproTest...:83
```

The malformed SQL is the localization table (`_l`) create with a blank leading (PK reference) column name — it fails, but the implicit commit already committed `STATUS_APPROVED`. (DataGuard then deleted the leftover def 69307; with the guard restored the delete survives, which is exactly the guard's value.)

### EXISTING-TEST reproduction (what the guard was actually for)

Memory `overnight-dataguard-and-fullrun.md:41` recorded the original: "1 SQL bug: ObjectDefinition→SQLSyntaxErrorException: deleteObjectDefinition→_dropTable runs literal `DROP_TABLE_IF_EXISTS()` ... Rare edge (object definition with no/blank table)" and listed **`ObjectDefinitionExportImportTest (classMethod)`** as a RED test. (That export/import test only reproduces the *creation* of a blank empty-model def, which stays `STATUS_EMPTY` and deletes cleanly — NOT the crash.)

### CONFIRMED OFFENDER (un-guarded + instrumented full object-test run, 2026-08-01, caught ~test #250, killed on first sight)

`com.liferay.object.internal.upgrade.v10_19_0.ObjectDefinitionUpgradeProcessTest.testUpgrade` raw-SQL-inserts a bare row (`insert into ObjectDefinition (objectDefinitionId, rootObjectDefinitionId) values (...)`) — every other column defaults, so `companyId=0`, `dbTableName` blank, `status=0 == STATUS_APPROVED`. It never deletes the row → DataGuard `@AfterClass` deletes it → `isApproved()` true (default 0) → `_dropTable(blank)` at :709.

Verbatim marker + stack:

```
ERROR [ObjectDefinitionLocalServiceImpl:710] ##BLANKTABLE## objectDefinitionId=154088757 name= erc= status=0 companyId=0
  ObjectDefinitionLocalServiceImpl.deleteObjectDefinition:717
  DataGuardTestRuleUtil.smartDelete:184 -> _autoDeleteLeftovers:396 -> _autoDeleteAndAssert:281 -> afterClass:94
```

### FIX APPLIED + VERIFIED (2026-08-01)

- **Guard DROPPED entirely** (the original `e2e82a2`/`6f5230d` guard commit was removed from the branch). A blank table name at `_dropTable` is never supposed to happen, so `_dropTable` stays `runSQL("DROP_TABLE_IF_EXISTS(" + dbTableName + ")")` with no `Validator.isNull` guard — if a blank ever reaches it again, it must explode loudly (a real caller bug), not be quietly swallowed.
- `a87f17f` — the SOLE fix: `v10_19_0.ObjectDefinitionUpgradeProcessTest.testUpgrade` wraps its body in `try/finally` and deletes the inserted `ObjectDefinition` row + derived `ObjectDefinitionSetting`, so DataGuard never deletes a tableless, approved-looking leftover. Confirmed the only blank-table source: only this test raw-inserts a bare ObjectDefinition; `ObjectDefinitionPersistenceTest` uses a random non-blank name and `persistence.remove()` (never `_dropTable`); all service-layer creation derives a non-blank name.
- Verified: redeployed the **un-guarded** object-service + re-ran the fixed test → `testUpgrade PASSED`, **0** `DROP_TABLE_IF_EXISTS()`/`SQLSyntaxError` lines in teardown — proving the test fix alone suffices with no guard.

---

## 7f6c395 → bc6ef78 TIGHTENED: delete only the two NOTIFY_ASSIGNEE actions, not all (2026-08-01)

`testSendNotificationToAssignee` publishes an assignee-field object definition, which auto-wires THREE object actions in `ObjectFieldModelListener.onAfterCreate` (LPD-97905 / 057bb50): `ASSIGN_TO_ME` (`_addAssignToMeObjectAction`), `NOTIFY_ASSIGNEE_ON_AFTER_ADD`, `NOTIFY_ASSIGNEE_ON_AFTER_UPDATE` (`_addNotifyAssigneeObjectActions`). The two NOTIFY_ASSIGNEE ones bind a product-default `TYPE_USER_NOTIFICATION` template, adding a second notification-queue entry on entry-add → `assertNotificationQueueEntrySubject` (expects exactly 1 global entry) failed → cascaded.

**Original 7f6c395** deleted ALL actions via `for (ObjectAction a : objectActionLocalService.getObjectActions(defId)) deleteObjectAction(a)` — blunt: also nukes the unrelated `ASSIGN_TO_ME` and would swallow any future auto-wired action.

**Tightened bc6ef78**: two explicit `fetchObjectAction(defId, NAME_NOTIFY_ASSIGNEE_ON_AFTER_ADD/_UPDATE)` + null-check + `deleteObjectAction`, mirroring the product's own `_deleteNotifyAssigneeObjectActions`. Leaves `ASSIGN_TO_ME` intact. Import `ObjectActionNameConstants` added (object-api, exported, already a test dep).

### VERIFICATION (2026-08-01, /opt server, LPD-97905 auto-wiring confirmed in deployed com.liferay.object.service.jar)

- **Isolated `--tests "*EmailNotificationTypeTest.testSendNotificationToAssignee"` → PASSED** (BUILD SUCCESSFUL 17s). DB `NotificationQueueEntry` count 0 before AND after. This proves (a) the two NOTIFY_ASSIGNEE deletes take effect, and (b) `ASSIGN_TO_ME` does NOT fire a notification on entry-add — else the count would be 2 and the assertion would fail. So leaving ASSIGN_TO_ME is safe; tightened == original in behavior.
- **Whole-class run**: 12/16 FAILED, but the origin was NOT this method. `assertNotificationQueueEntrySubject` fetches ALL queue entries globally (`QueryUtil.ALL_POS`), asserts size==1, then deletes the one — so the first method to throw before its delete leaves a stray entry that poisons every later method's count. Earlier methods failed for unrelated reasons in this deployed env: `testFreeMarkerNotificationWithCommerceOrder` (expected 1 was 0), `testRichTextNotificationWithCommerceOrder` (`CommerceAccountOrdersException`), `testSendNotificationToInheritedRoleUsers`/`...OwnerRole` (`PrincipalException$MustHavePermission: ADD_OBJECT_ENTRY`), `testSendNotificationToSubscribers` (`InvalidFilterException: DepotEntry`). Assignee's failure showed 2 entries: `230433` subject "B2EFtBbk 7SoCe3Ta" = `user1.getFullName()` (assignee's OWN correct single entry) + `230217` subject "uOYYDEnb" = leftover from a prior failed method. Isolated pass confirms cascade, not my change.
- These whole-class failures are pre-existing/environmental (unrelated exception types a notification-object-action edit cannot cause), out of scope for the tightening.

### A/B CONTROL — proven broken in isolation WITHOUT the fix (2026-08-01)

Removed the whole delete block (uncommitted working-tree edit → pre-7f6c395 state: publish def, then directly `addNotificationTemplateObjectAction`), `compileTestIntegrationJava` re-executed, isolated run from a clean queue (DB count 0):

`testSendNotificationToAssignee FAILED` — `1 test completed, 1 failed`, `expected:<1> but was:<2>`. (Liferay testIntegration sets ignoreFailures → BUILD SUCCESSFUL/exit 0 is meaningless; read the per-method FAILED line.) The two entries name the mechanism:
- `{notificationQueueEntryId 235632, notificationTemplateId 235604, subject "You have a new assignment.", type "userNotification", body ""}` — the auto-wired `NOTIFY_ASSIGNEE_ON_AFTER_ADD` (LPD-97905 `addAssigneeNotificationTemplate` → `TYPE_USER_NOTIFICATION`, `you-have-a-new-assignment` key).
- `{notificationQueueEntryId 235635, notificationTemplateId 235613, subject "J9uu13Od 2dlPPenc" (=user1.getFullName()), type "email"}` — the test's own email action.

Only 2 entries (not 3) → `ASSIGN_TO_ME` never fires on add, reconfirming it is safe to leave in.

**A/B summary (both isolated, clean queue):** WITHOUT fix → FAIL, 2 entries (auto-wired userNotification + test email). WITH tightened fix → PASS, 1 entry (test email). Fix necessary and sufficient. Working tree restored to committed bc6ef78; DataGuard swept queue back to 0.
================================================================================

## OVERNIGHT DRIVE 2026-08-01/02 — run all object integration tests, fix all failures + WARN/ERROR logs

Branch object-stabilization-aggregate-2 @ 1f66c57 (rebased onto upstream/master 9e74ff4).
Modules: object-test, object-rest-test, object-admin-rest-test (+ salesforce/sugarcrm CRM Assume-skip).
Setup: ant all (rebuild HEAD) -> wipe DB(lportal_pim)/osgi-state/ES -> fresh boot -> run serial (DataGuard port 42763 single-instance) -> capture failures + liferay.log WARN/ERROR -> triage/fix with repro -> re-run.

--------------------------------------------------------------------------------

### CLUSTER 1 (15 failures): _reindex null-indexer NPE — REAL, deterministic on fresh env

Failing (fresh ant-all + wiped DB, upstream/master 9e74ff4): ObjectEntryFolderLocalServiceTest(8 all), ObjectEntryFolderServiceTest.testAddObjectEntryFolder, ObjectEntryLocalServiceTest(testAdd/UpdateObjectEntryWithAttachmentObjectField, testAddObjectEntryWithDomain), ObjectFieldLocalServiceTest.testObjectFieldSettings, ObjectEntryInfoItemFieldValuesUpdaterTest.testUpdateFromInfoItemFieldValuesWithAttachmentField, ObjectEntrySharingEntryDropdownItemContributorTest.testGetSharingEntryDropdownItems.
VERBATIM STACK:
  java.lang.NullPointerException: Cannot invoke "com.liferay.portal.kernel.search.Indexer.reindex(Object)" because "indexer" is null
    at ObjectEntryLocalServiceImpl._reindex:6353
    at ObjectEntryLocalServiceImpl.updateStatus:2436
    at ObjectEntryWorkflowHandler.updateStatus:210/47
    at ObjectEntryLocalServiceImpl._startWorkflowInstance:6934/6915
    at ObjectEntryLocalServiceImpl.addObjectEntry:538
ROOT CAUSE (CORRECTED — earlier c448b62 had a FALSE "propagates into whichever test made the first request" mechanism; InitFilter runs sync() on the readiness probe, not a test): the def's Indexer is registered per-definition by ObjectDefinitionDeployerImpl during deploy; there is a window where the def is usable (addObjectEntry) but its indexer is not yet registered. addObjectEntry -> workflow auto-approve -> updateStatus -> _reindex -> getIndexer(def.className)==null -> NPE. The SIBLING guards already exist: ObjectFolderModelListener:91 + ObjectDefinitionModelListener:64 both use IndexerRegistryUtil.nullSafeGetIndexer — _reindex was the ONE place using unsafe getIndexer. CMS-lifting base (LPD-99210, deferred provisioning + more system-object deploys) widened the window -> deterministic (15/15 every run). NOT environmental (reproduces on fully clean ant-all+wiped-DB env), correcting the earlier "environmental/CI#12391 clean" claim.
FIX: ObjectEntryLocalServiceImpl._reindex :6346 getIndexer -> nullSafeGetIndexer (consistent with the 2 sibling listeners; reindex is best-effort — the entry is reindexed once the indexer settles + by ObjectEntryFullReindex). VERIFY: deploy + re-run the 15 -> expect all PASS.

### CLUSTER 1 VERIFIED (rerun 2026-08-02T05:22:09Z): 103 STARTED / 103 PASSED / 0 FAILED across the 6 classes; zero null-indexer NPE. Fix confirmed.

### CLUSTER 2 (1 failure): AccountEntry getResourceName UnsupportedOperationException — PRE-EXISTING CMS-lift (LPD-99210) regression, NOT this branch

Failing: SystemObjectEntryInfoItemFieldValuesProviderTest.testSystemObjectEntryInfoItemFieldValuesProvider (fails in setUp:86).
VERBATIM STACK:
  java.lang.UnsupportedOperationException
    at ObjectDefinitionImpl.getResourceName:154 (throws when isUnmodifiableSystemObject())
    at ObjectDefinitionResourcePermissionUtil._readDocument:311
    at ObjectDefinitionResourcePermissionUtil.populateResourceActions:66
    at ObjectFieldLocalServiceImpl._addObjectField:1036
    at ObjectFieldLocalServiceImpl.addCustomObjectField:158
    at ObjectFieldUtil.addCustomObjectField:71
    at SystemObjectEntryInfoItemFieldValuesProviderTest.setUp:86
WHAT THE TEST DOES: setUp adds a CUSTOM ATTACHMENT field to the AccountEntry system object definition. AccountEntry is NOT in _allowedModifiableSystemObjectDefinitionNames and the DB row shows modifiable=0 (isUnmodifiableSystemObject()==true; verified: SELECT ... FROM ObjectDefinition WHERE name='AccountEntry' -> modifiable=0, system_=1; same for User/Organization).
ROOT CAUSE (PINNED): a57a60eb394d0 "LPD-99210 Remove the LPD-17564 feature flag checks" (Mikel Lorza, Thu Jul 23 2026), hunk @@ -1032,9 +1029,7 @@ of ObjectFieldLocalServiceImpl._addObjectField. BEFORE: the attachment resource-permission block was gated
    if (FeatureFlagManagerUtil.isEnabled(objectField.getCompanyId(), "LPD-17564") &&
        objectField.compareBusinessType(BUSINESS_TYPE_ATTACHMENT)) { populateResourceActions(...); ... }
so with LPD-17564 OFF (pre-lift/CI baseline) the block was SKIPPED and getResourceName was never called -> test passed. AFTER: the flag check was removed, so the block runs unconditionally for every attachment field. For an UNMODIFIABLE system object (AccountEntry), populateResourceActions -> _readDocument -> getResourceName throws UnsupportedOperationException.
EVIDENCE IT IS BASELINE (not this branch): Testray ci:test:object shows this exact failure labeled "Common" (present on the comparison baseline) in builds 505272717, 505192174, 505192389; and the test class is NOT modified on this branch (empty `git log <mergebase>..HEAD -- *SystemObjectEntryInfoItemFieldValuesProviderTest.java`).
DECISION: OUT OF SCOPE for the object-test-stabilization branch. This is a Brian-side LPD-99210 (CMS-lift) product regression owned by its author; the product must decide between (a) AccountEntry should be modifiable, (b) guard the attachment block with !isUnmodifiableSystemObject() before populateResourceActions, or (c) reject custom fields on unmodifiable system objects. Do NOT hack a product fix into this branch. Surface to user with pinned root cause.

### CLUSTER 1 root-cause commit (pinned):
- CREATED WRONG: 5557ecbbd742a "LPS-135650 call indexer directly and avoid annotation" (Marco Leo, 2021-07-27) introduced the direct `IndexerRegistryUtil.getIndexer(objectDefinition.getClassName())` in `_reindex`, replacing the `@Indexable` annotation. getIndexer returns null when the object-entry indexer for that def is not yet registered.
- LATENT until MADE DETERMINISTIC by the CMS-lifted base (LPD-99210 cluster): the per-definition indexer registration window widened, so `addObjectEntry` -> workflow auto-approve -> `updateStatus:2436` -> `_reindex:6353` reliably runs before `ObjectDefinitionDeployerImpl` finishes registering the indexer -> `indexer` null -> NPE (15/15 deterministic on fresh env).
- CONSISTENCY: this SAME class already uses `IndexerRegistryUtil.nullSafeGetIndexer` at line 2326 (in updateStatus), and siblings ObjectFolderModelListener:91 / ObjectDefinitionModelListener:64 use it too. `_reindex` at 6346 was the lone outlier still using the throwing getIndexer.
- SCOPE NOTE: a sibling call at line 932 (the delete path in deleteObjectEntry) also uses plain getIndexer, but it runs at teardown/delete time (after the indexer has settled) and was NOT in any of the 15 failure stacks, so it is left unchanged (documented latent risk, not a verified failure).

### CLUSTER 1 REASSESSMENT (2026-08-01 night): nullSafeGetIndexer is a COVER-UP, NOT accepted as the fix

User challenge: prove the entity is indexed by end of transaction when nullSafe fires, else it's a cover-up. PROVEN it is NOT indexed:
- DummyIndexer.reindex(Object) is an empty no-op (DummyIndexer.java:161-162); _reindex calls it once (ObjectEntryLocalServiceImpl:6353), no retry.
- NO catch-up on deploy/registration path (workflow wf_9851906b-d3a verified: ObjectDefinitionDeployerImpl._deploy:319-391 only registerService's contributors+ModelSearchConfigurator; ModelSearchConfiguratorServiceTrackerCustomizer.addingService registers the Indexer without sweeping rows; no lifecycle listener reindexes entries).
- So an entry added while the indexer is null is committed to DB but absent from search indefinitely (until edit/full reindex) = silent DB-vs-index divergence. Pre-fix NPE aborted+rolled back the add (consistent failure); nullSafe converts it to a silent inconsistency.
DECISION: do NOT commit the nullSafe change. Keep it in the working tree ONLY so the full re-run can surface OTHER failures. Real fix (ordering / bounded catch-up reindex on indexer registration) + ownership TBD after empirical repro. Full facts: memory nullsafe-indexer-coverup-investigation.md.
OPEN: why is the indexer null at _reindex given registration is synchronous (no aries-dsl) and tests run post-startup? Repro plan in the memory file.

### FULL RE-RUN (fullrun2) other failures observed (to catalog after completion):
- ObjectDLFileEntryModelResourcePermissionConfiguratorTest.testContains: "ObjectDefinition is not mapped" (Hibernate/QuerySyntax) — pollution/ordering flavor, NEW vs first full run.
- ObjectDefinitionSettingUpgradeProcessTest.testUpgrade: "Table 'L_..._DataSet' already exists" (batch-engine leftover) — pollution flavor, NEW vs first full run.
- SystemObjectEntryInfoItemFieldValuesProviderTest: cluster-2 getResourceName (known, out of scope).

### EMPIRICAL RESULT 3 — FRESH-ENV PROBE RUN (2026-08-02): cover-up CONFIRMED live

Fresh boot (clean lportal_pim 511 tables). Readiness GET triggered BundleSiteInitializer (cms/dsr) on the test-executor-thread = CMS-lift deferred provisioning on first request. Full object-test w/ nullSafe+probe: 1285 passed / 1 failed (only cluster-2) / probe fired 46x.
- ALL 46 hits: becameAvailableAfterMs=-1 (indexer null for full 3s poll every time) => NOT transient; in-line wait/retry won't fix.
- ALL 46 hits: ONE def com.liferay.object.model.ObjectDefinition#S5M4 (23 entryIds x2), spanning ~16min = its whole test lifetime; its object-entry indexer NEVER registered. Other defs indexed fine.
- The would-be-15 null-indexer tests PASSED (nullSafe swallowed) while 23 entries were left permanently unindexed => cover-up demonstrated.
- Fresh env had ONLY cluster-2 failing => the 3 dirty-env failures (ObjectDefinition-not-mapped / DataSet-table-exists / concurrent OptimisticLock) are CONFIRMED pollution.
FIX DIRECTION: (D) catch-up reindex when the def's indexer registers (reuse ObjectEntryModelIndexerWriterContributor.getIndexableActionableDynamicQuery(objectDefinitionId)+DefaultIndexer.reindex), or fix why S5M4's ModelSearchConfigurator->Indexer conversion never completes (def deployed before PORTLETS_INITIALIZED => customizer inactive). Likely LPD-99210 owner. Probe reverted; nullSafe kept in WT (uncommitted) pending the real-fix decision tomorrow.

### object-rest-test (warm env, 2026-08-02): 334 passed / 3 failed — all pre-existing CI-baseline ("Common")
- testAddObjectEntryWithMissingObjectEntryFolderReference (DefaultObjectEntryManagerImplTest): ModelListenerException NoSuchGroupException key 0. Testray 3 Common / 2 Unique => baseline.
- testToObjectEntry (DefaultObjectEntryManagerImplTest): AssertionError "get-by-scope expected:<{}> but was:<null>". Testray 3 Common / 2 Unique => baseline. (NOT related to our LPD-100431 get-by-scope work, which was ObjectEntryRelatedObjectsResourceTest.)
- testPutByExternalReferenceCodeMultipleOneToManyRelationships (ObjectEntryResourceTest): AssertionError 200!=404. Testray 1 Common / 4 Unique => mostly PR/flaky, 1 baseline. Companion ERROR: TransactionCallbackUtil NoSuchObjectEntryFolderException {externalReferenceCode=L_CONTENTS} (CMS content folder).
- aries-DSL hang test testGetObjectEntryUnsafeSuppliers PASSED (no hang). 1 ERROR [TransactionCallbackUtil] tied to failure #3.
CONCLUSION: no NEW object-rest-test regressions from this branch; failures track the known object-rest-test baseline (see memory object-rest-test-baseline / ci-objectentryresourcetest-rootcause). object-admin-rest-test running next.

### object-admin-rest-test (warm env, 2026-08-02): 239 passed / 0 FAILED / 0 WARN-ERROR. CLEAN.

## OVERNIGHT 8/2 FINAL COVERAGE SUMMARY (all object test modules)
- object-test (FRESH env, nullSafe deployed): 1285 pass / 1 fail = cluster-2 getResourceName ONLY (pre-existing CMS-lift LPD-99210, out of scope). 15 null-indexer NPEs swallowed by nullSafe (COVER-UP, proven; real fix pending user). Dirty-env-only failures (ObjectDefinition-not-mapped, DataSet-table-exists, concurrent OptimisticLock) = pollution, absent on fresh env.
- object-rest-test (warm): 334 pass / 3 fail = all pre-existing CI-baseline ("Common"); no new branch regressions; aries-DSL hang test passed.
- object-admin-rest-test (warm): 239 pass / 0 fail. Clean.
NET: zero NEW branch-caused failures. Real open item = the null-indexer cover-up needs a real fix (catch-up reindex on indexer registration) — user decision tomorrow. Code state: one-line nullSafe in WT UNCOMMITTED; probe reverted; nothing committed.

## DEFINITIVE ROOT CAUSE — null-indexer cluster (2026-08-02), via write-side probe + git bisect

NOT a search race, NOT cache pollution, NOT product. Chain (confirmed by code + runtime log 18:07:35 on test-executor-thread + git):

1. REGRESSION COMMIT: a57a60eb394d0 "LPD-99210 Remove the LPD-17564 feature flag checks" (Mikel Lorza, Thu Jul 23 2026) removed the FeatureFlagManagerUtil.isEnabled(...,"LPD-17564") gate from CMSObjectEntryFolderDepotEntryLocalServiceWrapper.addDepotEntry -> _addCMSDefaultPermissions now runs UNCONDITIONALLY for every TYPE_SPACE depot entry. Pre-lift LPD-17564 was OFF in tests so CMSDefaultPermission was never exercised. (Same commit as the cluster-2 getResourceName regression.)

1. So CMSDefaultPermission (CMS system obj, batch-imported enableIndexSearch=false, NO object-entry indexer) is now provisioned + gets entries in tests.

1. CLOBBER: object/internal/upgrade/v9_2_0/test/ObjectDefinitionUpgradeProcessTest (a PLAIN Arquillian test, NOT a DB-partition test) directly runs the ancient v9_2_0.ObjectDefinitionUpgradeProcess (LPD-24564): doUpgrade = runSQL("update ObjectDefinition set enableIndexSearch = [$TRUE$]") -- UNSCOPED raw SQL over ALL rows/all companies (bypasses ORM updateImpl/setters -> why ORM probes never caught it; does not bump mvcc). setUpClass publishes ONE def enableIndexSearch=false; testUpgrade runs the blanket UPDATE (flips EVERY pre-existing def incl the default company's CMSDefaultPermission false->1, verified DB), asserts its own def flipped; tearDown deletes ONLY its own def -> every other def left clobbered true. (NOT v9_2_2.SchemaUpgradeProcessTest -- that only opens a connection + runs the single v9_2_2 view step, never v9_2_0.)

1. A later test adds a CMSDefaultPermission entry (depot add) -> _reindex reads enableIndexSearch=true -> getIndexer null (no indexer for it) -> NPE (the 15 null-indexer failures).

1. FLAKY = pure cross-test ordering (partition-upgrade test before a CMSDefaultPermission-reindexing test) -> only the long full run reproduces.
WHY v9_2_0 is not the culprit: in prod it is schema-version-gated to DBs far older than any CMS data -> never touches CMS defs. It only bites because an upgrade TEST runs it out of schema context against a fully-provisioned instance that (post-a57a60eb) now has CMS defs.
FIX (DONE): v9_2_0.ObjectDefinitionUpgradeProcessTest.testUpgrade captures every def enableIndexSearch=false (except its own) before the upgrade; tearDown re-fetches + setEnableIndexSearch(false) + updateObjectDefinition. nullSafeGetIndexer REMOVED (user: unnecessary -- restored def makes _reindex early-return). nullSafeGetIndexer in _reindex is a COVER-UP of the downstream NPE (kept uncommitted, defensive/optional). Probes reverted from all 6 files.

## SEMVER DEFECT — portal-test missing bump for JAXRSWhiteboardTestUtil (2026-08-02, found during brian-base baseline)

Branch commit 4d6f86b (pre-rebase 3029b80) "Start the JAX-RS whiteboard before a test invokes an object endpoint" ADDED a new public class `com.liferay.portal.kernel.test.util.JAXRSWhiteboardTestUtil` (portal-test module) but NEVER committed the required semver bump. Confirmed absent on brian base + on the pre-rebase backup (both packageinfo=10.8.0); no branch commit touches portal-test bnd.bnd/packageinfo.
SYMPTOM (== [[portal-test-rebuild-packageinfo-gotcha]]): ant all's Baseline auto-corrects the working tree (packageinfo 10.8.0->10.9.0, bnd.bnd 32.7.1->32.8.0) AFTER portal-test already deployed at 10.8.0; testIntegration then builds object.test.util requiring com.liferay.portal.kernel.test.util;version="10.9.0" -> UNRESOLVED (deployed exports 10.8.0) -> com.liferay.object.test can't resolve -> ALL 182 object-test tests fail instantly in 57s, 0 XML results. Cascade breaks every *.test.util (site.dsr, commerce.account, friendly.url, layout...).
FIX (for the run): KEEP the 10.9.0 bump (reverting just makes ant all re-bump, since the API addition is real) + re-run ant all so portal-test deploys at 10.9.0 consistently + fresh_boot (clear osgi/state) + re-run. BRANCH FIX (owed): add a "LPD-X Semantic versioning" commit (packageinfo 10.9.0 + bnd.bnd 32.8.0), ideally right after 4d6f86b. CI Baseline would flag this. COMMITTED as 0bd07990afea2 'LPD-X Semantic versioning', inserted right after 4d6f86b via git rebase -i; content of all other commits byte-identical (verified); SF clean.

## AFTERNOON-RUN FAILURE FIXES (2026-08-02) — branch object-stab-fixes, NOT closed out (user reviews tomorrow)

Base brianchandotcom/master b455e4a1; bundle /opt/liferay-portal-trunk (lportal_pim, fresh ant all + fresh boot). Triage via parallel workflow. All 4 = LPD-99210 CMS-lift fallout, NOT our branch; per user "scope is all object tests". Nothing pushed / no PR / no Jira.

### F1 — commit 8351676c "Skip resource action population for an unmodifiable system object attachment field"

ORIGINAL ERROR (object-test): SystemObjectEntryInfoItemFieldValuesProviderTest.testSystemObjectEntryInfoItemFieldValuesProvider FAILS in @Before setUp:86.
STACK: UnsupportedOperationException at ObjectDefinitionImpl.getResourceName:154 <- ObjectDefinitionResourcePermissionUtil._readDocument:311 <- populateResourceActions:66 <- ObjectFieldLocalServiceImpl._addObjectField:1036 (setUp adds a CUSTOM attachment field to the unmodifiable AccountEntry system object).
ROOT: a57a60eb (LPD-99210) removed the LPD-17564 gate wrapping the attachment block; the pre-existing early-return gate at 1027 only skips SYSTEM fields on unmodifiable objects, not CUSTOM fields.
FIX: `if (!objectDefinition.isUnmodifiableSystemObject() && objectField.compareBusinessType(ATTACHMENT))` at 1032. Column still created; delete/update attachment paths use getClassName (unaffected).
VERIFY: A/B = afternoon full run FAILED; after object-service redeploy (hot-refreshed, log STOPPED/STARTED [471]) + `--rerun-tasks` (testIntegration was NO-SOURCE for a product-only change) -> testSystemObjectEntryInfoItemFieldValuesProvider PASSED.

### F2 — ALREADY FIXED ON BRIAN (no commit)

ORIGINAL ERROR (object-test): ObjectEntryServiceTest.testAddObjectEntryHierarchy -> RuntimeException "Unable to get class name from id 235878" (GHOST classNameId, cross-test pollution).
CHAIN: a prior test leaks a portal-instance company; deleteCompany NPEs in NotificationTemplateLocalServiceImpl.deleteNotificationTemplate (null NotificationRecipient, no guard) -> DataGuard smartDelete swallows + raw session.delete bypasses PortalInstances.removeCompany -> pool ghost -> orphaned classNameId reindexed later.
RESOLUTION: the `if (notificationRecipient != null)` null-guard is ALREADY on brian (LPD-100095 / 1d0a59ec, NotificationTemplateLocalServiceImpl:315). deleteCompany no longer NPEs -> DataGuard deletes cleanly -> no ghost -> F2 resolved on current brian. Optional (not done): defensive fetchGroup+guard in CompanyLocalServiceImpl.doDeleteCompany group loop.

### F3 — commit 5fd71b53 "Tolerate a company scoped object entry folder in the CMS default permission lookup"

ORIGINAL ERROR (object-rest-test): DefaultObjectEntryManagerImplTest.testAddObjectEntryWithMissingObjectEntryFolderReference -> ModelListenerException wrapping NoSuchGroupException (group pk 0) from CMS ObjectEntryFolderModelListener.onAfterCreate. Companion ERROR log: TransactionCallbackUtil NoSuchObjectEntryFolderException {externalReferenceCode=L_CONTENTS}.
STACK: CMSDefaultPermissionUtil.getCMSDefaultPermissionJSONObject:211 `GroupLocalServiceUtil.getGroup(folder.getGroupId())` throws for a company-scoped folder (groupId 0).
ROOT: a57a60eb un-gated the CMS ObjectEntryFolderModelListener (LPD-17564 early-return removed).
FIX: `fetchGroup` + `if (group == null) return null;` (caller addCMSDefaultPermissions:59 treats null/empty as no-op; matches the null return at 188). A company-scoped folder has no depot group -> no depot default permissions.
VERIFY: after site-cms api+impl redeploy (hot-refreshed, log STARTED api_4.4.1 [1018] + initializer_1.0.36 [987]) -> testAddObjectEntryWithMissingObjectEntryFolderReference PASSED. Should also clear the tied TransactionCallbackUtil ERROR.

### F4 — commit 02e6c506 "Expect the full object entry action set in testToObjectEntry"

ORIGINAL ERROR (object-rest-test): DefaultObjectEntryManagerImplTest.testToObjectEntry -> AssertionError "get-by-scope expected:<{}> but was:<null>".
MECHANISM: BaseObjectEntryManagerImplTestCase.assertEquals:82 iterates the ACTUAL action keys and asserts each is present in the hardcoded expected map -> the expected map must list every action the manager emits. Whack-a-mole surfaced one missing key per run: get-by-scope -> move-replace -> duplicate -> copy.
ROOT: a57a60eb removed 14 LPD-17564 gates from the object-entry action suppliers in DefaultObjectEntryManagerImpl -> 8 previously-gated actions now unconditional.
FIX (definitive, from the manager's exact toObjectEntry block 3537-3702): expected map = the full 15 keys copy, copy-replace, delete, duplicate, expire, get, get-by-scope, move, move-replace, permissions, replace, restore, share, update, versions. NOT "patch" (it is the _addAction NAME arg for the "update" .put key, not a map key) and NOT subscribe/unsubscribe (putAll-merged only when subscription enabled = off for a plain test object; the 15-key map PASSED, confirming they are not emitted here). Extra expected keys would be harmless (helper checks actual keys only) but the map is kept exact.
VERIFY: 15-key map -> testToObjectEntry PASSED.

BROAD RE-VERIFY (DONE): full DefaultObjectEntryManagerImplTest = 87 PASSED / 0 FAILED (7 skipped) of 94, BUILD SUCCESSFUL 23m41s. Both fixed methods PASSED; 0 ERROR lines, 0 TransactionCallbackUtil / NoSuchObjectEntryFolder / NoSuchGroup in output -> the F3-tied ERROR is gone and no sibling regression from F3/F4.

## Cluster A — pt_BR instance-default-locale pollution (~77 failures) — FIXED

Original symptom: ~77 object-web Playwright failures across many variants; English label locators
(getByText('Author')/'Status'/'Create Date'/'(Copy)') time out because UI renders pt_BR.
Root cause: objectDefinition.spec.ts (1769/1951) sets INSTANCE default language to pt_BR; the
restoreInstanceDefaultLanguage teardown (localizationPagesTest.ts) reset via English-labelled UI
(getByLabel('Default Language'), Control-Panel link 'Localization'/'Language') which itself breaks
in pt_BR -> teardown times out -> instance left pt_BR. Local run shares ONE bundle across all 19
variants (CI isolates per batch) -> pt_BR leaks into every later variant.
Proof: fresh GET / returned GUEST_LANGUAGE_ID=pt_BR on the running bundle.
Fix: harden the teardown to first PATCH the admin user's languageId to en_US via page.request
(/o/headless-admin-user/v1.0/my-user-account -> user-accounts/{id}); the authenticated session makes
the admin UI English regardless of instance default, so the reset navigation+control resolve.
Verify: reset bundle to en_US; ran the 3 polluter tests (1769/1839/1951) -> all PASS and bundle left
en_US (was pt_BR mid-test). Ran 3 downstream victims -> 2 pass; the 3rd (view:320) now fails on a
DIFFERENT cluster (Ascending-sort dropdown intercept), proving the pt_BR label issue is resolved.
File: tests/site-admin-web/main/fixtures/localizationPagesTest.ts. SF/TS checks pass.

----

# F5 — CMS object entry folder cleanup NoSuchObjectEntryFolderException{L_CONTENTS} (commit ffb07d9c500c3)

Original error (verbatim, /opt/liferay-portal-trunk/logs, DataGuard afterClass):
  ERROR [...test-executor-thread][TransactionCallbackUtil:207] Unable to execute transaction callback
  com.liferay.object.exception.NoSuchObjectEntryFolderException: No ObjectEntryFolder exists with the key
    {externalReferenceCode=L_CONTENTS, groupId=<G>, companyId=<liferay.com>}
  ...at ObjectEntryFolderLocalServiceImpl.deleteObjectEntryFolderByExternalReferenceCode(...findByERC_G_C)
  ...at ObjectEntryFolderUtil.deleteObjectEntryFolders(:68)
  ...at CMSObjectEntryFolderDepotEntryLocalServiceWrapper.deleteDepotEntry(:113)
  ...at depot GroupModelListener.lambda$onAfterRemove$1(:60) [commit callback]
  ...at TransactionCallbackUtil._committed -> $Proxy.deleteGroup
  ...at DataGuardTestRuleUtil.smartDelete(:183) -> _autoDeleteLeftovers -> afterClass

Repro (clean env, ran TWICE, both reproduced): fresh DB (lportal_pim recreate) + clear osgi/state + ES;
boot; deploy object-service; `../gradlew :apps:object:object-rest-test:testIntegration --tests
"*ObjectEntryResourceTest"` (138 methods, all PASS, BUILD SUCCESSFUL — the error is LOG noise only, DB ends
clean). Run 1 hit group 77970; run 2 hit group 79196 (created by testGetObjectEntryShareAction:7360).

ROOT (proven by 3 probes: PROBE-ADD service create, PROBE-DEL service delete after the guard, PROBE-PREMOVE
persistence removeImpl). For the failing group: PROBE-ADD present, PROBE-DEL ABSENT, PROBE-PREMOVE ABSENT.
=> the folder was created+committed, then removed by a path bypassing BOTH the service (the "L_" system-folder
guard threw RequiredObjectEntryFolderException BEFORE PROBE-DEL) AND persistence.removeImpl. The only such path
is DataGuardTestRuleUtil.smartDelete's catch(Throwable) fallback (lines 187-223): when the service delete throws,
it raw-deletes via basePersistence.getCurrentSession().delete(model). So at afterClass DataGuard's leftover sweep
collides with itself — it raw-deletes the leaked system folder, then deletes the leaked depot group, whose depot
GroupModelListener.onAfterRemove commit callback (LOAD-BEARING, LPS-122464) re-runs deleteDepotEntry ->
deleteObjectEntryFolders -> findByERC_G_C on the already-raw-deleted folder -> NoSuch. Order-dependent: when
DataGuard reaches the depot entry/group FIRST, the wrapper force-deletes the folder while present (PROBE-DEL fires)
and DataGuard's later folder sweep refetches null and skips — no error. TEST-ONLY; production removes these
folders only through the same wrapper, so it never sees an absent one.

Also verified (workflow): ObjectEntryFolder is group+company scoped, NO objectDefinitionId; deleting an
ObjectDefinition does NOT delete folders; group deletion does NOT cascade folders (orphaned) except the CMS-space
deleteDepotEntry path. No companyId mismatch (both sides liferay.com). Commit callback is NOT removable
(reintroduces re-entrant deleteGroup<->deleteDepotEntry recursion).

Fix (user-chosen among DataGuard-skip-guarded / idempotent-wrapper / fix-leaking-tests): idempotent
deleteObjectEntryFolders — fetch each folder and delete only when present (_deleteObjectEntryFolder), mirroring
_addObjectEntryFolder and DataGuard's own refetch-null tolerance. File:
modules/apps/site/site-cms-site-initializer/.../util/ObjectEntryFolderUtil.java. Probes removed; tree clean.

================================================================================
CompanyModelListenerTest#testOnBeforeRemove (notification-test, ci:test:object scope)
================================================================================

Original failure (fresh clean-env boot, lportal_pim):
  Test asserts at lines 135-139 / 148-152 that L_COMMERCE_ORDER_TEMPLATE exists
  for the DEFAULT company (TestPropsValues.getCompanyId()) -> assertNotNull fails
  because the template row is absent (0 rows).

Root callstack (captured via a temporary PROBE on the swallowed catch in
AddCommerceOrderNotificationPortalInstanceLifecycleListener.portalInstanceRegistered):
  com.liferay.notification.exception.NotificationTemplateExternalReferenceCodeException$MustNotStartWithPrefix:
    The prefix L_ is reserved
    at BaseNotificationType.validateNotificationTemplate(BaseNotificationType.java:240)
    at EmailNotificationType.validateNotificationTemplate(EmailNotificationType.java:486)
    at NotificationTemplateLocalServiceImpl._validate(:517)
    at NotificationTemplateLocalServiceImpl.addNotificationTemplate(:111)
    at ...AddCommerceOrderNotificationPortalInstanceLifecycleListener._verifyCommerceOrderNotificationTemplate(:131)
    at ...portalInstanceRegistered(:57)

Root cause (WHEN/HOW it became wrong):
  BaseNotificationType.validateNotificationTemplate rejects an externalReferenceCode
  that starts with the reserved "L_" prefix
  (NotificationTemplateConstants.EXTERNAL_REFERENCE_CODE_PREFIX_SYSTEM_NOTIFICATION_TEMPLATE)
  unless the template is a system template (isSystem()==true), or an escape applies.
  Introduced by LPD-60998 895b6e534383f ("Throw exception if the notification
  template is not system and the ERC starts with `L_`").
  Originally the whole check was bypassed when FF LPD-66359 (and LPD-17564 / LPD-62272)
  was enabled. LPD-97613 4d0719bb3df41 ("Remove FF") REMOVED the `!isEnabled(LPD-66359)`
  bypass and replaced the throw with a NARROW escape hatch:
      Group group = GroupLocalServiceUtil.fetchGroup(companyId, GroupConstants.DSR);
      if (group == null) { throw MustNotStartWithPrefix(); }
  That escape only covers the DSR site initializer's own L_DSR_* templates (the DSR
  group exists while DSR provisions). The commerce listener provisions
  L_COMMERCE_ORDER_TEMPLATE with "system": false and there is NO DSR site/group in a
  plain portal -> the throw fires. This is NOT a timing issue: in any portal without a
  DSR site the DSR group never exists, so the commerce order notification template can
  NEVER be provisioned after LPD-97613. CompanyModelListenerTest merely detects it.

Fix:
  modules/apps/commerce/commerce-notification-service/src/main/resources/com/liferay/
  commerce/notification/internal/instance/lifecycle/dependencies/notification-template.json
  "system": false -> "system": true.
  The L_ prefix IS the reserved system-notification-template prefix; per LPD-60998's own
  rule an L_-prefixed template must be a system template. Marking it system:true is the
  intended, robust bypass (independent of FF / DSR group / startup ordering) and removes
  the nonsensical commerce<->DSR coupling. Merchants keep subject/body/recipient
  editability; only name/from are protected and the template becomes non-deletable
  (NotificationTemplateResourceImpl:332, NotificationTemplateLocalServiceImpl:376) -
  appropriate for a Liferay-provisioned system default.

Verification: fresh-DB boot with fixed jar -> L_COMMERCE_ORDER_TEMPLATE provisioned for
default company, zero "prefix L_ is reserved" errors; CompanyModelListenerTest passes.
Temporary PROBE instrumentation on the listener catch was reverted before commit.

-------- CORRECTION/COMPLETE (two guards, not one) --------
The first probe exposed only Rule A. A second comprehensive probe (logging each decision
point + the swallowed exception at ERROR, then triggering the listener) revealed that
system:true bypasses Rule A but then trips a SECOND guard:

  com.liferay.notification.exception.NotificationTemplateSystemException:
    Only allowed bundles can add system notification templates
    at NotificationTemplateUtil.validateInvokerBundle(NotificationTemplateUtil.java:24)
    at NotificationTemplateLocalServiceImpl.addNotificationTemplate(:107)

Two guards in addNotificationTemplate:
  Rule A (BaseNotificationType.validateNotificationTemplate:240): an L_-prefixed ERC is
         allowed only for a system template (isSystem()==true) OR the DSR-group escape.
  Rule B (NotificationTemplateUtil.validateInvokerBundle:24 -> ObjectDefinitionUtil
         .isInvokerBundleAllowed()): a SYSTEM template may be added only by an allowed
         invoker (skip-check thread-local, company-in-deletion, test mode, upgrading, or a
         batch-engine filename matching _ALLOWED_INVOKER_BUNDLE_SYMBOLIC_NAMES). The
         commerce listener adds directly (no batch engine), and
         com.liferay.commerce.notification.service is not on the list -> Rule B throws.

Historical state: before LPD-97613 the template was created as a NON-system L_ template,
permitted by FF LPD-66359 (Rule A bypass); Rule B never applied (not system). LPD-97613
4d0719bb removed that FF and left only the DSR-group escape, which commerce cannot use.

CI confirmation: Testray build 506136031 case-result 506138595 (and 506081763, 505863622,
505649975, 505487123, 505272717, ...) show CompanyModelListenerTest.testOnBeforeRemove
failing "Common" with the same assertNotNull AssertionError -> a real, persistent CI
failure since LPD-97613, not a local artifact. (PortalRunMode.isTestMode() is NOT set on
CI integration bundles either, so test mode does not mask it.)

FINAL FIX (two parts, both in commerce-notification-service, self-contained):

  1. notification-template.json: "system": false -> true  (satisfies Rule A; the L_ prefix
     constant is EXTERNAL_REFERENCE_CODE_PREFIX_SYSTEM_NOTIFICATION_TEMPLATE, so an L_
     template is a system template by the framework's own rule).

  1. AddCommerceOrderNotificationPortalInstanceLifecycleListener: wrap the direct
     addNotificationTemplate call in
       try (SafeCloseable c = ObjectDefinitionThreadLocal
              .setSkipBundleAllowedCheckWithSafeCloseable(true)) { ... }
     (satisfies Rule B; same trusted-internal-provisioner pattern used by
     ObjectFieldModelListener / ObjectActionLocalServiceImpl). commerce-notification-service
     already depends on object-api.
Merchants keep subject/body/recipient editability; name/from are protected and the template
is non-deletable (appropriate for a Liferay-provisioned system default).
================================================================================
FriendlyURL ArrayIndexOutOfBoundsException (PortalLogAssertorTest, ci:test:object 506136031)
================================================================================
Console: console-1.log (test group 6, object model-listener tests). Verbatim ERROR:
  ERROR [BatchEngineImportTaskExecutorImpl:576] Index 0 out of bounds for length 0
  java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
    at FriendlyURLEntryLocalServiceImpl._isBatchPortletDataHandler(:892) <- classNames[0]
    at FriendlyURLEntryLocalServiceImpl.addFriendlyURLEntry(:127) / (:169)
    at ObjectEntryLocalServiceImpl._addFriendlyURLEntry(:2778)
    at ObjectEntryLocalServiceImpl.addObjectEntry(:531) -> addOrUpdateObjectEntry(:689) (batch import, com.liferay.object.test-executor-thread)
Root: _isBatchPortletDataHandler does classNames[0] where classNames=StringUtil.split(_portal.fetchClassName(classNameId),...); fetchClassName returns null/empty -> split=[] -> AIOOBE. Caller _addFriendlyURLEntry passes classNameId=getClassNameId(objectDefinition.getClassName()); the branch fires only for object defs with a CUSTOM friendly URL separator during batch import. Regression: born broken fdb2d324829cb (LPD-74703) added the method + the isBatchImportInProcess() branch, unguarded classNames[0].
Fix: guard `if (Validator.isNull(className)) return false;` (a model whose class name can't be resolved is not a batch portlet data handler). NOT a cover-up: false is the correct answer for object entries. Verified: module compiles, SF clean.

## 2026-08-05 — DROPPED two Playwright fixes after re-evaluation (task #88)

### DROPPED: `2d3b16e` "Wait for the workflow configuration asset type row before editing"

Original claim: the asset type row lands on a later page / takes a moment to register, so retry + raise page size + dispatchEvent.
Why dropped — PROVEN INEFFECTIVE, not a root-cause fix:
- CI run shuyangzhou#12413 CONTAINED this commit and still failed:
  `objectWorkflow.spec.ts:472 › Expect "toPass" Error: Timeout 60000ms exceeded while waiting on the predicate
     at ../pages/portal-workflow-kaleo-designer-web/ConfigurationTabPage.ts:66
     65 | await expect(editButton).toBeVisible({timeout: 10000});
   > 66 | }).toPass({timeout: 60000});`
  Line 66 IS the `toPass` this commit added — the full 60s retry expired, so the row is genuinely absent, not slow.
- Its other target, `objectWorkflow.spec.ts:185` (Process Builder page), also still failed on 12413.
- Bundles three unrelated changes (60s retry + items-per-page 60 + dispatchEvent), violating smallest-logical-step.
Redo requires pinning WHY the asset type row never registers (product or fixture), not a retry.

### DROPPED: `dd5cee6` "Click object web model builder nodes through dispatchEvent"

Original claim: the right sidebar slideout is the topmost hit target so the click is intercepted.
Why dropped — unverified mechanism + unassessed blast radius:
- `ModelBuilderObjectDefinitionNodePage.clickShowAllFieldsButton` is called from **16 sites** across
  objectField.spec.ts and objectRelationship.spec.ts; the one-line change stripped Playwright actionability
  (visible/stable/enabled/hit-test) from ALL of them, most previously passing. Violates no-unnecessary-migration.
- The stated mechanism (click intercepted by the slideout) contradicts the recorded failure mode for the
  model-builder tests (`.react-flow__node` never resolves = element-not-found). dispatchEvent still waits for
  the locator to resolve, so it cannot fix a never-resolving locator. Only one account can be true; neither was proven.
Redo requires a trace showing the actual hit-target at the failing click.

### KEPT: `d2cf175` "Enter the object view date filter values in the displayed format"
- Recorded original failure signature matches the diagnosis exactly: the "Add Filter" button stayed
  `<button disabled>` because the date was typed as YYYY-MM-DD instead of the localized MM/DD/YYYY.
- Both targets (`objectView.spec.ts:1703` create date, `:1811` modified date) PASSED on CI 12413 with the fix present.
- Residual risk noted: it also pads `formatDisplayDate` to MM/DD/YYYY for the expected label; if the app rendered
  unpadded (8/4/2026) the assertion would break — CI passing with the fix is the evidence against that.

### LOCAL ENV BLOCKER (why local A/B was not usable)

object-web Playwright cannot be A/B'd on this bundle: the Objects admin folder sidebar is
search-index-backed (`ObjectFolderResourceImpl.getObjectFoldersPage` -> `SearchUtil.search`) and returns only the
4 CMS folders (index), while the DB holds 6 (incl. `Default`, `DSRStructures`). Definitions created by tests land in
`Default`, which is absent from the sidebar, so the folder-scoped list shows "0 Results Found for: ObjectDefinition<n>"
and every test dies at `ViewObjectDefinitionsPage.clickEditObjectDefinitionLink`. These same tests pass on CI, so this
is local state, not a branch regression. Screenshot evidence:
modules/test/playwright/test-results/objectView-can-filter-entries-by-create-date-in-custom-view-object-web-view/test-failed-1.png
FALSE LEADS burned (do not repeat): pagination pollution (only 12 indexed defs); corrupt `osgi/state` (cleared, no
change); "portal 404 / portlet unregistered" — INVALID PROBE, basic auth does not authenticate portal *pages*, so
core users-admin and site-admin 404 identically.

## 2026-08-05 — date filter format (LPD-X, aggregate 94f902a): systemic scan results

ROOT CAUSE PINNED: 447c7970a3af8 (LPD-89563, 05-20, reject invalid dates) + 1286edc9c0e26 (LPD-89563, 05-21,
locale format). Tests predate both: e78eb387699c3 (LPD-82342, 04-01, migrated off Poshi with the year-first form).
Affected widget = frontend-data-set-web `management_bar/.../DateTimeRangeFilter.tsx` (NOT the similarly named
`frontend-data-set-admin-web/.../DateRangeFilter.tsx`, which really does use yyyy-MM-dd and cost me an hour of
wrong-component analysis).

PRIOR ART / CANONICAL SOLUTION: 5bda6af23b4a9 (LPD-90051, 05-27) fixed the identical break in the CMS tests and
introduced `modules/test/playwright/utils/applyFDSDateTimeRangeFilter.ts` exporting `applyFDSDateTimeRangeFilter`
+ `formatDateTimeForUI` (padStart, `MM/dd/yyyy hh:mm AM`). Our fix now reuses that file via a new date-only
`formatDateForUI`. Do NOT hand-roll a third date formatter for these filters.

FULL CONSUMER SWEEP (Playwright) — who touches these From/To inputs:
- `site-cms-site-initializer/main/all.spec.ts` — SAFE, uses the util; its one hand-rolled block still calls
  `formatDateTimeForUI`.
- `frontend-data-set-web/main/tests/advanced/filters.spec.ts` — the author's own coverage (3dc7cb02e2f2b).
- `object-web/view/objectView.spec.ts` — fixed here.
- `commerce-order-content-web/main/commerceLayouts.spec.ts:3262`, `export-import-web/revamp/pages/ExportImportPage.ts:125`
  — different widgets, not this filter.
- **FOLLOW-UP (not a failure): `frontend-data-set-fragment-web/main/dateRangeFilters.spec.ts:112`** fills the
  year-first literal `'2020-01-02'` and then asserts the button is DISABLED, under the step name "filter cannot be
  applied when setting an impossible date range". It therefore PASSES for the wrong reason — the date is invalid,
  not the range impossible — and would keep passing if the range logic broke. Could not confirm by running: local
  env gap `POST /o/data-set-admin/data-sets -> 404`.

FORWARD EXPOSURE: `site-cms-site-initializer` registers FIVE of these filters (Create/Modified/Display/Expiration/
Review DateTimeRangeFDSFilter), so any new test against the CMS list hits this unless it uses the util.

LIMITS: this is a code-level sweep. Testray cannot filter on error text (`contains(errors,'…')` is unsupported —
the query hangs), and the sweep covers Playwright only, not Poshi.

## LPD-X Search the workflow configuration asset type before editing it (6b7363f3fdc25)

**CI failure fixed (Testray-backed):** `object-web/entry/objectActiveInactive.spec.ts:29`
"Verify that pending and completed Object entries with workflow are not displayed on the
Workflow Metrics page when inactivated" — Testray case 466148607.

- History: https://testray.liferay.com/#/project/35392/routines/45357/build/506489747/case-result/506490296/history
- Last PASSED 2026-07-18T09:27 (build 499646708). Weekly (infra-noise excluded):
  W28 19p/0f -> W29 6p/4f -> W30 0p/20f -> W31 0p/31f -> W32 0p/15f.
  66 consecutive real failures, zero passes, across ALL routines: ci:test:object (45357),
  EE Development Acceptance master (590307), ci:test:bpm (189378256), U152 (82964).
- Failure error (read verbatim on 2026-08-05/08-04 results):
  `Test timeout of 90000ms exceeded. locator.click: waiting for getByRole('row')
  .filter({has: getByRole('cell', {name: 'ObjectDefinition<n>', exact: true})})
  .getByRole('button', {name: 'Edit'})` at ConfigurationTabPage clickAssetTypeEditButton.
- Plain-master-routine failures verified VERBATIM too (caveat retired 2026-08-05):
  ci:test:bpm 8190 (2026-08-04T09:23) and 8165, EE Development Acceptance master 23
  (2026-08-04T05:45) and 22 — all four show the identical error above. The failure
  exists independent of any branch.

**Root cause (measured, not from CI):** table pages at 20; 36 asset types on a clean
instance; 19 sort before a generated `ObjectDefinition<digits>` name -> target sits at
position 20, the LAST page-1 slot, margin zero. Any extra asset type ahead of it (e.g. a
parallel spec's definition) pushes it to page 2 where nothing pages forward. Explains
April-onward flakiness (boundary) and the W30 flip to permanent (a new row landed ahead;
candidates Launch Entry/Launch Set at rows 16-17).

**ROOT CAUSE PINNED (now 264355a6d1b44): BORN BROKEN.**
- `df099ab16c0a5` (LPD-25960, 2024-06-20, Carlos Montenegro) created
  `clickAssetTypeEditButton` as a bare first-page lookup (networkidle -> row-by-name ->
  click). No paging, no filtering, ever. Verified with `git log -L` on the method: only
  TWO commits touched it in its life — birth, and `db6cd74acd8a8` (LPD-88846,
  2026-05-19) which only tightened the row locator to exact-cell matching, same
  assumption.
- Consuming spec born `b3706fa493d20` (LPD-80345, 2026-03-10, Poshi->Playwright
  conversion); its Testray case is flaky from its first recorded results (Apr 2026) —
  the defect predates the test; the test inherited it.
- Exposure = a CLASS (population growth + parallel specs), no single tipping commit
  claimed: e.g. `4e54c27dc2c13` (LPD-76298, 2026-06-02) added Launch Set + Launch Entry
  ahead of generated ObjectDefinition names. NOTE: the 19-ahead/position-20 numbers are
  LOCAL clean-master measurements; the local bundle is known to miss modules, so CI's
  table has >= that — consistent with CI's deterministic 0/66 flip in W30 while a lone
  local run still fits on page 1.

**Repro/validation (forced failing condition, local):** create 6 `AaaFiller*` definitions
so the target is off page 1, arrive via tab click:
  upstream logic -> FAILS `locator.click: Timeout 15000ms exceeded`
  new logic -> PASSES in 415ms
Scripts: scratchpad/validate_fix.mjs, validate_control.mjs. Cleaned up after (21 defs).

**Simplified to gate + single shot (user question, 2026-08-05, now 37671b387cf3f):** the
toPass retry was removed. Its last justification (a stale enabled toolbar from the
outgoing Workflows tab satisfying a pre-gate) was REFUTED by a 25ms in-page sampler
across the tab switch, warm and 20x throttled: the URL change and content swap land
together, and the swapped-in toolbar arrives with its button DISABLED — there is no
url=CONFIG/old-enabled-form sample, ever. So after waitForURL the gate can only pass on
the correct hydrated toolbar, and a single fill+Enter is deterministic. Re-validated
against the forced off-page-1 condition: warm 387ms; 20x throttle 3/3 at ~6.7s (the wait
IS the gate absorbing the measured disabled window, 1.5s->2.6s). Project expect timeout
15s covers both waits; no explicit timeouts. Scripts: stale_window.mjs,
validate_single.mjs, validate_single_throttled.mjs.

**Excluded mechanisms (verified):** definition-not-published (POST status {code:0}
returns approved and the asset type IS listed); registration lag (deploy+handler
registration synchronous inside the POST — ObjectDefinitionLocalServiceImpl.java:2583,
deployer :536; commit callback :2595 is cluster-peers only); "row absent" (over-read of
a pending-action log line); LPS-203533 search regression (search works in production —
user-verified manually).

## LPD-X Scope the layout tab locator to the layout designer's tab strip (6de53e5194c43)

**CI failures fixed (Testray-backed):** `LocalFile.CreateObject#FieldOptionRequired` and
`LocalFile.CreateObject#CanSetBlockCollapsible` — lastPASS 2026-04-21T16:0x, firstFAIL
2026-04-24T21:0x, ~123 consecutive FAILs each, error
`ElementNotFoundPoshiRunnerException: Element is not present at
"//div[@class = 'form-group']//div[contains(@class,'layout-tab__tab-types__title') ...]"`.

**Root cause:** born fragile + broken live. `ObjectAdmin#LAYOUT_LAYOUT_TAB` was
`//button[contains(text(), 'Layout')]`; `goToLayoutTabOnLayouts` uses its PRESENCE to
decide whether to SelectFrame into the kebab>Edit side-panel IFRAME that hosts the
layout designer. `687a83688b993` (LPD-81198, 2026-04-21) changed FDS creation buttons
from hardcoded "New" to their real label (CreationMenu.tsx) -> the layouts list button
became "Add Object Layout" -> matches the loose xpath in the TOP document -> iframe
switch skipped -> click creates a New Layout instead. Failure screenshot shows the New
Layout modal open where the tab tile picker was expected.

**Fix:** `//ul[contains(@class,'side-panel-iframe__tabs')]//button[contains(text(),'Layout')]`
— verified live: the designer strip is `ul.nav.nav-tabs.side-panel-iframe__tabs` with
exactly Info+Layout `role=tab` buttons; new xpath matches 1 inside the iframe, 0 in the
top document. All 4 usages are the same designer context.

**Local verification (fresh clean env):** pre-fix repro = EXACT CI error (93s). Post-fix
BOTH tests BUILD SUCCESSFUL (35s each). Two intermediate false-fail rounds were env
artifacts, documented: (a) my probe fixture reused the test's object name, (b) Poshi's
LIFERAY_ERROR scan reads WHOLE log files and tripped on the previous session's stale
errors ("Unable to sync callable" in liferay.2026-08-04.log, then "Caught unexpected
exception" 02:31 entries sharing today's file) — fixed by purging old-dated logs and
truncating live ones; clean-env rule updated.

## LPD-X Type and assert object rich text through CKEditor 5 (d7e232133c12c)

**CI failures fixed (Testray-backed):** ObjectFields#CanFormulaFieldBeUsedWithEmailNotification +
#CanEditConditionalReadOnlyFieldWhenConditionIsFalse — lastPASS 2026-04-17T01:06, firstFAIL
2026-04-18, ~128-131 consecutive FAILs, error `//*[contains(@id,'cke')]/iframe` /
`//label[...'Custom Field']...cke...iframe` not present.

**Root cause:** tests stale after editor default change. Chain: 1f5d3a2fc43c4 (LPD-68023) CKE5 in
RichTextLocalized behind LPD-11235; 3886e1ecc1daf (LPD-80539) deprecation-type FFs start DISABLED
on new DBs/companies (CompanyModelListener._processDeprecationFeatureFlags); 48a3b53cc7b14 +
245b2b606eccd (LPD-81886, authored Mar-06, MERGED 2026-04-16 per committer date) moved LPD-11235
to deprecation + inverted the gate. Every fresh CI DB -> CKE5 -> no iframe. KEY LESSON: author
date != merge date; committer date on brian matches Testray boundaries.

**Fix:** ObjectNotifications.macro both Type calls -> CKEditor#BODY_FIELD_CONTENTEDITABLE_WEB_CONTENT_ARTICLE
(key_fieldLabel="Template"); ObjectFields.testcase AssertEditable -> AssertElementPresent with new
ObjectPortlet#ENTRY_RICH_TEXT_EDITABLE (labeled ck-editor__editable_inline + @contenteditable='true'
+ value). Live-verified on both screens: labeled CKE5 xpath = exactly 1, old iframe xpath = 0,
zero iframes on either page. Shared FormFields#RICH_TEXT_CONTENT untouched (forms consumer).

**Local verification:** pre-fix = exact Testray errors. Post-fix: ConditionalReadOnly PASSES
(47s, isolated). EmailNotification walks the entire template flow and fails only at
Navigator.macro:354 `Invalid URL: http://localhost:8282` = MockMock fake-SMTP web UI (CI-only
sidecar, started by build-test.xml start-test-smtp-server, jar com.mockmock-1.4.0, `-p 25000`).
That tail is untouched by the fix; CI proves it. ENV NOTES: portal blacklists mail recipients
locally ("Email test@liferay.com ... blacklist" ERROR) — truncate logs BETWEEN sequential Poshi
runs or the scanner fails the NEXT test with the PREVIOUS test's mail error.


## DigitalSignature cluster (10 tests) — TRIAGE VERDICT: CI credentials, not code

All 10 failures are the DocuSign Sandbox integration suite. Architecture
(DigitalSignature.macro:288+): setUp types five credentials read via
PropsUtil.get("digital.signature.{api.username,api.accountId,account.base.uri,
integration.key,rsa.private.key}") into Instance Settings > Digital Signature, then
tests drive real envelope flows. All five properties are COMMENTED placeholders in
test.properties — CI injects real values. Failure faces map 1:1 to dead/absent
credentials: config form's apiUsername "not editable", no success alert after Send
(envelope create fails), envelope rows/labels absent downstream.

ROOT NOT YET DISCRIMINATED among three candidates: (a) CI-injected credentials
absent/rotated, (b) the DocuSign sandbox account itself dead/expired, (c) a product UI
regression in the Digital Signature configuration form — the "apiUsername not editable"
face is a FORM-STATE failure that would occur with perfectly valid credentials.
Discriminators queued: local config-form probe (no credentials needed — does apiUsername
become editable after checking 'enabled'?) and Testray last-pass dating vs DS commit
history. Only after those: handoff (credentials/env) or in-repo fix (product form).


## DigitalSignature FINAL VERDICT (2026-08-06 ~04:00)

Discriminator probe result: the Digital Signature configuration form is HEALTHY on
current code (apiUsername present/enabled/editable before and after checking
'enabled') -> product-form regression REFUTED. Combined with Testray dating (envelope
suite: 1819 passes, last 2026-06-22, permanent failure since; the June-22 LPD-28402
regen's DS slice is additive/benign), the nine envelope tests are broken on the
EXTERNAL side: CI-injected DocuSign Sandbox credentials rotted/absent or the sandbox
account itself dead — indistinguishable from this repo, same handoff either way.
SiteSettings#CanEnableDigitalSignatureBySiteSettings is separate: 1 pass in 1999
results over two years — chronically broken long before the June boundary (its
"not editable" face is specific to the fresh-virtual-instance flow it uses).

HANDOFF ASK (QA/CI infra): restore a working DocuSign Sandbox account + the five
digital.signature.* properties on the ci:test:object poshi environment, or decide the
DS suite's fate in the object scope. No in-repo code change can green these nine.

## e123b19a523d9 LPD-X Semantic versioning (commerce-test-util)
- CI: baseline validation failure for com.liferay.commerce.test.util.validator (getValidateCount() added without bumps).
- Culprit: 93a19a06642b8 (LPD-99094, Crescenzo Rega, 2026-08-05) — still unbumped on brian tip 98e1f20eed9d7.
- Verify: `:apps:commerce:commerce-test-util:baseline` BUILD SUCCESSFUL locally after packageinfo 1.0.0->1.1.0 + bnd 33.0.1->33.1.0.

## d22ee7408be1b LPD-X Assert the import active parameter on the semantics the import ships
- CI: ObjectDefinitionExportImportTest 2 failed — testImportObjectDefinitionWithActive (AssertionError at assertFalse) + testImportObjectDefinitionWithObjectFolderExternalReferenceCode (expected TESTIMPORTFOLDER1 but was default). First CI fail 2026-08-05T06:12 (first run containing b28c53b20101e), passed 2026-08-04T23:52 without it.
- Mechanism evidence:
  - Original test: importJSON(false) -> importJSON(true) -> assertFalse — self-contradictory.
  - Dedup alone still fails at :422: create path drops active (putByERC -> postObjectDefinition has no active arg; publish sets active=true in _publishObjectDefinition; action put#2 re-puts response DTO). Empirical: local run after dedup-only edit -> assertFalse FAILED, definition active.
  - Folder cascade: leftover published TESTIMPORTOBJECTDEFINITION + folder json status=draft -> ObjectDefinitionStatusException inside action (swallowed to error JSON, test ignores response) -> definition untouched in default folder. ComparisonFailure expected:<[TESTIMPORTFOLDER1]> but was:<[default]>.
  - Feature intent: 3ee8011451954 message — param for CMS re-import (update path); admin import omits param.
- Verify: class run 4/4 green TWICE on clean state (tasks bx0pyl3ys, biybpxyzo).

## e90f9a925f84d LPD-X Stub the enhanced source editing plugin in the missed Jest setup
- CI: :apps:object:object-dynamic-data-mapping-form-field-type:packageRunTest — ObjectRelationship.spec.tsx "Test suite failed to run".
- Local repro (real harness `npm test`): SyntaxError "Unexpected token 'export'" via @codemirror/state -> @marijn/find-cluster-break (ESM main); with @marijn transformed: TypeError "Super expression must either be null or a function" at _inherits in obfuscated ckeditor5-source-editing-enhanced dist (extends stubbed @ckeditor/ckeditor5-ui View).
- Culprits: d8e5dfe7f2df1 (LPD-83978, 08-03) added plugin to default editor config; c974d2ceab061 (08-05) stubbed 7 jest-setup.config.js files, missed the lone .ts variant.
- Verify: npm test -> 6/6 suites, 44 tests PASS. Rejected alternative: global @marijn whitelist in frontend-sdk getJestConfig.js (reverted; per-module stub is the established pattern).

## 3a58a26fe3447 LPD-X Resolve relationship selections through the value key they store (PRODUCT)
- CI: objectEntry:5171 commerce-products dead since 07-29 (last pass 07-29T08:52; boundary = b329f64541b9b LPD-97608 merged 07-28).
- Trace-proven (run bzwc8kl8y, trace5171): saved entry stores CProductId 75947 (valueKey=productId via CPDefinitionSystemObjectDefinitionManager.getRESTDTOIdPropertyName); post-save edit view by-id fetch GET /products/75947 returns 200 with id=75959 (version row); LPD-97608 guard Number(item?.id)===Number(value) rejects -> onChange(null) clears value -> "The field value is invalid" (screenshot in trace resources).
- Fix: compare item[valueKey] ?? item.id in BOTH list find and by-id guard (mirrors hidden-input line 435 convention). Only CProduct overrides the id property -> blast radius scoped to the broken class.
- Verify: jest 44/44; clean Playwright A/B PASS 15.4s (buymjd2lj) after resolving TWO local-env confounders:

  1. Product Versioning left ON by failed runs (test start-toggle blindly flips) -> reset via scratchpad/reset_versioning.js.

  1. DEPLOY SKEW: fresh module build imports ClayDropDownWithAI which the bundle's old frontend-js-clay-web lacked -> module evaluation SyntaxError (NO console/network signal; probe_import.js dynamic-import captured it) -> every DDM field of the module spun forever (broke Attachment too). Fixed by deploying frontend-js-clay-web peer.

## 2b711e7f7f1b3 LPD-X model builder pair (3456/3565)
- Call-log-proven interception: minimap svg + left c-slideout subtree intercept pointer events (by-design canvas overlays); fitView worsens (maxZoom on single node).
- Fix: isolated folder (postRandomObjectFolder; folder pushed before definition for LIFO cleanup) + dispatchEvent('click') in clickShowAllFieldsButton + field-row.
- Verify: 2 passed 8.5s, repeat 2 passed 8.2s (was 273F/8P chronic since 09d7f7ff5244c LPD-85191).

## PR#12418 run-2 Poshi residues — local clean-env investigation (2026-08-06 ~05:40 UTC)
- Run 2 result: 41/59 jobs; 12 of 15 Poshi fixes PROVED green on CI. DS 10x again (external, second corroboration).
- GOTCHA RECONFIRMED: fresh_boot.sh does NOT truncate logs; the LIFERAY_ERROR scanner read pre-boot ExceptionMapper nulls and poisoned the first local verdicts. Protocol now: glob-truncate ALL logs before boot.
- CanCreateActionWithCustomFieldInSystemObjects: local CLEAN PASS (BUILD SUCCESSFUL 2m15s). CI run-2 failure = UI TimeoutException, unreproducible locally; watch #12419.
- CanPublishObjectWithPublishPermission: local clean run — ALL UI steps pass; single ERROR line kills the scanner: POST /user/update-password 404. ROOT: `jsonws.web.service.paths.excludes=/user/update-password` is the portal DEFAULT since a8af10a35d25f (LPS-70590, 2019); JSONUser.setFirstPassword macro itself warns it needs portal.properties reconfig (LRQA-52401); password-policy testcases declare `property custom.properties = "jsonws.web.service.paths.excludes="` but CreateObject.testcase does NOT — CI tolerates the ERROR via its batch-level ignore.errors (value lives in Jenkins, not the repo). The 404 is functionally harmless (user password already set at creation; login-as-user succeeds). LOCAL ENV GAP, not a bug. CI run-2 failure cause unknown (auth-walled report) — single occurrence, not in baseline; watch #12419.
- CanFormulaFieldBeUsedWithEmailNotification: local boundary = Navigator to http://localhost:8282 (fake SMTP web UI, CI-only service) — Invalid URL locally by design. CI-side failing step unknown (auth-walled); needs fresh Testray curl or #12419 data.
- Self-inflicted probe trap logged: reused a companyId variable across a DB recreation -> chased phantom JSONWS 404s (get-user-id-by-email-address 404s = NoSuchUser for WRONG companyId, returns HTTP 404 + {}); also `JSONPath-less curl` captures quoted values ("20132") that break long params. Ground-truth companyId from DB first.

## PR#12419 run-3 verification (workflow wf_ad38e3e2-47b, 6 agents, adversarial)

VERDICT: NO REGRESSION FROM THE PULL; 34/34 targeted cases fixed as expected; 1 fix honestly ineffective (30s belt).
- 22 FAILED fully enumerated: 12 DS external + 7 pre-existing chronic + 3 new-vs-baseline ALL proven pre-existing:
  - CheckWorkflowNotTriggeredForDraftEntry: page-editor Components sidebar (PageEditor#FRAGMENT_SIDEBAR_TAB_BUTTON); fails on upstream bpm 8190/8165/8128 + U152; zero branch touches on portal-web.
  - relationship-Address: dragTo timeout in ModelBuilderDiagramPage.connectObjectDefinitionsNodeHandles (:60); ~14% chronic flake (7F/43P last 50); failures predate suspect commit incl. upstream EE-Acceptance 18; test never calls clickShowAllFieldsButton.
  - objectActiveInactive:29: 29F/1P last 30, ALL 12 non-shuyangzhou runs FAILED; the single PASS was OUR baseline (fluke). Same clickAssetTypeEditButton mechanism as :35/1485.
- Workflow-config search cluster (one mechanism, independent manifestation): objectView:1485 30/30 FAILED incl. all upstream; objectWorkflow:35 2F/28P, both failures ours, all upstream green (caveat: no upstream ci:test:object routine exists for exact comparison); activeInactive above. 30s belt (7cc577e2d23c3) did NOT cure — row genuinely never renders; mechanism = search yields no row for the just-created definition. OPEN INVESTIGATION.
- Salesforce ×2: master-wide dead since 2026-05-25/26 boundary (last pass 05-24), all routines. Collection-providers: master-wide 100% fail since 2026-07-18 (76 consecutive), all routines incl. foreign PRs.
- Top Level Build: synthetic Testray aggregate mirroring overall job result — not a test.
- UNTESTED 10 = 9 never-run class (0 passes EVER, 209-280 consecutive UNTESTED) + password-policies-admin semver batch-skip (12% UNTESTED historically). Only PASSED->UNTESTED flip = that batch skip.
- Absent 3 = CanSetBlockCollapsible + CanCollapseAndExpandBlock (upstream deletion 72ea272d3ee21) + job wrapper.
- Critic spot-checks defeated "coincidental pass": 1769 baseline '<Forbidden' at fixture:35 == teardown fix mechanism; 5171 deterministic pre-fix with trace-pinned product root cause; CanSetERCFieldAsTitleField //body timeout == saveSidePanel mechanism; localization domino = 10 mechanism-correlated cases flipping together.
- Critic gaps (bookkeeping only): objectView:1912 needed explicit STILL entry (chronic, byte-identical baseline error, pre-existing); baseline is a sibling PR run not upstream master (verdicts relying on non-shuyangzhou histories unaffected); Testray session rotted mid-critique (all history data captured before).

## CORRECTION (2026-08-06, user-directed re-verification): import active test — REAL root cause is the merge-pipeline SF commit
- MY EARLIER NARRATIVE WAS WRONG: I had claimed Victor's b28c53b20101e was "born broken with a duplicated import". FALSE — I had diffed the post-SF file state and attributed it to the authoring commit.
- PROVEN FACTS (all empirical, this session):

  1. Victor Galan's authored tests (b28c53b20101e) run 4/4 GREEN as written (sequence: import active, import inactive, assert inactive, import active, assert active — deactivation AND reactivation via the update path). Verified locally against merged product code.

  1. Brian's `LPD-99246 SF` commit 09d9be9eed6fd contains EXACTLY ONE change (1 file, +2/-3): swaps the boolean args of the two adjacent importJSON calls (false before true) and removes the blank line between them → the test then asserts inactive immediately after importing active → deterministic fail at assertFalse (:425 local; CI from first run 2026-08-05T06:12Z, SF commit 01:45:39Z).

  1. The repo's OWN SourceFormatter does NOT reproduce the swap (format-source-current-branch AND single-file with validate.commit.messages=false both leave Victor's order untouched; MethodCallsOrderCheck only targets put/add/setAttribute on specific receivers). The reorder came from the merge pipeline's separate formatting step (not in this repo) — tool vs manual slip not determinable from here; the commit is authored "Brian Chan ... LPD-99246 SF".

  1. Folder test = pure cascade (Victor's version 4/4; fix version 4/4).
- FIX REPLACED: e3799830b0a44 restores Victor's full assertions (deactivate + reactivate) and interleaves get+assert after EVERY import so no two importJSON calls are adjacent — immune to consecutive-call sorting. 4/4 green twice.
- BONUS: SF vulnerability-keyword validation rejects commit titles containing "csrf" (source-formatter.properties:145) in source.files mode — my token-retry commit retitled to "...authentication token" (9139f4ec022ca) preemptively.
- REBASE MECHANICS GOTCHA: GIT_SEQUENCE_EDITOR sed on 13-char hashes MISSED a todo entry (abbreviation length mismatch) → amend landed on the wrong stop; repaired via subject-matched python todo editor. Always match todo lines by SUBJECT, not hand-copied hash prefixes; verify `git log -1 --format=%s` at every stop BEFORE amending.

## a49269a6f8e31 LPD-X Wait for the workflow configuration tab before searching its asset types
- ROOT CAUSE PROVEN (deterministic local repro): the assignment types into the PREVIOUS tab's management-bar search form. Callers reach the tab two ways: goTo() (waits for `=configuration` URL) vs `configurationTabLink.click()` + immediate assignment (no wait). Until the nav lands, the Process Builder definitions table is still on screen WITH its own ENABLED submit button — so LPD-101364's "wait for submit enabled" gate is satisfied by the wrong screen.
- CORRELATION: all 3 CI failures use the direct-click path (objectWorkflow:35, objectActiveInactive:29, objectView:1485); all 17 goTo() callers are immune. Direct-click sites: objectWorkflow ×14, objectActiveInactive ×2, objectView ×2, formView ×1.
- REPRO RECIPE (scratchpad/race_repro.js): 12 filler definitions named AAAFiller* (sort before "Account") push the target off page 1 + page.route 6s latency on **/group/control_panel/manage** + press Enter as soon as submit is enabled -> final table empty, Edit never found in 30s. scratchpad/which_table.js samples the DOM during the window and shows the definitions table ("Single Approver") + enabled submit throughout.
- GATE DISCRIMINATION: submit-enabled alone = FAILS; generic `getByRole('row').nth(1)` = FAILS (definitions table has rows too); waiting for an asset-type data cell ('Account') = PASSES; waiting for the `=configuration` URL (same gate goTo already has) = PASSES. Chose the URL gate: semantic, instance-independent, mirrors goTo.
- Also refuted along the way: locale/pt_BR poison (search works in pt_BR), draft-definition (all 3 use status {code:0}), search-matches-wrong-field (search by label filters to exactly 1 row), pagination alone (search works with the target on page 2).
- Verify: tsc clean; objectWorkflow:35 and objectActiveInactive:29 pass locally after the change; urlgate variant of the latency repro passes. objectView:1485 has a SEPARATE later-step defect locally (line 1561, 'Entry Test 2' not visible) — tracked separately, NOT claimed fixed here.
- NOTE: the dropped 30s belt (never committed upstream) was a band-aid on this same symptom; the real gate makes the default 15s sufficient.

## DocuSign investigation (2026-08-06) — DEFINITE ANSWER: THE CREDENTIALS WORK

Evidence, in order of strength:

1. DIRECT: built a JWT from the file's values (iss=integration.key, sub=api.username, aud=account-d.docusign.com, scope="signature impersonation") -> POST /oauth/token = HTTP 200 Bearer token (so key + user + integration key + CONSENT all valid). userinfo 200: exactly 1 account whose id and base_uri match the file. envelopes list 200.

1. THROUGH THE PRODUCT: fed the credentials into a local Poshi run (-Ddigital.signature.*), ran LocalFile.DigitalSignatureListView#HaveMoreRecipientsThanOne -> the portal CREATED A REAL ENVELOPE in the sandbox (verified via API: subject 'Email Subject', created 2026-08-06T18:07:15Z, status voided after the test deleted it). End-to-end portal->DocuSign auth works.

## The key-format trap (product parser rules, proven with net.oauth PEMReader itself)

DSAccessTokenWebCacheItem._readPrivateKey uses net.oauth.signature.pem.PEMReader, which is LINE-BASED: it readLine()s to find "-----BEGIN", derives the END marker from that line, then accumulates following lines until it. Consequences (tested with the real jar, KeyProbe.java):
  A raw file value (single line, literal \\n) -> IOException: Invalid PEM file: No end marker
  B one unescape pass (real newlines, BEGIN header glued) -> IOException: No end marker (glued header corrupts END-marker derivation)
  C real newlines + newline after the BEGIN header -> PARSED OK
  D 2-char unescape leaving stray backslashes -> No end marker
=> The stored value MUST be, in a properties file: `-----BEGIN RSA PRIVATE KEY-----\n<64-char lines separated by \n>\n-----END RSA PRIVATE KEY-----` using SINGLE-escaped 2-char \n (26 of them for this key), so java.util.Properties yields real newlines. The user's exported file has 3-char \\n (50 backslashes) AND no break after the BEGIN header — both fatal.
Verified round-trip: writing the value that way into a properties file -> Properties.load -> 27 lines -> PEMReader PARSED OK.

## What that means for the 12 CI failures (mixed causes — a feed fix alone will NOT green them all)
- CI fails EARLIER than a correctly-fed local run: on CI HaveMoreRecipientsThanOne dies with the success alert absent (envelope creation step); locally with correct credentials it creates the envelope and dies at the LAST assertion (line 196, badge still present after delete). That delta is the proof CI's credential feed is broken.
- Seeded-data class: DigitalSignatureCanBeEnabled creates no envelope at all — it asserts a 'Sent' status label exists, which needs pre-existing envelopes. The sandbox has ZERO envelopes ever (totalSetSize=0 before my run). Same shape likely for CanViewEnvelopeDetails / CanViewCorrectDate / CanNavigateBetweenPages / CanDownloadEnvelope*. These cannot pass on credentials alone.
- Genuine test bug class: HaveMoreRecipientsThanOne's final AssertElementNotPresent on the recipients badge fails locally right after a successful delete (reproducible, credentials fine).
- CanEnableDigitalSignatureBySiteSettings fails with "Element is not editable at //input[contains(@name,'apiUsername')]" — a different, config-screen symptom worth separate triage.

## Salesforce / SugarCRM (same class, different mechanism)
- tests/object-web/salesforce/salesforce.config.ts reads OBJECT_STORAGE_SALESFORCE_{CONSUMER_KEY,CONSUMER_SECRET,LOGIN_URL,PASSWORD,USERNAME} from process.env and SILENTLY FALLS BACK TO '' (login URL defaults to https://test.salesforce.com/). A missing or misnamed env var is therefore indistinguishable from a wrong credential: the test just fails downstream ("entry creation never returns success", "Thank you..." never visible). Recommend a fail-fast guard naming the missing variable.
- No SugarCRM Playwright/Poshi credential wiring exists in this repo (only the object-storage-sugarcrm modules); nothing named sugar appears in either CI failure CSV, so there is no SugarCRM test failure in the object suite to fix here.

## DocuSign local sweep with CORRECTLY-FED credentials (2026-08-06): 0 of 12 pass — three distinct classes

Measured, not inferred (each test run individually on the local bundle with -Ddigital.signature.* fed in the proven-correct form):
CLASS 1 — SEEDED-DATA (5 tests): fail with the SAME step and SAME locator as CI, so credentials are irrelevant to them.
  DigitalSignatureCanBeEnabled macro:414 //span[label-item ... 'Sent'] absent
  CanViewEnvelopeDetails macro:430 //a[contains(., 'Test')] absent
  CanNavigateBetweenPages macro:62 'Go to the next page' absent
  CanDownloadEnvelopeByEnvelopeList macro:421 //tr[...'Test']//button absent
  CanViewCorrectDate (same family; CI signature identical)
  + CanDownloadEnvelopeByEnvelopeDescription (same family)
  They read an envelope named 'Test', a Sent status label, and a SECOND PAGE of envelopes. The sandbox holds ZERO envelopes -> unfixable by credentials; needs seeded data or self-created fixtures.
CLASS 2 — GENUINE TEST BUG (1): HaveMoreRecipientsThanOne. With working credentials the portal CREATES the envelope (verified in DocuSign: subject 'Email Subject', 18:07:15Z, voided by the test's delete), adds the recipient, deletes it, then fails the FINAL AssertElementNotPresent on the recipients badge (testcase:196). Mine to fix.
CLASS 3 — LOCALLY INCONCLUSIVE (3+): CanViewSuccessMessage, CanAddMoreThan1Document, CanEnableDigitalSignatureBySiteSettings, plus the two DM tests, all die on LOCAL ENV NOISE: '## 19 Liferay Exceptions were thrown' from the LIFERAY_ERROR scanner reading `/api/jsonws?contextName=&signature=user -> NPE jsonWebServiceActionMapping is null` (known local-only artifact; the DM pair additionally hits ElementNotInteractableException at macro:275). Their CI signature (success alert absent / apiUsername not editable) is the credential-dependent one, so on CI they may well improve once the feed is fixed — NOT CLAIMABLE from local runs.
=> Michael's feed fix is necessary but sufficient for at most the class-2/3 group; the class-1 five will still fail. Do not read a partial improvement as a failed fix.

## 2026-08-10 OVERNIGHT — draft-entry (CheckWorkflowNotTriggeredForDraftEntry) root-cause characterization

Target: `Element is not present at (//div[@data-name='Form Container'])[1]` — Poshi, fails
deterministically on clean env in ~2m40s, and its Playwright twin is skipped on CI.

**Four-way experiment matrix (all on brian 7872a15 + portal healthy):**
| drive | result |
| --- | --- |
| Poshi `Simulate.dragAndDrop` (synthetic JS events, the shipped path) | fragment never lands, ZERO network requests |
| synthetic + added `dragenter` | same — dead |
| selenium mouse primitives (mouseOver/Down/MoveAt/Release, real webdriver Actions) | same — HTML5 dnd never initiates |
| Playwright `dragTo` (CDP-trusted drag events) | **works: fragment lands, 1 POST** |

**Conclusions:** the PRODUCT is healthy (trusted input adds the fragment); the failure is the
test-infra drag: untrusted synthetic events no longer drive react-dnd's HTML5 backend on current
Chrome (local chromium 150 AND playwright chromium 139), and classic webdriver mouse events have
never fired HTML5 dragstart. Suspects eliminated by A/B or dating: `2b6bfddf09c3f` upload.js
(clean refute: target failure with AND without), `c0767f79f6668` fragment-key guard (commit-dated
before the passing 8103 nightly). The chrome pin (139) dates to Feb-25 (`fe263b12bf0f9`
LPD-80521), NOT the 8/1 boundary.

**Open question for the morning (needs live Testray):** whether OTHER PageEditor.addFragment
users (32 portal-web testcases + WorkflowContentPage in the bpm suite) also started failing at
the 8/1 boundary — that decides "CI browser image rolled" vs "something narrower". In the object
batch, the draft-entry test is the ONLY addFragment user, so this failure being alone there is
consistent with a global simulate break.

**Fix options (user decision):**

1. Teach the Poshi drag to dispatch trusted CDP drag events (Input.dispatchDragEvents) — poshi
   runner change, fixes ALL simulate-drag tests, biggest scope.

1. Rewrite this test's SETUP to create the content page with the mapped form container via the
   headless API (the drag is setup, not the behavior under test) — smallest scope, leaves other
   simulate users broken.

1. Both: 2 now, 1 as infra follow-up.

Experiment artifacts: scratchpad/poshi_draft2.log (baseline fail), AB_DRAFTENTRY.md + poshi_revertside/restoredside.log
(suspect refute), poshi_mouse2.log (selenium-mouse fail), the zzDragProbe spec (removed; recreate from this note).

## DS CanEnableDigitalSignatureBySiteSettings — REPRODUCED LOCALLY (fresh env, no creds needed)

Same error as CI: `Element is not editable at //input[contains(@name,'apiUsername')]` at
DigitalSignature.macro:326 (enableDigitalSignature). The failure-time screenshot shows the driver
STILL on Instance Settings > Digital Signature (strategy select in green unsaved-edit state) —
meaning task 2's whole navigation chain (Navigator.openURL → ProductMenu.gotoPortlet(Configuration,
Site Settings) → Click ListGroupItem "Digital Signature") silently did nothing, and the Type hit
the instance JSP's apiUsername (present in DOM at portal_settings/digital_signature.jsp:53 but not
rendered visible) → "not editable". ProductMenu.gotoPortlet works in other tests on this bundle
(draft-entry reached Content & Data fine), so suspect a stale window/driver target or the
SystemSettings.saveConfiguration() before it never completing (Save still visible + green select
= save possibly never clicked). NEXT: rerun with -Dtest.skip.tear.down=true and step screenshots;
check SystemSettings.saveConfiguration macro's wait conditions on this screen.
Repro: nq_ds.log + test-results/LocalFile.DigitalSignatureSiteSettings_*/screenshots.

## DS CanEnable — ROOT CAUSE NARROWED (round 2, kept-state screenshots + DB check)

after2.jpg (SITE SCOPE) shows every field DISABLED: Enabled toggle off/greyed, API User ID /
Account ID / Base URI / Integration Key / Environment / RSA all greyed. Per
site_settings/digital_signature.jsp:38 fields disable when siteSettingsStrategy is
"always-inherit" OR null. So site scope reads a NULL strategy → apiUsername Type hits a disabled
input → "not editable".

DB confirms nothing persisted: `SELECT configurationId FROM Configuration_ WHERE ... signature`
returns ZERO rows — neither setUp's instance-scope enable NOR the test's
selectSiteStrategy("Always Override")+save committed. `SystemSettings.saveConfiguration` (macro
line 287) calls `PortletEntry.save()` — the same in-panel save family as the side-panel commit
(aae5df4). HYPOTHESIS (NOT proven, do NOT ship a guessed fix): the DS config save does not commit
on this screen, same class as the //body frame issue, so site scope legitimately shows null and
disables the fields. Needs: step-through of saveConfiguration's success-wait on the DS instance
screen + confirm whether the strategy ever reaches the DB with skip-tear-down state live.
This is the quarantined (`portal.release=quarantine`), DocuSign-credential-gated test — LOW
priority, parked with this precise narrowing rather than a speculative product change.