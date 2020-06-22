package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import java.net.URI

class ClassSectionWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

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

    private val classSectionUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (classSectionId != "" && courseAcronym != "" && calendarTerm != "" && classSectionUri != "") {
            val classSectionServer =
                ionWebAPI.getFromURI(URI(classSectionUri), SirenEntity::class.java)
                    .toClassSection()
            classSectionDao.updateClassSection(classSectionServer)
            return true
        }
        return false
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