# ci:test:object — issues needing fixes

Source: CI `#12415` (Testray build `506489747`, 36/59 jobs, **70 real failures**) on brian base `ffabfbf`,
cross-referenced with a full local object-web Playwright run (551 tests, 465p/75f) and a local
`object-test` integration run (1374 tests, 1288p/2f).

Counting note: the CSV's 70 rows over-count. 15 rows are a **single** build failure, and several
Playwright/Poshi rows share one root cause. Distinct issues below: **~20**.

---

## P0 — build blocker (15 CSV rows, one cause)

### 1. `notification-test` cannot run: missing `.lfrbuild-portal` on a DSR module

```
Execution failed for task ':apps:notification:notification-test:testIntegration'.
> Please create marker file .../modules/apps/site/site-dsr-site-initializer-api/.lfrbuild-portal
```

Kills the whole module, reported as 15 "was not executed" rows (all `com.liferay.notification.*`).

**Root cause — got broken later, pinned:**
- `0ebc902f5f628` (LPD-99914, shuyangzhou, **2026-07-28**) added
  `testIntegrationImplementation project(":apps:site:site-dsr-site-initializer-api")` to
  `notification-test/build.gradle` (to assert the DSR Seller role).
- `378ce21dd12e4` (LPD-100525, andrea-ale-sbarra, **2026-08-03**) then **deleted** `.lfrbuild-portal`
  from 11 DSR modules, including `site-dsr-site-initializer-api`, to exclude DSR from the portal bundle.

A portal test module now depends on a module deliberately excluded from the portal bundle.
**Do NOT just re-add the marker** — that reverts LPD-100525's intent. Fix on the dependency side:
drop the DSR dep + DSR Seller assertion from `GetEmailNotificationRolesMVCResourceCommandTest`, or move
that assertion into a DSR-side test module. Note the dependency is our own commit, so this is ours to fix.

---

## P1 — integration + log assertor (4 distinct)

### 2. `ObjectDefinitionExportImportTest` — 2 tests (REPRODUCES LOCALLY)

`testImportObjectDefinitionWithActive` (`:425` assertFalse) and
`testImportObjectDefinitionWithObjectFolderExternalReferenceCode` (`:463`,
`expected:<TESTIMPORTFOLDER1> but was:<default>`).

