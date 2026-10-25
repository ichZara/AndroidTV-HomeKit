# AndroidTV-HomeKit (Modernized & Extended)

Native Apple HomeKit (HAP) accessory running directly on Android TV devices.  
*Doğrudan Android TV üzerinde çalışan yerel Apple HomeKit (HAP) köprü servisi.*

> **Credits & Upstream Project:**  
> This project is a modernized, bug-fixed, and heavily extended fork of the original work by **[Samuel Thomas (samuelthomas2774/android-tv-homekit)](https://github.com/samuelthomas2774/android-tv-homekit)** (created 7 years ago). Huge thanks to Samuel Thomas for the foundational architecture!  
> *Bu proje, Samuel Thomas tarafından 7 yıl önce başlatılan orijinal [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit) projesinin modernize edilmiş, kritik hataları giderilmiş ve yeni özelliklerle zenginleştirilmiş güncel sürümüdür.*

---

## English

### What's New & Modernized (2026 Fork)
1. **Critical Pairing Fix ("Accessory is already in another home"):**
   - The original project had an off-by-one bit-shift in `generateSetupUri` (`category << 30` and `flags << 26`). Shifting category 31 set **bit 30 to 1**, which according to Apple's 45-bit `X-HM://` setup payload specification is the **"Paired" flag**. iOS Home rejected the accessory immediately.
   - Fixed to `category << 31` and `flags << 27`, setting bit 30 to `0` (unpaired), allowing instant and smooth pairing.
2. **Modernized Build System:**
   - Updated Gradle wrapper and Android Gradle Plugin (AGP) for modern Android SDKs (Android 11 / 12 / 13 / 14, Java 17 / 21 compatibility).
3. **On-Screen Setup Dashboard:**
   - Added a native Android TV UI (`MainActivity`) displaying a live generated QR code, formatted 8-digit setup PIN (`031-45-154`), uppercase MAC address, and live connection status.
4. **Universal + Philips Enhanced Dual Mode:**
   - **Universal Mode (Any Android TV / Google TV):**
     - Standby power control (Sleep / Wake).
     - Full Apple TV Remote in iOS Control Center: D-Pad, OK, Back, Play/Pause.
     - Volume control via physical iPhone volume rockers.
     - Input switching via installed Android apps.
   - **Philips Enhanced Mode (JointSPACE v6):**
     - Native Philips REST API integration.
     - **Ambilight** exposed as an independent Apple Home Lightbulb (Power, Brightness, Color Hue & Saturation).
     - Direct switching between HDMI 1–4 and TV tuner channels.

### Quick Start
1. Build or download the APK (`AndroidTvHomeKit.apk`):
   ```bash
   .\gradlew.bat assembleDebug
   ```
2. Install via ADB to your Android TV:
   ```bash
   adb connect <TV_IP_ADDRESS>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
3. Launch the dashboard on your TV:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
4. Open the **Apple Home** app on your iPhone or iPad, tap **Add Accessory**, and scan the on-screen QR code (or enter PIN: `031-45-154`).

---

## Türkçe

### Neler Değişti ve Güncellendi? (2026 Sürümü)
1. **Kritik Eşleşme Hatası Çözüldü ("Aksesuar zaten başka bir evde"):**
   - Orijinal kodda `generateSetupUri` içerisindeki bit kaydırma hatasından ötürü (`category << 30`), Apple HAP protokolünün 30. biti ("eşleşmiş aksesuar") sahte olarak `1` oluyordu ve iOS cihazlar QR kodu okutur okutmaz eşleşmeyi reddediyordu. Bit kaydırmaları Apple standartlarına göre düzeltildi (`category << 31`, `flags << 27`).
2. **Güncel Gradle ve Android SDK Desteği:**
   - 7 yıllık eski Gradle yapısı modernize edildi; Android 11, 12, 13 ve 14 sistemlerinde sorunsuz derlenecek hale getirildi.
3. **TV Ekranı Kurulum Arayüzü (Dashboard):**
   - Televizyon ekranında açılan, kumanda ile kontrol edilebilen, canlı QR kod ve 8 haneli PIN (`031-45-154`) gösteren modern TV arayüzü eklendi.
4. **Evrensel + Philips İkili Mod:**
   - **Evrensel Mod (Tüm Android TV & Google TV Cihazları - Sony, TCL, Xiaomi, Chromecast vb.):**
     - Açma / Kapatma (Standby).
     - iPhone Denetim Merkezi Kumandası: Yön tuşları, OK, Geri, Oynat/Duraklat ve iPhone'un yan tuşlarıyla ses ayarı.
     - Uygulama başlatma girişleri.
   - **Philips Gelişmiş Mod (JointSPACE v6):**
     - Apple Ev uygulamasında bağımsız **Ambilight** ışık kontrolü (Açma/kapama, parlaklık ve renk seçimi).
     - Doğrudan HDMI 1–4 girişleri ve TV kanalları geçişi.

### Kurulum
1. Derleyin veya hazır APK'yı alın:
   ```bash
   .\gradlew.bat assembleDebug
   ```
2. ADB üzerinden televizyona kurun:
   ```bash
   adb connect <TV_IP_ADRESI>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
3. TV'deki arayüzü açın:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
4. iPhone/iPad **Ev** uygulamasından **Aksesuar Ekle** seçeneğine dokunup ekrandaki QR kodu okutun (veya `031-45-154` PIN'ini girin).

---

## Orijinal Proje ve Lisans / Credits & License
- Original Author: [Samuel Thomas (samuelthomas2774)](https://github.com/samuelthomas2774)
- Original Repository: [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit)
- License: MIT License
