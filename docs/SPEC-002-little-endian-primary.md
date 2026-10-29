# Spesifikasi: Pengalihan Format Utama dan Auto-Copy ke Little-Endian 10 Digit

## Problem Statement
Pada versi rilis v1.0.0, aplikasi Maos NFC RFID menampilkan dan menyalin angka 10 digit dengan konvensi Big-Endian sebagai nilai utama (`4152066192`). Namun saat diuji langsung dengan perangkat keras USB RFID Reader di komputer, mesin reader fisik tersebut menghasilkan format Little-Endian (`2424339447`). Akibatnya, angka yang otomatis tersalin ke clipboard tidak cocok dengan input form web admin Pesantrenqu jika admin tidak menyalin format Little-Endian secara manual dari bagian info sekunder.

## Solution
Menyesuaikan aplikasi agar format Little-Endian 10 digit (`%010d`) menjadi nilai utama di seluruh antarmuka dan alur otomatisasi:
1. Menampilkan angka Little-Endian (`2424339447`) dengan ukuran font besar (36sp) sebagai `Nomor RFID (10 Digit)` utama.
2. Otomatis menyalin (*auto-copy*) angka Little-Endian ke *clipboard* saat kartu di-tap.
3. Menjadikan angka Little-Endian sebagai judul utama pada setiap item riwayat pemindaian dan aksi salin per item.
4. Menampilkan format Big-Endian (`4152066192`) sebagai informasi sekunder di bawah bersama Hex UID mentah.

## User Stories

1. As an administrator, I want the app to automatically copy the Little-Endian 10-digit number (e.g., `2424339447`) to my clipboard upon tapping a card, so that the copied number directly matches the output of my physical desktop USB reader.
2. As an administrator, I want the large primary number displayed on the screen to be the Little-Endian format, so that I can immediately verify the number against the physical card registration form.
3. As an administrator, I want the scan history list to feature the Little-Endian 10-digit number as the primary title for each entry, so that tapping any history item copies the correct number.
4. As an administrator, I want the Big-Endian number to remain available in the secondary details section, so that I still have access to it if an alternative reader type is ever used.
5. As an administrator, I want the "Share" action to share the primary Little-Endian 10-digit number, so that recipients receive the exact number expected by the Pesantrenqu system.

## Implementation Decisions

- **Domain Model & Parser Alignment:**
  - Pada `CardScanResult`, `primaryDecimal10Digit` ditetapkan ke hasil perhitungan Little-Endian (`toLittleEndianDecimal`), sedangkan format Big-Endian disajikan sebagai `secondaryDecimal10Digit`.
  - Nilai desimal utama diformat dengan *zero-padding* 10 digit menggunakan `Locale.US` (`%010d`).
- **ViewModel Auto-Copy:**
  - Pemanggilan `ClipData.newPlainText` pada saat kartu di-tap secara otomatis menyalin format Little-Endian primer ke *clipboard*.
- **UI Components Alignment:**
  - `ScanResultCard`: Menampilkan nilai Little-Endian pada teks monospace 36sp utama. Pada bagian detail sekunder, menampilkan label `Big-Endian: XXXXXXXXXX` dan `Hex UID: XX:XX:XX:XX`.
  - `ScanHistoryList` & `HistoryItem`: Judul utama entri riwayat menampilkan Little-Endian, dan tombol salin riwayat menyalin Little-Endian.
- **Penyimpanan Database:**
  - Mempertahankan persistensi lokal dengan `decimal10Digit` menyimpan format Little-Endian utama.

## Testing Decisions

- **Filosofi Pengujian:** Menguji seam konversi byte (`UidConverter`) dengan memastikan bahwa input byte aktual lapangan `[0xF7, 0x7B, 0x80, 0x90]` menghasilkan format primer `"2424339447"`.
- **Modul yang Diuji:** `UidConverterTest` pada JVM lokal.
- **Skenario Uji:**
  - Kartu santri aktual: byte `[0xF7, 0x7B, 0x80, 0x90]` harus menghasilkan format primer `"2424339447"` dan sekunder `"4152066192"`.
  - Zero-padding Little-Endian untuk nilai byte kecil.
  - Penanganan kartu 7-byte dengan ekstraksi 4 byte pertama.

## Out of Scope

- Menghapus dukungan Big-Endian sepenuhnya (format Big-Endian tetap disediakan sebagai data sekunder untuk kompatibilitas perangkat lain).
- Perubahan skema tabel Room Database yang memerlukan migrasi destruktif.

## Further Notes

- Konfirmasi langsung dari tangkapan layar ponsel pengguna pada pengujian v1.0.0 menunjukkan UID `F7:7B:80:90` menghasilkan `Little-Endian: 2424339447`.
- Didokumentasikan pada ADR `docs/adr/0003-little-endian-primary-format.md`.
