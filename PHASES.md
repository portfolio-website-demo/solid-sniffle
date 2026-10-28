# Recover — Implementation Phases

Complete each phase and stop at its boundary.

## Phase 0 — Asset audit (do first)
Read all docs; recursively inspect `assets/`; create `docs/asset-map.md` listing every relative path, dimensions, format, classification, target screen, sequence and confidence. Inspect the focus-session frames in order and identify frame 012. Identify the real logo and single white Focus Card asset. Record ambiguity and propose enforcement/permission architecture. Do not build screens yet. Report and stop.

## Phase 1 — Fresh foundation
Create Kotlin + Compose project, single app ID, theme/navigation skeleton, Room/DataStore baseline and tests. Add only confirmed production assets. Run debug build and tests. Stop and report.

## Phase 2 — Design system and screen shells
Create reusable theme/components and native screen shells matching references: splash, onboarding, auth, Pro offer, permissions, app selection, dashboard, focus setup, wallet, transactions, analytics, rankings, account/settings. Wire navigation; no reference screenshots as backgrounds.

## Phase 3 — Onboarding, permissions, app selector
Implement onboarding; contextual OS permission flow and actual state checks; installed-app discovery; persistence and later editing of selected packages. Test on device.

## Phase 4 — Focus and mandatory AR/presence
Implement state machine, duration setup, camera/presence, placement zone where referenced, countdown, strikes and safe emergency exit. Define third-strike outcome. Persist/recover session state.

## Phase 5 — Enforcement and unlock
Implement documented Android enforcement architecture. Block selected apps during focus; one credit/60 seconds unlock for one selected package; accurate countdown where feasible; expiry and re-block independent of overlay. Handle permission revocation, process death, reboot and OEM limitations. Physical-device tests required.

## Phase 6 — Ledger and completion routes
Implement persisted ledger and idempotent all-or-nothing rewards. Separate normal completion from user-confirmed early end. Early end: confirm → zero credits → native frame-012-matching result screen → Return to Dashboard. Test cancel, confirm, repeated taps, process recreation and duplicate callbacks.

## Phase 7 — Wallet and analytics
Use real ledger/session data. Test credit consistency and analytics thresholds (21 days, 10 completed sessions, 7 distinct days for peak-focus analysis).

## Phase 8 — Profile and integrations
Implement profile/settings and honest offline/backend states. Integrate auth, billing, ads or rankings only when real configuration is available. No fake success.

## Phase 9 — Hardening
Test supported Android versions and physical devices: permissions, app discovery, AR/camera, blocking, unlock expiry, re-blocking, recovery, data integrity, accessibility, battery and crashes. Remove test data and update release checklist.
