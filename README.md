# AndroidTV-HomeKit

Connect your Android TV to the Apple Home app on your iPhone or iPad!  
*Android televizyonunuzu iPhone veya iPad'deki Apple Ev (Home) uygulamasına bağlayın!*

![AndroidTV HomeKit Dashboard](pairing_screen.png)

> **Credits & Upstream Project:**  
> This project is a modernized, bug-fixed, and extended fork of **[Samuel Thomas (samuelthomas2774/android-tv-homekit)](https://github.com/samuelthomas2774/android-tv-homekit)**. Huge thanks to Samuel Thomas for the foundational architecture!  
> *Bu proje, Samuel Thomas tarafından başlatılan orijinal [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit) projesinin güncellenmiş ve genişletilmiş sürümüdür.*

---

## English

### What Does This App Do?
This app runs directly on your Android TV and turns it into an **Apple HomeKit Television**.

You don't need a Raspberry Pi, Mac, Homebridge, or Home Assistant server running. Simply install this APK on your TV, scan the QR code from your iPhone, and your TV is instantly in your Apple ecosystem!

### What Can You Control from iPhone / iPad?
1. **Apple Home (Ev) App:**
   - **Power:** Turn your TV On or Off (Sleep / Wake).
   - **Inputs:** Switch between HDMI ports, TV tuner, and installed streaming apps (Netflix, YouTube, Prime Video, etc.).
   - **Ambilight (Philips TVs):** Shows up as an independent Apple Home Lightbulb — adjust brightness, turn on/off, or change colors.
2. **Apple TV Remote in iOS Control Center:**
   - Swipe down from the top-right of your iPhone to open Control Center, tap the Remote icon, and select your TV.
   - Use the D-Pad, OK, Back, and Play/Pause buttons to navigate TV menus without the physical remote.
   - Use your iPhone's **physical volume buttons** on the side of your phone to adjust the TV volume.

### Compatibility
- **Universal Mode (All Android TV & Google TV devices):** Sony, TCL, Xiaomi, Chromecast with Google TV, Onn, Philips, etc.
- **Philips Enhanced Mode (Auto-detected):** Adds Ambilight light controls and direct HDMI 1–4 switching.

### How to Connect (Step-by-Step)
1. Install the APK (`AndroidTvHomeKit.apk`) onto your TV using ADB or any USB file manager:
   ```bash
   adb connect <TV_IP_ADDRESS>:5555
   adb install -r AndroidTvHomeKit.apk
   ```
2. Open the app on your TV:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
3. Take your iPhone or iPad:
   - Open the **Home (Ev)** app.
   - Tap **+** -> **Add Accessory (Aksesuar Ekle)**.
   - Point your camera at the QR code shown on your TV screen (or enter the 8-digit PIN: `031-45-154`).
4. Done! Your TV now appears in your Apple Home and Control Center Remote.

---

## Türkçe

### Bu Uygulama Ne İşe Yarar?
Bu uygulama doğrudan Android televizyonunuzun üzerinde çalışır ve TV'nizi **Apple Ev (HomeKit)** destekli bir akıllı televizyona dönüştürür.

Harici bir Raspberry Pi, Mac, Homebridge veya Home Assistant sunucusu kurmanıza gerek kalmaz. APK'yı televizyonunuza yükleyip iPhone'unuzdan ekrandaki QR kodu okutarak televizyonunuzu Apple ekosistemine bağlayabilirsiniz.

### iPhone veya iPad'inizden Neleri Kontrol Edebilirsiniz?
1. **Apple Ev (Home) Uygulamasından:**
   - **Açma / Kapatma:** TV'yi tek dokunuşla açıp kapatma (uykuya alma/uyandırma).
   - **Giriş ve Uygulama Seçimi:** HDMI girişleri, TV kanalları ve Netflix, YouTube, Prime Video gibi yüklü uygulamalar arasında geçiş yapma.
   - **Ambilight (Philips TV'ler):** Apple Ev içinde ayrı bir akıllı lamba olarak görünür; açıp kapatabilir, parlaklığını kısabilir ve renklerini değiştirebilirsiniz.
2. **iOS Denetim Merkezi Kumandası (Apple TV Remote):**
   - iPhone ekranınızı sağ üstten aşağı kaydırıp **Kumanda** simgesine dokunun ve televizyonunuzu seçin.
   - Kumandanızı aramadan telefonunuzdan yön tuşları, OK, Geri ve Oynat/Duraklat tuşlarıyla TV'yi kontrol edin.
   - Telefonunuzun **yanındaki fiziksel ses açma/kısma tuşlarıyla** televizyonun sesini doğrudan ayarlayın.

### Uyumluluk
- **Tüm Android TV & Google TV Cihazları (Evrensel):** Sony, TCL, Xiaomi, Chromecast, Philips ve diğer tüm Android TV'lerde kumanda, ses, güç ve uygulama başlatma çalışır.
- **Philips TV'ler:** Otomatik algılanır; ekstra olarak Ambilight lamba kontrolü ve HDMI 1–4 geçişleri aktif olur.

### Nasıl Bağlanır? (Adım Adım)
1. APK dosyasını (`AndroidTvHomeKit.apk`) ADB veya USB bellek ile televizyonunuza kurun:
   ```bash
   adb connect <TV_IP_ADRESI>:5555
   adb install -r AndroidTvHomeKit.apk
   ```
2. Uygulamayı televizyonunuzda açın:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```
3. iPhone veya iPad'inizi elinize alın:
   - **Ev (Home)** uygulamasını açın.
   - Sağ üstteki **+** simgesine dokunup **Aksesuar Ekle** seçeneğini seçin.
   - Televizyon ekranındaki QR kodu kameranızla okutun (veya ekrandaki `031-45-154` kodunu elle girin).
4. Eşleşme tamamlandı! Televizyonunuz artık Apple Ev uygulamasında ve Denetim Merkezi Kumandası'nda görünecektir.

---

## Lisans / License
MIT License. Based on original project by `samuelthomas2774/android-tv-homekit`.
