package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.BackgroundWorker

@Dao
abstract class WorkerDao {

    @Query("SELECT * FROM BackgroundWorker WHERE id = :id")
    abstract suspend fun getWorkerById(id: Int): BackgroundWorker

    @Insert
    abstract suspend fun insertWorker(worker: BackgroundWorker): Long

    @Delete
    abstract suspend fun deleteWorker(worker: BackgroundWorker)

    @Update
    abstract suspend fun updateWorker(worker: BackgroundWorker)


}
