package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassSummary

@Dao
abstract class ClassSummaryDao {

    @Query("SELECT * FROM Favorite WHERE courseAcronym = :courseAcronym AND calendar_term = :calendarTerm ")
    abstract suspend fun getClassSummariesByCourseAndCalendarTerm(
        courseAcronym: String,
        calendarTerm: String
    ): List<ClassSummary>

    @Query("DELETE FROM Favorite WHERE courseAcronym = :courseAcronym AND calendar_term = :calendarTerm ")
    abstract suspend fun deleteClassSummariesByCourseAndCalendarTerm(
        courseAcronym: String,
        calendarTerm: String
    )

    @Insert
    abstract suspend fun insertClassSummaries(classSummaries: List<ClassSummary>)

    @Delete
    abstract suspend fun deleteClassSummaries(classSummaries: List<ClassSummary>)

    @Update
    abstract suspend fun updateClassSummaries(classSummaries: List<ClassSummary>)
}