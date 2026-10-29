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

    // == Little-Endian 10 Digit (Format Utama) ==

    @Test
    fun `konversi UID kartu santri menghasilkan desimal 10 digit Little-Endian 2424339447`() {
        // UID kartu santri dari pengujian aktual ponsel & USB Reader PC: F7:7B:80:90
        val uid = byteArrayOf(0xF7.toByte(), 0x7B, 0x80.toByte(), 0x90.toByte())
        val result = UidConverter.convert(uid)

        // Format primer harus Little-Endian
        assertEquals("2424339447", result.decimal10Digit)
        // Format sekunder harus Big-Endian
        assertEquals("4152066192", result.decimalBigEndian)
        // Format heksadesimal mentah
        assertEquals("F7:7B:80:90", result.hexFormatted)
    }

    @Test
    fun `konversi 4-byte UID dengan urutan byte kecil menghasilkan zero-padding 10 digit`() {
        val uid = byteArrayOf(0x01, 0x00, 0x00, 0x00)
        val result = UidConverter.convert(uid)
        assertEquals("0000000001", result.decimal10Digit)
    }

    @Test
    fun `konversi UID bernilai nol menghasilkan 10 digit nol`() {
        val uid = byteArrayOf(0x00, 0x00, 0x00, 0x00)
        val result = UidConverter.convert(uid)
        assertEquals("0000000000", result.decimal10Digit)
        assertEquals("0000000000", result.decimalBigEndian)
    }

    @Test
    fun `konversi UID bernilai maksimum 32-bit unsigned`() {
        // 0xFFFFFFFF = 4294967295 (tepat 10 digit)
        val uid = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        val result = UidConverter.convert(uid)
        assertEquals("4294967295", result.decimal10Digit)
        assertEquals("4294967295", result.decimalBigEndian)
    }

    @Test
    fun `konversi UID dengan byte tinggi yang menyebabkan signed integer negatif`() {
        // 0x00000080 dalam Little-Endian -> byte[3]=0x80 -> 2147483648
        val uid = byteArrayOf(0x00, 0x00, 0x00, 0x80.toByte())
        val result = UidConverter.convert(uid)
        assertEquals("2147483648", result.decimal10Digit)
    }

    // == Big-Endian 10 Digit (Format Sekunder) ==

    @Test
    fun `format sekunder Big-Endian dihitung dengan benar`() {
        val uid = byteArrayOf(0x90.toByte(), 0x80.toByte(), 0x78, 0x0F)
        val result = UidConverter.convert(uid)
        // Big-Endian dari 9080780F adalah 2424338447
        assertEquals("2424338447", result.decimalBigEndian)
    }

    // == Hex Formatted ==

    @Test
    fun `format hex untuk 4-byte UID`() {
        val uid = byteArrayOf(0xF7.toByte(), 0x7B, 0x80.toByte(), 0x90.toByte())
        val result = UidConverter.convert(uid)
        assertEquals("F7:7B:80:90", result.hexFormatted)
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

    // == Kartu 7-Byte ==

    @Test
    fun `konversi 7-byte UID mengekstrak 4 byte pertama untuk desimal`() {
        val uid = byteArrayOf(
            0xF7.toByte(), 0x7B, 0x80.toByte(), 0x90.toByte(),
            0xAA.toByte(), 0xBB.toByte(), 0xCC.toByte()
        )
        val result = UidConverter.convert(uid)
        // Desimal 10 digit primer tetap dari 4 byte pertama
        assertEquals("2424339447", result.decimal10Digit)
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
        val uid = byteArrayOf(0xF7.toByte(), 0x7B, 0x80.toByte(), 0x90.toByte())
        val result = UidConverter.convert(uid)

        assertEquals(uid.toList(), result.rawBytes.toList())

        // Memastikan immutability
        uid[0] = 0x00
        assertEquals(0xF7.toByte(), result.rawBytes[0])
    }
}
