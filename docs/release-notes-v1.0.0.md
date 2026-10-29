# Maos NFC RFID Android v1.0.0 🚀

Rilis perdana aplikasi **Maos NFC RFID Android**, solusi praktis untuk membaca kartu identitas RFID santri (frekuensi 13.56 MHz ISO 14443-A) menggunakan smartphone Android ber-NFC dan secara otomatis mengonversinya menjadi **nomor desimal 10 digit** standar USB RFID Reader untuk sistem web Pesantrenqu.

---

### ✨ Fitur Utama

- 🔢 **Konversi Instan 10 Digit:** Mengonversi 4-byte hardware UID kartu langsung ke format 10 digit desimal Big-Endian (`%010d`). Contoh: UID `[0x90, 0x80, 0x78, 0x0F]` $\rightarrow$ `2424338447`.
- 📋 **Auto-Copy ke Clipboard:** Begitu kartu ditempelkan di belakang ponsel, nomor 10 digit langsung otomatis tersalin ke *clipboard* tanpa perlu klik manual.
- 📳 **Haptic Feedback:** Getaran konfirmasi instan (60ms) begitu kartu berhasil terbaca.
- ⚡ **Zero-Latency Reader Mode:** Menggunakan `NfcAdapter.enableReaderMode` dengan flag `FLAG_READER_NFC_A`, `FLAG_READER_SKIP_NDEF_CHECK`, dan `FLAG_READER_NO_PLATFORM_SOUNDS` untuk pemindaian mulus tanpa pop-up sistem dan tanpa suara bawaan OS.
- 🔄 **Dukungan Multi-Format & Kartu 7-Byte:** Menampilkan format sekunder Little-Endian (`0259555472`) dan Heksadesimal mentah (`90:80:78:0F`), serta otomatis mengekstrak 4 byte pertama untuk kartu 7-byte (MIFARE Ultralight / NTAG).
- 📜 **Riwayat Lokal Persisten:** Riwayat pemindaian disimpan secara aman di SQLite/Room database lokal (100% offline tanpa login) dengan tombol salin ulang dan hapus riwayat.
- 📤 **Bagikan Cepat:** Tombol bagikan langsung ke WhatsApp, Catatan, atau aplikasi pesan lainnya.
- 🎨 **Material 3 UI:** Antarmuka modern yang secara otomatis beradaptasi dengan Dark Mode dan Light Mode sistem.

---

### 📲 Cara Pemasangan (Instalasi)

1. Unduh berkas APK (**`Maos-NFC-RFID-Android-v1.0.0.apk`**) dari bagian **Assets** rilis ini.
2. Buka berkas APK di ponsel Android Anda dan izinkan instalasi dari sumber tidak dikenal jika diminta.
3. Pastikan fitur **NFC** aktif di pengaturan ponsel.
4. Buka aplikasi dan tempelkan kartu RFID di bodi belakang ponsel Anda!
