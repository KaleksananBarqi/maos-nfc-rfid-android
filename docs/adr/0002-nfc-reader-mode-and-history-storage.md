# Penggunaan NFC Reader Mode dan Penyimpanan Riwayat Lokal

## Konteks
Aplikasi membutuhkan pemindaian kartu RFID/NFC yang instan di lapangan tanpa gangguan sistem operasi (seperti suara sistem bawaan, dialog pemilih aplikasi, atau delay pemrosesan NDEF), serta kemampuan mempertahankan riwayat kartu yang telah di-scan saat pengguna berpindah ke browser web admin.

## Keputusan
1. **Mekanisme NFC:** Menggunakan `NfcAdapter.enableReaderMode` dengan flag `FLAG_READER_NFC_A`, `FLAG_READER_SKIP_NDEF_CHECK`, dan `FLAG_READER_NO_PLATFORM_SOUNDS`. Ini mengabaikan pemrosesan NDEF yang tidak diperlukan dan memberikan respon haptik kustom secara langsung.
2. **Format Standar:** Menetapkan format baku 10 digit dengan zero-padding (`%010d`) berbasis Big-Endian unsigned 32-bit integer, dengan ekstraksi 4-byte pertama untuk kartu ber-UID 7-byte.
3. **Penyimpanan Riwayat:** Menggunakan SQLite/Room Database lokal untuk persistensi riwayat scan offline secara ringan tanpa kebutuhan sinkronisasi cloud atau login pengguna.
