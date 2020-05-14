package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassSection

@Dao
interface ClassSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassSection(classSection: ClassSection)

    @Query("SELECT * FROM ClassSection WHERE id = :id AND courseAcronym = :courseAcronym AND calendar_term = :calendarTerm")
    suspend fun getClassSectionByIdAndCourseAndCalendarTerm(
        id: String,
        courseAcronym: String,
        calendarTerm: String
    ): ClassSection?

    @Delete
    suspend fun deleteClassSection(classSection: ClassSection)

    @Update
    suspend fun updateClassSection(classSection: ClassSection)

    @Query("DELETE FROM ClassSection WHERE id = :id AND courseAcronym = :courseAcronym AND calendar_term = :calendarTerm")
    fun deleteClassSectionByIdAndCourseAndCalendarTerm(
        id: String,
        courseAcronym: String,
        calendarTerm: String
    )
}