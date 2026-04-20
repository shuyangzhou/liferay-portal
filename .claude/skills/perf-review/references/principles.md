# Performance Principles

Liferay performance engineering standards, verified against ~2,363 commits over 2 years and ~400+ diffs.

Each principle has an ID, a **risk rating**, detection signatures, and exemplar commit hashes. Principles in the **Appendix** have limited evidence — apply only when the anti-pattern is unambiguous.

Risk ratings reflect the severity of leaving the anti-pattern in production code:
- **Critical** — correctness risk or severe throughput degradation under concurrent load.
- **High** — measurable latency or memory impact at portal scale.
- **Medium** — noticeable in hot paths or bulk operations; worth fixing before merge.
- **Low** — minor or speculative gain; flag as suggestion, not a blocker.

---

## Principle Index

| ID | Title | Risk |
|---|---|---|
| P03 | ThreadLocal for Thread-Unsafe Stateful JDK Classes | Critical |
| P17 | Collect FutureTask Before Gathering Results | Critical |
| P01 | Minimize ConcurrentHashMap Lock Duration for Hot Keys | High |
| P10 | Bulk Array Finders Over N+1 Lookups | High |
| P19 | Avoid Redundant DB Round-Trips | High |
| P02 | Build Strings With StringBundler | High |
| P16 | Coalesce Small Work Items; Throttle Parallelism | High |
| P12 | Cache Negative Lookup Results | Medium |
| P08 | Petra Unsync I/O and StreamUtil | Medium |
| P15 | Static Utilities Over OSGi Service Injection | Medium |
| P09 | Liferay Utilities for Standard Operations | Medium |
| P21 | Early Return Before Expensive Operations | Medium |
| P22 | Transient Field Caching for Model Objects | Medium |
| P07 | Static Caches Are private static final | Medium |
| P11 | Cache ThreadLocal Reads in a Local Variable | Medium |
| P13 | Concrete Typed Methods; Avoid Object Boxing | Low |
| P14 | Direct Invocation Over Unnecessary Abstraction | Low |
| P04 | Single try-with-resources for Multiple SafeCloseables | Low |
| P18 | Sort Work Largest-First for Thread Pool Dispatch | Low |

Appendix (limited evidence): P05, P06, P20

---

## P15 — Static Utilities Over OSGi Service Injection for Stateless Implementations

**Why it matters:** OSGi `@Reference` injection involves proxy object allocation, service registry lookup at activation, and dynamic dispatch. When the implementation has no mutable state and no `@Activate` / `@Deactivate` lifecycle, a static utility class provides the same functionality with zero allocation overhead and improved JIT inlining. This is the single most common optimization pattern in the codebase — two major campaigns converted 50+ Elasticsearch query translators and 18+ index request executors to static POJOs.

**Preferred pattern:** Convert a trivial `@Component` class to a `final class` with `private` constructor and `static` methods. Callers drop the `@Reference` field and call the static method directly.

**Detection signatures:**
- `@Component` class with only `@Reference` fields — no mutable instance state, no `@Activate` / `@Deactivate` — flag for possible static conversion.
- An interface implemented by exactly one class that has no lifecycle — flag the interface + implementation pair.

**Nuances:** Do NOT convert to static if the class: (a) has `@Activate`/`@Deactivate` lifecycle hooks, (b) holds mutable instance state, (c) depends on OSGi services it must track, or (d) is designed to be overridden by customers.

**Exemplar commits:** 36a8f2b1f2348, b9e6e8d0 (DocumentBuilderFactory), cb38708b (GeoBuilders), 844fbffe (TermFilterTranslator)

---

## P08 — Use Petra Unsync I/O and StreamUtil — Not JDK 9+ APIs

**Why it matters:** `InputStream.readAllBytes()`, `InputStream.transferTo()`, and similar JDK 9+ methods are unavailable in JDK 8 build targets. The JDK synchronized I/O classes (`ByteArrayOutputStream`, `BufferedReader`, etc.) hold locks on every read/write even when accessed from a single thread, which is the common case for request processing. Petra's `Unsync*` equivalents are lock-free and therefore faster in that context.

