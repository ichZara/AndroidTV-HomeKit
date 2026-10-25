# Progress — Android TV HomeKit (Philips JointSPACE v6 + Ambilight)

## Current state
- Native Android TV HomeKit service (`uk.org.fancy.AndroidTvHomeKit`) built and deployed to Philips 58PUS8507/12 (`192.168.1.8:5555`).
- Fixed the "Accessory is already in another home" ("Aksesuar zaten başka bir evde") QR code bit-shift bug and lowercase/off-by-one MAC generation bug.
- JointSPACE v6 digest credentials (`homekitphilipstv`) stored and verified.

## Decisions
- 2026-10-25: Fixed `HomeKitSetupPayload.generateSetupUri` bit shifts (`category << 31` and `flags << 27` instead of `<< 30` and `<< 26`) — shifting `category = 31` by `<< 30` had set bit 30 (`Paired/WAC` flag in the 4-bit setup flags field), which made iOS Home immediately report "Accessory is already in another home".
- 2026-10-25: Fixed `HomekitUtils.generateMac()` to format all 6 bytes as uppercase `%02X` so `sh` (setup hash) in mDNS TXT records matches `id` (`MAC`) and `X-HM://` setup ID (`PTV8`).
- 2026-10-25: Overrode `AuthInfo.hasUser()` to return `isPaired()` and regenerated a fresh uppercase MAC on `resetPairings()` while updating NSD TXT records (`sf=1`, `id`, `sh`).

## Open issues / Next
- Ready for user to scan the updated QR code in iOS Home ("Ev") app.

## Log
- 2026-10-25: Fixed QR payload bit shifts, MAC formatting, and NSD advertisement — `HomeKitSetupPayload.java`, `HomekitUtils.java`, `AuthInfo.java`, `AuthState.java`, `Advertiser.java`, `HomeKitService.java` — verified with `gradlew assembleDebug`, `adb install -r`, `pm clear`, and screenshot `homekit_qr_fixed.png`.
