package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.model.IResource

/**
 * Base worker contains all the worker managing logic like adding to database,
 * decrementing jobs etc...
 *
 * The details are described here:
 * https://github.com/i-on-project/android/blob/master/docs/api_request_approach.md
 */
abstract class NumberedCoroutineWorker(
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(
        context,
        params
    ) {

    private val workerRepository by lazy {
        IonApplication.workerRepository
    }

    /**
     * Regular worker job, should request resource from WebAPi
     * compare to the one in Db and updates if there are differences
     *
     * @param resourceId is the id of the resource it is managing
     */
    abstract suspend fun job(resourceId: Int)

    /**
     * The last job of the worker, should
     * remove the resource it is managing
     * from the db
     *
     * @param resourceId is the id of the resource it is managing
     */
    abstract suspend fun lastJob(resourceId: Int)

    override suspend fun doWork(): Result {
        val workerId = inputData.getInt(WORKER_ID_KEY, -1)

        //ProgrammeId is mandatory for work execution therefore throw exceptions if it doesn't come
        if (workerId == -1)
            throw IllegalArgumentException("${this::class.simpleName} did not receive a worker Id")

        val worker = workerRepository.getWorkerById(workerId)
        worker.decrementNumberOfJobs()
        workerRepository.updateWorker(worker)

        if (worker.currNumberOfJobs == 0) {
            //Worker has finished all its jobs so perform lastJob
            lastJob(worker.resourceId)
            workerRepository.deleteWorker(worker)
            return Result.failure()
        }

        //Worker still jobs to perform
        job(worker.resourceId)
        return Result.success()
    }

}
