package com.pesantrenqu.maos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database Room utama aplikasi Maos NFC RFID.
 *
 * Menyimpan riwayat pemindaian kartu secara lokal dan offline.
 */
@Database(entities = [ScanHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Mendapatkan instance singleton database.
         * Menggunakan pola double-checked locking untuk thread safety.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maos_nfc_rfid.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
