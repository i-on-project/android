package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.JsonHome
import java.net.URI

class RootResourceWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val rootDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.rootDao()
    }

    private val rootResourceURI by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(ROOT_RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if(rootResourceURI != "") {
            val rootResource = ionWebAPI.getFromURI(URI(rootResourceURI), JsonHome::class.java).toRoot()
            if(rootResource != null) {
                rootDao.deleteRootResource()
                rootDao.insertRootResource(rootResource)
                return true
            }
        }
        return false
    }

    override suspend fun lastJob() = rootDao.deleteRootResource()

}