# Env Diff Companion

IntelliJ-family plugin. Select exactly two env-style files in the
Project view — `.env` vs `.env.example`, `.env.development` vs
`.env.production`, anything with `KEY=VALUE` lines — run **Compare Env
Files**, and get a real report of which keys are missing from each
side.

## Why it exists

Ports a pattern that's genuinely popular elsewhere — env-diff/env-sync
tooling exists across other editors and CLI tools — with no real
equivalent anywhere in JetBrains Marketplace (confirmed by search
before building this, not assumed). A deliberate "port a proven
concept" bet — see `CONSTITUTION.md` §1 for the documented-exception
discipline this follows (same treatment as Refactor Simulator/Bean
Copy Companion/Turbo Log Companion/Change Case Companion/JSON to Code
Companion).

The #1 real-world bug this catches: a new environment variable added
to `.env` during development and never added to `.env.example`, so the
next person to set up the project has no idea it's required.

## Why built this way

- **Presence diff, not a values diff — deliberately.** Different
  values per environment (a dev vs. prod database URL) are usually
  intentional, not a bug; only whether a key exists at all is a real
  signal worth flagging. A values diff would just be noise for the
  most common real use case.
- **Plain-text `KEY=VALUE` parsing, never PSI.** Works identically
  whether or not the platform's own `.env` language plugin is
  installed, and on any filename — `.env` files are famously not
  always named with a recognizable extension.
- **Recognizes `export KEY=VALUE`**, a real, common shell-sourced
  `.env` convention some projects use.
- **Regenerates its own report in place** (`env-diff-report.md`, same
  file every run) rather than piling up numbered copies — it's a
  disposable, regenerate-anytime artifact, not something meant to
  accumulate history.
- **100% local** — no network call, no account, no telemetry.

## Usage

Select exactly two files in the Project view (Ctrl/Cmd+click) →
right-click → **Compare Env Files**. Opens `env-diff-report.md` next
to the compared files.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us
at **gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
