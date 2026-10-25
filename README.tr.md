# AndroidTV-HomeKit (Philips JointSPACE v6 & Ambilight)

Philips Android TV modelleri için doğrudan televizyon üzerinde çalışan yerel Apple HomeKit (HAP) ve Ambilight köprü servisi.

Harici bir Raspberry Pi, Mac veya Homebridge sunucusu gerektirmez.

---

## Öne Çıkan Özellikler

- **Yerel Apple TV Profili (iOS Ev / Home):**
  - **Açma / Kapatma (Standby):** TV'yi kapatma ve uyanıkken açma.
  - **Giriş / Uygulama Seçimi:** Ana Ekran, HDMI 1–4, TV Kanalları, Netflix, YouTube, Prime Video vb.
  - **Kumanda (Apple TV Remote):** iPhone/iPad Denetim Merkezi'ndeki Kumanda arayüzünden yön, OK, geri, menü tuşları ve telefonun yan tuşlarıyla ses kontrolü.
- **Ambilight Kontrolü:**
  - Apple Ev uygulamasında ayrı bir ışık (Lightbulb) olarak görünür.
  - Açma/kapatma, parlaklık ayarı ve renk seçimi TV'nin yerel Ambilight LED'lerine yansıtılır.
- **Philips JointSPACE v6 Entegrasyonu:**
  - TV'nin kendi yerel REST API'si (`/6/ambilight`, `/6/input/key`, `/6/audio/volume`) üzerinden çalışır.
- **Yerleşik Eşleşme Ekranı (Dashboard):**
  - TV arayüzünde otomatik oluşturulan QR kod, 8 haneli HomeKit kurulum kodu ve eşleşme durumu.
- **HAP QR Kod Düzeltmesi:**
  - `X-HM://` spesifikasyonundaki bit kaydırma hatası düzeltildi ("Aksesuar zaten başka bir evde" hatası çözüldü).

---

## Kurulum

1. Projeyi derleyin:
   ```bash
   .\gradlew.bat assembleDebug
   ```

2. Televizyonunuza yükleyin:
   ```bash
   adb connect <TV_IP_ADRESI>:5555
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

3. Kurulum ekranını açın:
   ```bash
   adb shell am start -n uk.org.fancy.AndroidTvHomeKit/.MainActivity
   ```

4. iPhone veya iPad'inizde **Ev (Home)** uygulamasını açın:
   - **Aksesuar Ekle** seçeneğine dokunun.
   - TV ekranındaki QR kodu okutun veya ekrandaki **031-45-154** PIN kodunu girin.

---

## Lisans

Bu proje açık kaynaklıdır. Orijinal `android-tv-homekit` projesine Philips JointSPACE v6 ve Ambilight desteği eklenerek geliştirilmiştir.
