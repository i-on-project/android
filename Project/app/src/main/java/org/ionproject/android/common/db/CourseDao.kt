package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.ionproject.android.common.model.Course
import java.net.URI

@Dao
interface CourseDao {

    @Query("SELECT * FROM Course WHERE id = :id")
    suspend fun getCourseById(id: Int): Course?

    @Query("SELECT * FROM Course WHERE selfUri = :uri")
    suspend fun getCourseByUri(uri: URI): Course?

    @Insert
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Query("DELETE FROM Course WHERE id = :id")
    suspend fun deleteCourseById(id: Int)

}