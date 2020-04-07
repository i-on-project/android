package edu.isel.ion.android.class_section

import androidx.lifecycle.*
import edu.isel.ion.android.common.ClassesRepository
import edu.isel.ion.android.common.model.ClassSection
import edu.isel.ion.android.common.model.ClassSummary
import kotlinx.coroutines.launch

private const val COURSE = "course"
private const val CALENDAR_TERM = "calendarTerm"
private const val KLASS = "class"

class ClassSectionViewModel(private val classSectionRepository: ClassesRepository) :
    ViewModel() {

    private val classSectionLiveData = MutableLiveData<ClassSection>()

    fun getClassSectionDetails(classSummary: ClassSummary) {
        viewModelScope.launch {
            val classSectionData =
                classSectionRepository.getClassSection(classSummary)
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