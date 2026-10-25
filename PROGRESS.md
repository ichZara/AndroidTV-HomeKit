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
- [Yarın bakılacak] iOS Home kumandada ses kontrolü:
  - Apple TV Remote arayüzünde ekranda ses tuşları bulunmaz; Apple'ın HomeKit TV standardına göre ses, iPhone'un fiziksel yan tuşları (+/-) ile yönetilir.
  - Fiziksel tuşların tetiklenmesi için `TelevisionService`'in `getLinkedServices()` metoduna `TelevisionSpeakerService`'in bağlanması (`linkedServices.add(speaker)`) ve `VolumeControlType` / `VolumeSelector` akışının doğrulanması gerekiyor.
- [Yarın bakılacak] iOS Home TV aksesuar arayüzü ve kumanda tuşları:
  - TV aksesuarına basıldığında çıkan arayüzün dağınık görünmesinin sebebi tüm TIF/uygulama girişlerinin (InputSource) filtrelenmeden listelenmesi. Giriş kaynaklarının görünürlük (`TargetVisibilityState`, `CurrentVisibilityState`, `IsConfigured`) ayarları düzenlenecek.
  - iOS Denetim Merkezi (Control Center) kumandası ve Home içi kumanda ikonu entegrasyonu netleştirilecek.

## Log
- 2026-10-25: Separated English and Turkish README sections and added English installation guide — `README.md` — verified on GitHub.
