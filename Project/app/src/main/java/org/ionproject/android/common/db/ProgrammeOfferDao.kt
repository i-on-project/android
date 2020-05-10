package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.ionproject.android.common.model.ProgrammeOffer

@Dao
abstract class ProgrammeOfferDao {

    @Query("SELECT * FROM ProgrammeOffer WHERE id = :id")
    abstract suspend fun getProgrammeOfferById(id: Int): ProgrammeOffer?

    @Insert
    abstract suspend fun insertProgrammeOffer(programmeOffer: ProgrammeOffer)

    @Update
    abstract suspend fun updateProgrammeOffer(programmeOffer: ProgrammeOffer)

    @Query("DELETE FROM ProgrammeOffer WHERE id = :id")
    abstract suspend fun deleteProgrammeOfferById(id: Int)
}