**Preferred patterns:**
- `StreamUtil.toByteArray(inputStream)` instead of `inputStream.readAllBytes()`.
- `UnsyncByteArrayOutputStream` instead of `ByteArrayOutputStream`.
- `UnsyncBufferedReader` instead of `BufferedReader` in single-threaded contexts.
- `UnsyncByteArrayInputStream` instead of `ByteArrayInputStream`.

**Detection signatures:**
- `.readAllBytes()` called on an `InputStream`.
- `.transferTo(` called on an `InputStream`.
- `new ByteArrayOutputStream(` — flag for possible replacement with `UnsyncByteArrayOutputStream`.
- `new BufferedReader(` — flag for possible replacement with `UnsyncBufferedReader`.

**Utility classes:**
- `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/StreamUtil.java`
- `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/unsync/`

**Exemplar commits:** 6d2414d (Avoid InputStream.readAllBytes()), bfcd134, 34eff70, 1481aed

---

## P19 — Avoid Redundant Database Round-Trips

**Why it matters:** The most common DB performance anti-pattern in the codebase is issuing a second (or third) query to retrieve data already available in the call stack, or issuing a `COUNT(*)` implicitly by loading all rows. This principle covers three related sub-patterns: (a) cache during reindexing to avoid re-fetching entities already loaded, (b) separate count queries from data queries, (c) avoid Hibernate session overhead for hot single-row updates.

**Sub-patterns:**
- **Reindex cache**: Use `ReindexCacheThreadLocal` to cache entities during document contribution so each contributor does not re-fetch the same object. Seen in 38+ "Apply index cache to X" commits.
- **Count before fetch**: Provide a `performCount()` / `getReindexEntryCount()` method for callers that only need a count, not the full result set.
- **Raw SQL for hot single-row updates**: Hand-craft `UPDATE` SQL via `DB.runSQL()` to bypass Hibernate session overhead for frequently-called operations like `updateLastLoginDate`.

**Detection signatures:**
- `findAll()`, `findByC_*()`, or any full-fetch finder called solely to get `.size()` — flag for a dedicated count method.
- A service method called twice in the same request with the same arguments — flag as possible reindex cache candidate.
- `.size()` called on the result of a fetch in a method named `reindex*` or `contribute*` — check whether `ReindexCacheThreadLocal` applies.

**Exemplar commits:** b05b0d5d105ba (performCount), a2387e18f661b, c4d4896 (ReindexCacheThreadLocal), 1cf9b91 (raw SQL for updateLastLogin), a90f8b3 (BLOB caching)

---

## P10 — Prefer Bulk Array-Based Finders Over Per-Item Lookups (N+1 Avoidance)

**Why it matters:** A loop that calls a single-item service method N times issues N database round-trips. Bulk finders accept an array or list of IDs and return all records in a single query, which the caller can post-filter in memory. A related variant: rather than fetching a full entity when only one field is needed, refactor the method signature to accept the field value directly (avoiding the fetch entirely).

**Preferred patterns:**
```java
// Bulk fetch once, post-filter in memory
Map<Long, Entity> entityMap = entityLocalService.getEntityMap(ids);

for (long id : ids) {
    Entity entity = entityMap.get(id);
    process(entity);
}
```

Also: refactor `getConfig(Company company)` to `getConfig(long companyId)` so callers with only the ID do not need to fetch the Company object.

**Detection signatures:**
- A `for` or `while` loop whose body calls a single-ID service method (e.g., `get*ById`, `fetch*`, `find*ByPrimaryKey`) — flag as potential N+1.
- A method that takes a full entity object but only uses one of its IDs — flag the signature for refactor.
- `persistence.findByC_C(companyId, classPK)` or similar inside a loop.

**Exemplar commits:** b05b0d5d105ba, a2387e18f661b, 9861764b (bulk fetch + post-filter), fe686fb, 4a58668, 9329c4e (PrevAndNext replaced with bulk queries), 68a39ef (bulk draft layout fetching), c2c25f5

---

## P09 — Use Liferay Utilities for Standard Collection and Type Operations

**Why it matters:** `stream().filter().collect()` chains create intermediate objects for simple operations that Liferay utilities handle in a single pass. Inline null-coercion and JSON building patterns create implicit allocations and are less readable than the standardized utilities. Inlining a method used in only one place eliminates the call overhead and removes unnecessary abstraction.

