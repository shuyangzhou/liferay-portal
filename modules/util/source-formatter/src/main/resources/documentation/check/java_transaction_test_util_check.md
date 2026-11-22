## JavaTransactionTestUtilCheck

When a service method is removed because only tests called it, the tests read the persistence themselves through `TransactionTestUtil.invoke`. Such a call is identified by the persistence and the method it reads, for example `AccountEntryUtil.findByC_S`, and it must be kept in one place.

The check reads the call shape `TransactionTestUtil.invoke(<Name>Util.getPersistence(), <parameter> -> <parameter>.<method>(...))` in test code, which is any Java file under a module whose name holds `-test`, a `test` or `testIntegration` directory, or `portal-test`, and any `*TestUtil` class. A test util class is a class in a `-test-util` module, a class in `portal-test`, or a class named `*TestUtil`. Every other class is a test class.

- A call in a test class is reported when another test class makes the same call. Move the call into one test util method that all of them call.
- A call in a test util method is reported when fewer than two other classes call that method. Remove a method that no class calls, and move a method that one class calls into that class.
- A method of a class named `*TestUtil` that reads `<Entity>Util.getPersistence()` is reported unless the class is named `<Entity>TestUtil`. Move the method into the test util of that entity: a method that reads `FooSettingUtil.getPersistence()` belongs in `FooSettingTestUtil`, not in `FooTestUtil`. This rule applies to the persistence read in any shape, with or without `TransactionTestUtil`, and not to a class that is not named `*TestUtil`.

A caller of a test util method is another Java file that names the class with its package, or sits in that package, and calls the method on the class or passes it as a method reference. A caller that reaches the method through a static import or a subclass is not counted.

The check does not report a call in any other shape, such as one that passes a persistence held in a field or a lambda with a block body, a call in a test util class outside a method of the top level class, or a test util method that its own class names again through an overload or a call from another method. The placement rule reads only the methods of the top level class, so a persistence read in a field initializer or a nested class is not reported. Comments and string literals are ignored.

### Example

Incorrect, when `FooLocalServiceTest` makes the same call:

```java
public class FooServiceTest {

	@Test
	public void testAddFoo() throws Exception {
		List<Foo> foos = TransactionTestUtil.invoke(
			FooUtil.getPersistence(),
			fooPersistence -> fooPersistence.findByGroupId(_group.getGroupId()));
	}

}
```

Correct, when both tests call `FooTestUtil.getFoos`:

```java
public class FooTestUtil {

	public static List<Foo> getFoos(long groupId) throws Exception {
		return TransactionTestUtil.invoke(
			FooUtil.getPersistence(),
			fooPersistence -> fooPersistence.findByGroupId(groupId));
	}

}
```