# Per-Module Compile

Deploys each module the branch changed, which checks that it compiles and bundles its resources whatever the change was. The jar task runs `compileJSP`, so a deploy also compiles the module's JSPs, apart from a fragment's, which compile only against their host. When the deploy set grows past the point where one full build is cheaper, it hands off to **Full Portal Build**. Modules carrying `.lfrbuild-portal-deprecated` and the `testIntegration` source of `-test` modules belong to **Cross-Module Compile** instead. A deploy compiles `src/main` alone, so a path under `src/test` or `src/testIntegration` selects nothing here, and a module whose only change sits there is left to **Java Unit Tests** and **Cross-Module Compile**.

## Match

`^modules/.+\.(java|js|jsx|mjs|cjs|ts|tsx|css|scss|sass|ftl|jsp|jspf)$|^modules/.+/src/main/.+\.properties$|^modules/.+/(bnd\.bnd|gradle\.properties|package-lock\.json|yarn\.lock|package\.json)$ &! ^modules/test/playwright/|/src/test/|/src/testIntegration/|(^|/)test\.properties$`

## Preconditions

- Portal Snapshots

## Command

The deploy set is the Gradle project paths of the changed modules, such as `apps:blogs:blogs-api`. The expansions below match on that form, not on the directory:

```bash
bash "${SKILL_DIR}/select_paths.sh" "${MERGE_BASE}" "${VALIDATION_FILE}" \
	| bash "${SKILL_DIR}/find_modules.sh" "${MERGE_BASE}" \
	| cut -d " " -f1 \
	| command grep '^modules/' \
	| sort --unique \
	| sed "s#^modules/##; s#/#:#g"
```

Drop a module the branch deleted, which `find_modules.sh` still names through the merge base but which has nothing left to deploy, and report its paths below as paths that sit in no module. A module was deleted when `git cat-file -e "HEAD:<module directory>"` fails. When the runner says Full Portal Build ran and succeeded, drop each module carrying `.lfrbuild-portal` as well, since `ant all` already deployed it. When the runner says it failed, drop nothing, since a failed `ant all` stops at its first error and vouches for no module.

Expand by consumers only when the change can break one. An added `public` or `protected` member is source and binary compatible, so it expands nothing. A removed member, or one whose signature changed, does break consumers. Collect the removed and added member lines separately and expand only on a removal with no matching addition, since a member that was moved or reformatted appears as both and breaks nobody:

```bash
git diff "${MERGE_BASE}...HEAD" -- '<changed file>' | command grep --extended-regexp '^-\s*(public|protected)\b'
git diff "${MERGE_BASE}...HEAD" -- '<changed file>' | command grep --extended-regexp '^\+\s*(public|protected)\b'
```

Take the consumers that name the changed **type**, not every module that declares a dependency on its project. A project edge means a module could see the type; only a source reference means it does. Search for the fully qualified name, the package from the file's `package` line followed by the file name without `.java`, since a file outside the package has to import the type or spell that name out and the repository has no `com.liferay` wildcard imports. A file in the same package needs no import, so search those for the simple name. Search the index, since a recursive `command grep` over `modules` descends into `build` and `node_modules` and does not finish:

```bash
(cd "${REPO_ROOT}" && git grep --cached --files-with-matches --fixed-strings --word-regexp '<FullyQualifiedName>' -- '*.java')
(cd "${REPO_ROOT}" && git grep --all-match --cached --files-with-matches --fixed-strings --word-regexp -e 'package <package>;' -e '<TypeName>' -- '*.java')
```

Take the project of each file either search lists:

```bash
printf '%s\n' <consumer file>... \
	| bash "${SKILL_DIR}/find_modules.sh" "${MERGE_BASE}" \
	| cut -d " " -f1 \
	| command grep '^modules/' \
	| sort --unique \
	| sed "s#^modules/##; s#/#:#g"
```

A simple name collides with every unrelated type of the same name. `Test` alone matches each `import org.junit.Test`, 6,661 files outside the module that declares it, while its fully qualified name matches none.

The difference is not marginal. Removing a member from a mid sized API class put 188 modules on the project edge and 6 on the type reference, and only 2 of those were production consumers that could break. Match the type rather than the member name, which collides across unrelated classes.

Drop any module that is a `-test` or `-test-util` module or carries `.lfrbuild-portal-deprecated`.

Apply the handoff below to the set you now have, **before** capping it. When the handoff does not fire, cap the consumers at 12 in sorted path order so two runs on the same diff build the same set, and name the full consumer count in the result.

This expansion reads Java signatures, so it says nothing about a consumer that breaks any other way. A marker change is [module-registration.md](module-registration.md)'s, and a resource or configuration change reaches consumers this test cannot see.

