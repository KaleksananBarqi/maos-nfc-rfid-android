package com.pesantrenqu.maos.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Unit test untuk [UidConverter].
 *
 * Menguji seam utama konversi byte UID kartu RFID ke format desimal 10 digit.
 * Test ini berjalan di JVM lokal tanpa emulator Android.
 *
 * Data test menggunakan nilai literal yang telah diverifikasi terhadap
 * output USB RFID Reader fisik pada sistem Pesantrenqu.
 */
class UidConverterTest {

    // == Big-Endian 10 Digit ==

    @Test
    fun `konversi 4-byte UID standar ke desimal 10 digit Big-Endian`() {
        // UID kartu santri "Adi Siestaa" dari sistem Pesantrenqu
        val uid = byteArrayOf(0x90.toByte(), 0x80.toByte(), 0x78, 0x0F)
        val result = UidConverter.convert(uid)
        assertEquals("2424338447", result.decimal10Digit)
    }

    @Test
    fun `konversi UID bernilai kecil menghasilkan zero-padding 10 digit`() {
        // UID yang menghasilkan angka kurang dari 10 digit harus di-pad dengan nol
        val uid = byteArrayOf(0x00, 0x00, 0x00, 0x01)
        val result = UidConverter.convert(uid)
        assertEquals("0000000001", result.decimal10Digit)
    }

    @Test
    fun `konversi UID bernilai nol menghasilkan 10 digit nol`() {
        val uid = byteArrayOf(0x00, 0x00, 0x00, 0x00)
        val result = UidConverter.convert(uid)
        assertEquals("0000000000", result.decimal10Digit)
    }

    @Test
    fun `konversi UID bernilai maksimum 32-bit unsigned`() {
        // 0xFFFFFFFF = 4294967295 (tepat 10 digit)
        val uid = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        val result = UidConverter.convert(uid)
        assertEquals("4294967295", result.decimal10Digit)
    }

    @Test
    fun `konversi UID dengan byte tinggi yang menyebabkan signed integer negatif`() {
        // 0x80000000 = 2147483648, yang akan menjadi negatif jika diperlakukan sebagai signed Int
        val uid = byteArrayOf(0x80.toByte(), 0x00, 0x00, 0x00)
        val result = UidConverter.convert(uid)
        assertEquals("2147483648", result.decimal10Digit)
    }

    // == Little-Endian 10 Digit ==

    @Test
    fun `konversi 4-byte UID ke desimal 10 digit Little-Endian`() {
        val uid = byteArrayOf(0x90.toByte(), 0x80.toByte(), 0x78, 0x0F)
        val result = UidConverter.convert(uid)
        assertEquals("0259555472", result.decimalLittleEndian)
    }

    @Test
    fun `Little-Endian dengan zero-padding`() {
        val uid = byteArrayOf(0x01, 0x00, 0x00, 0x00)
        val result = UidConverter.convert(uid)
        assertEquals("0000000001", result.decimalLittleEndian)
    }

    // == Hex Formatted ==

    @Test
    fun `format hex untuk 4-byte UID`() {
        val uid = byteArrayOf(0x90.toByte(), 0x80.toByte(), 0x78, 0x0F)
        val result = UidConverter.convert(uid)
        assertEquals("90:80:78:0F", result.hexFormatted)
    }

    @Test
    fun `format hex untuk 7-byte UID menampilkan seluruh byte`() {
        val uid = byteArrayOf(
            0x04, 0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte(),
            0x12, 0x34, 0x56
        )
        val result = UidConverter.convert(uid)
        assertEquals("04:AB:CD:EF:12:34:56", result.hexFormatted)
    }

    @Test
    fun `format hex menggunakan huruf kapital dan zero-pad per byte`() {
        val uid = byteArrayOf(0x0A, 0x0B, 0x0C, 0x0D)
        val result = UidConverter.convert(uid)
        assertEquals("0A:0B:0C:0D", result.hexFormatted)
    }

    // == Kartu 7-Byte ==

    @Test
    fun `konversi 7-byte UID mengekstrak 4 byte pertama untuk desimal`() {
        // Untuk kartu 7-byte (MIFARE Ultralight/NTAG), hanya 4 byte pertama yang digunakan
        val uid = byteArrayOf(
            0x90.toByte(), 0x80.toByte(), 0x78, 0x0F,
            0xAA.toByte(), 0xBB.toByte(), 0xCC.toByte()
        )
        val result = UidConverter.convert(uid)
        // Desimal 10 digit tetap dari 4 byte pertama
        assertEquals("2424338447", result.decimal10Digit)
    }

    // == Validasi Input ==

    @Test
    fun `UID kurang dari 4 byte melempar IllegalArgumentException`() {
        val shortUid = byteArrayOf(0x01, 0x02, 0x03)
        assertThrows(IllegalArgumentException::class.java) {
            UidConverter.convert(shortUid)
        }
    }

    @Test
    fun `UID kosong melempar IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            UidConverter.convert(byteArrayOf())
        }
    }

    // == Raw Bytes ==

    @Test
    fun `rawBytes menyimpan salinan UID asli tanpa modifikasi`() {
        val uid = byteArrayOf(0x90.toByte(), 0x80.toByte(), 0x78, 0x0F)
        val result = UidConverter.convert(uid)

        // rawBytes harus berisi data yang sama
        assertEquals(uid.toList(), result.rawBytes.toList())

        // rawBytes harus merupakan salinan terpisah (bukan referensi yang sama)
        uid[0] = 0x00
        assertEquals(0x90.toByte(), result.rawBytes[0])
    }
}
