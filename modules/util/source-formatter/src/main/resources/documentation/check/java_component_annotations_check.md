## JavaComponentAnnotationsCheck

### Required `configurationPolicy`

A `@Component` whose behavior is entirely gated by an off by default switch in its system scoped configuration must declare `configurationPolicy = ConfigurationPolicy.REQUIRE`. Out of the box no configuration is deployed, so with the default optional policy the component activates only to find the switch off and do nothing. With a required policy it stays inactive until an administrator deploys the configuration.

The check applies when all of the following hold:

- The component declares a single `configurationPid` whose `@Meta.OCD` interface can be found, and no `configurationPolicy` other than `ConfigurationPolicy.OPTIONAL`.
- The interface is not a factory configuration and its `@ExtendedObjectClassDefinition` scope, if any, is `SYSTEM`.
- The interface has a `boolean` attribute with `deflt = "false"` whose name contains `enable`, the switch.
- The component reads the switch, directly from the configuration object, from `GetterUtil.getBoolean(properties.get("<switch>"))`, or from a field assigned one of these, and either:
  - an `is*` method returns the switch, no other public method reads the configuration, and some other public method does more than return a field or a getter, as with an `isEnabled()` or `isAvailable()` that the caller consults before using the component; or
  - every public method the component class declares starts with `if (!<switch> || ...) { return ...; }` or consists of a single `if (<switch> && ...) { ... }` without an `else`.

Methods annotated with `@Activate`, `@Deactivate`, `@Modified` or `@Reference` are ignored, and methods inherited from a superclass whose source can be found count unless the component overrides them.

Company and group scoped configurations and configurations read at use time through `ConfigurationProvider` cannot use a required policy and are out of scope. Test code is skipped. A component whose consumers cannot work without it is listed in the `allowedOptionalConfigurationPolicyClassNames` property.

#### Example

Incorrect:

```java
@Component(
	configurationPid = "com.liferay.foo.configuration.FooConfiguration",
	service = FooTracker.class
)
public class FooTracker {

	public void track(String url) {
		if (!_fooConfiguration.enabled()) {
			return;
		}

		_fooEntryLocalService.addFooEntry(url);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_fooConfiguration = ConfigurableUtil.createConfigurable(
			FooConfiguration.class, properties);
	}

	private volatile FooConfiguration _fooConfiguration;

	@Reference
	private FooEntryLocalService _fooEntryLocalService;

}
```

Correct:

```java
@Component(
	configurationPid = "com.liferay.foo.configuration.FooConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE,
	service = FooTracker.class
)
public class FooTracker {

	public void track(String url) {
		if (!_fooConfiguration.enabled()) {
			return;
		}

		_fooEntryLocalService.addFooEntry(url);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_fooConfiguration = ConfigurableUtil.createConfigurable(
			FooConfiguration.class, properties);
	}

	private volatile FooConfiguration _fooConfiguration;

	@Reference
	private FooEntryLocalService _fooEntryLocalService;

}
```