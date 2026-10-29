# Spesifikasi: Maos NFC RFID (10 Digit RFID Taker)

## Problem Statement
Admin atau pengurus lembaga sering kali perlu mendaftarkan nomor kartu fisik RFID santri (frekuensi 13.56 MHz, standar ISO 14443-A) ke formulir web admin portal (seperti Pesantrenqu) di lapangan atau asrama. Selama ini, proses tersebut bergantung pada perangkat keras USB RFID Reader yang harus terhubung ke komputer/laptop. Ketika tidak membawa reader tersebut, admin kesulitan membaca kartu. Meskipun smartphone Android mereka telah dilengkapi fitur NFC, aplikasi pemindai NFC umum di Play Store hanya menampilkan data dalam format Heksadesimal mentah (Hex UID), bukan dalam format angka desimal 10 digit (`Nomor RFID (10 Digit)`) yang diwajibkan oleh sistem web.

## Solution
Membangun aplikasi Android Native mandiri (*Maos NFC RFID*) yang memanfaatkan sensor NFC smartphone untuk membaca kartu RFID santri secara instan. Begitu kartu ditempelkan di bagian belakang ponsel, aplikasi akan:
1. Membaca UID perangkat keras kartu secara langsung tanpa delay suara sistem atau dialog pemilih aplikasi (*app chooser*).
2. Mengonversi UID tersebut menjadi angka desimal 10 digit (Big-Endian unsigned 32-bit integer dengan *zero-padding*) yang 100% identik dengan hasil bacaan USB RFID Reader di komputer.
3. Memberikan getaran haptik responsif dan secara otomatis menyalin (*auto-copy*) angka 10 digit ke *clipboard* ponsel sehingga admin bisa langsung menempelkannya (*paste*) ke browser.
4. Menampilkan tombol cepat untuk salin ulang dan membagikan (*share*) nomor kartu ke aplikasi lain (seperti WhatsApp).
5. Menyimpan riwayat pembacaan kartu secara persisten dan lokal (offline) agar data tidak hilang saat admin berpindah aplikasi.

## User Stories

1. As an administrator, I want to tap an RFID santri card on my NFC-enabled phone, so that I can instantly obtain the 10-digit decimal number without carrying a physical USB reader.
2. As an administrator, I want the 10-digit decimal number to match the exact output of standard desktop USB RFID readers, so that the card works seamlessly with the Pesantrenqu web portal.
3. As an administrator, I want the app to automatically copy the 10-digit number to my clipboard upon scanning, so that I can immediately paste it into the registration web form without manual selection.
4. As an administrator, I want subtle haptic feedback (vibration) when a card is successfully scanned, so that I know the scan succeeded without looking at the screen.
5. As an administrator, I want the scanned 10-digit number displayed in large, high-contrast typography, so that I can read it easily in bright outdoor or classroom environments.
6. As an administrator, I want to see the secondary Little-Endian and raw Hexadecimal representations below the primary number, so that I can verify compatibility with alternative readers or legacy systems.
7. As an administrator, I want the scan to be processed instantly with zero OS sound delays or application chooser popups, so that registration of multiple cards in batches is rapid and uninterrupted.
8. As an administrator, I want a persistent scan history saved locally on my device, so that previously scanned numbers are preserved when switching between apps or restarting the phone.
9. As an administrator, I want to tap any previous entry in the scan history to re-copy its 10-digit number, so that I can recover a number if my clipboard was overwritten.
10. As an administrator, I want a "Clear History" button with a confirmation safeguard, so that I can clear old records when a registration batch is finished.
11. As an administrator, I want a "Share" button for the current scan, so that I can send the number directly via WhatsApp, messaging apps, or notes.
12. As an administrator, I want the app to handle 7-byte UID tags (such as MIFARE Ultralight or NTAG) gracefully by parsing the first 4 bytes, so that the app never crashes or produces inconsistent digits when non-standard cards are scanned.
13. As an administrator, I want clear visual guidance when NFC is disabled on my device, with a direct shortcut to open Android NFC settings, so that I can quickly turn it on.
14. As an administrator, I want the app to operate 100% offline without requiring internet access or account logins, so that it functions reliably anywhere with zero data privacy concerns.
15. As an administrator, I want a clean Material 3 UI that automatically adapts to Dark and Light mode, so that using the app is comfortable in any lighting condition.

