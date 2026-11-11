## AUIValueFunctionCheck

Pass the getter of the bean to `aui:input` and `aui:select` in the `valueFunction` attribute. The getter is a method reference, so the compiler checks that the property exists, and the tag reads it with a plain method call instead of looking it up by name.

### Example

Incorrect:

```jsp
<aui:model-context bean="<%= organization %>" model="<%= Organization.class %>" />

<aui:input name="comments" type="textarea" />

<aui:select listType="<%= ListTypeConstants.ORGANIZATION_STATUS %>" listTypeFieldName="statusListTypeId" name="statusId" />
```

Correct:

```jsp
<aui:model-context bean="<%= organization %>" model="<%= Organization.class %>" />

<aui:input name="comments" type="textarea" valueFunction="<%= Organization::getComments %>" />

<aui:select listType="<%= ListTypeConstants.ORGANIZATION_STATUS %>" listTypeFieldName="statusListTypeId" name="statusId" valueFunction="<%= Organization::getStatusListTypeId %>" />
```

The check fixes `.jsp` and `.jspf` files. It adds the attribute in its sorted position and leaves a tag that already has a `valueFunction` alone.

The check looks at the tags that read the bean by name:

- An `aui:input` with a `type` of `color`, `email`, `number`, `range`, `tel`, `text` or `textarea`, unless `ignoreRequestValue` is `true`. The property is the `name` of the input.
- An `aui:input` with a `type` of `hidden` and no `value`. The property is the `name` of the input.
- An `aui:input` with a `type` of `timeZone` and no `value`. The property is the `field` of the input, or its `name` when the tag has none.
- An `aui:select`. With a `listType`, the property is the `listTypeFieldName`, or `listTypeId` when the tag has none. Without one, the property is the `name` of the select.

A hidden or time zone input reads its `valueFunction` only when its `value` is `null`, so the two do not go together on these inputs. The check adds the attribute only to a tag without a `value`, and reports a hidden or time zone input that has both. A page that needs the bean as a fallback writes it into the `value` expression.

The bean is the `bean` attribute of the tag, or the bean of the `aui:model-context` that is active at the tag. The check looks for that `aui:model-context` earlier in the file, in the files included before the tag, and in the files that include the file.

The type of the bean comes from the declaration of the variable, from the `className` of the `liferay-ui:search-container-row` that declares it as its `modelVar`, from the tag that defines it for a variable such as `user` or `stagingGroup`, from the return type of the method for a bean such as `displayContext.getEntry()`, or from a cast. When the check cannot tell, it uses the `model` class. The getter is `get` followed by the property name with its first letter in upper case. The check reads the source of the type and of the types that it extends.

When the type has no such getter, the name is not a bean property and the check leaves the tag alone. When the getter is overloaded, a method reference is ambiguous, so the check adds a lambda with a typed parameter:

```jsp
<aui:input name="title" type="text" valueFunction="<%= (AssetCategory assetCategory) -> assetCategory.getTitle() %>" />
```

The check reports the tag instead of fixing it when:

- It cannot resolve the type of the bean, or the type is `Object`.
- It cannot find the source of the type or of a type that it extends, and it found no getter.
- The type of the bean of an `aui:select` with a `listType` has no getter of its property. Such a select shows the list type of its bean, so the name is a mistake rather than a field that is not a bean property.
- The getter of an `aui:select` with a `listType` does not return `byte`, `int`, `long` or `short`.
- The getter declares a thrown exception.
- The getter is overloaded and both lambda parameter names, such as `assetCategory` and `curAssetCategory`, are taken.
- The file is included by pages whose beans have different types.