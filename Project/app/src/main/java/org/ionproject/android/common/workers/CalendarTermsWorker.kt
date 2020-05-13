package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.toCalendarTermList
import java.net.URI

// TODO Obtain from root element
private const val CALENDAR_TERMS_PATH_V0 = "/v0/calendar-terms"

class CalendarTermsWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val calendarTermDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.calendarTermDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    override suspend fun job(): Boolean {
        val calendarTermsLocal = calendarTermDao.getAllCalendarTerms()
        val calendarTermsServer =
            ionWebAPI.getFromURI(URI(CALENDAR_TERMS_PATH_V0), SirenEntity::class.java)
                .toCalendarTermList()
        if (calendarTermsLocal != calendarTermsServer) {
            calendarTermDao.deleteAllCalendarTerms()
            calendarTermDao.insertCalendarTerms(calendarTermsServer)
        } else {
            val calendarTermsToUpdate = mutableListOf<CalendarTerm>()
            for (i in 0..calendarTermsLocal.count()) {
                if (calendarTermsLocal[i] != calendarTermsServer[i]) {
                    calendarTermsToUpdate.add(calendarTermsServer[i])
                }
            }
            if (calendarTermsToUpdate.count() > 0)
                calendarTermDao.updateCalendarTerms(calendarTermsToUpdate)
        }
        return true
    }

    override suspend fun lastJob() {
        calendarTermDao.deleteAllCalendarTerms()
    }

}