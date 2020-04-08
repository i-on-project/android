package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM ClassSection INNER JOIN Favorite ON ClassSection.id = Favorite.class_section")
    suspend fun findAllFavorites(): List<ClassSection>

}