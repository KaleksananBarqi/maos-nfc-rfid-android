package com.pesantrenqu.maos.ui

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pesantrenqu.maos.domain.NfcReaderEngine
import com.pesantrenqu.maos.ui.theme.MaosNfcRfidTheme

/**
 * Activity utama aplikasi Maos NFC RFID.
 *
 * Bertanggung jawab untuk:
 * - Menginisialisasi NFC Reader Mode engine
 * - Mengelola lifecycle NFC (enable di onResume, disable di onPause)
 * - Menghubungkan sensor NFC dengan ViewModel dan UI Compose
 */
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var nfcReaderEngine: NfcReaderEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaosNfcRfidTheme {
                viewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()

                // Inisialisasi NFC reader engine saat pertama kali
                nfcReaderEngine = NfcReaderEngine { uid ->
                    viewModel.onTagScanned(uid)
                }

                MainScreen(
                    uiState = uiState,
                    onCopy = { text -> viewModel.copyToClipboard(text) },
                    onShare = { text -> shareText(text) },
                    onClearHistory = { viewModel.clearHistory() },
                    onOpenNfcSettings = { openNfcSettings() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Periksa status NFC setiap kali Activity kembali ke foreground
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val isSupported = nfcAdapter != null
        val isEnabled = nfcAdapter?.isEnabled == true

        if (::viewModel.isInitialized) {
            viewModel.updateNfcStatus(isSupported, isEnabled)
        }

        // Aktifkan pembacaan NFC jika adapter tersedia dan aktif
        if (isEnabled && ::nfcReaderEngine.isInitialized) {
            nfcReaderEngine.enable(this)
        }
    }

    override fun onPause() {
        super.onPause()
        // Nonaktifkan pembacaan NFC saat Activity tidak di foreground
        if (::nfcReaderEngine.isInitialized) {
            nfcReaderEngine.disable(this)
        }
    }

    /** Membuka intent Share Android standar untuk membagikan teks */
    private fun shareText(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Nomor RFID 10 Digit: $text")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Bagikan Nomor RFID")
        startActivity(shareIntent)
    }

    /** Membuka halaman pengaturan NFC di Android Settings */
    private fun openNfcSettings() {
        startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
    }
}
