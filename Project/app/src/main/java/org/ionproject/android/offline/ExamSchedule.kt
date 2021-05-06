package org.ionproject.android.offline

/**
 * This data class parses the exam schedule representation from Json
 */
data class ExamSchedule(
    val school: SchoolAndProgrammeDetails,
    val programme: SchoolAndProgrammeDetails,
    val calendarTerm: String,
    val exams: List<ExamDetails>
)

/**
 * Each of the exams from the "exams" array
 */
data class ExamDetails(
    val name: String,
    val startDate: String,
    val endDate: String,
    val category: String
)