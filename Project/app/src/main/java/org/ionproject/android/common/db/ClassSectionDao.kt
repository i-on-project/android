package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassSection

@Dao
interface ClassSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassSection(classSection: ClassSection)

    @Query("SELECT * FROM ClassSection WHERE id = :classSection")
    suspend fun getClassSectionById(classSection: String): ClassSection

    @Delete
    suspend fun deleteClassSection(classSection: ClassSection)

    @Update
    suspend fun updateClassSection(classSection: ClassSection)
}