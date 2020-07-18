package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
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
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Obtains all calendar terms from the IonWebAPI and sorts
     * them by the descending order of the year
     *
     * E.g: (1920v,1920i,1819v,1819i...)
     */
    suspend fun getAllCalendarTerm(calendarTermsUri: URI): List<CalendarTerm> =
        withContext(dispatcher) {
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

    suspend fun getMostRecentCalendarTerm(calendarTermsUri: URI): CalendarTerm? =
        withContext(Dispatchers.IO) {
            val calendarTerms = getAllCalendarTerm(calendarTermsUri)
            if (calendarTerms.count() > 0) {
                var mostRecentCalendarTerm = calendarTerms.first()
                calendarTerms.forEach {
                    if (it.year > mostRecentCalendarTerm.year)
                        mostRecentCalendarTerm = it
                }
                return@withContext mostRecentCalendarTerm
            }
            return@withContext null
        }
}