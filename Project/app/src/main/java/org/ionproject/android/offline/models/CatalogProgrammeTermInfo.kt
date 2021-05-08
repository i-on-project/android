package org.ionproject.android.offline.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
NOTE: These classes are redundant since the same result could be achieved using the
catalog programmes and catalog programme classes. However, despite using JSON properties with the
same names, the name of the values within each class changes and makes it easier to understand
the parsing code
 */

/**
 * This is the Term details for the chosen programme
 *
 * This represents the folder of the term chosen, where we can see the files exam schedule or a timetable
 *
 * Each element of the array is a ProgramInfoFile, and can either be an exam schedule or a timetable
 *
 */

data class CatalogProgrammeTermInfo(
    @JsonProperty("tree")
    var files: List<CatalogProgrammeTermInfoFile>
)