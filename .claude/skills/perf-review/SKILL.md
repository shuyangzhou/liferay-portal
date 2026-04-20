---
allowed-tools: [Bash, Glob, Grep, Read]
argument-hint: "[optional file or module path]"
description: Review the current branch's Java and JSP changes for known performance anti-patterns and report violations with file:line references. Use when the user asks for a performance review, asks to check changes for perf issues, mentions scalability concerns, or invokes /perf-review.
name: perf-review
---

# Performance Review

Scan the current branch's changed Java and JSP/JSPF files for performance anti-patterns documented by Liferay's performance engineering standards. Report violations with file:line references and recommended fixes. Do not make any edits.

## 1. Gather Context

Identify the changed files:

```bash
git diff master...HEAD --name-only
```

Filter the results to `*.java`, `*.jsp`, and `*.jspf` files only. When `${ARGUMENTS}` supplies a file path or module path, restrict the scan to files under that path. When no Java or JSP files have changed, report that explicitly and stop.

Retrieve the diff for all relevant files:

```bash
git diff master...HEAD -- "*.java" "*.jsp" "*.jspf"
```

When `${ARGUMENTS}` restricts scope, pass the path to the diff command as well.

## 2. Load the Principle Catalog

Read both reference files in full before scanning:

- `${CLAUDE_SKILL_DIR}/references/principles.md` — the numbered principle catalog (P01–P20) with detection signatures and rationale.
- `${CLAUDE_SKILL_DIR}/references/patterns.md` — the anti-pattern → preferred-pattern code samples and the Petra/Kernel Utility Index.

Do not reproduce these files in output. Reference them by principle ID when reporting findings.

## 3. Scan Each Changed File

For each changed file:

1. Read the diff lines (added and modified lines only — do not flag deleted code).
2. Read the full file with the Read tool to understand surrounding context when a finding is ambiguous.
3. Check each added or modified line against the anti-pattern signatures in `patterns.md`.
4. When a potential violation is found, use Grep to check whether the preferred utility class or pattern is already available in the same file or module before flagging — do not flag code that already uses the preferred approach elsewhere in the same method.
5. Record every confirmed violation with: file path, line number, principle ID, offending code excerpt, and the preferred pattern from `patterns.md`.

Focus on lines the author added or changed. Do not flag pre-existing code that was not touched by the branch.

## 4. Report Findings

Produce a single markdown report. Group findings by file, then by principle within each file. For each violation use this format:

```
### <file-path>:<line-number> — <P-ID> <Principle Title>

**Offending code:**
```java
<excerpt>
```

**Preferred pattern:**
```java
<excerpt from patterns.md>
```

**Why:** <one sentence rooted in the principle rationale>
```

When a file has no violations, list it under a **Clean Files** section at the end — do not silently omit it.

Do not make any edits. Do not open PRs. Do not post to Jira.

## 5. Summary

After all findings, append a summary table:

| Metric | Value |
|---|---|
| Files reviewed | N |
| Files with violations | N |
| Total violations | N |
| Most frequent principle | P-ID — Title |

List each triggered principle with its violation count. When no violations are found across all files, state that explicitly: "No performance violations found."
