package edu.isel.ion.android.course_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.isel.ion.android.common.CourseRepository
import edu.isel.ion.android.common.IonApplication

class CoursesDetailsViewModelProvider : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            CourseDetailsViewModel :: class.java -> CourseDetailsViewModel(
                IonApplication.coursesRepository
            )
            else -> throw IllegalArgumentException("Class ${modelClass} not supported by this provider")
        } as T
    }

}
