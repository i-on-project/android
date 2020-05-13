package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication

/**
 * Base worker contains all the worker managing logic like adding to database,
 * decrementing jobs etc...
 *
 * The details are described here:
 * https://github.com/i-on-project/android/blob/master/docs/api_request_approach.md
 */
abstract class NumberedWorker(
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(
        context,
        params
    ) {

    private val workerRepository by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.workerRepository
    }

    /**
     * Regular worker job, should request resource from WebAPi
     * compare to the one in Db and updates if there are differences.
     *
     * If there was a problem while obtaining the input data
     * it should return false so that the work stops.
     */
    abstract suspend fun job(): Boolean

    /**
     * The last job of the worker, should
     * remove the resource it is managing
     * from the db
     */
    abstract suspend fun lastJob()

    override suspend fun doWork(): Result {
        val workerId = inputData.getLong(WORKER_ID_KEY, -1).toInt()

        // workerId is mandatory for work execution therefore stop it if it doesn't come
        if (workerId == -1)
            return Result.failure()

        val worker = workerRepository.getWorkerById(workerId)
        worker.decrementNumberOfJobs()
        workerRepository.updateWorker(worker)

        if (worker.currNumberOfJobs == 0) {
            // Worker has finished all its jobs so perform lastJob
            lastJob()
            workerRepository.deleteWorker(worker)
            return Result.failure()
        }

        // Worker still has jobs to perform
        if (!job()) // There was a problem while passing the input data to the worker so we stop it
            return Result.failure()
        return Result.success()
    }

}
