package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.WorkerDao
import org.ionproject.android.common.model.BackgroundWorker

class WorkerRepository(
    private val workerDao: WorkerDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getWorkerById(id: Int) =
        withContext(dispatcher) { workerDao.getWorkerById(id) }

    suspend fun deleteWorker(worker: BackgroundWorker) =
        withContext(dispatcher) { workerDao.deleteWorker(worker) }

    suspend fun insertWorker(worker: BackgroundWorker) =
        withContext(dispatcher) { workerDao.insertWorker(worker) }

    suspend fun updateWorker(worker: BackgroundWorker) =
        withContext(dispatcher) { workerDao.updateWorker(worker) }

    suspend fun resetWorkerJobsById(id: Int) = withContext(dispatcher) {
        val worker = workerDao.getWorkerById(id)
        worker.resetNumberOfJobs()
        updateWorker(worker)
    }

}