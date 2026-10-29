package com.pesantrenqu.maos.domain

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle

/**
 * Mesin pembaca kartu NFC menggunakan ReaderMode API.
 *
 * Menggunakan [NfcAdapter.enableReaderMode] untuk pemindaian instan
 * tanpa suara sistem bawaan, tanpa dialog pemilih aplikasi,
 * dan tanpa delay pemrosesan NDEF.
 *
 * Flag yang digunakan:
 * - [NfcAdapter.FLAG_READER_NFC_A]: Membaca kartu ISO 14443-3A (MIFARE)
 * - [NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK]: Melewati pemrosesan NDEF untuk kecepatan
 * - [NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS]: Membisukan suara sistem bawaan
 */
class NfcReaderEngine(
    private val onTagDiscovered: (ByteArray) -> Unit
) : NfcAdapter.ReaderCallback {

    /**
     * Callback saat tag NFC terdeteksi.
     * Mengekstrak UID dari tag dan meneruskannya ke handler.
     */
    override fun onTagDiscovered(tag: Tag) {
        val uid = tag.id
        if (uid != null && uid.size >= 4) {
            onTagDiscovered.invoke(uid)
        }
    }

    /**
     * Mengaktifkan mode pembaca NFC pada Activity yang diberikan.
     * Panggil di onResume() Activity.
     */
    fun enable(activity: Activity) {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(activity) ?: return

        val options = Bundle().apply {
            // Delay antar pemindaian untuk menghindari pembacaan ganda (dalam milidetik)
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
        }

        nfcAdapter.enableReaderMode(
            activity,
            this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
            options
        )
    }

    /**
     * Menonaktifkan mode pembaca NFC.
     * Panggil di onPause() Activity.
     */
    fun disable(activity: Activity) {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(activity) ?: return
        nfcAdapter.disableReaderMode(activity)
    }
}
