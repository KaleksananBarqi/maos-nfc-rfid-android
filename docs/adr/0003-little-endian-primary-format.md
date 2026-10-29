# Penetapan Format Little-Endian sebagai Format Utama Pemindaian 10 Digit

## Konteks
Pengujian langsung menggunakan perangkat keras USB RFID Reader fisik di lapangan membuktikan bahwa perangkat pembaca PC membaca byte UID kartu RFID santri dalam urutan **Little-Endian** (byte dibalik: LSB ke MSB). Sebagai contoh, kartu dengan UID `[0xF7, 0x7B, 0x80, 0x90]` menghasilkan angka desimal `2424339447` pada reader USB PC, sementara format Big-Endian menghasilkan `4152066192`.

## Keputusan
1. **Format Utama:** Menetapkan konversi **Little-Endian 10 Digit** (`%010d`) sebagai format utama aplikasi yang ditampilkan dengan font besar di kartu hasil pemindaian dan disimpan sebagai judul riwayat kartu.
2. **Auto-Copy Clipboard:** Nilai yang otomatis disalin ke *clipboard* perangkat saat kartu di-tap diubah dari Big-Endian menjadi Little-Endian agar admin dapat langsung menempelkannya ke sistem web Pesantrenqu tanpa penyesuaian manual.
3. **Format Sekunder:** Big-Endian 10 Digit tetap dihitung dan disajikan di bawah angka utama sebagai format sekunder bersama format Heksadesimal mentah untuk fleksibilitas.
