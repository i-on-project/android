package org.ionproject.android.courses

import org.ionproject.android.common.model.CourseSummary
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.MappingFromSirenException
import org.ionproject.android.common.siren.SirenEntity

/**
 *  Converts from a [SirenEntity] to [List] of [CourseSummary]
 */
fun SirenEntity.toCourseSummaryList(): List<CourseSummary> {
    val coursesList = mutableListOf<CourseSummary>()

    entities?.forEach {
        val embeddedEntity = (it as EmbeddedEntity)

        val acr = embeddedEntity.properties?.get("acronym")
        val detailsUri = embeddedEntity.links?.first()?.href

        if (acr != null && detailsUri != null)
            coursesList.add(CourseSummary(acronym = acr, detailsUri = detailsUri))
        else
            throw MappingFromSirenException("Cannot convert ${this} to List of CourseSummary")
    }

    return coursesList
}