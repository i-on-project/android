package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenICalendar
import org.ionproject.android.common.model.toEventsSummary
import java.net.URI

class EventsWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val eventsDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.eventsDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val eventsUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (eventsUri != "") {
            val eventsURI = URI(eventsUri)
            val eventsServer =
                ionWebAPI.getFromURI(eventsURI, SirenICalendar::class.java).toEventsSummary()
            // We have to delete all events because we don't know if events were removed from server
            eventsDao.deleteEventsByUri(eventsURI)
            eventsDao.insertEvents(eventsServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (eventsUri != "") {
            eventsDao.deleteEventsByUri(URI(eventsUri))
        }
    }


}