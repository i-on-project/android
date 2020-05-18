package org.ionproject.android.common.repositories

import org.ionproject.android.common.db.WorkerDao
import org.ionproject.android.common.model.BackgroundWorker

class WorkerRepository(private val workerDao: WorkerDao) {

    suspend fun getWorkerById(id: Int) = workerDao.getWorkerById(id)

    suspend fun deleteWorker(worker: BackgroundWorker) = workerDao.deleteWorker(worker)

    suspend fun insertWorker(worker: BackgroundWorker) = workerDao.insertWorker(worker)

    suspend fun updateWorker(worker: BackgroundWorker) = workerDao.updateWorker(worker)

    suspend fun resetWorkerJobsById(id: Int) {
        val worker = workerDao.getWorkerById(id)
        if(worker != null) {
            worker.resetNumberOfJobs()
            updateWorker(worker)
        }
    }

}