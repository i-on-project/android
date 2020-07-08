package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import org.ionproject.android.common.model.*
import org.ionproject.android.common.repositories.WorkerRepository
import java.net.URI
import java.util.concurrent.TimeUnit


/**
 * These are random keys used to pass data to workers
 * TODO Is their value important? Or can it be random as it is?
 */
const val WORKER_ID_KEY = "19x781x2b"
const val PROGRAMME_ID_KEY = "c4094c20"
const val PROGRAMME_OFFER_ID_KEY = "4crcnc34r"
const val COURSE_DETAILS_ID_KEY = "2xm0h2r0x"
const val CLASS_SUMMARIES_COURSE_KEY = "20xh38nx2"
const val CLASS_SUMMARIES_CALENDAR_TERM_KEY = "x20m3hxm2h8"
const val CLASS_SECTION_ID_KEY = "x2r0hmh20"
const val CLASS_SECTION_COURSE_KEY = "af2929ff2h9"
const val CLASS_SECTION_CALENDAR_TERM_KEY = "m2hmx23x23m"
const val RESOURCE_URI_KEY = "xgx0n2gdngx208"
const val ROOT_RESOURCE_URI_KEY = "xwg3e0cRmnT54r"

/**
 * Hides the complexity of launching workers with [WorkManager]
 */
class WorkerManagerFacade(context: Context, private val workerRepository: WorkerRepository) {

    private val workManager = WorkManager.getInstance(context)

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated for information about [Root] and will check for any changes
     * to it and update the local database.
     */
    suspend fun enqueueWorkForRootResource(
        workImportance: WorkImportance,
        root: Root
    ) = enqueueWorkWithInputData(
        workImportance,
        RootResourceWorker::class.java,
        root.rootUri
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to all [ProgrammeSummary] and will check for any changes
     * to them and update the local database in case there are.
     */
    suspend fun enqueueWorkForAllProgrammeSummaries(
        workImportance: WorkImportance,
        programmeSummariesUri: URI
    ) = enqueueWorkWithInputData(
        workImportance,
        ProgrammeSummariesWorker::class.java,
        programmeSummariesUri
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to a [Programme] and will check for any changes
     * to it and update the local database in case there are.
     */
    suspend fun enqueueWorkForProgramme(
        programme: Programme,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ProgrammeWorker::class.java,
        programme.selfUri,
        PROGRAMME_ID_KEY to programme.id
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to a [ProgrammeOffer] and will check for any changes
     * to it and update the local database in case there are.
     */
    suspend fun enqueueWorkForProgrammeOffer(
        programmeOffer: ProgrammeOffer,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ProgrammeOfferWorker::class.java,
        programmeOffer.selfUri,
        PROGRAMME_OFFER_ID_KEY to programmeOffer.id
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to a [Course] and will check for any changes
     * to it and update the local database in case there are.
     */
    suspend fun enqueueWorkForCourse(
        course: Course,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        CourseWorker::class.java,
        course.selfUri,
        COURSE_DETAILS_ID_KEY to course.id
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to a [ClassCollection] and will check for any changes
     * to them and update the local database in case there are.
     */
    suspend fun enqueueWorkForClassCollection(
        classCollection: ClassCollection,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ClassCollectionWorker::class.java,
        classCollection.fields.selfUri,
        CLASS_SUMMARIES_COURSE_KEY to classCollection.fields.courseAcronym,
        CLASS_SECTION_CALENDAR_TERM_KEY to classCollection.fields.calendarTerm
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to a [ClassSection] and will check for any changes
     * to it and update the local database in case there are.
     */
    suspend fun enqueueWorkForClassSection(
        classSection: ClassSection,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ClassSectionWorker::class.java,
        classSection.selfUri,
        CLASS_SECTION_ID_KEY to classSection.id,
        CLASS_SECTION_CALENDAR_TERM_KEY to classSection.calendarTerm,
        CLASS_SECTION_COURSE_KEY to classSection.courseAcronym
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager.
     * The worker is associated to all [CalendarTerm] and will check for any changes
     * to them and update the local database in case there are.
     */
    suspend fun enqueueWorkForAllCalendarTerms(
        workImportance: WorkImportance,
        calendarTermsUri: URI
    ) = enqueueWorkWithInputData(
        workImportance,
        CalendarTermsWorker::class.java,
        calendarTermsUri
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager
     *
     * @param repeatInterval is the frequency of the [PeriodicWorkRequest]
     * @param repeatIntervalTimeUnit is the time unit of the [repeatInterval] (e.g Minutes)
     * @param workerClass is the class of the worker
     * @param resourceUri is the URI to obtain the resource from the Web API
     * @param inputData is the data to pass to the worker
     */
    private suspend fun <T : NumberedWorker> enqueueWorkWithInputData(
        workImportance: WorkImportance,
        workerClass: Class<T>,
        resourceUri: URI,
        vararg inputData: Pair<String, Any>
    ): Int {
        val workerId = workerRepository.insertWorker(
            BackgroundWorker(
                numberOfJobs = workImportance.numberOfJobs
            )
        )
        val work = PeriodicWorkRequest.Builder(
            workerClass,
            workImportance.repeatInterval,
            workImportance.repeatIntervalTimeUnit
        )
            .setConstraints(
                //This constraints should be reconsidered, especially in very important work that generates notifications like for an event
                Constraints.Builder()
                    // Most work will involve the db so if the storage is low it shouldn't be done
                    .setRequiresStorageNotLow(true)
                    // Some of the work is CPU intensive, for example parsing a Web API response
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .setInputData(
                workDataOf(
                    WORKER_ID_KEY to workerId,
                    RESOURCE_URI_KEY to resourceUri.toString(),
                    *inputData
                )
            )
            //There is no need to start the work immediately because we have just obtained the most recent resource
            .setInitialDelay(workImportance.repeatInterval, workImportance.repeatIntervalTimeUnit)
            .build()
        workManager.enqueue(work)
        return workerId.toInt()
    }

    /**
     * Resets the number of jobs a Worker has associated
     * with an [ICacheable]
     */
    suspend fun resetWorkerJobsByCacheable(iCacheable: ICacheable) {
        workerRepository.resetWorkerJobsById(iCacheable.workerId)
    }
}


enum class WorkImportance {
    VERY_IMPORTANT {
        override val repeatInterval: Long = 8
        override val repeatIntervalTimeUnit: TimeUnit = TimeUnit.HOURS
        override val numberOfJobs: Int = 21 // Number of 8 hour periods in a week
    },
    IMPORTANT {
        override val repeatInterval: Long = 1
        override val repeatIntervalTimeUnit: TimeUnit = TimeUnit.DAYS
        override val numberOfJobs: Int = 7 // One week
    },
    NOT_IMPORTANT {
        override val repeatInterval: Long = 7
        override val repeatIntervalTimeUnit: TimeUnit = TimeUnit.DAYS
        override val numberOfJobs: Int = 1 // Avg number of weeks in an year
    };

    abstract val repeatInterval: Long
    abstract val repeatIntervalTimeUnit: TimeUnit
    abstract val numberOfJobs: Int
}



