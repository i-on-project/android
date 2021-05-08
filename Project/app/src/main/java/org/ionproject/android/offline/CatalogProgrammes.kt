package org.ionproject.android.offline

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *This is literally the ProgramList of the catalog
 *
 * This data class has an array with all the folders inside the
 * "programmes" directory of the integration-data repo
 *
 * Each element of the array is a data class that represents the JSON
 * representation of each programme's folder
 *
 * This class is used to fill the Programmes listfragment
 */
data class CatalogProgrammes(
    @JsonProperty("tree")
    val programmes: List<CatalogProgramme>
)