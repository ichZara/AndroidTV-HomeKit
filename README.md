# AndroidTV-HomeKit

Native Apple HomeKit accessory service running directly on Android TV & Google TV.  
*Doğrudan Android TV ve Google TV cihazları üzerinde çalışan yerel Apple HomeKit köprü servisi.*

![AndroidTV HomeKit Dashboard](pairing_screen.png)

> **Credits & Upstream Project:**  
> This project is an updated and extended fork of **[Samuel Thomas (samuelthomas2774/android-tv-homekit)](https://github.com/samuelthomas2774/android-tv-homekit)**. We are grateful to Samuel Thomas for establishing the foundation of this project.  
> *Bu proje, Samuel Thomas tarafından başlatılan orijinal [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit) projesinin güncellenmiş ve genişletilmiş sürümüdür.*

---

## Device Compatibility Notice / Cihaz Uyumluluk Bildirimi

- **Verified & Tested Model / Bizzat Test Edilip Doğrulanan Model:**  
  **Philips 58PUS8507/12 (58PUS8507/62, Android TV 11)** — Apple Home pairing, iPhone Control Center Remote, physical volume rockers, HDMI input switching, and Ambilight light controls were verified.
- **Other Android TV & Google TV Devices / Diğer Cihazlar:**  
  Designed for Android TV 9 through 14 (Sony, TCL, Xiaomi, Chromecast with Google TV, Onn, etc.). However, **hardware and manufacturer API differences mean compatibility cannot be 100% guaranteed on every brand and model**. Please test on your own TV and report your findings in GitHub Issues!  
  *Android TV 9–14 kullanan diğer tüm cihazları destekleyecek şekilde tasarlanmıştır. Ancak üretici ve donanım kısıtlamaları nedeniyle her cihazda %100 kusursuz çalışma garantisi verilemez; lütfen kendi cihazınızda test edip geri bildirimde bulunun.*

---

## English

### What Does This App Do?
AndroidTV-HomeKit turns your Android TV into an official **Apple HomeKit Television** without requiring external hardware like a Raspberry Pi, Mac, or home server running Homebridge / Home Assistant.

### Features
1. **Apple Home App:**
   - **Power (Standby):** Sleep and Wake your TV directly from your iPhone, iPad, or Apple Watch.
   - **Input Switching:** Switch between HDMI ports, TV tuner, and installed streaming apps.
   - **Ambilight (Philips TVs):** Integrated as a native Apple Home Lightbulb (Power, Brightness, Color Hue & Saturation).
2. **Apple TV Remote (iOS Control Center):**
   - Swipe down on iPhone/iPad to use the directional pad, OK, Back, and Play/Pause.
   - Control TV volume using your phone's **physical side volume buttons**.

---

## Adım Adım Kurulum Rehberi (Step-by-Step Installation)

### 1. Televizyonda ADB Hata Ayıklamayı Açma
1. TV'de **Ayarlar (Settings)** -> **Cihaz Tercihleri** -> **Hakkında** yolunu izleyin.
2. **Android TV Derlemesi (Build)** seçeneğine kumandanın **OK** tuşuyla **7 kez** arka arkaya basın (*"Artık bir geliştiricisiniz"* bildirimi çıkana kadar).
3. Geri gelip **Geliştirici Seçenekleri** menüsüne girin ve **USB Hata Ayıklama (USB Debugging)** seçeneğini açın.
4. **Ayarlar** -> **Ağ ve İnternet** kısmından TV'nizin yerel IP adresini not edin (örnek: `192.168.1.50`).

### 2. Uygulamayı Televizyona Yükleme
Bilgisayarınızdan terminal veya PowerShell açın:

```bash
# 1. Televizyona bağlanın:
adb connect <TELEVIZYON_IP_ADRESI>:5555
# (TV ekranına 'Bu bilgisayara izin verilsin mi?' uyarısı gelir; 'Her zaman izin ver'i seçip OK tuşuna basın)

# 2. İndirdiğiniz APK'yı yükleyin (Releases sayfasından indirebilirsiniz):
adb install -r AndroidTvHomeKit.apk

# 3. TV ekranındaki kurulum ve QR kod arayüzünü başlatın:
adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
```

### 3. iPhone / iPad'e Ekleme
1. iPhone'unuzda **Ev (Home)** uygulamasını açın.
2. Sağ üstteki **+** simgesine dokunup **Aksesuar Ekle** deyin.
3. Televizyon ekranında açılan QR kodu okutun (veya ekrandaki `031-45-154` PIN kodunu girin).

---

## Yapay Zeka (AI) ile Otomatik Kurulum Promptu / AI Prompt for Setup

Aşağıdaki hazır promptu kopyalayıp kullandığınız yapay zeka asistanına (ChatGPT, Claude, Gemini veya yerel AI terminalinize) vererek kurulumu baştan sona ona yaptırabilirsiniz:

````markdown
Aşağıdaki GitHub deposunu kullanarak Android TV'me Apple HomeKit köprüsünü kurmak istiyorum:
Repo: https://github.com/ichZara/AndroidTV-HomeKit
Televizyonumun yerel IP adresi: [BURAYA_TELEVIZYONUN_IP_ADRESINI_YAZIN, örn: 192.168.1.50]

Lütfen şu adımları benim için sırasıyla açıkla veya terminal komutlarıyla çalıştır:
1. Depodaki son sürüm APK'yı (AndroidTvHomeKit.apk) indir veya depoyu klonlayıp gradle ile derle.
2. Bilgisayarımdan ADB ile yukarıdaki televizyon IP adresine bağlan.
3. APK'yı televizyona yükle ve kurulum arayüzünü (MainActivity) televizyon ekranında başlat.
4. Hata alırsam (örneğin cihaz bulunamadı veya bağlantı reddedildi) nasıl düzelteceğimi belirt.
````

---

## Hata Bildirimi ve Destek / Issues & Feedback

Cihazınızda bir hata, çökme veya kumanda tuş uyumsuzluğu fark ederseniz lütfen **[GitHub Issues](https://github.com/ichZara/AndroidTV-HomeKit/issues)** sayfası üzerinden bildirin!

Bildirirken televizyonunuzun **markasını, tam model kodunu ve Android sürümünü** eklemeniz sorunun tespit edilip çözülmesini kolaylaştıracaktır.

---

## Lisans / License
MIT License. Based on original project by `samuelthomas2774/android-tv-homekit`.
