# Progress — Android TV HomeKit

## Current state
- Repository published on GitHub: https://github.com/ichZara/AndroidTV-HomeKit
- Cleanly separated English and Turkish documentation with full step-by-step installation guides (both pre-built APK and source clone/build), AI prompts, and hardware disclaimers.
- Sanitized dashboard screenshot (`pairing_screen.png`) and clean dynamic device naming.
- Release v1.0 published with downloadable `AndroidTvHomeKit.apk`.

## Decisions
- 2026-10-25: Restructured `README.md` into distinct English (`# 🇬🇧 English Documentation`) and Turkish (`# 🇹🇷 Türkçe Dokümantasyon`) sections with table of contents navigation.
- 2026-10-25: Added full English installation instructions including ADB commands and source build options.

## Open issues / Next
- [HomeKit Bağımlılıkları] Projedeki 7 yıl öncesinden kalma eski kütüphanelerin (commons-io:2.4, jmdns:3.4.1, srp6a, bouncycastle, hap-java alt modülü) güncellenmesi ve modernize edilmesi.
- [Siri / InputSource] Siri'ye "YouTube'u aç" dendiğinde açılmama nedeni: HomeKit TV standardında uygulamalar "Giriş Kaynağı" (Input Source) olarak tanımlanır. Apple ekosisteminde "YouTube'a geç" / "Televizyonun girişini YouTube yap" denmelidir. Ayrıca tüm sistem girişleri listelendiği için HomeKit giriş sınırına takılmaktadır; giriş listesi sadeleştirilecek.
- [Ses Kontrolü] Apple TV Remote kumandasında iPhone fiziksel ses tuşlarının TV sesine bağlanması (`TelevisionService` -> `TelevisionSpeaker` linked service).
- [Arayüz Sadeleştirme] Home uygulamasındaki TV aksesuarında yüzlerce gereksiz TIF girişinin gizlenip yalnızca temel girişlerin (HDMI, TV) bırakılması.

## Log
- 2026-10-25: Separated English and Turkish README sections and added English installation guide — `README.md` — verified on GitHub.
