# RFID 10 Angka Taker

Konteks untuk aplikasi pembacaan kartu identitas RFID santri berbasis Android NFC dan konversi identitas kartu ke format desimal 10 digit yang kompatibel dengan sistem Pesantrenqu.

## Language

**UID (Unique Identifier)**:
Nomor identitas unik bawaan dari chip RFID (biasanya 4 byte atau 7 byte) sesuai standar ISO 14443.
_Avoid_: ID Kartu, Hex Code, Serial Fisik

**Nomor RFID (10 Digit)**:
Representasi desimal 32-bit unsigned integer (panjang 10 digit dengan zero-padding jika kurang) dari 4 byte UID kartu RFID yang digunakan sebagai kunci pencocokan data santri di sistem web.
_Avoid_: Barcode, Nomor Akun, ID Santri

**RFID USB Reader**:
Perangkat keras pembaca kartu 13.56 MHz eksternal berbasis USB keyboard emulation yang langsung mengeluarkan 10 digit desimal saat kartu ditempel.
_Avoid_: Scanner Barcode, Dongle NFC

**RFID Taker App**:
Aplikasi Android berbasis NFC yang menangkap UID kartu dan langsung menyajikannya dalam format desimal 10 digit untuk disalin atau dikirim.
_Avoid_: NFC Tools, Tag Info

**ReaderMode API**:
Antarmuka pemrograman Android (`enableReaderMode`) untuk memindai tag NFC secara instan dengan menonaktifkan suara sistem bawaan dan mencegah dialog pemilih aplikasi.
_Avoid_: Foreground Dispatch, Intent Filter

**Endianness**:
Konvensi urutan pembacaan byte UID chip; Big-Endian membaca dari byte paling signifikan (MSB) ke LSB, sedangkan Little-Endian membaca dari urutan sebaliknya.
_Avoid_: Byte Reversing, Arah Angka
