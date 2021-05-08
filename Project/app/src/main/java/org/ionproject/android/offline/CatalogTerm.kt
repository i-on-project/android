package org.ionproject.android.offline

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This class is used to populate each of the items of the terms list
 */
data class CatalogTerm(
    @JsonProperty("path")
    var term: String,
    @JsonProperty("url")
    var linkToInfo: String
)