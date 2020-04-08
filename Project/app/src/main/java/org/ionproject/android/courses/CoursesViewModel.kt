package org.ionproject.android.courses

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.CourseRepository
import org.ionproject.android.common.model.CourseSummary

class CoursesViewModel(private val coursesRepository: CourseRepository) : ViewModel() {

    private val coursesLiveData = MutableLiveData<List<CourseSummary>>()

    fun observeCoursesLiveData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: () -> Unit
    ) {
        coursesLiveData.observe(lifecycleOwner, Observer<List<CourseSummary>> {
            onUpdate()
        })
    }

    val courses: List<CourseSummary>
        get() = coursesLiveData.value ?: emptyList()

    fun getAllCourses() {
        //TODO("Confirm if this is executing with the Dispacher.Main")
        viewModelScope.launch {
            val allCourses = coursesRepository.getAllCourses()
            coursesLiveData.postValue(allCourses)
        }
    }

}
