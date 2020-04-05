package edu.isel.ion.android.courses

import androidx.lifecycle.*
import edu.isel.ion.android.common.CourseRepository
import edu.isel.ion.android.common.model.CourseSummary
import kotlinx.coroutines.launch

class CoursesViewModel(private val coursesRepository: CourseRepository) : ViewModel() {

    private val coursesLiveData  = MutableLiveData<List<CourseSummary>>()

    fun observeCoursesLiveData(lifecycleOwner: LifecycleOwner,
                               onUpdate : (List<CourseSummary>) -> Unit) {
        coursesLiveData.observe(lifecycleOwner,Observer<List<CourseSummary>> {
            onUpdate(it)
        })
    }

    val courses : List<CourseSummary>
        get() = coursesLiveData.value?: emptyList()

    fun getAllCourses() {
        //TODO("Confirm if this is executing with the Dispacher.Main")
        viewModelScope.launch {
            val allCourses = coursesRepository.getAllCourses()
            coursesLiveData.postValue(allCourses)
        }
    }

}