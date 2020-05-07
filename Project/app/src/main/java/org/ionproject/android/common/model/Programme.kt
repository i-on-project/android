package org.ionproject.android.common.model

import java.net.URI

/**
 * This type represents a Programme in the context of this application.
 */
data class Programme(
    override val id: Int,
    val name: String,
    val acronym: String,
    val termSize: Int,
    val programmeOffers: List<ProgrammeOfferSummary>,
    override val selfUri: URI
): IResource {
    override val type: ResourceType = ResourceType.PROGRAMME
}

/**
 * This type represents a the summary of a Programme in the context of this application.
 */
data class ProgrammeSummary(
    override val id: Int,
    val acronym: String,
    val detailsUri: URI,
    override val selfUri: URI
): IResource {
    override val type: ResourceType = ResourceType.PROGRAMME_SUMMARY
}

/**
 * This type represents a ProgrammeOffer in the context of this application.
 * A programmeOffer is the same as a ClassSummary and Favorite
 */
data class ProgrammeOffer(
    val id: Int,
    val acronym: String,
    val termNumber: Int,
    val optional: Boolean,
    val detailsUri: URI
)

/**
 * This type represents a ProgrammeOfferSummary in the context of this application.
 */
data class ProgrammeOfferSummary(
    val courseId: Int,
    val termNumber: Int,
    val detailsUri: URI
)

