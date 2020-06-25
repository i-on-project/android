package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassSummary

@Dao
interface ClassSummaryDao {

    @Query("SELECT * FROM ClassSummary WHERE courseAcronym = :courseAcronym AND calendarTerm = :calendarTerm ")
    suspend fun getClassSummariesByCourseAndCalendarTerm(
        courseAcronym: String,
        calendarTerm: String
    ): List<ClassSummary>

    @Query("DELETE FROM ClassSummary WHERE courseAcronym = :courseAcronym AND calendarTerm = :calendarTerm ")
    suspend fun deleteClassSummariesByCourseAndCalendarTerm(
        courseAcronym: String,
        calendarTerm: String
    )

    @Insert
    suspend fun insertClassSummaries(classSummaries: List<ClassSummary>)

    @Delete
    suspend fun deleteClassSummaries(classSummaries: List<ClassSummary>)

    @Update
    suspend fun updateClassSummaries(classSummaries: List<ClassSummary>)
}