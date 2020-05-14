package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.ionproject.android.common.model.ProgrammeOffer

@Dao
interface ProgrammeOfferDao {

    @Query("SELECT * FROM ProgrammeOffer WHERE id = :id")
    suspend fun getProgrammeOfferById(id: Int): ProgrammeOffer?

    @Insert
    suspend fun insertProgrammeOffer(programmeOffer: ProgrammeOffer)

    @Update
    suspend fun updateProgrammeOffer(programmeOffer: ProgrammeOffer)

    @Query("DELETE FROM ProgrammeOffer WHERE id = :id")
    suspend fun deleteProgrammeOfferById(id: Int)
}