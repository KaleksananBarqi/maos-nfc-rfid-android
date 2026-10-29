package com.pesantrenqu.maos.domain

/**
 * Hasil konversi UID kartu RFID ke berbagai format representasi.
 *
 * @property decimal10Digit Format desimal 10 digit Big-Endian (format utama, kompatibel dengan USB RFID Reader)
 * @property decimalLittleEndian Format desimal 10 digit Little-Endian (format alternatif)
 * @property hexFormatted Format heksadesimal mentah dipisahkan titik dua (contoh: "90:80:78:0F")
 * @property rawBytes Array byte UID asli dari kartu
 */
data class CardScanResult(
    val decimal10Digit: String,
    val decimalLittleEndian: String,
    val hexFormatted: String,
    val rawBytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CardScanResult) return false
        return decimal10Digit == other.decimal10Digit &&
                decimalLittleEndian == other.decimalLittleEndian &&
                hexFormatted == other.hexFormatted &&
                rawBytes.contentEquals(other.rawBytes)
    }

    override fun hashCode(): Int {
        var result = decimal10Digit.hashCode()
        result = 31 * result + decimalLittleEndian.hashCode()
        result = 31 * result + hexFormatted.hashCode()
        result = 31 * result + rawBytes.contentHashCode()
        return result
    }
}