**Preferred patterns:**
- `ListUtil.filter(list, predicate)` instead of `list.stream().filter(...).collect(Collectors.toList())`.
- `ArrayUtil.contains(array, value)` instead of `Arrays.stream(array).anyMatch(...)`.
- `GetterUtil.getString(obj, default)` instead of `obj == null ? default : obj.toString()`.
- `JSONUtil.put(key, value)` instead of manual JSON string building.
- Inline a method that has exactly one call site; do not retain abstraction for its own sake.
- Avoid `BeanPropertiesUtil.getString(obj, "field")` when the field is directly accessible — call the getter instead.

**Detection signatures:**
- `.stream().filter(` on a `List` or array where the result is collected to a list — check whether `ListUtil.filter` applies.
- `Arrays.stream(` for a contains-check — check whether `ArrayUtil.contains` applies.
- `(obj == null ? defaultValue : ` for type coercion — check whether `GetterUtil` applies.
- Manual `StringBuilder`-based JSON building — check whether `JSONUtil` applies.
- `BeanPropertiesUtil.getString(` where the object has a typed getter — flag.

**Utility classes:**
- `portal-kernel/src/com/liferay/portal/kernel/util/ListUtil.java`
- `portal-kernel/src/com/liferay/portal/kernel/util/ArrayUtil.java`
- `portal-kernel/src/com/liferay/portal/kernel/util/GetterUtil.java`
- `portal-kernel/src/com/liferay/portal/kernel/json/JSONUtil.java`

**Exemplar commits:** af064ed (ListUtil.getPreviousAndNext), ed01425 (JSONUtil), 316c6b2 (inline MBMessage indexing), f087dc6 (avoid BeanPropertiesUtil)

---

## P16 — Coalesce Small Work Items; Throttle Parallelism Appropriately

**Why it matters:** Thread pool task submission overhead (queue insertion, context switch, wake-up) exceeds the work time for small items. Creating a thread-per-company when there are fewer companies than CPUs means threads are pooled then immediately discarded. Commits reverting aggressive parallelism that caused OOM and ES 429 errors in CI confirm this is not purely theoretical.

**Preferred patterns:**
- Sort items by estimated size; submit large items individually, batch small items below a threshold into one task.
- When company count << CPU count, a plain sequential loop outperforms a thread pool.
- Add a configuration switch to disable parallelism when resources are constrained (e.g., CI environments).

**Detection signatures:**
- `executorService.submit(` or `executorService.execute(` inside a `for` loop with no size check — flag if the work unit could be small.
- `new ThreadPoolExecutor(N, N, ...)` where N is derived from `Runtime.availableProcessors()` but applied to a workload that is often smaller than N — flag for threshold check.

**Nuances:** This is context-dependent. The codebase has many commits both adding and limiting parallelism. The principle is not "avoid parallelism" but "match parallelism to actual work size and resource availability."

**Exemplar commits:** f4ab265a9af50 (bundle small indexers), 80c9ed0 (avoid crazy thread creation), 10531c13c1d1b, ff0c5ec (avoid async for single indexer)

---

## P02 — Build Strings With StringBundler

**Why it matters:** `StringBuilder` requires pre-allocating capacity or triggers internal array resizing. String `+=` in a loop creates an intermediate `StringBuilder` and a new `String` object on every iteration — scaling to O(n²) allocation for n elements. `StringBundler` defers allocation until `toString()`, growing a reference array of `Object` instead, and generates the final string in one pass.

**Preferred patterns:**
- `StringBundler.concat(a, b, c)` for static joins.
- `new StringBundler(estimatedSize)` + repeated `.append()` for dynamic accumulation.
- Never `+=` on a `String` variable inside a loop.
- Never `StringUtil.split()` + mutate array + `StringUtil.merge()` inside a loop.

**Nuances:** `StringBundler` is optimized for `String` concatenation. Do **not** use `StringBundler.append(char)` for character-by-character iteration — `StringBuilder` performs better in that case (confirmed contradiction: commit 72267497a52095 explicitly reverted `StringBundler` char appends).

