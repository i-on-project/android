package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters

class CalendarTermsWorker(
    context: Context,
    params: WorkerParameters
) : NumberedCoroutineWorker(context, params) {

    override suspend fun job() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun lastJob() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}