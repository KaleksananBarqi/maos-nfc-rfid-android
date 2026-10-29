package com.pesantrenqu.maos.domain

import java.util.Locale

/**
 * Mesin konversi UID kartu RFID ke format desimal 10 digit.
 *
 * Mengkonversi byte UID mentah (4 atau 7 byte) menjadi representasi
 * desimal 10 digit Little-Endian yang identik dengan output USB RFID Reader fisik.
 *
 * Logika konversi:
 * - Mengambil 4 byte pertama dari UID (untuk kompatibilitas kartu 4-byte dan 7-byte)
 * - Mengkonversi ke unsigned 32-bit integer Little-Endian sebagai format primer
 * - Mengkonversi ke unsigned 32-bit integer Big-Endian sebagai format sekunder
 * - Memformat dengan zero-padding 10 digit (%010d) menggunakan Locale.US
 */
object UidConverter {

    /**
     * Mengkonversi byte UID mentah menjadi [CardScanResult] berisi semua format representasi.
     *
     * @param uid Array byte UID dari kartu NFC (minimal 4 byte)
     * @return [CardScanResult] dengan format desimal 10 digit Little-Endian (utama), Big-Endian, dan Hex
     * @throws IllegalArgumentException jika UID kurang dari 4 byte
     */
    fun convert(uid: ByteArray): CardScanResult {
        require(uid.size >= 4) {
            "UID harus minimal 4 byte, diterima ${uid.size} byte"
        }

        // Ambil 4 byte pertama untuk konversi 10 digit
        val fourBytes = uid.copyOfRange(0, 4)

        // Little-Endian adalah format desimal 10 digit utama sesuai pembaca USB fisik
        val primaryLittleEndian = toLittleEndianDecimal(fourBytes)
        val secondaryBigEndian = toBigEndianDecimal(fourBytes)
        val hexString = toHexFormatted(uid)

        return CardScanResult(
            decimal10Digit = primaryLittleEndian,
            decimalBigEndian = secondaryBigEndian,
            hexFormatted = hexString,
            rawBytes = uid.copyOf()
        )
    }

    /**
     * Mengkonversi 4 byte ke desimal 10 digit dalam urutan Big-Endian.
     * Byte pertama adalah Most Significant Byte (MSB).
     *
     * Contoh: [0xF7, 0x7B, 0x80, 0x90] -> 4152066192
     */
    internal fun toBigEndianDecimal(bytes: ByteArray): String {
        val value = ((bytes[0].toLong() and 0xFF) shl 24) or
                ((bytes[1].toLong() and 0xFF) shl 16) or
                ((bytes[2].toLong() and 0xFF) shl 8) or
                (bytes[3].toLong() and 0xFF)
        return String.format(Locale.US, "%010d", value)
    }

    /**
     * Mengkonversi 4 byte ke desimal 10 digit dalam urutan Little-Endian.
     * Byte terakhir adalah Most Significant Byte (MSB).
     *
     * Contoh: [0xF7, 0x7B, 0x80, 0x90] -> 2424339447
     */
    internal fun toLittleEndianDecimal(bytes: ByteArray): String {
        val value = ((bytes[3].toLong() and 0xFF) shl 24) or
                ((bytes[2].toLong() and 0xFF) shl 16) or
                ((bytes[1].toLong() and 0xFF) shl 8) or
                (bytes[0].toLong() and 0xFF)
        return String.format(Locale.US, "%010d", value)
    }

    /**
     * Memformat seluruh byte UID menjadi string heksadesimal dengan pemisah titik dua.
     *
     * Contoh: [0xF7, 0x7B, 0x80, 0x90] -> "F7:7B:80:90"
     */
    internal fun toHexFormatted(bytes: ByteArray): String {
        return bytes.joinToString(":") { byte ->
            String.format(Locale.US, "%02X", byte)
        }
    }
}