**Detection signatures:**
- `StringBuilder` in any context — flag for review.
- `+=` on a `String` variable inside a loop body.
- `StringUtil.merge(array)` called inside a loop that also calls `StringUtil.split`.
- `StringBundler.append(char` or `StringBundler.append((char)` — flag as incorrect use.

**Exemplar commits:** 0383a14a13b04 (split/merge disaster, 177 elements), c18adcb, 62285fb, b437d7d (fix bad StringBundler pattern), 7434be2

---

## P07 — Static Caches Are private static final

**Why it matters:** A non-`final` static field can be reassigned, making the cache semantics unclear and preventing JVM constant-folding. This also covers `@CacheField` on model objects and `PortalCache`-backed caches initialized in `@Activate` — all must be `final` or clearly lifecycle-bound.

**Preferred patterns:**
```java
private static final Map<String, ConfigYAML> _configYAMLMap =
    new ConcurrentHashMap<>();
```

For model-level caching, use `transient` for per-instance lazy fields that must not be serialized:
```java
private transient Group _group;
```

**Detection signatures:**
- `private static Map<` or `private static List<` or `private static Set<` without `final`.
- Any `static` collection field missing `final` at the class level.

**Exemplar commits:** 746bba9 (cache parsed YAML), 5558762 (transient _group field), 7112e60 (PortletPreferences cache), 249bcd7

---

## P03 — ThreadLocal for Thread-Unsafe Stateful JDK Classes

**Why it matters:** `DateFormat`, `SimpleDateFormat`, `Calendar`, and `java.util.regex.Matcher` are not thread-safe. A `static` shared instance causes data corruption under concurrent access. A new instance per call is safe but allocates on every invocation. This also applies to request-scoped computation: `ThreadLocalCacheManager.REQUEST` caches results for the duration of a single request, avoiding repeated lookups across method calls in the same thread.

**Preferred patterns:**
```java
private static final ThreadLocal<DateFormat> _dateFormat =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

// Request-scoped: use Liferay's ThreadLocalCacheManager
ThreadLocalCacheManager.get(Lifecycle.REQUEST).put(key, value);
```

**Detection signatures:**
- `private static final DateFormat` or `private static final SimpleDateFormat` without `ThreadLocal`.
- `private static final Calendar`.
- `new SimpleDateFormat(` inside a method body (per-call allocation).
- `_matcher.reset(` where `_matcher` is a non-`ThreadLocal` field shared across threads.

**Exemplar commits:** b334548 (FreeMarkerTool DateFormat), d351e01 (BufferableThreadLocal), b9caf03 (cache CompanyThreadLocal.get), b93dbd4 (PortletPreferencesLocalService ThreadLocalCache)

---

## P12 — Cache Negative Lookup Results

**Why it matters:** Exception construction calls `fillInStackTrace()`, which walks the JVM stack. When a lookup is expected to fail repeatedly (e.g., a `ClassLoader` asked for the same missing class during code generation, or a `get*()` service method throwing `NoSuch*Exception` on every miss), throwing a new exception each time is expensive. The broader pattern is: whenever an absence is expected and frequent, represent it with a fast constant rather than an expensive operation.

**Preferred patterns:**
```java
// Singleton exception with suppressed stack trace
ClassNotFoundException _missingException = new ClassNotFoundException() {
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
};

// Sentinel object to distinguish "not found" from "not yet looked up"
private static final byte[] _NULL_AUTH_TOKEN = new byte[0];

// Use fetch + null check instead of get + try-catch
Entity entity = entityLocalService.fetchEntity(id); // returns null, no exception
if (entity == null) { ... }
```

**Detection signatures:**
- `throw new ClassNotFoundException(` or `throw new NoSuch*Exception(` inside a loop or method called repeatedly — check for a negative cache nearby.
- `entityLocalService.get*(` where the entity may not exist — check whether a `fetch*` variant exists.
- `try { ... } catch (NoSuch*Exception e) { return null; }` inside a loop body — flag as expensive control flow.

**Exemplar commits:** 9f735c11b3072 (ClassLoader negative cache), eb5ad3e, 4d044ae (DL file text extraction), b219464 (FriendlyURLMapper null-safe cache), e041f26 (AuthToken null cache), e72a622 (shortcut before JSON parsing), 9900636 (bean lifecycle method lookup)

