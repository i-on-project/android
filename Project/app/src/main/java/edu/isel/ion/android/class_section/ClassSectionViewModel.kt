package edu.isel.ion.android.class_section

import androidx.lifecycle.*
import edu.isel.ion.android.common.ClassSectionRepository
import edu.isel.ion.android.common.model.ClassSection
import kotlinx.coroutines.launch

private const val COURSE = "course"
private const val CALENDAR_TERM = "calendarTerm"
private const val KLASS = "class"

class ClassSectionViewModel(private val classSectionRepository: ClassSectionRepository) :
    ViewModel() {

    private val classSectionLiveData = MutableLiveData<ClassSection>()

    fun getClassSectionDetails(params: Map<String, String>) {
        val courses = params[COURSE]
        val calendarTerm = params[CALENDAR_TERM]
        val klass = params[KLASS]

        viewModelScope.launch {
            val classSectionData =
                classSectionRepository.getClassSection(courses, calendarTerm, klass)
            classSectionLiveData.postValue(classSectionData)
        }
    }

    fun observeForClassSectionData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (ClassSection) -> Unit
    ) {
        classSectionLiveData.observe(lifecycleOwner, Observer(onUpdate))
    }
}