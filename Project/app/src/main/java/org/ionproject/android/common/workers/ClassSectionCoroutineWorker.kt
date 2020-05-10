package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity

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

    private val classSectionId by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(CLASS_SECTION_ID_KEY) ?: ""
    }

    private val courseAcronym by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(CLASS_SECTION_COURSE_KEY) ?: ""
    }

    private val calendarTerm by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(CLASS_SECTION_CALENDAR_TERM_KEY) ?: ""
    }

    override suspend fun job() {
        if (classSectionId != "" && courseAcronym != "" && calendarTerm != "") {
            val classSectionLocal = classSectionDao.getClassSectionByIdAndCourseAndCalendarTerm(
                classSectionId,
                courseAcronym,
                calendarTerm
            )
            if (classSectionLocal != null) {
                val classSectionServer =
                    ionWebAPI.getFromURI(classSectionLocal.selfUri, SirenEntity::class.java)
                        .toClassSection()

                if (classSectionLocal != classSectionServer)
                    classSectionDao.updateClassSection(classSectionServer)
            }
        }
    }

    override suspend fun lastJob() {
        if (classSectionId != "" && courseAcronym != "" && calendarTerm != "") {
            classSectionDao.deleteClassSectionByIdAndCourseAndCalendarTerm(
                classSectionId,
                courseAcronym,
                calendarTerm
            )
        }
    }

}