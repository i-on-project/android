package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Reduces boilerplate code from the rest of daos
 */
@Dao
interface BaseDao<T> {

    @Insert
    suspend fun insert(obj: T)

    @Insert
    suspend fun insertMultiple(objs: List<T>)

    @Delete
    suspend fun delete(vararg obj: T)

    @Delete
    suspend fun deleteMultiple(objs: List<T>)

    @Update
    suspend fun update(vararg obj: T)

    @Update
    suspend fun updateMultiple(objs: List<T>)

}
