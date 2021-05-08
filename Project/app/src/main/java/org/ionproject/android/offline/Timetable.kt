package org.ionproject.android.offline

/**
 * This class parses the timetable from JSON
 *
 * NOTA: O JSON DO ARRAY CLASSES E INCONSISTENTE TANTO NA PRESENÇA DE "LOCATION"
 */
data class Timetable(
    val school: SchoolAndProgrammeDetails,
    val programme: SchoolAndProgrammeDetails,
    val calendarTerm: String,
    val classes: List<ClassesDetails>
)

/**
 * Each of the classes from the "classes" array
 */
data class ClassesDetails(
    val acr: String,
    val sections: List<Section>?
)

data class Section(
    val section: String,
    val events: List<TimetableEvent>,
    val instructors: List<Instructor>
)

data class TimetableEvent(
    val category: String,
    val location: List<String>?,
    val beginTime: String,
    val duration: String,
    val weekday: String //all caps, first two letters of the weekday
)

data class Instructor(
    val name: String,
    val category: String //either "LECTURE" OR "PRACTICE"
)
