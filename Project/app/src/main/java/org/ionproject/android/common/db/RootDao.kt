package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.Root

@Dao
interface RootDao {
    @Query("SELECT * FROM ROOT LIMIT 1")
    fun getRootResource(): Root?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRootResource(root: Root)

    @Update
    suspend fun updateRootResource(newRoot: Root)

    @Query("DELETE FROM ROOT")
    suspend fun deleteRootResource()
}