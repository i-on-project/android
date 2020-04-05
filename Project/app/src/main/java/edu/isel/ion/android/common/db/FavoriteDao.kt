package edu.isel.ion.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.isel.ion.android.common.model.ClassSection
import edu.isel.ion.android.common.model.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite : Favorite)

    @Query("SELECT * FROM ClassSection INNER JOIN Favorite ON ClassSection.id = Favorite.class_section")
    suspend fun findAllFavorites() : List<ClassSection>


}