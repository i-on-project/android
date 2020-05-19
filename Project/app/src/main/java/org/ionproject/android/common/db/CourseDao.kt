package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.ionproject.android.common.model.Course

@Dao
interface CourseDao {

    @Query("SELECT * FROM Course WHERE id = :id")
    suspend fun getCourseById(id: Int): Course?

    @Insert
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Query("DELETE FROM Course WHERE id = :id")
    suspend fun deleteCourseById(id: Int)

}