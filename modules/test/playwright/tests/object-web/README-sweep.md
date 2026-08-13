# Object Suite Flake Sweep

Placeholder for repeated full `ci:test:object` runs against master. Each sweep re-runs the
whole suite on an otherwise unchanged tree so that any failure is a flake rather than a code
change.

Run B also carries the pending `site-cms-site-initializer-api` baseline fix, so the sweep
measures flakes instead of re-reporting a break that fails every build on master.

Not for merge.
