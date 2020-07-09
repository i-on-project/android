package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.CalendarTermDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.toCalendarTermList
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import java.net.URI

class CalendarTermRepository(
    private val ionWebAPI: IIonWebAPI,
    private val calendarTermDao: CalendarTermDao,
    private val workerManagerFacade: WorkerManagerFacade
) {

    /**
     * Obtains all calendar terms from the IonWebAPI
     */
    suspend fun getAllCalendarTerm(calendarTermsUri: URI): List<CalendarTerm> =
        withContext(Dispatchers.IO) {
            var calendarTerms = calendarTermDao.getAllCalendarTerms()

            if (calendarTerms.count() == 0) {
                calendarTerms = ionWebAPI
                    .getFromURI(calendarTermsUri, SirenEntity::class.java)
                    .toCalendarTermList()
                val workerId =
                    workerManagerFacade.enqueueWorkForAllCalendarTerms(
                        WorkImportance.NOT_IMPORTANT,
                        calendarTermsUri
                    )
                for (calendarTerm in calendarTerms)
                    calendarTerm.workerId = workerId
                calendarTermDao.insertCalendarTerms(calendarTerms)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(calendarTerms[0])
            }
            calendarTerms.sortedByDescending { it.year }
        }

    suspend fun getMostRecentCalendarTerm(calendarTermsUri: URI): CalendarTerm =
        withContext(Dispatchers.IO) {
            val calendarTerms = getAllCalendarTerm(calendarTermsUri)
            var mostRecentCalendarTerm = calendarTerms[0]
            calendarTerms.forEach {
                if (it.year > mostRecentCalendarTerm.year)
                    mostRecentCalendarTerm = it
            }
            mostRecentCalendarTerm
        }
}