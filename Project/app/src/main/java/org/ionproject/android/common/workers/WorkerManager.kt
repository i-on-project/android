package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import org.ionproject.android.common.model.*
import org.ionproject.android.common.repositories.WorkerRepository
import java.util.concurrent.TimeUnit

const val WORKER_ID_KEY = "19x781x2b"
const val PROGRAMME_ID_KEY = "c4094c20"
const val PROGRAMME_OFFER_ID_KEY = "4crcnc34r"
const val COURSE_DETAILS_ID_KEY = "2xm0h2r0x"
const val CLASS_SUMMARIES_COURSE_KEY = "20xh38nx2"
const val CLASS_SUMMARIES_CALENDAR_TERM_KEY = "x20m3hxm2h8"
const val CLASS_SECTION_ID_KEY = "x2r0hmh20"
const val CLASS_SECTION_COURSE_KEY = "af2929ff2h9"
const val CLASS_SECTION_CALENDAR_TERM_KEY = "m2hmx23x23m"

/**
 * Hides the complexity of launching workers with [WorkManager]
 */
class WorkerManagerFacade(context: Context, private val workerRepository: WorkerRepository) {

    private val workManager = WorkManager.getInstance(context)


    suspend fun enqueueWorkForAllProgrammeSummaries(
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ProgrammeSummariesCoroutineWorker::class.java
    )

    suspend fun enqueueWorkForProgramme(
        programme: Programme,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ProgrammeCoroutineWorker::class.java,
        PROGRAMME_ID_KEY to programme.id
    )

    suspend fun enqueueWorkForProgrammeOffer(
        programmeOffer: ProgrammeOffer,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ProgrammeOfferCoroutineWorker::class.java,
        PROGRAMME_OFFER_ID_KEY to programmeOffer.id
    )

    suspend fun enqueueWorkForCourse(
        course: Course,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        CourseCoroutineWorker::class.java,
        COURSE_DETAILS_ID_KEY to course.id
    )

    suspend fun enqueueWorkForClassSummaries(
        classSummaries: List<ClassSummary>,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ClassSummariesCoroutineWorker::class.java,
        CLASS_SUMMARIES_COURSE_KEY to classSummaries[0].courseAcronym,
        CLASS_SECTION_CALENDAR_TERM_KEY to classSummaries[0].calendarTerm
    )

    suspend fun enqueueWorkForClassSection(
        classSection: ClassSection,
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        ClassSectionCoroutineWorker::class.java,
        CLASS_SECTION_ID_KEY to classSection.id,
        CLASS_SECTION_CALENDAR_TERM_KEY to classSection.calendarTerm,
        CLASS_SECTION_COURSE_KEY to classSection.courseAcronym
    )

    suspend fun enqueueWorkForAllCalendarTerms(
        workImportance: WorkImportance
    ) = enqueueWorkWithInputData(
        workImportance,
        CalendarTermsWorker::class.java
    )

    /**
     * Creates a [PeriodicWorkRequest] and adds it to the workerManager
     *
     * @param repeatInterval is the frequency of the [PeriodicWorkRequest]
     * @param repeatIntervalTimeUnit is the time unit of the [repeatInterval] (e.g Minutes)
     * @param workerClass is the class of the worker
     * @param inputData is the data to pass to the worker
     */
    private suspend fun <T : NumberedCoroutineWorker> enqueueWorkWithInputData(
        workImportance: WorkImportance,
        workerClass: Class<T>,
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
                    *inputData
                )
            )
            //There is not need to start the work immediately because we have just obtained the most recent resource
            .setInitialDelay(1, workImportance.repeatIntervalTimeUnit)
            .build()
        workManager.enqueue(work)
        return workerId.toInt()
    }


    suspend fun resetWorkerJobsByCacheable(iCacheable: ICacheable) {
        workerRepository.resetWorkerJobsById(iCacheable.workerId)
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
        override val numberOfJobs: Int = 1 // Avg number of weeks in an year
    };

    abstract val repeatInterval: Long
    abstract val repeatIntervalTimeUnit: TimeUnit
    abstract val numberOfJobs: Int
}



