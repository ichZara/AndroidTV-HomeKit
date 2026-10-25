# AndroidTV-HomeKit (Universal Android TV & Google TV)

Native Apple HomeKit (HAP) accessory running directly on Android TV devices.  
*Doğrudan Android TV üzerinde çalışan yerel Apple HomeKit (HAP) köprü servisi.*

> **Credits & Upstream Project:**  
> This project is a modernized, bug-fixed, and heavily extended fork of the original work by **[Samuel Thomas (samuelthomas2774/android-tv-homekit)](https://github.com/samuelthomas2774/android-tv-homekit)** (created 7 years ago). Huge thanks to Samuel Thomas for the foundational architecture!  
> *Bu proje, Samuel Thomas tarafından 7 yıl önce başlatılan orijinal [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit) projesinin modernize edilmiş, kritik hataları giderilmiş ve yeni özelliklerle zenginleştirilmiş güncel sürümüdür.*

---

## English

### Universal Architecture (Single APK for ALL Android TVs)
**No separate apps or versions needed!** This single APK automatically detects the TV hardware at runtime:
1. **Universal Mode (Active on ALL Android TV & Google TV devices — Sony, TCL, Xiaomi, Chromecast, Onn, Philips, etc.):**
   - **Power (Standby):** Sleep / Wake using Android standard system APIs.
   - **Apple TV Remote (iOS Control Center):** Complete D-Pad (Up, Down, Left, Right), Select (OK), Back, Home, and Play/Pause.
   - **Volume Control:** Real-time volume up/down using iPhone/iPad hardware volume buttons.
   - **App Switching:** Launches installed TV apps (Netflix, YouTube, Prime Video, etc.) as HomeKit inputs.
2. **Philips Enhanced Mode (Auto-activated ONLY on Philips TVs):**
   - If the device is detected as Philips, it connects to the local JointSPACE v6 API.
   - Exposes **Ambilight** as an independent Apple Home Lightbulb (Power, Brightness, Color Hue & Saturation).
   - Adds direct HDMI 1–4 physical port switching and TV Tuner channels.
   - *On non-Philips TVs, these Philips-specific services gracefully sleep with zero overhead.*

### What's New & Modernized (2026 Fork)
- **Critical Pairing Fix ("Accessory is already in another home"):**  
  Fixed Apple HAP `X-HM://` QR code bit-shift layout (`category << 31`, `flags << 27`). The original code mistakenly set bit 30 to 1 (Apple's "Paired" flag), causing iOS Home to reject pairing immediately.
- **Modernized Android SDK & Gradle:** Upgraded build system for Android 11, 12, 13, and 14 with Java 17/21 support.
- **On-Screen Setup Dashboard:** Native TV interface showing a live QR code, 8-digit PIN (`031-45-154`), uppercase MAC address, and connection status.

### Quick Start
1. Download or build the APK:
   ```bash
   .\gradlew.bat assembleDebug
   ```
2. Install via ADB on your Android TV:
   ```bash
   adb connect <TV_IP_ADDRESS>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
3. Open the dashboard:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
4. Open the **Apple Home** app on your iPhone/iPad, tap **Add Accessory**, and scan the QR code (or enter `031-45-154`).

---

## Türkçe

### Evrensel Mimari (TÜM Android TV'ler İçin Tek APK)
**Ayrı dosyalar veya farklı sürümler yoktur!** Tek bir APK kurulur ve çalıştığı televizyonu otomatik algılar:
1. **Evrensel Mod (TÜM Android TV ve Google TV'lerde Aktif — Sony, TCL, Xiaomi, Chromecast, Philips vb.):**
   - **Güç Kontrolü:** Standart Android sistem çağrılarıyla TV'yi uyku moduna alma ve uyandırma.
   - **Apple TV Kumandası (iOS Denetim Merkezi):** Yön tuşları, OK, Geri, Ana Ekran ve Oynat/Duraklat.
   - **Ses Ayarı:** iPhone'un yanındaki fiziksel ses açma/kısma tuşlarıyla televizyonun sesini doğrudan kontrol etme.
   - **Giriş Değiştirme:** Yüklü TV uygulamalarını (Netflix, YouTube vb.) HomeKit girişi olarak başlatma.
2. **Philips Gelişmiş Mod (Yalnızca Philips TV algılandığında otomatik devreye girer):**
   - TV Philips ise yerel JointSPACE v6 API'sine bağlanır.
   - **Ambilight** ışıklarını Apple Ev'de bağımsız bir lamba olarak gösterir (Aç/Kapat, parlaklık ve renk seçimi).
   - Doğrudan HDMI 1–4 portları ve TV kanalları arası geçiş sağlar.
   - *Philips olmayan televizyonlarda bu özellikler sistemi yormadan tamamen devre dışı kalır, genel kumanda çalışır.*

### Bu Sürümde Yapılan Yenilikler (2026)
- **Kritik Eşleşme Hatası Çözüldü ("Aksesuar zaten başka bir evde"):**  
  `X-HM://` QR kodundaki bit kaydırma hatası düzeltildi (`category << 31`, `flags << 27`). Orijinal projedeki 30. bit ("eşleşmiş cihaz") hatası tamamen giderildi.
- **Modern SDK & Gradle:** Android 11, 12, 13 ve 14 sistemlerine tam uyumlu modern derleme altyapısı.
- **TV Ekranı Kurulum Arayüzü (Dashboard):** TV ekranında kumandayla kontrol edilen canlı QR kod ve 8 haneli PIN (`031-45-154`).

### Kurulum
1. Derleyin veya hazır APK'yı alın:
   ```bash
   .\gradlew.bat assembleDebug
   ```
2. ADB üzerinden televizyona yükleyin:
   ```bash
   adb connect <TV_IP_ADRESI>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
3. TV'deki arayüzü başlatın:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
4. iPhone/iPad **Ev** uygulamasından **Aksesuar Ekle** seçeneğine dokunup QR kodu okutun (veya `031-45-154` kodunu girin).

---

## Orijinal Proje ve Lisans / Credits & License
- Original Author: [Samuel Thomas (samuelthomas2774)](https://github.com/samuelthomas2774)
- Original Repository: [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit)
- License: MIT License
