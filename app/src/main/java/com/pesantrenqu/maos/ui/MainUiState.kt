package com.pesantrenqu.maos.ui

import com.pesantrenqu.maos.data.ScanHistoryEntity

/**
 * State UI utama aplikasi Maos NFC RFID.
 *
 * Merepresentasikan seluruh kondisi layar yang perlu ditampilkan
 * oleh Composable, termasuk status NFC, hasil pemindaian terakhir,
 * dan riwayat pemindaian.
 */
data class MainUiState(
    /** Apakah perangkat mendukung NFC */
    val isNfcSupported: Boolean = true,

    /** Apakah NFC saat ini aktif di pengaturan perangkat */
    val isNfcEnabled: Boolean = true,

    /** Angka 10 digit desimal Little-Endian hasil pemindaian terakhir (format utama) */
    val lastDecimal10Digit: String? = null,

    /** Angka 10 digit desimal Big-Endian hasil pemindaian terakhir (format sekunder) */
    val lastDecimalBigEndian: String? = null,

    /** Format heksadesimal mentah hasil pemindaian terakhir */
    val lastHexFormatted: String? = null,

    /** Apakah baru saja berhasil menyalin ke clipboard (untuk animasi snackbar) */
    val justCopied: Boolean = false,

    /** Daftar riwayat pemindaian dari database lokal */
    val history: List<ScanHistoryEntity> = emptyList()
)
