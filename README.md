# AndroidTV-HomeKit

Connect your Android TV to the Apple Home app on your iPhone or iPad!  
*Android televizyonunuzu iPhone veya iPad'deki Apple Ev (Home) uygulamasına bağlayın!*

![AndroidTV HomeKit Dashboard](pairing_screen.png)

> **Credits & Upstream Project:**  
> This project is a modernized, bug-fixed, and extended fork of **[Samuel Thomas (samuelthomas2774/android-tv-homekit)](https://github.com/samuelthomas2774/android-tv-homekit)**. Huge thanks to Samuel Thomas for the foundational architecture!  
> *Bu proje, Samuel Thomas tarafından başlatılan orijinal [samuelthomas2774/android-tv-homekit](https://github.com/samuelthomas2774/android-tv-homekit) projesinin güncellenmiş ve genişletilmiş sürümüdür.*

---

## Uyumluluk ve Test Edilen Modeller / Compatibility

- **Bizzat Test Edilip Doğrulanan Model:** **Philips 58PUS8507/12 (58PUS8507/62, Android TV 11)** üzerinde tüm fonksiyonlarıyla (Apple Ev eşleşmesi, Ambilight lamba/parlaklık/renk kontrolü, iPhone Denetim Merkezi Remote kumandası, ses tuşları ve HDMI geçişleri) test edilmiş ve sorunsuz çalıştığı doğrulanmıştır.
- **Tüm Android TV & Google TV Cihazları (Evrensel):** Sony, TCL, Xiaomi, Chromecast with Google TV, Onn ve diğer tüm Android TV (Android 9+) cihazlarında çalışır. Cihazınızın marka ve modelini otomatik algılayarak HomeKit üzerinde adlandırır.

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

---

## Adım Adım Kurulum Rehberi (Türkçe)

Kurulumu ister bilgisayarınızdan manuel olarak yapabilir, isterseniz aşağıdaki hazır yapay zeka (AI) promptunu ChatGPT / Claude / Gemini'ye kopyalayarak tüm adımları ona yaptırabilirsiniz.

### 1. Televizyonda Geliştirici Seçenekleri ve Hata Ayıklamayı Açma
1. Televizyonunuzda **Ayarlar (Settings)** -> **Cihaz Tercihleri** -> **Hakkında** bölümüne gidin.
2. En altta bulunan **Android TV Derlemesi (Build)** seçeneğine gelin ve kumandanın **OK (Orta)** tuşuna arka arkaya **7 kez** hızlıca basın. Ekranda *"Artık bir geliştiricisiniz!"* bildirimi çıkacaktır.
3. Bir önceki menüye dönüp **Geliştirici Seçenekleri**'ne girin.
4. **USB Hata Ayıklama (USB Debugging)** ve varsa **Kablosuz Hata Ayıklama** seçeneğini aktif hale getirin.
5. TV'nizin IP adresini öğrenin (**Ayarlar** -> **Ağ ve İnternet** -> Bağlı olduğunuz Wi-Fi'ye tıklayın, örn: `192.168.1.50`).

### 2. Uygulamayı Televizyona Yükleme
Bilgisayarınızda ADB kuruluysa (veya telefonunuzdaki ADB uygulamasıyla):
```bash
# 1. Televizyona kablosuz bağlanın:
adb connect <TELEVIZYON_IP_ADRESI>:5555
# (Televizyon ekranında 'Bu bilgisayara her zaman izin verilsin mi?' kutucuğu çıkar, kumandayla 'Her zaman izin ver'i işaretleyip OK deyin)

# 2. Releases sayfasından indirdiğiniz APK'yı yükleyin:
adb install -r AndroidTvHomeKit.apk

# 3. TV'deki arayüzü başlatın:
adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
```

### 3. iPhone / iPad'den Ev Uygulamasına Ekleme
1. iPhone'unuzda **Ev (Home)** uygulamasını açın.
2. Sağ üstteki **+** simgesine dokunup **Aksesuar Ekle** seçeneğini seçin.
3. Televizyon ekranında açılan QR kodu kameranızla okutun (veya ekranda yazan 8 haneli PIN kodunu girin).
4. Eşleşme tamamlandı! Artık televizyonunuz Apple Ev uygulamasında ve iPhone Denetim Merkezi Remote kumandasında hazırdır.

---

## Yapay Zeka (AI) ile Otomatik Kurulum Promptu

ADB komutlarıyla uğraşmak istemiyorsanız, aşağıdaki promptu kopyalayıp kullandığınız yapay zekaya (ChatGPT, Claude, Gemini vb.) yapıştırın:

```markdown
Bilgisayarım ile aynı Wi-Fi ağına bağlı bir Android TV'm var.
Televizyonumun IP adresi: [BURAYA_TELEVIZYON_IP_ADRESINI_YAZIN]
İndirdiğim APK dosyasının yolu: [BURAYA_INDIRILEN_AndroidTvHomeKit.apk_DOSYA_YOLUNU_YAZIN]

Lütfen bilgisayarımdan ADB aracılığıyla televizyonuma bağlanıp bu APK'yı kurmamı ve televizyonda açmamı sağlayan adımları ve komutları çalıştır veya bana sırasıyla yaz.
```

---

## Hatalar ve Geri Bildirim / Issue Reporting

Bir sorunla, beklenmeyen bir hatayla veya TV modelinizle ilgili bir uyumsuzlukla karşılaşırsanız lütfen çekinmeden **[GitHub Issues](https://github.com/ichZara/AndroidTV-HomeKit/issues)** sayfasından bildirin! 

Hata bildirirken televizyonunuzun marka/modelini ve Android sürümünü belirtmeniz sorunun hızla çözülmesine yardımcı olacaktır.

---

## Lisans / License
MIT License. Based on original project by `samuelthomas2774/android-tv-homekit`.
