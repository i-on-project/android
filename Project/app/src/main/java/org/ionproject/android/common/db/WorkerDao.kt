package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.BackgroundWorker
import org.ionproject.android.common.model.IResource
import org.ionproject.android.common.model.ResourceType

@Dao
abstract class WorkerDao {

    @Query("SELECT * FROM BackgroundWorker WHERE id = :id")
    abstract suspend fun getWorkerById(id: Int): BackgroundWorker

    @Query("SELECT * FROM BackgroundWorker WHERE resourceId = :resourceId AND resourceType = :resourceType")
    abstract suspend fun _getWorkerByResource(resourceId: Int, resourceType: ResourceType) : BackgroundWorker

    suspend fun getWorkerByResource(resource: IResource) = _getWorkerByResource(resource.id,resource.type)

    @Insert
    abstract suspend fun insertWorker(worker: BackgroundWorker): Int

    @Delete
    abstract suspend fun deleteWorker(worker: BackgroundWorker)

    @Update
    abstract suspend fun updateWorker(worker: BackgroundWorker)



}
