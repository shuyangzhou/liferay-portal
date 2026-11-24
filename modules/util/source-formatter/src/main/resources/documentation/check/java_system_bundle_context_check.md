## JavaSystemBundleContextCheck

Module code has a `BundleContext` of its own, so it does not use the system
bundle's context through `SystemBundleUtil.getBundleContext()`. The system
bundle's context hides which bundle registers a service or opens a tracker, so a
registration outlives the bundle that made it.

- A component uses the `BundleContext` its `@Activate` method receives.
- A static utility or a plain object uses `FrameworkUtil.getBundle(<Class>.class)`
and its `BundleContext`.
- An integration test uses `BundleContextTestUtil.getBundleContext(<Class>.class)`.

Unit tests under `src/test` run without an OSGi framework and are not checked.
Files that keep the system bundle's context on purpose, such as code that runs
in a second portal JVM or outside any bundle, are listed in the
`allowedFileNames` property.

### Example

Incorrect:

```java
BundleContext bundleContext = SystemBundleUtil.getBundleContext();
```

Correct:

```java
Bundle bundle = FrameworkUtil.getBundle(XStreamConfiguratorRegistryUtil.class);

BundleContext bundleContext = bundle.getBundleContext();
```