---

## P01 — Minimize ConcurrentHashMap Lock Duration for Hot Keys

**Why it matters:** `computeIfAbsent` holds the bucket lock for the entire lambda execution. For hot, frequently repeated keys (e.g., `companyId`) with expensive lambdas (service lookups, array transforms), this serializes concurrent callers. Manual `get()` → compute → `putIfAbsent()` avoids holding any lock during computation.

**Preferred pattern:**
```java
long[] classNameIds = _classNameIdsMap.get(companyId);

if (classNameIds == null) {
    classNameIds = expensiveComputation(companyId);

    long[] previous = _classNameIdsMap.putIfAbsent(companyId, classNameIds);
    if (previous != null) {
        classNameIds = previous;
    }
}
```

**Nuances:** `computeIfAbsent` is acceptable when: (a) the key distribution is not hot (many distinct keys, rare repeats), or (b) the lambda is a simple constructor call (`new Foo()`) with negligible cost. The anti-pattern is specifically hot keys with expensive lambdas. The scan found commits using `computeIfAbsent` correctly for locale/feature-flag caches alongside commits removing it for companyId hot paths.

**Detection signatures:**
- `computeIfAbsent` on a `ConcurrentHashMap` where the lambda body contains service calls, DB lookups, or array transforms.
- `compute(` or `merge(` on a `ConcurrentHashMap` — always flag, these hold locks even for reads.

**Exemplar commits:** 063a9922bc148 (avoid on sparse hot keys), c0b52642 (nested computeIfAbsent fix), 6583db24778257 (avoid compute() holding lock)

---

## P13 — Invoke Concrete Typed Methods; Prefer Direct Field Access Over Map Lookup

**Why it matters:** Passing a primitive to a method accepting `Object` boxes it. The callee introspects and unboxes back to the primitive — two unnecessary allocations. The same principle applies to HashMap key lookup via `instanceof` chains: an O(n) scan beats an O(1) hash lookup by factor n. VarHandle provides direct, zero-overhead field access without reflection overhead.

**Preferred patterns:**
```java
// Direct typed overload instead of Object boxing:
builder.value(floatValue);        // not builder.value((Object)floatValue)

// HashMap over instanceof scan:
Map<Class<?>, Handler> handlers = new HashMap<>();
handlers.get(obj.getClass()).handle(obj);   // not: if (obj instanceof A) ... else if (obj instanceof B)

// VarHandle over reflection:
private static final VarHandle _fieldVarHandle = ...;
_fieldVarHandle.set(obj, value);            // not: field.set(obj, value)
```

**Detection signatures:**
- Explicit cast to `Object` before passing to a method with typed overloads (`int`, `long`, `float`, `double`, `boolean`).
- `if (x instanceof A) ... else if (x instanceof B) ...` chains — check whether a dispatch map applies.
- `field.set(obj, value)` or `field.get(obj)` via reflection in a hot path — check for `VarHandle`.

**Exemplar commits:** 345eefd1c8853 (XContentBuilder typed methods), 2b888d3b + 436ae7ea (HashMap vs instanceof), ae73086 (VarHandle for BigEndianCodec), 69e78e8 (VarHandle dispatch), 028c5b3 (direct field access)

---

## P14 — Direct Invocation Over Unnecessary Abstraction on Hot Paths

**Why it matters:** Thin wrapper classes, unnecessary interface dispatch, copy-on-write "protection" for read-only access, and lambda creation in hot paths all add allocation and dispatch overhead. When the called code has no mutable shared state, defensive copies and indirection are waste.

**Preferred patterns:**
- Replace `.accept(executor)` visitor dispatch with `executor.executeSpecificType(object)`.
- Remove single-method thin wrapper classes that only delegate.
- Provide `getFooWithoutCopy()` variants for read-only callers when the default applies defensive copying.
- Replace lambdas in hot-path constructors/methods with pre-instantiated objects or direct calls.

**Detection signatures:**
- `.accept(` called with a visitor/executor argument — flag for possible direct-dispatch replacement.
- A class with one method that calls another class's one method with no transformation.
- `clone()` or copy constructor called in a method named `get*` where the caller is read-only — check whether a no-copy variant is appropriate.
- Lambda created inside a loop — check whether the lambda captures state or can be extracted as a field.

