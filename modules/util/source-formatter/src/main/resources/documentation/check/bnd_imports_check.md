## BNDImportsCheck

Wildcards are not allowed in `bnd` files.

### Example

Incorrect:

```
Export-Package:\
    com.liferay.account.*,\
    com.liferay.account.service.persistence
```

Correct:

```
Export-Package:\
    com.liferay.account.constants,\
    com.liferay.account.exception,\
    com.liferay.account.model,\
    com.liferay.account.retriever,\
    com.liferay.account.role,\
    com.liferay.account.service,\
    com.liferay.account.service.persistence
```

An entry of `Export-Package` or `-exportcontents` whose last segment starts upper case names a class, not a package, and is not allowed. A module that generates its classes from `wsdl` or `xsd`, whose XML namespaces can map to such package names, and a package directory in the module's own sources are exceptions. This does not apply to a `.bnd` file inside a `src` directory.