package org.ionproject.android.offline

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This class shows each of the terms of a programme
 *
 * This class is used to populate the details fragment
 */
data class CatalogProgrammeTerms(
    @JsonProperty("tree")
    val terms: List<CatalogTerm>
)