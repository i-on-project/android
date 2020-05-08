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
    @PrimaryKey override val id: Int,
    val name: String,
    val acronym: String,
    val termSize: Int,
    override val selfUri: URI
) : IResource {
    override var type: ResourceType = ResourceType.PROGRAMME
}

/**
 * This type represents a the summary of a Programme in the context of this application.
 */
@Entity
data class ProgrammeSummary(
    @PrimaryKey override val id: Int,
    val acronym: String,
    val detailsUri: URI,
    override val selfUri: URI
) : IResource {
    override var type: ResourceType = ResourceType.PROGRAMME_SUMMARY
}


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
@Entity(primaryKeys = ["id", "programmeId"])
data class ProgrammeOffer(
    override val id: Int,
    val acronym: String,
    val termNumber: Int,
    val optional: Boolean,
    val detailsUri: URI,
    val programmeId: Int,
    override val selfUri: URI
) : IResource {
    override var type: ResourceType = ResourceType.PROGRAMME_OFFER
}

/**
 * This type represents a ProgrammeOfferSummary in the context of this application.
 */
@Entity(primaryKeys = ["courseId", "programmeId"])
data class ProgrammeOfferSummary(
    val courseId: Int,
    val termNumber: Int,
    val detailsUri: URI,
    val programmeId: Int
)

