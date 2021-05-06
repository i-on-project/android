package org.ionproject.android.offline

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This is a Programme
 *
 * This data class represents each of the programmes that are present in the
 * folder with the programme list
 *
 * Each Programme has an array with its terms
 *
 * term: Name of the term
 *
 * linkToInfo: URl to that programme's contents (timetable and exam schedule)
 *
 * This is the class that is used to fill the program details fragment
 */
data class CatalogProgramme(
    @JsonProperty("tree")
    val terms: List<Term>
)

data class Term(
    @JsonProperty("path")
    var term: String,
    @JsonProperty("url")
    var linkToInfo: String
)