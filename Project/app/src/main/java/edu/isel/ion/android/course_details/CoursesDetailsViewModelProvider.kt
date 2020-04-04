package edu.isel.ion.android.course_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CoursesDetailsViewModelProvider : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            CourseDetailsViewModel :: class.java -> CourseDetailsViewModel()
            else -> throw IllegalArgumentException("Class ${modelClass} not supported by this provider")
        } as T
    }

}
