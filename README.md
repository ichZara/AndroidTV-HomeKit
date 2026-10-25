# AndroidTV-HomeKit (Universal Android TV & Philips Ambilight)

Native Apple HomeKit (HAP) accessory running directly on Android TV devices.  
*Doğrudan Android TV üzerinde çalışan yerel Apple HomeKit (HAP) köprü servisi.*

---

## English

### Overview
AndroidTV-HomeKit turns your Android TV into a native HomeKit Television accessory without needing an external Raspberry Pi, Mac, or Homebridge hub.

### Compatibility
- **All Android TV Devices (Universal Mode):** Works on Sony, TCL, Xiaomi, Chromecast with Google TV, Philips, and other Android TV / Google TV boxes (SDK 26+).
  - Standby Power (On / Sleep via Android PowerManager / KeyEvents)
  - Volume Up / Down via Remote Widget & iPhone physical volume buttons
  - D-Pad (Up, Down, Left, Right), Select (OK), Back, Home, and Play/Pause remote control
  - App launching (Netflix, YouTube, Prime Video, etc.)
- **Philips Android TV (Enhanced Mode):**
  - Philips JointSPACE v6 REST API integration
  - Ambilight control exposed as an independent Apple Home Lightbulb (Power, Brightness, Color Hue & Saturation)
  - Direct HDMI 1–4 source switching and TV Tuner channels

### Key Improvements in this Fork
- **Fixed "Accessory is already in another home" Pairing Bug:** Fixed Apple HAP `X-HM://` QR code bit-shift layout (category shifted `<< 31`, flags shifted `<< 27`, preventing bit 30 from falsely reporting "already paired").
- **Uppercase MAC & Valid Setup Hash:** Full compliance with Apple HomeKit mDNS TXT records (`id`, `sh`, `sf=1`).
- **On-Screen Dashboard:** Displays live QR code, 8-digit setup PIN (`031-45-154`), and connection status.

### Installation & Pairing
1. Build the APK or download from releases:
   ```bash
   .\gradlew.bat assembleDebug
   ```
2. Install via ADB:
   ```bash
   adb connect <TV_IP_ADDRESS>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
3. Open dashboard:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
4. Open the **Apple Home** app on your iPhone or iPad, tap **Add Accessory**, and scan the QR code or enter `031-45-154`.

---

## Türkçe

### Genel Bakış
AndroidTV-HomeKit, harici bir Raspberry Pi, Mac veya Homebridge sunucusu gerektirmeden doğrudan Android TV üzerinde çalışan yerel bir Apple HomeKit köprüsüdür.

### Uyumluluk
- **Tüm Android TV Cihazları (Evrensel Mod):** Sony, TCL, Xiaomi, Chromecast, Philips vb. tüm Android TV / Google TV cihazlarında temel TV kontrollerini destekler:
  - Açma / Kapatma (Uyku moduna alma / uyandırma)
  - iPhone Denetim Merkezi Remote kumandası (Yön tuşları, OK, Geri, Oynat/Duraklat)
  - iPhone'un yan ses açma/kısma tuşlarıyla TV ses kontrolü
  - Yüklü uygulamaları (Netflix, YouTube vb.) giriş olarak başlatma
- **Philips Android TV'ler (Gelişmiş Mod):**
  - Philips JointSPACE v6 yerel API entegrasyonu
  - Apple Ev içinde bağımsız **Ambilight** ışığı (Açma/kapatma, parlaklık ve renk ayarı)
  - Doğrudan HDMI 1–4 ve TV kanalları arası geçiş

### Bu Sürümde Yapılan Düzeltmeler
- **"Aksesuar zaten başka bir evde" Eşleşme Hatası Çözüldü:** `X-HM://` QR kodundaki bit kaydırma hatası giderildi (30. bit artık sahte "eşleşti" uyarısı vermez).
- **Büyük Harf MAC ve Kurulum Özeti:** Apple HAP mDNS spesifikasyonuna tam uyum sağlandı.
- **TV Dashboard Ekranı:** TV arayüzünde canlı QR kod, 8 haneli PIN (`031-45-154`) ve bağlantı durumu.

### Kurulum ve Eşleştirme
1. Derleyin veya APK'yı indirin:
   ```bash
   .\gradlew.bat assembleDebug
   ```
2. ADB ile televizyona yükleyin:
   ```bash
   adb connect <TV_IP_ADRESI>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
3. Dashboard ekranını açın:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
4. iPhone/iPad **Ev (Home)** uygulamasından **Aksesuar Ekle** seçeneğine dokunup QR kodu okutun (veya `031-45-154` kodunu girin).

---

## License / Lisans
Open source, based on `samuelthomas2774/android-tv-homekit`.
