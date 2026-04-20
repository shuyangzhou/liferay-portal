# Performance Patterns

Anti-pattern → preferred-pattern code samples for each principle. Use these when scanning diffs and when writing the "Preferred pattern" section of a violation report.

---

## P01 — ConcurrentHashMap Lock Duration

**Anti-pattern:**
```java
private static final Map<Long, long[]> _classNameIdsMap =
    new ConcurrentHashMap<>();

long[] classNameIds = _classNameIdsMap.computeIfAbsent(
    companyId,
    key -> TransformUtil.transformToLongArray(
        classNames, className -> getClassNameId(className)));
```

**Preferred pattern:**
```java
private static final Map<Long, long[]> _classNameIdsMap =
    new ConcurrentHashMap<>();

long[] classNameIds = _classNameIdsMap.get(companyId);

if (classNameIds == null) {
    classNameIds = TransformUtil.transformToLongArray(
        classNames, className -> getClassNameId(className));

    long[] previousClassNameIds = _classNameIdsMap.putIfAbsent(
        companyId, classNameIds);

    if (previousClassNameIds != null) {
        classNameIds = previousClassNameIds;
    }
}
```

---

## P02 — String Building

**Anti-pattern (string accumulation in loop):**
```java
String result = "";

for (String element : elements) {
    result += element + ", ";
}
```

**Anti-pattern (split/merge cycle):**
```java
String[] parts = StringUtil.split(value);
parts = ArrayUtil.append(parts, newPart);
value = StringUtil.merge(parts);
```

**Preferred pattern (static join):**
```java
String result = StringBundler.concat(partA, ", ", partB, ", ", partC);
```

**Preferred pattern (dynamic accumulation):**
```java
StringBundler sb = new StringBundler(elements.size() * 2);

for (String element : elements) {
    sb.append(element);
    sb.append(", ");
}

String result = sb.toString();
```

---

## P03 — ThreadLocal for Thread-Unsafe Classes

**Anti-pattern:**
```java
private static final DateFormat _dateFormat =
    new SimpleDateFormat("yyyy-MM-dd");

public String format(Date date) {
    return _dateFormat.format(date); // not thread-safe
}
```

**Preferred pattern:**
```java
private static final ThreadLocal<DateFormat> _dateFormat =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

public String format(Date date) {
    return _dateFormat.get().format(date);
}
```

---

## P04 — Multiple SafeCloseables

**Anti-pattern (nested try blocks):**
```java
try (SafeCloseable s1 = CompanyThreadLocal.setWithSafeCloseable(companyId)) {
    try (SafeCloseable s2 = ReindexCacheThreadLocal.openReindexMode()) {
        doWork();
    }
}
```

**Preferred pattern (single try-with-resources):**
```java
try (SafeCloseable s1 = CompanyThreadLocal.setWithSafeCloseable(companyId);
     SafeCloseable s2 = ReindexCacheThreadLocal.openReindexMode()) {

    doWork();
}
```

---

## P05 — Null and Empty Checks

**Anti-pattern:**
```java
if (value == null || value.isEmpty()) {
    return;
}

if (reference != null && !reference.isEmpty()) {
    process(reference);
}
```

**Preferred pattern:**
```java
if (Validator.isNull(value)) {
    return;
}

if (Validator.isNotNull(reference)) {
    process(reference);
}
```

---

## P06 — Hashtable to HashMapDictionary

**Anti-pattern:**
```java
ConcurrentMap<String, Dictionary<?, ?>> configs = new ConcurrentHashMap<>();

Dictionary<?, ?> config = configurationHandler.read(stream); // returns Hashtable
configs.put(key, config); // Hashtable inside ConcurrentMap = double locking
```

**Preferred pattern:**
```java
ConcurrentMap<String, Dictionary<?, ?>> configs = new ConcurrentHashMap<>();

Hashtable<?, ?> raw = configurationHandler.read(stream);
Dictionary<?, ?> config = new HashMapDictionary<>((Map<Object, Object>)raw);
configs.put(key, config);
```

---

## P07 — Static Cache Declaration

**Anti-pattern:**
```java
private static Map<String, ConfigYAML> _configYAMLMap =
    new ConcurrentHashMap<>();
```

**Preferred pattern:**
```java
private static final Map<String, ConfigYAML> _configYAMLMap =
    new ConcurrentHashMap<>();
```

