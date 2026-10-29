package com.pesantrenqu.maos.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pesantrenqu.maos.ui.components.NfcDisabledCard
import com.pesantrenqu.maos.ui.components.ScanHistoryList
import com.pesantrenqu.maos.ui.components.ScanResultCard
import com.pesantrenqu.maos.ui.components.WaitingForCard

/**
 * Layar utama aplikasi Maos NFC RFID.
 *
 * Menampilkan status NFC, area hasil pemindaian, dan riwayat kartu.
 * Tata letak responsif yang menyesuaikan konten berdasarkan state:
 * - NFC nonaktif: menampilkan peringatan dan tombol pengaturan
 * - Menunggu kartu: menampilkan animasi ikon NFC berkedip
 * - Hasil pemindaian: menampilkan kartu dengan angka 10 digit besar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    onCopy: (String) -> Unit,
    onShare: (String) -> Unit,
    onClearHistory: () -> Unit,
    onOpenNfcSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Maos NFC RFID",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Bagian atas: Status NFC atau hasil pemindaian
            if (!uiState.isNfcSupported || !uiState.isNfcEnabled) {
                NfcDisabledCard(
                    isNfcSupported = uiState.isNfcSupported,
                    onOpenNfcSettings = onOpenNfcSettings,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else if (uiState.lastDecimal10Digit != null) {
                ScanResultCard(
                    decimal10Digit = uiState.lastDecimal10Digit,
                    decimalBigEndian = uiState.lastDecimalBigEndian ?: "",
                    hexFormatted = uiState.lastHexFormatted ?: "",
                    justCopied = uiState.justCopied,
                    onCopy = onCopy,
                    onShare = onShare,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                WaitingForCard()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bagian bawah: Riwayat pemindaian
            ScanHistoryList(
                history = uiState.history,
                onCopy = onCopy,
                onClearHistory = onClearHistory,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
