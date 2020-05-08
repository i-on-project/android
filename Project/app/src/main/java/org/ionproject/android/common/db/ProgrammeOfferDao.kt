package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ProgrammeOffer

@Dao
abstract class ProgrammeOfferDao {

    @Query("SELECT * FROM ProgrammeOffer WHERE termNumber = :termNumber AND programmeId = :programmeId")
    abstract suspend fun getProgrammeOffersByTermAndProgrammeId(
        termNumber: Int,
        programmeId: Int
    ): List<ProgrammeOffer>

    @Insert
    abstract suspend fun insertProgrammeOffers(programmeOffers: List<ProgrammeOffer>)

    @Update
    abstract suspend fun updateProgrammeOffers(programmeOffers: List<ProgrammeOffer>)

    @Delete
    abstract suspend fun deleteProgrammeOffers(programmeOffers: List<ProgrammeOffer>)
}