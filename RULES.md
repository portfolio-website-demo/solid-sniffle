# Recover — Engineering Rules

1. One standalone Android app. Never create a companion blocker, App A/App B, installer or hidden package.
2. Read all root docs before each phase. Inspect the actual asset tree before coding.
3. Classify every file as **PRODUCTION ASSET**, **REFERENCE SCREENSHOT/FRAME**, or **AMBIGUOUS — CONFIRM** in `docs/asset-map.md`.
4. Production assets are used directly when appropriate. Reference screenshots are recreated as native Compose UI, never used as full-screen backgrounds. Ambiguous files are not shipped until clarified.
5. Preserve original asset names, folders and files. Do not flatten, rename, overwrite or destructively edit.
6. `assets/focus session/ezgif-frame-001.jpg` through `ezgif-frame-016.jpg` are an ordered reference sequence unless inspection proves otherwise. `ezgif-frame-012.jpg` is specifically the reference for the user-confirmed early-End-Session result screen.
7. Do not hardcode installed apps. Query the actual device with compliant Android APIs/system picker.
8. Do not fake permissions, app blocking, AR/presence, credits, transaction history, analytics, rankings, sign-in, billing, ads or entitlement.
9. Real blocking must not depend solely on an overlay. Test enforcement on a physical device; disclose limitations.
10. One credit unlocks one selected app for 60 seconds. Failed unlock does not charge. Expiry re-blocks even if overlay is hidden.
11. Ledger is the source of truth. Rewards and debits are atomic/idempotent. No duplicate reward or debit on retries.
12. All-or-nothing session reward: only planned successful completion earns credits; confirmed early end earns zero.
13. End Session behavior: tap → confirmation; cancel stays in active session; confirm ends early once → zero credits → native result screen matching frame 012 → Return to Dashboard. Never use this screen for normal timer expiry or unrelated interruption.
14. AR/presence is mandatory. Camera permission is contextual. Define third-strike behavior. Provide emergency exit. Do not store/upload camera frames without approval.
15. Use only the single supplied white Focus Card asset for initial release; no gallery.
16. Ask only for permissions the chosen architecture genuinely requires. Verify real OS state and recheck on resume.
17. No dead buttons, fake success states, sample production data, giant files or duplicated business logic. Provide loading, empty, error and insufficient-permission states.
18. Run tests/build and report exact results. Never claim tests passed unless executed. Update `MEMORY.md` after each phase.
