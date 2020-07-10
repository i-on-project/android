package org.ionproject.android.course_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CoursesDetailsViewModelProvider : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CourseDetailsViewModel::class.java -> CourseDetailsViewModel(
                IonApplication.coursesRepository,
                IonApplication.classesRepository,
                IonApplication.calendarTermRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }

}