---

## P08 — Petra Unsync I/O

**Anti-pattern:**
```java
byte[] data = inputStream.readAllBytes(); // JDK 9+, not available in JDK 8 targets

ByteArrayOutputStream baos = new ByteArrayOutputStream(); // synchronized
```

**Preferred pattern:**
```java
byte[] data = StreamUtil.toByteArray(inputStream); // petra, JDK 8 compatible

UnsyncByteArrayOutputStream baos = new UnsyncByteArrayOutputStream(); // lock-free
```

---

## P09 — Liferay Utilities for Collection Operations

**Anti-pattern (stream for simple filter):**
```java
List<BlogsEntry> filtered = entries.stream()
    .filter(e -> e.getStatus() == WorkflowConstants.STATUS_APPROVED)
    .collect(Collectors.toList());
```

**Anti-pattern (inline null coercion):**
```java
String value = obj == null ? "default" : obj.toString();
```

**Anti-pattern (manual JSON building):**
```java
String json = "{\"index\":{\"number_of_replicas\":\"" + replicas +
              "\",\"refresh_interval\":\"" + interval + "\"}}";
```

**Preferred pattern (ListUtil filter):**
```java
List<BlogsEntry> filtered = ListUtil.filter(
    entries,
    entry -> entry.getStatus() == WorkflowConstants.STATUS_APPROVED);
```

**Preferred pattern (GetterUtil coercion):**
```java
String value = GetterUtil.getString(obj, "default");
```

**Preferred pattern (JSONUtil builder):**
```java
String json = JSONUtil.put(
    "index",
    JSONUtil.put(
        "number_of_replicas", replicas
    ).put(
        "refresh_interval", interval
    )
).toString();
```

---

## P10 — Bulk Finders vs. N+1 Per-Item Lookups

**Anti-pattern:**
```java
for (long id : ids) {
    Map<String, String[]> prefs = portalPreferencesLocalService.getPreferences(id);
    result.putAll(prefs);
}
```

**Preferred pattern:**
```java
Map<Long, Map<String, String[]>> prefsMap =
    portalPreferencesLocalService.getPreferencesMap(ids);

for (long id : ids) {
    Map<String, String[]> prefs = prefsMap.get(id);

    if (prefs != null) {
        result.putAll(prefs);
    }
}
```

---

## P11 — Caching ThreadLocal Reads

**Anti-pattern:**
```java
if (BackgroundTaskThreadLocal.hasBackgroundTask()) {
    doA();
}

// ... some lines later

if (BackgroundTaskThreadLocal.hasBackgroundTask()) { // second lookup
    doB();
}

// ... some lines later

if (BackgroundTaskThreadLocal.hasBackgroundTask()) { // third lookup
    doC();
}
```

**Preferred pattern:**
```java
boolean hasBackgroundTask = BackgroundTaskThreadLocal.hasBackgroundTask();

if (hasBackgroundTask) {
    doA();
}

if (hasBackgroundTask) {
    doB();
}

if (hasBackgroundTask) {
    doC();
}
```

---

## P12 — Negative Lookup Cache With Singleton Exception

**Anti-pattern:**
```java
public Class<?> loadClass(String name) throws ClassNotFoundException {
    try {
        return super.loadClass(name); // throws every time for missing classes
    } catch (ClassNotFoundException e) {
        throw e; // new stack trace on every miss
    }
}
```

**Preferred pattern:**
```java
private static final ClassNotFoundException _missingException =
    new ClassNotFoundException() {

        @Override
        public Throwable fillInStackTrace() {
            return this; // no stack trace construction
        }

    };

private static final Set<String> _missingClasses =
    ConcurrentHashMap.newKeySet();

public Class<?> loadClass(String name) throws ClassNotFoundException {
    if (_missingClasses.contains(name)) {
        throw _missingException;
    }

    try {
        return super.loadClass(name);
    } catch (ClassNotFoundException e) {
        _missingClasses.add(name);
        throw e;
    }
}
```

---

## P13 — Concrete Typed Method Invocation

**Anti-pattern:**
```java
xContentBuilder.value((Object)floatValue);   // boxes float → Float
xContentBuilder.value((Object)intValue);     // boxes int → Integer
```

**Preferred pattern:**
```java
xContentBuilder.value(floatValue);   // direct float overload
xContentBuilder.value(intValue);     // direct int overload
```

