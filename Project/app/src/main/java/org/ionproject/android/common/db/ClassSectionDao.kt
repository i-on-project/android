package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.ionproject.android.common.model.ClassSection

@Dao
interface ClassSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassSections(vararg classSections: ClassSection)

    @Query("SELECT * FROM ClassSection WHERE id = :classSectionId")
    suspend fun findClassSectionById(classSectionId: String): ClassSection
}