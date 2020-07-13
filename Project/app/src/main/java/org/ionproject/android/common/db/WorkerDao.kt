package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.BackgroundWorker

@Dao
interface WorkerDao {

    @Query("SELECT * FROM BackgroundWorker WHERE id = :id")
    suspend fun getWorkerById(id: Int): BackgroundWorker?

    @Insert
    suspend fun insertWorker(worker: BackgroundWorker): Long

    @Delete
    suspend fun deleteWorker(worker: BackgroundWorker)

    @Update
    suspend fun updateWorker(worker: BackgroundWorker)


}
