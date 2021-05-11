package org.ionproject.android.course_details

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Classes
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.CourseRepository
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogProgrammeTermInfoFile
import org.ionproject.android.offline.models.CatalogTerm
import org.ionproject.android.offline.models.ExamSchedule
import org.ionproject.android.offline.models.Timetable
import java.net.URI

class CourseDetailsViewModel(
    private val courseRepository: CourseRepository,
    private val classesRepository: ClassesRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val classesListLiveData = MutableLiveData<List<ClassSummary>>()

    val classesList
        get() = classesListLiveData.value ?: emptyList()

    fun observeClassesListLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        classesListLiveData.observe(lifecycleOwner, Observer { onUpdate() })
    }

    /**
     * LiveData with a list with the parsed json representations of the exam schedule and timetable
     *
     * These objects need to be fetched, decoded from base 64, parsed into the correct classes and only then
     * can we configure the adapters for the View
     */
    private val catalogTermFilesLiveData: MutableLiveData<List<CatalogProgrammeTermInfoFile>> =
        MutableLiveData()

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
    fun getClassesFromCourse(classesUri: URI) {
        viewModelScope.launch {
            classesRepository.getClassCollectionByUri(
                classesUri
            ).let {
                classesListLiveData.postValue(it?.classes ?: emptyList())
            }
        }

    }

    /**
     * Course classes by calendar term
     */
    private val classesLiveData = MutableLiveData<List<Classes>>()

    val classes: List<Classes> get() = classesLiveData.value ?: emptyList()

    fun getClasses(classesUri: URI) {
        viewModelScope.launch {
            val classes = classesRepository.getClassesFromUri(classesUri)
            classesLiveData.postValue(classes)
        }
    }

    fun observeClasses(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<Classes>) -> Unit
    ) {
        classesLiveData.observe(
            lifecycleOwner,
            Observer { onUpdate(it) }
        )
    }

    //-------Catalog functions
    /**
     * Gets the term files from the catalog folder
     */
    fun getCatalogTermFiles(
        catalogTerm: CatalogTerm
    ) {
        viewModelScope.launch {
            val catalogTermFiles = catalogRepository.getTermInfo(
                URI(catalogTerm.linkToInfo)
            )
            catalogTermFilesLiveData.postValue(catalogTermFiles)
        }
    }

    fun observeCatalogTermFilesLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        catalogTermFilesLiveData.observe(lifecycleOwner, Observer {
            println("the fucking list:$it")
            onUpdate()
        })
    }

    /**
     * Get the Exam Schedule from the [catalogTermFilesLiveData] object
     */
    fun getCatalogExamSchedule(programme: String, term: String, onResult: (ExamSchedule) -> Unit) {
        viewModelScope.launch {

            catalogRepository.getFileFromGithub(
                programme,
                term,
                ExamSchedule::class.java
            ).let {
                onResult(it)
            }
        }
    }

    /**
     * Get the TimeTable from the [catalogTermFilesLiveData] object
     */
    fun getCatalogTimetable(programme: String, term: String, onResult: (Timetable) -> Unit) {
        viewModelScope.launch {

            catalogRepository.getFileFromGithub(
                programme,
                term,
                Timetable::class.java
            ).let {
                onResult(it)
            }
        }
    }

}
