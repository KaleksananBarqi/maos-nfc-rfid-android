# Maos NFC RFID Android v1.0.1 🚀

Pembaruan v1.0.1 menyesuaikan konvensi format desimal 10 digit menjadi **Little-Endian** sebagai format utama dan target *auto-copy* ke *clipboard*, sesuai dengan hasil pemindaian langsung pada perangkat keras USB RFID Reader di PC.

---

### 🔧 Perubahan & Peningkatan

- 🔄 **Little-Endian 10 Digit Jadi Format Utama:**
  - Kartu santri dengan UID `F7:7B:80:90` kini langsung menampilkan nomor **`2424339447`** dalam font besar (36sp) di kartu utama.
  - Sesuai 100% dengan output scanner USB PC yang terhubung ke web admin Pesantrenqu.
- 📋 **Auto-Copy Little-Endian:**
  - Menempelkan kartu ke belakang ponsel kini otomatis menyalin angka **`2424339447`** ke *clipboard*.
- 📜 **Penyelarasan Riwayat:**
  - Setiap kartu di riwayat pemindaian menampilkan nomor Little-Endian sebagai judul utama.
  - Subtitle riwayat kini menyajikan format alternatif `BE: <nomor> • Hex: <UID>`.
  - Tombol salin pada riwayat otomatis menyalin nomor Little-Endian.
- 🧪 **Pengujian Unit Diperbarui:**
  - Menambahkan pengujian spesifik untuk kartu santri aktual `[0xF7, 0x7B, 0x80, 0x90]` $\rightarrow$ `"2424339447"`.

---

### 📲 Cara Pemasangan (Instalasi)

1. Unduh berkas APK (**`Maos-NFC-RFID-Android-v1.0.1.apk`**) dari bagian **Assets** di bawah.
2. Pasang/timpa (update) APK pada ponsel Android Anda.
3. Tempelkan kartu RFID santri ke bagian belakang HP: nomor **`2424339447`** akan langsung tersalin ke clipboard!
