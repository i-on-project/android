package edu.isel.ion.android.course_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.isel.ion.android.common.CourseRepository
import edu.isel.ion.android.common.model.Course
import edu.isel.ion.android.common.model.CourseSummary
import kotlinx.coroutines.launch

class CourseDetailsViewModel(private val courseRepository: CourseRepository) : ViewModel() {

    /**
     *  Requests the details of a course from the API
     *  and calls onResult once the result is available
     *
     *  @param courseSummary summary representation of a course
     *  @param callback to be executed once the course details are available
     */
    fun getCourseDetails(courseSummary: CourseSummary, onResult :(Course) -> Unit) {
        viewModelScope.launch {
            val course = courseRepository.getCourseDetails(courseSummary)
            onResult(course)
        }
    }



}