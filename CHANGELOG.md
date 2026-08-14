<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Env Diff Companion Changelog

## [Unreleased]

## [0.1.0]

### Added

- **Compare Env Files**: select two env-style files, get a report of
  keys missing from each side -- `env-diff-report.md`, regenerated in
  place on re-run.
- Presence diff only, not values -- different values per environment
  are treated as expected, not flagged.
- Recognizes `export KEY=VALUE` lines.

[Unreleased]: https://github.com/GapHunterLabs/env-diff-companion/compare/0.1.0...HEAD
[0.1.0]: https://github.com/GapHunterLabs/env-diff-companion/commits/0.1.0
