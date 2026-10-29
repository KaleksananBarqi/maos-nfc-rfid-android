package com.pesantrenqu.maos.domain

/**
 * Mesin konversi UID kartu RFID ke format desimal 10 digit.
 *
 * Mengkonversi byte UID mentah (4 atau 7 byte) menjadi representasi
 * desimal 10 digit yang identik dengan output USB RFID Reader standar.
 *
 * Logika konversi:
 * - Mengambil 4 byte pertama dari UID (untuk kompatibilitas kartu 4-byte dan 7-byte)
 * - Mengkonversi ke unsigned 32-bit integer menggunakan tipe Long untuk menghindari
 *   masalah signed integer negatif di Java/Kotlin
 * - Memformat dengan zero-padding 10 digit (%010d)
 */
object UidConverter {

    /**
     * Mengkonversi byte UID mentah menjadi [CardScanResult] berisi semua format representasi.
     *
     * @param uid Array byte UID dari kartu NFC (minimal 4 byte)
     * @return [CardScanResult] dengan format desimal 10 digit Big-Endian, Little-Endian, dan Hex
     * @throws IllegalArgumentException jika UID kurang dari 4 byte
     */
    fun convert(uid: ByteArray): CardScanResult {
        require(uid.size >= 4) {
            "UID harus minimal 4 byte, diterima ${uid.size} byte"
        }

        // Ambil 4 byte pertama untuk konversi 10 digit
        val fourBytes = uid.copyOfRange(0, 4)

        val bigEndianDecimal = toBigEndianDecimal(fourBytes)
        val littleEndianDecimal = toLittleEndianDecimal(fourBytes)
        val hexString = toHexFormatted(uid)

        return CardScanResult(
            decimal10Digit = bigEndianDecimal,
            decimalLittleEndian = littleEndianDecimal,
            hexFormatted = hexString,
            rawBytes = uid.copyOf()
        )
    }

    /**
     * Mengkonversi 4 byte ke desimal 10 digit dalam urutan Big-Endian.
     * Byte pertama adalah Most Significant Byte (MSB).
     *
     * Contoh: [0x90, 0x80, 0x78, 0x0F] -> 2424338447
     */
    internal fun toBigEndianDecimal(bytes: ByteArray): String {
        val value = ((bytes[0].toLong() and 0xFF) shl 24) or
                ((bytes[1].toLong() and 0xFF) shl 16) or
                ((bytes[2].toLong() and 0xFF) shl 8) or
                (bytes[3].toLong() and 0xFF)
        return String.format("%010d", value)
    }

    /**
     * Mengkonversi 4 byte ke desimal 10 digit dalam urutan Little-Endian.
     * Byte terakhir adalah Most Significant Byte (MSB).
     *
     * Contoh: [0x90, 0x80, 0x78, 0x0F] -> 0259555472
     */
    internal fun toLittleEndianDecimal(bytes: ByteArray): String {
        val value = ((bytes[3].toLong() and 0xFF) shl 24) or
                ((bytes[2].toLong() and 0xFF) shl 16) or
                ((bytes[1].toLong() and 0xFF) shl 8) or
                (bytes[0].toLong() and 0xFF)
        return String.format("%010d", value)
    }

    /**
     * Memformat seluruh byte UID menjadi string heksadesimal dengan pemisah titik dua.
     *
     * Contoh: [0x90, 0x80, 0x78, 0x0F] -> "90:80:78:0F"
     */
    internal fun toHexFormatted(bytes: ByteArray): String {
        return bytes.joinToString(":") { byte ->
            String.format("%02X", byte)
        }
    }
}
