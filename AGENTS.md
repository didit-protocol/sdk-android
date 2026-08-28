# sdk-android

## Public repository privacy gate

This is a public distribution repository. Every pull request title and body,
issue, comment, review, commit message, branch name, release note, screenshot,
log, fixture, and published artifact must be safe for anyone on the internet to
read.

- Never publish customer or prospect names, organization names, contact names,
  email addresses, account or session identifiers, private support details,
  private tracker keys, or links to private repositories and internal tools.
- Describe reports generically, for example "a physical-device report" or "an
  integrator", and keep the identifiable source only in the private tracker.
- Before posting, pushing, or tagging, review the full text and inspect artifact
  metadata. If any detail might identify a customer, person, account, private
  system, or build machine, stop and sanitize it first.
- Public GitHub content is permanent disclosure even when later edited. Treat
  this as a hard pre-publication gate, not a cleanup task.

## Release rules

- Never commit directly to `main`; release artifacts through a pull request.
- Verify all six Maven modules share the release version and that their POM
  dependencies point to the same version.
- Tag only the reviewed merge commit after the release PR is merged.
