package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.Programme
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.model.ProgrammeWithOffers
import java.net.URI

@Dao
abstract class ProgrammeDao {

    @Transaction //Using this annotation so because it performs two db operations which this way are atomic
    @Query("SELECT * FROM Programme WHERE id = :id")
    abstract suspend fun getProgrammeWithOffersById(id: Int): ProgrammeWithOffers?

    @Transaction //Using this annotation so because it performs two db operations which this way are atomic
    @Query("SELECT * FROM Programme WHERE selfUri = :uri")
    abstract suspend fun getProgrammeWithOffersByUri(uri: URI): ProgrammeWithOffers?

    suspend fun insertProgrammeWithOffers(programmeWithOffers: ProgrammeWithOffers) {
        insertProgramme(programmeWithOffers.programme)
        insertProgrammeOfferSummaries(programmeWithOffers.programmeOffers)
    }

    @Insert
    abstract suspend fun insertProgramme(programme: Programme)

    @Insert
    abstract suspend fun insertProgrammeOfferSummaries(programmeOfferSummaries: List<ProgrammeOfferSummary>)

    @Update
    abstract suspend fun updateProgramme(programme: Programme)

    @Update
    abstract suspend fun updateProgrammeOfferSummaries(programmeOfferSummaries: List<ProgrammeOfferSummary>)

    @Transaction
    @Query("DELETE FROM Programme WHERE id = :id")
    abstract suspend fun deleteProgrammeWithOffersById(id: Int)

    suspend fun updateProgrammeWithOffers(programmeWithOffers: ProgrammeWithOffers) {
        updateProgrammeOfferSummaries(programmeWithOffers.programmeOffers)
        updateProgramme(programmeWithOffers.programme)
    }

    @Query("SELECT * FROM ProgrammeSummary")
    abstract suspend fun getAllProgrammeSummaries(): List<ProgrammeSummary>

    @Query("DELETE FROM ProgrammeSummary")
    abstract suspend fun deleteAllProgrammeSummaries()

    @Insert
    abstract suspend fun insertProgrammeSummaries(programmeSummaries: List<ProgrammeSummary>)

    @Update
    abstract suspend fun updateProgrammeSummary(programmeSummary: ProgrammeSummary)

    @Update
    abstract suspend fun updateProgrammeSummaries(programmeSummaries: List<ProgrammeSummary>)

}