**Exemplar commits:** 04831bd5b5457 (visitor → direct), 526b3ef (BatchIndexingActionable wrapper removal), b7ba054 (avoid copy on read), e247b02 (no-copy reads), 95cea94 (remove lambdas), 519f99b (shortcut predicate)

---

## P21 — Early Return Before Expensive Operations

**Why it matters:** Checking a cheap condition (string `.contains()`, empty collection, null flag) before entering an expensive operation (DOM parsing, full deserialize, DB query, JSON parse) eliminates the expensive work for the common negative case. This is stated explicitly in multiple commit messages: "Do cheaper shortcut first", "Shortcut before parsing JSON", "Shortcut on accept all Predicate".

**Preferred pattern:**
```java
// Check cheap condition first
String html = fragmentEntryLink.getHtml();

if (!html.contains("lfr-drop-zone")) {
    return Collections.emptyList(); // skip expensive DOM parse
}

Elements elements = Jsoup.parse(html).getElementsByTag("lfr-drop-zone");
```

**Detection signatures:**
- An expensive operation (JSON parse, full XML deserialize, DOM parse, DB query) at the top of a method with no preceding guard — check whether a cheap pre-condition exists.
- `.contains(` / `.isEmpty()` / `size() == 0` check that could be moved earlier to short-circuit a method.

**Exemplar commits:** e72a622 (shortcut before JSON parsing), babb24b (cheaper shortcut first), 519f99b (shortcut on accept-all predicate), bd62ed9 (string search before XML parse), b45a0f9 (skip full DDMForm deserialization)

---

## P04 — Single try-with-resources for Multiple SafeCloseables

**Why it matters:** Nested `try` blocks for `SafeCloseable` resources add indentation and obscure cleanup order. Java's multi-resource `try-with-resources` closes in reverse declaration order, which is the correct semantic for nested thread-local scope management.

**Preferred pattern:**
```java
try (SafeCloseable s1 = CompanyThreadLocal.setWithSafeCloseable(companyId);
     SafeCloseable s2 = ReindexCacheThreadLocal.openReindexMode()) {

    doWork();
}
```

**Detection signatures:**
- Two or more consecutive `try (SafeCloseable` blocks where the inner block is entirely nested inside the outer.

**Exemplar commits:** 076f300, 61aac3e466999c, 7434be2

---

## P11 — Cache ThreadLocal Reads in a Local Variable

**Why it matters:** `ThreadLocal.get()` performs a lookup on the current thread's local map — not a simple field read. Calling it three or more times in a method, especially when the calling object has deep wrapping layers (permission checker, proxy chains), is measurable overhead. Commit 4fb555b confirms this was deliberate: "At this point the permissionChecker object has many layers of wrappers, repeatedly calling these two getters with deep call stacks when the foreach loop is large can be very costly."

**Preferred pattern:**
```java
boolean hasBackgroundTask = BackgroundTaskThreadLocal.hasBackgroundTask();
long userId = permissionChecker.getUserId();
boolean signedIn = permissionChecker.isSignedIn();

for (Item item : items) {
    if (hasBackgroundTask) { ... }
    if (signedIn) { ... }
}
```

**Detection signatures:**
- The same `SomeThreadLocal.get()` call appearing three or more times in a single method body.
- `permissionChecker.getUserId()` or `permissionChecker.isSignedIn()` called inside a loop.

**Exemplar commits:** 49603e92f50ef, 1588de6330ce62, 4fb555b (partial revert + rationale), b9caf03

---

## P17 — Collect FutureTask Before Gathering Results

**Why it matters:** Adding results to a shared list from inside `executor.execute()` requires external synchronization and silently drops exceptions. Collecting `FutureTask` instances before calling `.get()` provides structured exception handling and safe result collection without shared mutable state in runnables.

**Preferred pattern:**
```java
List<FutureTask<Result>> tasks = new ArrayList<>();

for (Item item : items) {
    FutureTask<Result> task = new FutureTask<>(() -> process(item));
    tasks.add(task);
    executorService.execute(task);
}

List<Result> results = new ArrayList<>();

for (FutureTask<Result> task : tasks) {
    try {
        results.add(task.get());
    }
    catch (Exception e) {
        _log.error("Processing failed", e);
    }
}
```

