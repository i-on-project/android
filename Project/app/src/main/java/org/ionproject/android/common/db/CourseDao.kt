package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.ionproject.android.common.model.Course

@Dao
abstract class CourseDao {

    @Query("SELECT * FROM Course WHERE id = :id")
    abstract suspend fun getCourseById(id: Int): Course?

    @Insert
    abstract suspend fun insertCourse(course: Course)

    @Update
    abstract suspend fun updateCourse(course: Course)

    @Query("DELETE FROM Course WHERE id = :id")
    abstract suspend fun deleteCourseById(id: Int)

}