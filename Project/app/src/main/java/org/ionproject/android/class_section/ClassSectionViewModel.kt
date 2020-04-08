package org.ionproject.android.class_section

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.ClassesRepository
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary

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