Expand by shared build tooling: `modules/frontend-sdk/**` and `modules/node-scripts.config.js` feed every module's JavaScript build, so a change to either breaks deploys the diff never touched, and no `project(":...")` edge leads to them.

Take one of three branches for `modules/node-scripts.config.js`, in order.

1. The change is only a comment or a blank line, so it cannot alter a build. Skip it and expand nothing.

1. The content changed but the `hash` field did not. The file's own header declares it generated from each module's own `node-scripts.config.js`, so this is a hand edit. Report it as a finding, name the file, and expand nothing.

1. The file was regenerated, `hash` and all. Add the modules whose entry in the `imports` map is a nonempty array. Those entries are the ones that expose a package to other modules' builds, so they are the edges a change travels along, while the empty ones expose nothing. Strip any `@liferay/` prefix and resolve the name to a module directory under `modules/apps` or `modules/dxp/apps`.

When the deploy set exceeds **40 modules**, stop and hand off to [full-portal-build.md](full-portal-build.md). One `ant all` costs about 8 minutes and covers every module, while each module here is a separate `gradlew` invocation measured at about 11 seconds on a warm daemon, almost all of it per invocation overhead. That puts the crossover near 44, and lower on a cold tree. A build that covers everything beats a sample of a set too large to finish.

Report the count and the cost math, run Full Portal Build in this validation's place, and give this row **its** result. When Full Portal Build is not in the run or does not produce one, report **NOT VERIFIED** naming the count, since this validation then has no result of its own either. The verdict rules below apply only when no handoff happened.

Run the lockfile check regardless, since it needs no build and a handoff does not make a mismatched dependency any less broken.

Deploy each module. The **Portal Snapshots** precondition rebuilds the `portal-kernel`/`portal-impl` snapshot from the branch tree before any module compiles, so a module referencing a portal core symbol is checked against the branch's kernel rather than a stale snapshot. A kernel change from a separate PR that is not yet merged is only caught once local `master` includes it, since pr-check never fetches a remote.

```bash
("${REPO_ROOT}/gradlew" \
	--parallel \
	--project-dir "${REPO_ROOT}/modules" \
	:<path>:deploy)
```

FAIL when a module the diff **changed** reports `BUILD FAILED`, and name the module and the compiler error.

A module reached only by an expansion is one the branch never touched, so judge its failure by cause. FAIL when the error names a symbol from the changed module. When it names none of them, the module is already broken on the merge base, so report it and do not fail the branch. The common shape is a `-test` module failing on a package that exists in the tree, which fails on every branch alike. Confirm that by compiling one module with no dependency on anything the diff touched, and when it fails the same way the breakage is the environment's.

A `deploy` never installs a newly added npm dependency, so it cannot tell you whether one resolves. `modules` is a yarn workspace, the root `yarnInstall` owns installs, and both it and every module's `npmInstall` report `SKIPPED` on a normal deploy whatever the diff changed. `packageRunBuild` then runs against whatever is already in `node_modules`.

Check the lockfile instead, which needs no build at all. A dependency the diff adds or changes in a `package.json` has to be matched by a change to `modules/yarn.lock`, or CI's `yarn install --frozen-lockfile` rejects the branch. FAIL when the diff changes a `dependencies` or `devDependencies` entry and touches no lockfile, and name the package and the range.

Confirm the range against what is actually locked, since an entry can be present at a version that does not satisfy it:

```bash
command grep --fixed-strings '<package>@' "${REPO_ROOT}/modules/yarn.lock"
```

Do not hand this to [javascript-unit-test.md](javascript-unit-test.md). Jest resolves files rather than version ranges, so a module whose sources never import the package, or which resolves the hoisted copy, passes its suite with the declared range never evaluated.

Treat `UP-TO-DATE` on a changed module's own `compileJava` with the same suspicion. Gradle's cache has served a stale output in this repository before, so confirm the change reached the jar rather than reading the task line as proof.

A changed path that sits in no module, other than the shared tooling above, has nothing to build. Find those paths by resolving the changed paths:

```bash
bash "${SKILL_DIR}/select_paths.sh" "${MERGE_BASE}" "${VALIDATION_FILE}" \
	| bash "${SKILL_DIR}/find_modules.sh" "${MERGE_BASE}" \
	| command grep '^- ' \
	| cut -d " " -f2-
```

Report **NO COVERAGE** naming every such path, and also when that tooling expanded to no module. When a changed path does sit inside a module and the set is still empty, the derivation is broken, so report that as a FAIL. The validation passes when every module in the deploy set reports `BUILD SUCCESSFUL`.

## Checklist

```
- [ ] (One subitem per deploy-set module:) Deploy <module path>
```

## Time Estimate

About 10 sec per module on a warm daemon and a minute or more on a cold one. Each module is its own `gradlew` invocation paying its own configuration, and `--parallel` works within an invocation rather than across them, so the cost is linear in N and the cap above is what keeps it bounded.