# AndroidTV-HomeKit

Native Apple HomeKit (HAP) accessory running directly on Android TV devices.  
*Doğrudan Android TV üzerinde çalışan yerel Apple HomeKit (HAP) köprü servisi.*

> **Credits & Upstream Project:**  
> This project is a modernized and extended fork of **[Samuel Thomas (samuelthomas2774/android-tv-homekit)](https://github.com/samuelthomas2774/android-tv-homekit)**. Huge thanks to Samuel Thomas for the foundational architecture!  
> *Bu proje, Samuel Thomas tarafından başlatılan orijinal [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit) projesinin güncellenmiş ve genişletilmiş sürümüdür.*

---

## English

### Overview
Turns your Android TV into an Apple HomeKit TV accessory without needing a Raspberry Pi or Homebridge.

- **Works on All Android TVs (Sony, TCL, Xiaomi, Chromecast, etc.):**
  - Sleep / Wake control from Apple Home
  - Full Apple TV Remote in iOS Control Center (D-Pad, OK, Back, Play/Pause)
  - TV Volume control using iPhone physical volume buttons
  - Launch installed apps as inputs
- **Philips Android TV Special Features (Auto-detected):**
  - Controls **Ambilight** as an Apple Home Light (On/Off, Brightness, Colors)
  - Direct switching to HDMI 1–4 ports
- **What Was Fixed & Modernized:**
  - Fixed the QR pairing bug where iOS reported "Accessory is already in another home"
  - Added a TV screen dashboard with QR code and setup PIN (`031-45-154`)
  - Updated build system for modern Android versions (Android 11, 12, 13, 14)

### Quick Start
1. Install APK on your Android TV via ADB:
   ```bash
   adb connect <TV_IP>:5555
   adb install -r AndroidTvHomeKit.apk
   ```
2. Open the dashboard on your TV:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
3. Open **Apple Home** on your iPhone, tap **Add Accessory**, and scan the QR code (or enter `031-45-154`).

---

## Türkçe

### Genel Bakış
Raspberry Pi veya Homebridge gerekmeden Android TV'nizi Apple Ev (HomeKit) televizyonuna dönüştürür.

- **Tüm Android TV'lerde Çalışır (Sony, TCL, Xiaomi, Chromecast vb.):**
  - Apple Ev uygulamasından TV'yi Açma / Kapatma (Uykuya alma)
  - iPhone Denetim Merkezi Kumandası (Yön tuşları, OK, Geri, Oynat/Duraklat)
  - iPhone'un yan ses tuşlarıyla TV'nin sesini açıp kısma
  - Yüklü uygulamaları giriş olarak başlatma
- **Philips Android TV'lere Özel Ekstralar (Otomatik Algılanır):**
  - **Ambilight** ışıklarını Apple Ev'de ayrı bir lamba gibi kontrol etme (Aç/Kapat, parlaklık, renk ayarı)
  - Doğrudan HDMI 1–4 girişleri arası geçiş
- **Yapılan Güncellemeler:**
  - iOS'taki "Aksesuar zaten başka bir evde" eşleşme hatası giderildi
  - TV ekranına QR kod ve PIN (`031-45-154`) gösteren arayüz eklendi
  - Güncel Android sürümleriyle (Android 11, 12, 13, 14) uyumlu hale getirildi

### Kurulum
1. APK'yı televizyonunuza kurun:
   ```bash
   adb connect <TV_IP>:5555
   adb install -r AndroidTvHomeKit.apk
   ```
2. TV ekranındaki arayüzü açın:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
3. iPhone'unuzdan **Ev** uygulamasını açıp **Aksesuar Ekle** diyerek ekrandaki QR kodu okutun (veya `031-45-154` PIN'ini girin).

---

## Lisans / License
MIT License. Based on `samuelthomas2774/android-tv-homekit`.
