package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.ionproject.android.common.model.CalendarTerm

@Dao
interface CalendarTermDao {

    @Query("SELECT * FROM CalendarTerm")
    suspend fun getAllCalendarTerms(): List<CalendarTerm>

    @Query("DELETE FROM CalendarTerm")
    suspend fun deleteAllCalendarTerms()

    @Update
    suspend fun updateCalendarTerms(calendarTerms: List<CalendarTerm>)

    @Insert
    suspend fun insertCalendarTerms(calendarTerms: List<CalendarTerm>)
}