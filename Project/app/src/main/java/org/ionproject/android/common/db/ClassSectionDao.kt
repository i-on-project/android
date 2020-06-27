package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassSection
import java.net.URI

@Dao
interface ClassSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassSection(classSection: ClassSection)

    @Query("SELECT * FROM ClassSection WHERE id = :id AND courseAcronym = :courseAcronym AND calendarTerm = :calendarTerm")
    suspend fun getClassSectionByIdAndCourseAndCalendarTerm(
        id: String,
        courseAcronym: String,
        calendarTerm: String
    ): ClassSection?

    @Query("SELECT * FROM ClassSection WHERE selfUri = :uri")
    fun getClassSectionByUri(uri: URI): ClassSection?

    @Delete
    suspend fun deleteClassSection(classSection: ClassSection)

    @Update
    suspend fun updateClassSection(classSection: ClassSection)

    @Query("DELETE FROM ClassSection WHERE id = :id AND courseAcronym = :courseAcronym AND calendarTerm = :calendarTerm")
    fun deleteClassSectionByIdAndCourseAndCalendarTerm(
        id: String,
        courseAcronym: String,
        calendarTerm: String
    )
}