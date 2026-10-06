# Privacy Policy — Env Diff Companion

**Effective date:** 2026-10-06

Env Diff Companion is a Gap Hunter Labs plugin for IntelliJ Platform
IDEs. This policy is short because the plugin's design makes it short:
there is nothing to disclose beyond what's below.

## What this plugin collects

**Nothing.** Env Diff Companion does not collect, transmit, or
sell any data — no source code, no file contents, no usage analytics,
no telemetry, no crash reports, no personally identifiable information.
This matters more than usual for a plugin that reads `.env` files: the
comparison only ever looks at KEY names, never at the secret VALUES on
the right side of the `=`, and nothing about either is ever sent
anywhere.

## What it keeps on your machine

To decide when to show its one-time rating prompt, the plugin keeps two values
in the IDE's own settings on your computer: how many findings it has shown and
whether you have answered the prompt. Neither is ever sent anywhere.

## Network access

**None.** Env Diff Companion makes zero network calls during normal
operation. Every comparison it performs runs entirely in-process,
inside your IDE, against the files already in your project. Nothing
you write, open, or edit is ever sent anywhere.

## Third parties

None. Env Diff Companion has no third-party SDKs, no analytics
libraries, no ad networks, no external dependencies that phone home.

## Changes to this policy

If this ever changes, this file will be updated and the change will be
noted in the plugin's `CHANGELOG.md`.

## Contact

Questions about this policy: **gaphunterlabs@gmail.com**
