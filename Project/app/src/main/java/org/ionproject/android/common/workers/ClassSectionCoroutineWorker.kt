package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication

class ClassSectionCoroutineWorker(
    context: Context,
    params: WorkerParameters
) : NumberedCoroutineWorker(context, params) {

    private val classSectionDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.classSectionDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    override suspend fun job() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun lastJob() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}