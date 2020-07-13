package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.course_details.toClasses
import java.net.URI

class ClassesWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val classesDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.classesDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val classesUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }


    override suspend fun job(): Boolean {
        if (classesUri != "") {
            val classesURI = URI(classesUri)
            val classesServer =
                ionWebAPI.getFromURI(classesURI, SirenEntity::class.java).toClasses()
            classesDao.deleteClassesByUri(classesURI)
            classesDao.insertClasses(classesServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (classesUri != "") {
            val classesURI = URI(classesUri)
            classesDao.deleteClassesByUri(classesURI)
        }
    }

}