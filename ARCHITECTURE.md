# Recover — Architecture

## Fresh project and stack
Assume no existing source or Gradle structure. Recommended baseline: Kotlin + Jetpack Compose, one Android application ID and one installable app. Keep UI, domain rules, persistence and Android enforcement separate.

Suggested layers: `ui/` (Compose/screens/navigation/theme), `domain/` (session/credit rules), `data/` (repositories and persistence), `platform/` (app discovery, permissions, enforcement, overlays, camera/sensors and recovery), `docs/`.

## Source of truth
- Room: sessions, credit ledger, selected blocked packages, unlock grants.
- DataStore: simple preferences.
- Ledger determines balance; UI never mutates balance directly.
- Session records determine history/rewards.
- Permission state comes from Android APIs/settings, not local pretend toggles.
- Use monotonic elapsed time for timers and document reboot/process recovery.

## Minimum models
`FocusSession` (stable ID, planned duration, start, status, end reason, reward status, strikes); `CreditTransaction` (unique ID, signed delta, type, related session/unlock, idempotency key); `BlockedApp` (package name, label, icon, selected); `UnlockGrant` (package, start/expiry, debit transaction, state); `PermissionStatus` (capability, actual state, checked time); `UserSettings`.

## Installed-app discovery
Use a compliant system picker or package discovery mechanism. Inspect package visibility rules for the selected target SDK. Do not hardcode common apps. Store by package name, not label. Provide empty/error states.

## Enforcement decision record before implementation
Document exact foreground detection/block API, permissions, grant verification, unlock exception logic, recovery after process death/reboot, revocation behavior, Play-policy implications, OEM limitations and physical-device test plan. A visual overlay alone is not blocking.

Conceptual decision: identify foreground package → if not selected, allow → if exact package has valid unexpired unlock, allow → otherwise enforce block under active policy → at expiry invalidate grant and block again. Recheck on app transitions and recovery. Overlay visibility is never authoritative.

## Session state machine
Use explicit states for setup, camera/AR preparation, active, completed normally, ended early, interrupted, failed/recovery. Follow ordered frames in `assets/focus session/`; do not infer the whole sequence from filenames alone. Camera permission is contextual to camera-dependent AR/presence. AR is mandatory. Define third-strike behavior before coding. Emergency exit must be safe.

For End Session: active + explicit tap → confirmation. Cancel returns to active session. Confirm finalizes early once, applies zero reward under all-or-nothing policy, then shows a native result screen matching `ezgif-frame-012.jpg`, with Return to Dashboard. Timer expiry uses a different normal-completion path.

## Ledger transactions
Unlock: validate selected package and balance → create idempotency key → commit debit and unlock grant atomically → on failure, no charge → on retry, query key before any new debit. Recheck expiry independently of UI countdown. Rewards: finalize session and insert a single eligible ledger transaction atomically/idempotently.

## Permissions
Use a capability matrix. Accessibility only if chosen for enforcement; overlay only if required; Usage Access only if using UsageStatsManager; camera for mandatory camera-dependent AR/presence; notifications if the real feature needs them. Battery exemption only if testing proves necessity. Recheck permissions on resume. Never claim a UI switch itself grants system permission.

## Tests
Unit tests: reward calculation/qualification, early-end zero reward, idempotency, ledger balance, package matching, unlock expiry, analytics thresholds, state transitions. Instrumentation: navigation, permission recheck, process recreation and database transactions. Physical device: app discovery, blocking, overlays, app switches, expiry/re-block, reboot/clock changes, camera/AR/strikes and OEM behavior.
