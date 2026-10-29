package com.pesantrenqu.maos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas database untuk menyimpan riwayat pemindaian kartu RFID.
 *
 * Setiap baris merepresentasikan satu kali pemindaian kartu dengan
 * seluruh format representasi UID dan stempel waktu pemindaian.
 */
@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Format desimal 10 digit Big-Endian (format utama) */
    val decimal10Digit: String,

    /** Format desimal 10 digit Little-Endian (format alternatif) */
    val decimalLittleEndian: String,

    /** Format heksadesimal mentah (contoh: "90:80:78:0F") */
    val hexFormatted: String,

    /** Stempel waktu pemindaian dalam milidetik epoch */
    val scannedAt: Long = System.currentTimeMillis()
)
