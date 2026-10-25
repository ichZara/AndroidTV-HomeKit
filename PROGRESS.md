# Progress — Android TV HomeKit

## Current state
- Proje derlendi ve TV üzerinde aktif çalışıyor (Port 9123).
- Apple TV Remote kumandasında iPhone fiziksel ses tuşları (`TelevisionSpeakerService`) `TelevisionService`'e `linkedService` olarak bağlandı; ses kontrol tipi `RELATIVE_WITH_CURRENT` yapıldı.
- TV'deki 60+ dağınık sistem paketinin HomeKit'i kirletmesi engellendi; giriş listesi popüler medya uygulamaları (YouTube, Netflix, Prime Video, Disney+, Apple TV, Spotify, Max) ve HDMI girişleri ile filtrelendi.
- 7 yıllık eski bağımlılıklar modernize edildi (`targetSdk 34`, `commons-io:2.16.1`, `async-http-client:2.12.3`, `ecc-25519-java:1.0.3`, `javax.json:1.1.4`, `versionCode 3`, `versionName 1.2.0`).
- `HomekitUtils.java` içinde MAC formatlama ve PIN doğrulama dizgi karşılaştırma hataları düzeltildi.

## Decisions
- 2026-10-25: `TelevisionSpeaker` `linkedServices` bağlantısı Apple TV Remote fiziksel ses kontrolü için zorunludur.
- 2026-10-25: `InputSourceManager` içine `allowedMediaApps` beyaz listesi eklendi. Böylece Siri'nin girişleri eşleştirmesi hızlandı ve arayüz sadeleşti.
- 2026-10-25: HAP-Java modülü ile uyumlu en güncel bağımlılık kombinasyonu sağlandı.

## Open issues / Next
- Tüm kullanıcı talepleri tamamlandı ve TV üzerinde doğrulandı.

## Log
- 2026-10-25: Donanım ses tuşları, giriş filtreleme, bağımlılık güncellemesi ve HAP alt modül düzeltmeleri — TV'ye kuruldu ve doğrulandı.
- 2026-10-25: İki dilli (İngilizce/Türkçe) dokümantasyon ve `pairing_screen.png` güncellendi.
