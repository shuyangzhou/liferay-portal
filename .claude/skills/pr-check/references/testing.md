# Testing Changes to PR Check

A change to this skill is prose, so reading it proves nothing. Test it by running the text against changes planted for the purpose on a branch that is never pushed. Judge the result from git and from the logs rather than from the summary a run prints.

## Selection

When a change touches a `## Match` section, the routing, the list in `SKILL.md`, or `select_paths.sh`, resolve the paths of recent commits on the base branch and select from them twice, once with the rules on the base branch and once with the change, and account for every validation and path that differs. Include a path the change must select and one it must not, so that a broken comparison reads as a disagreement rather than as agreement. Running `select_paths.sh` on a single validation is the quickest check of what it selects, and piping a single path into `find_modules.sh` the quickest check of its module.

## Scripts

When a change touches `select_paths.sh`, `select_validations.sh`, or `find_modules.sh`, test it locally in a throwaway Git repository, with commit signing turned off, that plants the marker files each case needs, and compare every printed line with the line you expect. None of the scripts carries committed test cases.

For `find_modules.sh`, cover a path under `src/main`, `src/test`, `src/testIntegration`, and `src/jmh`; a `.groovy` resource; a path with a space; a nested module, which resolves to the outermost; a module deleted on the branch, which resolves through the merge base; `modules/.releng`; `modules/test/playwright`, which is no module; Poshi and `portal-web/test`; a file at the repository root; and, in a workspace, a client extension, a module, and a theme, which is no module there.

For `select_paths.sh`, cover a branch, a portal, and a workspace validation; a `## Match` with an ` &! ` exclude side; a portal validation that must not see workspace paths; the regenerated and owned paths of a workspace other than `liferay-sample-workspace`; and the three errors: a file with no `## Match`, an unknown folder, and a workspace validation without a workspace name.

Break each rule in a copy of the script and confirm that a case fails, so that a case that can never fail does not read as coverage.

## Commands

When a change touches a `## Command` or `## Autocommit` section, extract exactly that section, as the runner hands it to a subagent, and confirm the extract carries the change and nothing from the rest of the file. Set `${SKILL_DIR}`, `${MERGE_BASE}`, and `${VALIDATION_FILE}` the way the runner does, since a **Command** builds its paths, modules, and projects with `select_paths.sh` and has nothing to work on without them. Give the new text and the text on `master` to two subagents that know nothing about the change, with the same planted diff, and ask each for its verdict and the sentence that decided it. A change that works splits the verdicts. Identical verdicts mean the change made no difference where it is read.

When a command reimplements a rule another tool enforces, run both against the same planted cases and require them to agree, including a case that must fail on both sides. A command that crashes prints nothing, which reads as a pass, so check its exit status as well as its output.

Run every shell command in a validation under both zsh and bash. The Bash tool runs zsh on macOS, and zsh does not split an unquoted variable or command substitution into words, so under zsh a command that builds its arguments in a variable passes them to the tool as a single argument.

## Whole Runs

Run `/pr-check` from the root of a worktree that holds the planted changes, for example with `claude -p "/pr-check"`. A headless session cannot answer the question Pass 1 asks when the estimate exceeds 20 minutes, so say in the prompt to proceed without asking. Plant one change for each outcome to prove, each in its own commit, and check the verdicts against git, for example against the `<TICKET> SF` commit a formatter makes.

A branch in this repository always carries the rule change in its own diff, so a test run here also reports on the `.claude` files the change touched. That is expected.

To test the rules as the private repository runs them, copy `pr`, `pr-check`, and `pr-check-publish` from the branch holding the change into a worktree of `liferay-portal-ee` on `master-private`. The copies do not come from local `master`, so name the rules commit in the prompt.

## Measurements

A change that can alter how long a run takes or how many tokens it uses, such as a new precondition, a narrower `## Match`, or a different **Command**, comes with numbers. Run `/pr-check` twice on the same planted branch, once with the rules on the base branch and once with the change, and put both sets of numbers side by side in the pull request description. A number without its base run says nothing about the change.

For each run, record the wall time of the whole run and of each shared precondition, and the tokens the runner and every subagent used together. A headless session writes its transcript to `~/.claude/projects/<project>/<session>.jsonl` and each subagent's to `~/.claude/projects/<project>/<session>/subagents`, and the sum of the `usage` of every message across those files is the total.

Run both on an idle machine and record its load average, since a build under load can take several times as long and reads as a slower change. Start both from the same Gradle build cache, empty or full, and say which, since a full cache hides most of what a build costs.