---

## P14 — Direct Invocation vs. Visitor Dispatch

**Anti-pattern:**
```java
documentRequest.accept(_documentRequestExecutor); // visitor double-dispatch
```

**Preferred pattern:**
```java
_documentRequestExecutor.executeBulkDocumentRequest(documentRequest); // direct
```

---

## P15 — Static Utility vs. OSGi Service Injection

**Anti-pattern:**
```java
@Component(service = DocumentBuilderFactory.class)
public class DocumentBuilderFactoryImpl implements DocumentBuilderFactory {

    @Override
    public DocumentBuilder builder(Document document) {
        return new DocumentBuilderImpl(document);
    }

}

// Caller:
@Reference
private DocumentBuilderFactory _documentBuilderFactory;

DocumentBuilder builder = _documentBuilderFactory.builder(document);
```

**Preferred pattern:**
```java
public final class DocumentBuilderFactory {

    public static DocumentBuilder builder(Document document) {
        return new DocumentBuilderImpl(document);
    }

    private DocumentBuilderFactory() {
    }

}

// Caller:
DocumentBuilder builder = DocumentBuilderFactory.builder(document);
```

---

## P16 — Coalescing Small Work Items

**Anti-pattern:**
```java
for (Indexer<?> indexer : indexers) {
    executorService.submit(() -> reindex(indexer)); // one task per indexer, regardless of size
}
```

**Preferred pattern:**
```java
List<Indexer<?>> smallIndexers = new ArrayList<>();

for (Indexer<?> indexer : indexers) {
    long count = indexer.getReindexEntryCount(companyId);

    if (count > _LARGE_INDEXER_THRESHOLD) {
        executorService.submit(() -> reindex(indexer));
    }
    else {
        smallIndexers.add(indexer);
    }
}

if (!smallIndexers.isEmpty()) {
    executorService.submit(
        () -> {
            for (Indexer<?> indexer : smallIndexers) {
                reindex(indexer);
            }
        });
}

private static final long _LARGE_INDEXER_THRESHOLD = 200;
```

---

## P17 — Collect FutureTask Before Gathering Results

**Anti-pattern:**
```java
List<Result> results = Collections.synchronizedList(new ArrayList<>());

for (Item item : items) {
    executorService.execute(
        () -> results.add(process(item))); // silent exception loss
}
```

**Preferred pattern:**
```java
List<FutureTask<Result>> futureTasks = new ArrayList<>();

for (Item item : items) {
    FutureTask<Result> futureTask = new FutureTask<>(() -> process(item));

    futureTasks.add(futureTask);
    executorService.execute(futureTask);
}

List<Result> results = new ArrayList<>();

for (FutureTask<Result> futureTask : futureTasks) {
    try {
        results.add(futureTask.get());
    }
    catch (Exception e) {
        _log.error("Processing failed", e);
    }
}
```

---

## P18 — Largest-First Dispatch

**Anti-pattern:**
```java
for (Indexer<?> indexer : indexers) { // unsorted — small items may finish first
    executorService.submit(() -> reindex(indexer));
}
```

**Preferred pattern:**
```java
indexers.sort(
    Comparator.comparingLong(
        indexer -> -reindexEntryCounts.getOrDefault(indexer, 0L)));

for (Indexer<?> indexer : indexers) {
    executorService.submit(() -> reindex(indexer));
}
```

---

## P19 — Separate Count Query From Data Query

**Anti-pattern:**
```java
long count = indexer.getReindexEntries(companyId).size(); // fetches all records to count
```

**Preferred pattern:**
```java
long count = indexer.getReindexEntryCount(companyId); // SELECT COUNT(*) only

// Implementation uses ProjectionFactoryUtil:
ActionableDynamicQuery adq = getActionableDynamicQuery();

adq.setProjection(ProjectionFactoryUtil.rowCount());

return (long)adq.performCount();
```

---

## P20 — IdentityHashMap for Object-Identity Cache Keys

**Anti-pattern:**
```java
Map<OpenAPIYAML, Map<String, Schema>> cache = new HashMap<>();
// calls OpenAPIYAML.equals() and .hashCode() on every lookup
```

**Preferred pattern:**
```java
Map<OpenAPIYAML, Map<String, Schema>> cache = new IdentityHashMap<>();
// uses == comparison — O(1), no equals() overhead
```

---

