package org.ionproject.android.search

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.dto.findByRel
import org.ionproject.android.common.model.ClassSectionResult
import org.ionproject.android.common.model.CourseDetailsResult
import org.ionproject.android.common.model.ProgrammeDetailsResult
import org.ionproject.android.common.model.SearchResult

fun SirenEntity.toSearchResults(): List<SearchResult> = entities?.map {
    (it as EmbeddedEntity).toSearchResult()
} ?: emptyList()

private fun EmbeddedEntity.toSearchResult(): SearchResult {
    val classes = this.clazz
    val properties = this.properties
    val resourceUri = this.links?.findByRel("self")

    if (classes != null && properties != null && resourceUri != null) {
        return when {
            classes.contains("class-section") -> ClassSectionResult(properties, resourceUri)
            classes.contains("course") -> CourseDetailsResult(properties, resourceUri)
            classes.contains("programme") -> ProgrammeDetailsResult(properties, resourceUri)
            else -> throw MappingFromSirenException("SearchResult with classes $classes not supported")
        }
    }

    throw MappingFromSirenException("Cannot convert $this into a SearchResult")
}