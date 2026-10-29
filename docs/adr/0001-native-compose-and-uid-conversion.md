# Pilihan Native Android Kotlin Compose dan Konversi UID 32-bit Unsigned

## Konteks
Aplikasi membutuhkan akses perangkat keras NFC untuk membaca kartu santri (13.56 MHz ISO 14443-A) dan langsung menghasilkan angka 10 digit desimal yang identik dengan output perangkat RFID USB Reader di sistem Pesantrenqu (`2424338447`).

## Keputusan
1. **Framework:** Menggunakan Native Android dengan Kotlin dan Jetpack Compose tanpa plugin pihak ketiga, guna memastikan akses NFC tanpa latensi dan ukuran aplikasi yang minimal.
2. **Logika Konversi:** Mengonversi 4-byte UID kartu RFID menjadi bilangan 32-bit unsigned integer (menggunakan tipe data `Long` untuk menghindari masalah *signed integer* negatif di Java/Kotlin), dengan format standar 10 digit Big-Endian, serta menampilkan opsi Little-Endian dan raw Hex sebagai referensi sekunder.
3. **Penyampaian Output:** Menggunakan mekanisme otomatis salin ke clipboard (*auto-copy*) disertai umpan balik haptik/suara agar pengguna dapat langsung menempelkan (*paste*) hasil pemindaian ke formulir pendaftaran santri di browser atau aplikasi lain.
