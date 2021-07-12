package org.ionproject.android.offline.models

/**
 * This class parses the Calendar from JSON
 */
data class CatalogCalendar(
    val creationDateTime: String,
    val retrievalDateTime: String,
    val school: SchoolAndProgrammeDetails,
    val language: String,
    val terms: List<CalendarTerm>
)

data class CalendarTerm(
    val calendarTerm: String,
    val interruptions: List<CalendarEvent>,
    val evaluations: List<Evaluation>,
    val lectures: List<Details>,
    val otherEvents: List<CalendarEvent>
)

data class CalendarEvent(
    val name: String,
    val startDate: String,
    val endDate: String
)

data class Evaluation(
    val name: String,
    val startDate: String,
    val endDate: String,
    val duringLectures: Boolean
)

data class Details(
    val name: String,
    val curricularTerm: List<CurricularTerm>,
    val startDate: String,
    val endDate: String
)

data class CurricularTerm(
    val id: Int
)