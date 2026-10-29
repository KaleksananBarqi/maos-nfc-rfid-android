package com.pesantrenqu.maos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NfcOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Kartu peringatan yang ditampilkan saat NFC tidak didukung
 * atau sedang dinonaktifkan di pengaturan perangkat.
 *
 * Menyediakan tombol pintasan langsung ke halaman pengaturan NFC Android.
 */
@Composable
fun NfcDisabledCard(
    isNfcSupported: Boolean,
    onOpenNfcSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.NfcOff,
                contentDescription = "NFC Nonaktif",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isNfcSupported) "NFC Tidak Aktif" else "NFC Tidak Tersedia",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isNfcSupported)
                    "Aktifkan NFC di pengaturan perangkat untuk memulai pemindaian kartu RFID."
                else
                    "Perangkat ini tidak memiliki sensor NFC. Diperlukan HP Android yang mendukung NFC.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            if (isNfcSupported) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onOpenNfcSettings) {
                    Text("Buka Pengaturan NFC")
                }
            }
        }
    }
}
