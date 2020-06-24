package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.toCalendarTermList
import java.net.URI

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

    private val calendarTermsUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (calendarTermsUri != "") {
            val calendarTermsServer =
                ionWebAPI.getFromURI(URI(calendarTermsUri), SirenEntity::class.java)
                    .toCalendarTermList()
            calendarTermDao.deleteAllCalendarTerms()
            calendarTermDao.insertCalendarTerms(calendarTermsServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        calendarTermDao.deleteAllCalendarTerms()
    }

}