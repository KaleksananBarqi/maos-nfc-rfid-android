package com.pesantrenqu.maos.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pesantrenqu.maos.data.AppDatabase
import com.pesantrenqu.maos.data.ScanHistoryEntity
import com.pesantrenqu.maos.domain.UidConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel utama yang menghubungkan mesin konversi UID, database riwayat,
 * clipboard, dan getaran haptik dengan lapisan UI Compose.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).scanHistoryDao()
    private val clipboard =
        application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager =
            application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Memuat riwayat pemindaian secara reaktif dari database
        viewModelScope.launch {
            dao.getAllHistory().collect { historyList ->
                _uiState.update { it.copy(history = historyList) }
            }
        }
    }

    /**
     * Memproses byte UID mentah dari kartu NFC yang baru saja dipindai.
     *
     * Alur:
     * 1. Mengkonversi UID ke format 10 digit menggunakan [UidConverter]
     * 2. Menyalin angka 10 digit ke clipboard secara otomatis
     * 3. Memberikan getaran haptik sebagai konfirmasi
     * 4. Menyimpan ke riwayat database lokal
     * 5. Memperbarui state UI
     */
    fun onTagScanned(uid: ByteArray) {
        viewModelScope.launch {
            try {
                val result = UidConverter.convert(uid)

                // Auto-copy ke clipboard
                val clip = ClipData.newPlainText("RFID 10 Digit", result.decimal10Digit)
                clipboard.setPrimaryClip(clip)

                // Getaran haptik konfirmasi (60ms)
                triggerHapticFeedback()

                // Simpan ke riwayat database
                dao.insert(
                    ScanHistoryEntity(
                        decimal10Digit = result.decimal10Digit,
                        decimalLittleEndian = result.decimalBigEndian,
                        hexFormatted = result.hexFormatted
                    )
                )

                // Perbarui UI
                _uiState.update {
                    it.copy(
                        lastDecimal10Digit = result.decimal10Digit,
                        lastDecimalBigEndian = result.decimalBigEndian,
                        lastHexFormatted = result.hexFormatted,
                        justCopied = true
                    )
                }

                // Reset flag justCopied setelah 2 detik (untuk animasi snackbar)
                delay(2000)
                _uiState.update { it.copy(justCopied = false) }

            } catch (e: IllegalArgumentException) {
                // UID terlalu pendek, abaikan pemindaian yang tidak valid
            }
        }
    }

    /** Menyalin teks tertentu ke clipboard dan memberikan feedback haptik */
    fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("RFID 10 Digit", text)
        clipboard.setPrimaryClip(clip)
        triggerHapticFeedback()

        viewModelScope.launch {
            _uiState.update { it.copy(justCopied = true) }
            delay(2000)
            _uiState.update { it.copy(justCopied = false) }
        }
    }

    /** Menghapus seluruh riwayat pemindaian */
    fun clearHistory() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }

    /** Memperbarui status ketersediaan NFC di perangkat */
    fun updateNfcStatus(isSupported: Boolean, isEnabled: Boolean) {
        _uiState.update {
            it.copy(isNfcSupported = isSupported, isNfcEnabled = isEnabled)
        }
    }

    /** Memberikan getaran haptik singkat (60ms) sebagai konfirmasi pemindaian */
    private fun triggerHapticFeedback() {
        vibrator.vibrate(
            VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
}
