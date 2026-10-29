package com.pesantrenqu.maos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk operasi CRUD riwayat pemindaian kartu.
 */
@Dao
interface ScanHistoryDao {

    /** Mengambil seluruh riwayat pemindaian, diurutkan dari yang terbaru */
    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun getAllHistory(): Flow<List<ScanHistoryEntity>>

    /** Menyimpan satu entri pemindaian baru */
    @Insert
    suspend fun insert(entry: ScanHistoryEntity)

    /** Menghapus seluruh riwayat pemindaian */
    @Query("DELETE FROM scan_history")
    suspend fun clearAll()
}