## Implementation Decisions

- **Framework & UI Toolkit:** Android Native dibangun dengan bahasa Kotlin dan Jetpack Compose serta komponen desain Material 3 untuk ukuran file yang ringan, performa cepat, dan tampilan modern.
- **NFC Engine Architecture:** Menggunakan `NfcAdapter.enableReaderMode` dengan kombinasi flag `FLAG_READER_NFC_A`, `FLAG_READER_SKIP_NDEF_CHECK`, dan `FLAG_READER_NO_PLATFORM_SOUNDS` yang terikat pada siklus hidup Activity untuk membaca kartu tanpa latensi dan tanpa interupsi sistem OS.
- **UID Decoding Engine:**
  - Konversi UID 4-byte: Dikonversi ke bilangan 32-bit Big-Endian unsigned integer menggunakan tipe data 64-bit `Long` untuk menghindari angka negatif Java, diformat dengan *zero-padding* 10 digit (`%010d`).
  - Penanganan UID 7-byte: Mengekstrak 4-byte pertama untuk standarisasi 10 digit, serta tetap menyajikan byte heksadesimal lengkap pada detail sekunder.
  - Format Sekunder: Menghitung format 10 digit Little-Endian (`%010d`) dan format Heksadesimal mentah (`XX:XX:XX:XX`).
- **Storage & Persistence:** Penyimpanan lokal menggunakan SQLite / Room Database untuk menyimpan riwayat (Nomor 10-digit primer, format alternatif, Hex, dan stempel waktu) secara offline tanpa sinkronisasi internet.
- **Clipboard & Feedback Engine:** Integrasi langsung dengan `ClipboardManager` sistem Android untuk auto-copy instan dan `Vibrator` sistem untuk getaran konfirmasi satu kali (50-80ms).
- **Architectural Seam:** Satu seam utama pemrosesan kartu yang menerima raw byte UID dan menghasilkan state model hasil konversi, terpisah dari lifecycle Android UI.

## Testing Decisions

- **Filosofi Pengujian:** Pengujian difokuskan pada perilaku eksternal modul parsing UID dan pemrosesan alur data, bukan pada detail implementasi internal atau mocking siklus hidup Android yang berlebihan.
- **Seam yang Diuji:** Modul logika konversi byte (`UidConverter` / `CardProcessor`) melalui unit test lokal JVM yang cepat dan deterministik.
- **Cakupan Pengujian:**
  - Verifikasi konversi 4-byte Big-Endian standar (contoh: byte `[0x90, 0x80, 0x78, 0x0F]` harus menghasilkan persis `"2424338447"`).
  - Verifikasi zero-padding 10 digit pada nilai integer yang memiliki panjang digit < 10 (contoh: `[0x0F, 0x78, 0x80, 0x90]` harus menghasilkan `"0259555472"`).
  - Penanganan kartu 7-byte dan validasi byte kosong atau tidak valid.
  - Akurasi pemformatan Little-Endian dan Heksadesimal.

## Out of Scope

- Integrasi API langsung atau otomatisasi pengisian form ke portal web Pesantrenqu (aplikasi ini bertindak sebagai alat utilitas penangkap nomor).
- Operasi penulisan data ke dalam kartu RFID/NFC (hanya operasi baca UID).
- Otentikasi pengguna, sistem akun, atau sinkronisasi cloud.
- Pembacaan sektor terenkripsi internal chip MIFARE (hanya membaca nomor serial perangkat keras publik / UID).

## Further Notes

- Repositori proyek terhubung ke: `https://github.com/KaleksananBarqi/maos-nfc-rfid-android`.
- Keputusan arsitektur tercatat di `docs/adr/0001-native-compose-and-uid-conversion.md` dan `docs/adr/0002-nfc-reader-mode-and-history-storage.md`.
- Istilah resmi dicatat di `GLOSSARY.md`.