## P21 — Early Return Before Expensive Operations

**Anti-pattern:**
```java
public List<Element> getDropZones(FragmentEntryLink fragmentEntryLink) {
    // Parses full HTML even when there are no drop zones
    Elements elements = Jsoup.parse(
        fragmentEntryLink.getHtml()
    ).getElementsByTag("lfr-drop-zone");

    return new ArrayList<>(elements);
}
```

**Preferred pattern:**
```java
public List<Element> getDropZones(FragmentEntryLink fragmentEntryLink) {
    String html = fragmentEntryLink.getHtml();

    if (!html.contains("lfr-drop-zone")) {
        return Collections.emptyList(); // skip expensive DOM parse
    }

    Elements elements = Jsoup.parse(html).getElementsByTag("lfr-drop-zone");

    return new ArrayList<>(elements);
}
```

**Anti-pattern (full deserialize for single field):**
```java
String defaultLanguageId = ddmFormValuesSerializer.serialize(ddmFormValues).get("defaultLanguageId");
```

**Preferred pattern (string search for attribute):**
```java
String xml = DDMFormXSDSerializer.serialize(ddmFormValues);
int index = xml.indexOf("defaultLanguageId=\"");

if (index == -1) {
    return null;
}

int start = index + "defaultLanguageId=\"".length();
int end = xml.indexOf('"', start);

return xml.substring(start, end);
```

---

## P22 — Transient Field Caching for Model Objects

**Anti-pattern:**
```java
public Group getGroup() {
    return GroupLocalServiceUtil.fetchGroup(groupId); // fetches every call
}

// Used three times in same request path:
Group g1 = user.getGroup(); // DB hit
Group g2 = user.getGroup(); // DB hit again
Group g3 = user.getGroup(); // DB hit again
```

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

**Anti-pattern (null sentinel for negative cache):**
```java
private transient PortletFriendlyURLMapperMatch _match;

public PortletFriendlyURLMapperMatch getMatch() {
    if (_match == null) {
        _match = compute(); // could legitimately return null — ambiguous!
    }

    return _match; // null means both "not found" and "not yet looked up"
}
```

**Preferred pattern (sentinel distinguishes not-found from un-fetched):**
```java
private static final PortletFriendlyURLMapperMatch _NULL_MATCH =
    new PortletFriendlyURLMapperMatch();

private transient PortletFriendlyURLMapperMatch _match;

public PortletFriendlyURLMapperMatch getMatch() {
    if (_match == null) {
        PortletFriendlyURLMapperMatch match = compute();

        _match = (match == null) ? _NULL_MATCH : match;
    }

    return (_match == _NULL_MATCH) ? null : _match;
}
```

---

## Petra / Kernel Utility Index

Verified file paths as of 2026-04-17. Run `ls <path>` to confirm before citing in a report.

| Utility | Path |
|---|---|
| `StringBundler` | `modules/core/petra/petra-string/src/main/java/com/liferay/petra/string/StringBundler.java` |
| `StreamUtil` | `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/StreamUtil.java` |
| `UnsyncByteArrayOutputStream` | `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/unsync/UnsyncByteArrayOutputStream.java` |
| `UnsyncByteArrayInputStream` | `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/unsync/UnsyncByteArrayInputStream.java` |
| `UnsyncBufferedReader` | `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/unsync/UnsyncBufferedReader.java` |
| `UnsyncBufferedWriter` | `modules/core/petra/petra-io/src/main/java/com/liferay/petra/io/unsync/UnsyncBufferedWriter.java` |
| `SafeCloseable` | `modules/core/petra/petra-lang/src/main/java/com/liferay/petra/lang/SafeCloseable.java` |
| `HashMapDictionary` | `portal-kernel/src/com/liferay/portal/kernel/util/HashMapDictionary.java` |
| `ListUtil` | `portal-kernel/src/com/liferay/portal/kernel/util/ListUtil.java` |
| `ArrayUtil` | `portal-kernel/src/com/liferay/portal/kernel/util/ArrayUtil.java` |
| `GetterUtil` | `portal-kernel/src/com/liferay/portal/kernel/util/GetterUtil.java` |
| `Validator` | `portal-kernel/src/com/liferay/portal/kernel/util/Validator.java` |
| `JSONUtil` | `portal-kernel/src/com/liferay/portal/kernel/json/JSONUtil.java` |
