package org.ionproject.android.offline.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CatalogAcademicYears(
    @JsonProperty("tree")
    val years: List<CatalogAcademicYear>
)