package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.*
import org.ionproject.android.common.db.WorkerDao
import org.ionproject.android.common.model.BackgroundWorker
import org.ionproject.android.common.model.IResource
import org.ionproject.android.common.model.ResourceType
import org.ionproject.android.common.repositories.WorkerRepository
import java.util.concurrent.TimeUnit

const val WORKER_ID_KEY = "19x781x2b"

/**
 * Hides the complexity of launching workers with [WorkManager]
 */
class WorkerManagerFacade(context: Context, private val workerRepository: WorkerRepository) {

    private val workManager = WorkManager.getInstance(context)

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager
     *
     * @param repeatInterval is the frequency of the [PeriodicWorkRequest]
     * @param repeatIntervalTimeUnit is the time unit of the [repeatInterval] (e.g Minutes)
     * @param workerClass is the class of the worker
     * @param resourceId is the id of the resource the work will be managing (e.g ProgrammeId)
     */
    suspend fun <T : ListenableWorker> enqueuePeriodicWork(
        workImportance: WorkImportance,
        workerClass: Class<T>,
        resource: IResource
    ) {
        val workerId = workerRepository.insertWorker(
            BackgroundWorker(
                numberOfJobs = workImportance.numberOfJobs,
                resourceId = resource.id,
                resourceType = resource.type
            )
        )
        val work = PeriodicWorkRequest.Builder(workerClass, workImportance.repeatInterval, workImportance.repeatIntervalTimeUnit)
            .setConstraints(
                //This constraints should be reconsidered, especially in very important work that generates notifications like for an event
                Constraints.Builder()
                    // Most work will involve the db so if the storage is low it shouldn't be done
                    .setRequiresStorageNotLow(true)
                    // Some of the work is CPU intensive, for example parsing a Web API response
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .setInputData(workDataOf(
                WORKER_ID_KEY to workerId
            ))
            .build()
        workManager.enqueue(work)
    }
}

enum class WorkImportance {
    VERY_IMPORTANT {
        override val repeatInterval: Long = 1
        override val repeatIntervalTimeUnit: TimeUnit = TimeUnit.HOURS
        override val numberOfJobs: Int = 168 // Number of hours in a week
    },
    IMPORTANT {
        override val repeatInterval: Long = 1
        override val repeatIntervalTimeUnit: TimeUnit = TimeUnit.DAYS
        override val numberOfJobs: Int = 7 // One week
    },
    NOT_IMPORTANT {
        override val repeatInterval: Long = 7
        override val repeatIntervalTimeUnit: TimeUnit = TimeUnit.DAYS
        override val numberOfJobs: Int = 52 // Avg number of weeks in an year
    };

    abstract val repeatInterval: Long
    abstract val repeatIntervalTimeUnit: TimeUnit
    abstract val numberOfJobs: Int
}