**Detection signatures:**
- `executor.execute(() -> someList.add(` — shared mutable list written from a runnable without synchronization.
- `executor.submit(` where the returned `Future` is not collected — results and exceptions are silently lost.

**Exemplar commits:** cb2f413fa612b, 6adcbdd55e009, f37b459a51a06c

---

## P18 — Sort Work Largest-First When Dispatching to a Thread Pool

**Why it matters:** Dispatching smallest items first means the thread pool finishes early while one large item still runs on a single thread, serializing the tail. Largest-first keeps all threads busy until the final stretch.

**Preferred pattern:**
```java
indexers.sort(
    Comparator.comparingLong(
        i -> -reindexEntryCounts.getOrDefault(i, 0L)));

for (Indexer<?> indexer : indexers) {
    executorService.submit(() -> reindex(indexer));
}
```

**Detection signatures:**
- A loop submitting items to an executor where no size-based sort precedes the loop and the items have measurably different sizes (indexers, modules, files).

**Exemplar commits:** f4ab265a9af50, 7e255be (expand indexer sorting scope), 6b1ed35

---

## P22 — Transient Field Caching for Model Objects

**Why it matters:** Model objects are frequently serialized (to session, cluster cache, DB). A `transient` field survives for the lifetime of the in-memory object but is not serialized, making it ideal for caching an expensive lookup (e.g., `User._group`, `Portlet._portletApp`) that is needed multiple times in a single request or reindex pass.

**Preferred pattern:**
```java
private transient Group _group;

public Group getGroup() {
    if (_group == null) {
        _group = GroupLocalServiceUtil.fetchGroup(groupId);
    }

    return _group;
}
```

Also used with sentinel objects to distinguish null-fetch from un-fetched:
```java
private static final PortletFriendlyURLMapperMatch _NULL_MATCH =
    new PortletFriendlyURLMapperMatch();

private transient PortletFriendlyURLMapperMatch _cachedMatch;

public PortletFriendlyURLMapperMatch getMatch() {
    if (_cachedMatch == null) {
        _cachedMatch = _computeMatch(); // may return _NULL_MATCH
    }

    return _cachedMatch == _NULL_MATCH ? null : _cachedMatch;
}
```

**Detection signatures:**
- Repeated calls to the same `LocalServiceUtil.get*(id)` within the same model class with no caching — flag for `transient` field.
- `_log.isDebugEnabled()` or `properties.get("key")` called multiple times in the same method with no local variable — flag for caching.

**Exemplar commits:** 5558762 (User._group), b219464 (FriendlyURLMapper), e041f26 (AuthToken), 7112e60 (LazyPortletPreferences)

---

## Appendix — Low-Frequency Principles (≤2 Verified Commits)

These principles appeared in the seed scan but were confirmed in ≤2 commits during the full-history verification. Apply them only when the anti-pattern is unambiguous. They may be promoted in a future scan.

### P05 — Use Validator for Null and Empty Checks

Inline `== null` / `.isEmpty()` on `String` variables. Prefer `Validator.isNull(value)` / `Validator.isNotNull(value)`. Do not flag `== null` on non-String types.

**Utility class:** `portal-kernel/src/com/liferay/portal/kernel/util/Validator.java`

---

### P06 — Convert Hashtable to HashMapDictionary at Ingress

`Hashtable` returned from OSGi `ConfigurationHandler.read()` synchronizes every access. Convert immediately before storing in a `ConcurrentMap`:
```java
Dictionary<?, ?> config = new HashMapDictionary<>((Map<Object, Object>)hashtable);
```

**Utility class:** `portal-kernel/src/com/liferay/portal/kernel/util/HashMapDictionary.java`

**Exemplar commit:** a146c75749720

---

### P20 — Use IdentityHashMap When the Cache Key Is a Reused Object Instance

When caching by object identity (same instance reused across calls, such as parsed YAML or AST nodes), `IdentityHashMap` uses `==` comparison, which is faster than `equals()` for large domain objects.

**Exemplar commits:** 1c68a0e4a072a, 7edd179
