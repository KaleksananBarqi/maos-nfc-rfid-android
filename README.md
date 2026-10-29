# Maos NFC RFID Android

Aplikasi Android utilitas untuk membaca kartu RFID santri (13.56 MHz, ISO 14443-A) menggunakan sensor NFC di smartphone, lalu mengkonversi UID kartu menjadi **angka desimal 10 digit** yang identik dengan output USB RFID Reader standar.

Dibuat untuk kebutuhan pendaftaran kartu santri pada sistem [Pesantrenqu](https://admin.pesantrenqu.id/) tanpa perlu membawa perangkat RFID Reader fisik.

## Fitur

- 🔢 **Konversi Instan** — Baca kartu RFID dan langsung tampilkan angka 10 digit (Big-Endian unsigned 32-bit)
- 📋 **Auto-Copy Clipboard** — Angka 10 digit otomatis tersalin ke clipboard HP begitu kartu ditempel
- 📳 **Haptic Feedback** — Getaran konfirmasi instan saat pemindaian berhasil
- 📤 **Bagikan** — Kirim nomor kartu langsung ke WhatsApp, Catatan, atau aplikasi lain
- 📜 **Riwayat Lokal** — Simpan riwayat pemindaian secara offline di database lokal
- 🔍 **Multi-Format** — Tampilkan format Big-Endian, Little-Endian, dan Hex UID
- 🌙 **Dark Mode** — Otomatis mengikuti tema sistem (Material 3 + Dynamic Color)
- 📡 **Offline 100%** — Tidak perlu internet, akun, atau login

## Logika Konversi

USB RFID Reader membaca 4 byte UID kartu (contoh: `90 80 78 0F`) dan menghasilkan bilangan desimal 32-bit unsigned Big-Endian:

```
0x90 << 24 | 0x80 << 16 | 0x78 << 8 | 0x0F = 2424338447
```

Aplikasi ini melakukan konversi yang identik melalui sensor NFC smartphone.

## Persyaratan

- Android 8.0 Oreo (API 26) ke atas
- Smartphone dengan sensor NFC
- Kartu RFID 13.56 MHz (MIFARE Classic / Ultralight / NTAG)

## Build

```bash
./gradlew assembleDebug
```

APK hasil build ada di `app/build/outputs/apk/debug/`.

## Arsitektur

- **Kotlin + Jetpack Compose** — UI modern dan deklaratif
- **NfcAdapter.enableReaderMode** — Pemindaian instan tanpa suara sistem
- **Room Database** — Penyimpanan riwayat lokal offline
- **Material 3** — Tampilan adaptif dengan Dynamic Color

## Dokumentasi

- [Spesifikasi Produk](docs/SPEC.md)
- [Glosarium Domain](GLOSSARY.md)
- [ADR: Native Compose & Konversi UID](docs/adr/0001-native-compose-and-uid-conversion.md)
- [ADR: NFC Reader Mode & Riwayat](docs/adr/0002-nfc-reader-mode-and-history-storage.md)

## Lisensi

[MIT License](LICENSE)
