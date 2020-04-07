package edu.isel.ion.android.course_details

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.isel.ion.android.common.ClassesRepository
import edu.isel.ion.android.common.CourseRepository
import edu.isel.ion.android.common.model.ClassSummary
import edu.isel.ion.android.common.model.Course
import edu.isel.ion.android.common.model.CourseSummary
import kotlinx.coroutines.launch

class CourseDetailsViewModel(private val courseRepository: CourseRepository,
                             private val classesRepository: ClassesRepository) : ViewModel() {

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

    val classesListLiveData = MutableLiveData<List<ClassSummary>>()

    val classesList get() = classesListLiveData.value?: emptyList()


    fun observeClassesListLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        classesListLiveData.observe(lifecycleOwner, Observer{onUpdate()})
    }

    /**
     *  Requests the list of classes, from a course, from the API
     *  and calls onResult once the result is available
     *
     *  @param course detailed representation of a course
     *  @param callback to be executed once the class list is available
     */
    fun getCourseClasses(course : Course) {
        viewModelScope.launch {
            val classes = classesRepository.getClassesFromCourse(course)
            classesListLiveData.postValue(classes)
        }
    }
}