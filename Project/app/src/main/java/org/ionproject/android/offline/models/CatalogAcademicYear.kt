package org.ionproject.android.offline.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CatalogAcademicYear(
    @JsonProperty("path")
    var year: String,
    @JsonProperty("url")
    var linkToInfo: String
)