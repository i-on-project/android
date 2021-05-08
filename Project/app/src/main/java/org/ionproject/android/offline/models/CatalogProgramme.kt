package org.ionproject.android.offline.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This is a Programme
 *
 * This data class represents each of the programmes that are present in the
 * folder with the programme list
 *
 * Each Programme has a name and a link to their terms
 *
 * name: Name of the programme
 *
 * linkToInfo: URl to that programme's terms (CatalogProgrammeTerms)
 *
 * This class is used to populate each of the items of the programmes list fragment
 *
 */
data class CatalogProgramme(
    @JsonProperty("path")
    var programmeName: String,
    @JsonProperty("url")
    var linkToInfo: String
)