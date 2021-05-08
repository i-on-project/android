package org.ionproject.android.offline.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
This data class represents each file's JSON representation within the array
 */
data class CatalogProgrammeTermInfoFile(
    @JsonProperty("path")
    var fileName: String,
    @JsonProperty("url")
    var linkToFile: String
)