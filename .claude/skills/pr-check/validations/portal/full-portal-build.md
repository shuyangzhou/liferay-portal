# Full Portal Build

Runs `ant all`, since no Gradle deploy builds portal core. **Per-Module Compile** also hands off to it when its deploy set grows past the point where one full build is cheaper.

## Match

`^(portal-impl|portal-kernel|portal-test|portal-web|support-tomcat|util-bridges|util-java|util-slf4j|util-taglib)/ &! ^portal-web/test/|\.properties$`

## Command

```bash
ant all -Dgradle.stop.daemon.enabled=false
```

`ant all` is `clean` + `compile` + `deploy`; the deploy target's marketplace branch deploys every project with a `.lfrbuild-portal` marker.

PASS on `BUILD SUCCESSFUL`. FAIL on `BUILD FAILED`, and name the failing task, the file the first error points at, and whether the diff changed that file:

```bash
git diff --name-only "${MERGE_BASE}...HEAD" -- '<file>'
```

A generated source such as `build/jspc/.../default_005faddresses_jsp.java` is not the file to name. Name the source it was generated from, `default_addresses.jsp` in that example.

## Notes

When this succeeds, **Per-Module Compile** drops the `.lfrbuild-portal` modules it would otherwise deploy, since `ant all` already deployed them. When it fails, it produces no compile signal and obviates nothing.

`ant all` compiles no `testIntegration` source and no module carrying `.lfrbuild-portal-deprecated`, so **Cross-Module Compile** runs whatever this returns.

## Time Estimate

~8 min.