package org.ionproject.android.common.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.net.URI

/**
 * This type represents a Programme in the context of this application.
 */
@Entity
data class Programme(
    @PrimaryKey val id: Int,
    val name: String?,
    val acronym: String,
    val termSize: Int,
    val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable

/**
 * This type represents a the summary of a Programme in the context of this application.
 */
@Entity
data class ProgrammeSummary(
    @PrimaryKey val id: Int,
    val acronym: String,
    val detailsUri: URI,
    val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable


data class ProgrammeWithOffers(
    @Embedded
    val programme: Programme,

    @Relation(
        parentColumn = "id",
        entityColumn = "programmeId",
        entity = ProgrammeOfferSummary::class
    )
    val programmeOffers: List<ProgrammeOfferSummary>
)

/**
 * This type represents a ProgrammeOffer in the context of this application.
 * A programmeOffer is the same as a ClassSummary and Favorite
 */
@Entity
data class ProgrammeOffer(
    @PrimaryKey val id: Int,
    val courseID: Int,
    val acronym: String,
    val termNumbers: List<Int>,
    val optional: Boolean,
    val detailsUri: URI,
    val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable

/**
 * This type represents a ProgrammeOfferSummary in the context of this application.
 */
@Entity
data class ProgrammeOfferSummary(
    @PrimaryKey val id: Int,
    val courseId: Int,
    val termNumbers: List<Int>,
    val detailsUri: URI,
    val programmeId: Int
)

