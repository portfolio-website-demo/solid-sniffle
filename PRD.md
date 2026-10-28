# Recover — Product Requirements

## Product
Recover is one standalone Android app for focus sessions, real app blocking, credits, and temporary app unlocks. This is a fresh project: do not assume source code or Gradle files exist.

## Asset classification is mandatory
Inspect the actual `assets/` tree recursively. Current visible folders: `app logos/`, `dashboard/`, `focus session/`, `onboarding/`, `splash screen/`. Preserve exact names, spacing, case, order, and originals.

Every file must be marked in `docs/asset-map.md` as:
- **PRODUCTION ASSET**: actual artwork to use directly, such as an approved logo or the single white Focus Card.
- **REFERENCE SCREENSHOT/FRAME**: visual/flow reference; recreate its screen in native Compose UI. Do not ship a phone photograph or screenshot as a full-screen UI background.
- **AMBIGUOUS — CONFIRM**: do not use until its role is clarified.

For every file, record relative path, dimensions, format, role, intended screen, sequence, and confidence. A `.jpg` or `.png` extension does not determine its role. Do not rename, flatten, overwrite, or destructively edit originals.

`assets/focus session/` visibly contains 16 files named like `ezgif-frame-001.jpg` through `ezgif-frame-016.jpg`. Inspect the real names and review them in sequence. Treat them as ordered **reference frames** unless visual inspection proves a file is a production asset.

**Special rule:** `assets/focus session/ezgif-frame-012.jpg` is a reference for a native result screen that appears only when the user taps **End Session** during an active focus session and confirms ending early. It must not be displayed as a bitmap. The screen shows actual credits earned and a **Return to Dashboard** button. Under the all-or-nothing reward policy, early ending earns zero credits. Do not show this screen for normal timer completion, an interruption, app launch, or a cancelled confirmation.

## User flow
Splash → How It Works carousel → sign-in/sign-up UI (real auth only if configured) → Pro offer with working Skip (no fake billing) → consolidated permissions explanation/setup → installed-app selector → dashboard → focus duration → mandatory AR/presence setup → active session → either normal completion or confirmed early end → dashboard. Wallet, transactions, analytics, rankings, account/settings and editing blocked apps must be navigable.

After permission setup, show a selector populated from apps actually installed and eligible on the device using Android-compliant discovery or the platform picker. Never hardcode YouTube, Instagram, TikTok, or any presumed app list. Persist package-name selections and allow editing later.

## Focus rewards and credits
- All-or-nothing: credits are banked only after successful completion of the planned session. Ending early earns zero.
- Default reward: one credit per full qualifying focused minute, unless a more specific approved rule supersedes this. Centralize calculation and test it.
- Never show provisional rewards as wallet balance.
- Each session can be rewarded at most once. Use stable IDs/idempotency keys.
- The ledger is the source of truth for balance.
- One credit unlocks one selected blocked app for 60 seconds.
- Failed unlocks must not charge. Retry must not double-charge. Debit and grant must be atomic/idempotent.
- Use monotonic time for timers and document safe recovery after process death/reboot.

## Real blocking
Blocking must be actual Android enforcement, not just a visual overlay. Select the enforcement approach after documenting Android API constraints, target SDK, package visibility, Google Play policy implications, and OEM limitations. If AccessibilityService is used, disclose its purpose and request explicit enablement. Do not claim tested reliability without physical-device tests. A countdown overlay is not enforcement; re-block at expiry even if overlay is hidden/dismissed. Handle app switches, permission revocation, process death and reboot. If a device limitation prevents reliable behavior, state it honestly.

## Mandatory AR/presence
AR/presence is mandatory in the focus-session flow. Follow the references for camera preview, placement zone where shown, countdown, movement/absence detection and strikes. Request camera permission contextually before the camera-dependent step, not just to display an overlay. Define and document third-strike behavior. Provide a safe emergency exit. Do not claim face identity verification or store/upload camera frames unless explicitly approved and necessary. If capability is unavailable, show an explicit error/fallback; do not silently skip the required flow.

## End Session special flow
Active session → user taps End Session → confirmation dialog. Cancel returns to active session without finalizing. Confirm finalizes as ended early exactly once, earns zero credits under all-or-nothing policy, then shows a native screen matching `ezgif-frame-012.jpg` with the actual reward amount and **Return to Dashboard**. Normal timer completion uses a separate completion route. Repeated taps/back/process recreation must not duplicate rewards or restart the session.

## Other requirements
- Initial release uses only the single supplied white Focus Card asset; no gallery or extra card finishes.
- Permissions must reflect real OS state. Only request permissions required by the chosen architecture. No unrelated contacts/location/microphone/storage/exact alarm/install-package permissions.
- No fake auth, Pro billing, ads, rankings, analytics, transactions, app list, AR, or blocking. Missing integrations/credentials must be reported honestly.
- Analytics use persisted data. Peak-focus analysis requires at least 21 calendar days of usable history, 10 completed sessions, and sessions on at least 7 distinct days; otherwise show insufficient data.
- Visual references define layout, hierarchy, color, spacing, typography and flow. Recreate natively. Dark dashboard: black/charcoal, white text, restrained blue; use blue/white onboarding and white screens only where references show them. Avoid neon and excessive gradients.
- Acceptance: fresh build runs, all assets classified, installed-app list is real, blocking/unlock expiry/re-block tested on a physical device, ledger and session rewards are idempotent, permissions verified, and unavailable integrations clearly disclosed.
