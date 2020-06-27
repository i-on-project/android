package org.ionproject.android.course_details

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.CourseRepository
import java.net.URI

class CourseDetailsViewModel(
    private val courseRepository: CourseRepository,
    private val classesRepository: ClassesRepository,
    private val calendarTermRepository: CalendarTermRepository
) : ViewModel() {

    private val classesListLiveData = MutableLiveData<List<ClassSummary>>()

    val classesList
        get() = classesListLiveData.value ?: emptyList()

    fun observeClassesListLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        classesListLiveData.observe(lifecycleOwner, Observer { onUpdate() })
    }

    /**
     *  Requests the details of a course from the API
     *  and calls onResult once the result is available
     *
     *  @param courseSummary summary representation of a course
     *  @param callback to be executed once the course details are available
     */
    fun getCourseDetails(courseDetailsUri: URI, onResult: (Course) -> Unit) {
        viewModelScope.launch {
            courseRepository.getCourseDetails(courseDetailsUri)?.let(onResult)
        }
    }

    //Adds the calendar term to the classes URI
    private fun URI.fromCalendarTerm(calendarTerm: CalendarTerm): URI {
        val newUri = "${toString()}/${calendarTerm.name}"
        return URI(newUri)
    }

    /**
     *  Requests the list of classes, from a course, from the API
     *  and calls onResult once the result is available
     *
     *  @param course detailed representation of a course
     *  @param callback to be executed once the class list is available
     */
    fun getClassesFromCourse(course: Course, calendarTerm: CalendarTerm) {
        viewModelScope.launch {
            var classes = emptyList<ClassSummary>()
            if(course.classesUri != null) {
                classesRepository.getClassCollectionByUri(
                    course.classesUri.fromCalendarTerm(calendarTerm)
                )?.let {
                    classes = it.classes
                }
            }
            classesListLiveData.postValue(classes)
        }
    }

    /**
     * Calendar terms Section
     */
    private val calendarTermsLiveData = MutableLiveData<List<CalendarTerm>>()

    fun getAllCalendarTerms(calendarTermsUri: URI) {
        viewModelScope.launch {
            val calendarTerms = calendarTermRepository.getAllCalendarTerm(calendarTermsUri)
            calendarTermsLiveData.postValue(calendarTerms)
        }
    }

    val calendarTerms: List<CalendarTerm> get() = calendarTermsLiveData.value ?: emptyList()

    fun observeCalendarTerms(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<CalendarTerm>) -> Unit
    ) {
        calendarTermsLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }
}
