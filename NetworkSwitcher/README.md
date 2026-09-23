# Network Switcher

A minimal Android app for quickly switching the default-data SIM between **Preferred 5G** and **Preferred 4G** using Shizuku.

## Important
Android normally blocks third-party apps from changing preferred network types because the telephony APIs require privileged/carrier access. This project therefore uses Shizuku to make the privileged Binder call.

## Build
1. Install Android Studio.
2. Open this folder as a Gradle project.
3. Let Gradle download dependencies.
4. Build `app` > `assembleDebug`.
5. Install the APK on your phone.

## Phone setup
1. Install and start Shizuku.
2. On Android 11+, start Shizuku using Wireless debugging.
3. Open Network Switcher.
4. Grant Shizuku permission.
5. Tap Preferred 5G or Preferred 4G.
6. Add **Network Switcher** to Quick Settings for one-tap switching.

## Notes
- This is **Preferred 5G** and **Preferred 4G**, not 5G-only/LTE-only.
- Carrier/OEM firmware can reject or override a requested mode.
- The first version targets Android 12+ for the modern telephony API.
