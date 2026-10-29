package com.pesantrenqu.maos.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Skema warna kustom yang terinspirasi identitas visual Pesantrenqu
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1B6B3E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA7F5C0),
    onPrimaryContainer = Color(0xFF002110),
    secondary = Color(0xFF4E6354),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0E8D6),
    onSecondaryContainer = Color(0xFF0B1F14),
    tertiary = Color(0xFF3B6470),
    surface = Color(0xFFFBFDF8),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDCE5DB),
    background = Color(0xFFFBFDF8),
    onBackground = Color(0xFF191C1A)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8CD8A5),
    onPrimary = Color(0xFF00391E),
    primaryContainer = Color(0xFF00522D),
    onPrimaryContainer = Color(0xFFA7F5C0),
    secondary = Color(0xFFB5CCBA),
    onSecondary = Color(0xFF203528),
    secondaryContainer = Color(0xFF374B3E),
    onSecondaryContainer = Color(0xFFD0E8D6),
    tertiary = Color(0xFFA2CED9),
    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3DE),
    surfaceVariant = Color(0xFF414942),
    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3DE)
)

/**
 * Tema Material 3 untuk aplikasi Maos NFC RFID.
 *
 * Menggunakan Dynamic Color (Material You) pada Android 12+ secara otomatis,
 * dengan fallback ke skema warna hijau kustom pada perangkat yang lebih lama.
 * Mendukung Dark Mode dan Light Mode secara otomatis mengikuti pengaturan sistem.
 */
@Composable
fun MaosNfcRfidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Gunakan Dynamic Color (Material You) pada Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