**Root cause — pinned to LPD-99246**, which shipped the feature *and* its tests:
`359e6bdcde01d` "Preserve each object definition folder on import" ·
`3ee8011451954` "Keep object definitions active on import when requested" ·
`b28c53b20101e` "Add integration tests for import active and object folder handling" · `09d9be9eed6fd` SF.
The impl only honours `objectFolderExternalReferenceCode` from the **request param**; the tests import
with the ERC in the **JSON body** and no param, so the folder falls back to `default`. Impl and tests
disagree — one of the two is wrong. (Absent from #12413, which predates LPD-99246: consistent.)

### 3. `ObjectActionLocalServiceTest.testConcurrentObjectActions`

`AssertionError: Thread ... caught concurrent failure: {level=ERROR, loggerName=com.liferay.object.internal.action.engine.ObjectActionEngineImpl...}`
An ERROR is logged under concurrent object actions.

### 4. `PortalLogAssertorTest` 0/12 — duplicate portlet registration

`Portlet id com_liferay_object_web_internal_object_definitions_portlet_ObjectDefinitionsPortlet_F1B8 is already in use`
Same family as the **ghost asset types** seen locally (object definition deploy/undeploy leaking
registrations). Likely one root cause with issue 8.

### 5. `PortalLogAssertorTest` 0/3 — `OptimisticLockException`

`ORMException: jakarta.persistence.OptimisticLockException: Row was updated or deleted by another transaction`

---

## P2 — Playwright (23 on CI: 9 with a local repro, 14 CI-only)

### 6. Workflow configuration asset-type row — **FIXED**

`ConfigurationTabPage:46` looked the row up in a paginated table listing every workflow-enabled asset
type; it is only on page 1 while few exist. Fixed by filtering by name (commit `5f71f2e`, still `LPD-X`).
`objectActiveInactive.spec.ts:29` now passes; `objectView:1484/:1919` and `objectEntry:6546/:6614`
advance past this step. Blast radius probed: notification-web + both DDM formView callers unchanged.

### 7. `objectView.spec.ts:1484` and `:1919` — entries not visible in custom view

After issue 6, both now fail at `getByText('Entry Test 2'/'Entry Test', {exact:true})`.

### 8. `objectEntry.spec.ts:6546` / `:6614` — workflow task + metrics page

Now fail at `getByLabel('<definition>: entry')` on the metrics page, and at the task page.

### 9. `objectWorkflow.spec.ts:185` — `getByRole('row', {name:'<definition>'})` never resolves

### 10. Management-bar `Search` box never appears

`objectField.spec.ts:2409` (in `beforeEach`) and `objectEntry.spec.ts:5171` wait for
`locator('.management-bar').getByRole('searchbox', {name:'Search'})` / `getByRole('textbox',{name:'Search'})`.
Probably the same search-box timing family as issue 6 — try the same treatment.

### 11. `objectField.spec.ts` "Create Object Fields" — 5 variants on CI (2 with local repro)

add-with-missing-required-properties, invalid name, delete, cancel, update field properties.

### 12. `objectDefinition.spec.ts` Page Templates ×2

`:2769` `getByText('<definition>')` not visible; `:2900` `POST /o/headless-delivery/v1.0/sites/<id>/site-pages` → **400**.

### 13. `objectEntry.spec.ts` — "different versions of Commerce Products have same input values"

### 14. CI-only, no local repro yet (14)

`objectLayout` ×3 · `objectDefinitionHierarchy` ×2 · `objectSalesforce` ×2 · `objectRelationship` bidirectional ·
`objectContentPageIntegration` Information Template · `objectWorkflow` draft entry ·
`objectEntry` friendly URL · `objectField` ×3.

---

## P3 — Poshi (25 rows, 3 groups)

### 15. `DigitalSignature*` ×10 — external-integration env gap

Tell-tale: `Element is not editable at "//input[contains(@name,'apiUsername')]"` (provider credentials),
plus 2 × `ElementNotInteractableException: has no size and location`.

### 16. 7 × timeout waiting for `//body` (5s) — browser/session never came up (infrastructure)

### 17. 6 × object-specific `ElementNotFoundPoshiRunnerException` — 4 distinct causes
- `CreateObject#FieldOptionRequired` + `#CanSetBlockCollapsible` → `layout-tab__tab-types__title`.
  **RULED OUT:** class is not stale (`ModalAddObjectLayoutTab.tsx:108`).
- `ObjectFields#CanFormulaFieldBeUsedWithEmailNotification` + `#CanEditConditionalReadOnlyFieldWhenConditionIsFalse`
  → `//*[contains(@id,'cke')]/iframe`. **RULED OUT:** no editor swap; `...email_notification_settings.jsp=ckeditor` unchanged.
- `ObjectFields#AllFieldsFromRelatedObjectAreDisplayedWhenFilteringLongIntegerFields` → `Relationship` button.
- `ObjectFields#CanEditPublishedObjectStorageFolder` → `Show Files in Documents and Media` label.

---

## P4 — js-unit (2)

### 18. `:apps:object:object-web:packageRunTest` and `:apps:object:object-dynamic-data-mapping-form-field-type:packageRunTest`

---

## Local-environment issues (block local verification, not CI)

### 19. Objects admin folder sidebar is index-backed and omitted `Default`

Fixed enough to unblock by reindexing (folders 4→5, definitions 12→14). See
`object-web-playwright-local-env-folder-blocker` memory for the 4 false leads burned here.

### 20. 65 local-only Playwright failures — env gaps, **not** CI blockers

16 × `headless-delivery` POST 400 · 9 × client-extension `HTTP 400` · 2 × mail `ERR_CONNECTION_REFUSED` ·
3 × `objectPersonalData` null fixture · commerce asset types never resolving.

## Poshi campaign snapshot (2026-08-05 evening)

Baseline: build 506489747 (PR#12415), 25 LocalFile.* failures, byte-identical to 506368212.

| # | cluster | tests | state |
|---|---------|-------|-------|
| 1 | Layouts pair | CreateObject#FieldOptionRequired, #CanSetBlockCollapsible | FIXED 6de53e5 (locator scoped to side-panel-iframe__tabs; root cause 687a836 LPD-81198 FDS label) — both green locally |
| 2 | CKEditor pair | ObjectFields#CanFormulaFieldBeUsedWithEmailNotification, #CanEditConditionalReadOnlyFieldWhenConditionIsFalse | fix applied (CKE5 locators; root cause LPD-81886 merge 2026-04-16 flipping LPD-11235 deprecation default on fresh DBs) — verification running |
| 3 | Feb-26 permanent | 7x ObjectFields (ERC/picklist/longtext/formula-view/storage-folder/long-integer-filter) | all lastPASS 2026-02-25T22:0x same build; die at //body; no property/alphabetical grouping; NEXT: individual local runs to test batch-poisoning theory |
| 4 | Recent-flaky //body | CreateObject#CanCollapseAndExpandBlock, ObjectFields#CannotEditReadOnlyFieldWhenTrue | lastPASS Jul-31; flake class; queued |
| 5 | DigitalSignature | 10 tests | external-provider class; queued last |