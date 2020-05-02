package org.ionproject.android.common.model

import java.net.URI

/**
 * This type represents a Programme in the context of this application.
 */
data class Programme(
    val id: Int,
    val name: String,
    val acronym: String,
    val termSize: Int,
    val programmeOffers: List<ProgrammeOfferSummary>
)

/**
 * This type represents a the summary of a Programme in the context of this application.
 */
data class ProgrammeSummary(
    val id: Int,
    val acronym: String,
    val detailsUri: URI
)

/**
 * This type represents a ProgrammeOffer in the context of this application.
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

