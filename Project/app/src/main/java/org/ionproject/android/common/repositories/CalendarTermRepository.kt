package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.CalendarTermDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.toCalendarTermList
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import java.net.URI

//TODO This should not be hardcoded, otherwise if its altered we have to refactor the code
private val CALENDAR_TERMS_URI = URI("/v0/calendar-terms")

class CalendarTermRepository(
    private val ionWebAPI: IIonWebAPI,
    private val calendarTermDao: CalendarTermDao,
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Obtains all calendar terms from the IonWebAPI
     */
    suspend fun getAllCalendarTerm(): List<CalendarTerm> =
        withContext(dispatcher) {
            var calendarTerms = calendarTermDao.getAllCalendarTerms()

            if (calendarTerms.count() == 0) {
                calendarTerms = ionWebAPI
                    .getFromURI(CALENDAR_TERMS_URI, SirenEntity::class.java)
                    .toCalendarTermList()
                val workerId =
                    workerManagerFacade.enqueueWorkForAllCalendarTerms(WorkImportance.NOT_IMPORTANT)
                for (calendarTerm in calendarTerms)
                    calendarTerm.workerId = workerId
                calendarTermDao.insertCalendarTerms(calendarTerms)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(calendarTerms[0])
            }
            calendarTerms
        }
}