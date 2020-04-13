package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassSection

@Dao
interface ClassSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassSection(classSection: ClassSection)

    @Query("SELECT * FROM ClassSection WHERE name = :classSection")
    suspend fun findClassSectionById(classSection: String): ClassSection

    @Delete
    suspend fun deleteClassSection(classSection: ClassSection)
}