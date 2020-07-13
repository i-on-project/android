package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.ionproject.android.common.model.Classes
import java.net.URI

@Dao
interface ClassesDao {

    @Insert
    suspend fun insertClasses(classes: List<Classes>)

    @Query("SELECT * FROM Classes WHERE upUri = :uri")
    suspend fun getClassesByUri(uri: URI): List<Classes>

    @Query("DELETE FROM Classes WHERE upUri = :uri")
    suspend fun deleteClassesByUri(uri: URI)

}