## BeanParamUtilGetterCheck

Pass the getter of the bean to `BeanParamUtil` instead of letting it look up the property by reflection. The getter is a method reference, so the compiler checks that the property exists and has the right type, and reading it costs a plain method call.

### Example

Incorrect:

```java
long folderId = BeanParamUtil.getLong(folder, request, "folderId");

String name = BeanParamUtil.getString(
    folder, request, "name", LanguageUtil.get(request, "untitled"));
```

Correct:

```java
long folderId = BeanParamUtil.getLong(
    folder, request, "folderId", BookmarksFolder::getFolderId);

String name = BeanParamUtil.getString(
    folder, request, "name", BookmarksFolder::getName,
    LanguageUtil.get(request, "untitled"));
```

The check fixes `.java` files and the Java code of `.jsp` and `.jspf` files. It inserts the getter after the parameter name, and it replaces a `*Silent` method with the method of the same type.

A call already passes a function when its fourth argument is a lambda, a method reference, or a name or a method call that ends in `Function` or `function`, such as `toLongFunction` or `getBeanPropertyFunction(name, value)`. The check leaves such a call alone, whatever its parameter name.

The type of the bean comes from the declaration of the variable or field, from the return type of a method without parameters declared in the same file, or from a cast. In a `.jsp` or `.jspf` file, the check also reads the files that it includes and the files that include it. The getter is `get` followed by the parameter name with its first letter in upper case, or `is` for `getBoolean` when the type has it. The check reads the source of the type and of the types that it extends.

The check reports the call instead of fixing it when:

- The parameter name is not a string literal.
- It cannot resolve the type of the bean, or the type is `Object` or a type variable.
- It cannot find the source of the type.
- The type has no getter for the parameter name. The reflective lookup then always returns the default value, so use `ParamUtil` instead.
- The getter returns a type that does not fit the method, for example a `BigDecimal` for `getDouble`, or a wrapper type that can be `null`.
- The getter declares a thrown exception, so it does not fit the function that the method takes.