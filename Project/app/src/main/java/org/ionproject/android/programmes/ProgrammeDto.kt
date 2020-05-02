package org.ionproject.android.programmes

import org.ionproject.android.common.model.Programme
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.MappingFromSirenException
import org.ionproject.android.common.siren.SirenEntity

fun SirenEntity.toProgrammeSummaryList(): List<ProgrammeSummary> {
    val programmesList = mutableListOf<ProgrammeSummary>()

    entities?.forEach {
        val embeddedEntity = (it as EmbeddedEntity)

        val id = embeddedEntity.properties?.get("id")
        val acr = embeddedEntity.properties?.get("acronym")
        val detailsUri = embeddedEntity.links?.first()?.href

        if (id != null && acr != null && detailsUri != null)
            programmesList.add(
                ProgrammeSummary(
                    id = id.toInt(),
                    acronym = acr,
                    detailsUri = detailsUri
                )
            )
        else
            throw MappingFromSirenException("Cannot convert ${this} to List of ProgrammeSummary")
    }

    return programmesList
}

fun SirenEntity.toProgramme(): Programme {
    val id = properties?.get("id")
    val acr = properties?.get("acronym")
    val name = properties?.get("name")
    val termSize = properties?.get("termSize")

    val programmeOfferSummaryList = mutableListOf<ProgrammeOfferSummary>()

    entities?.forEach {
        val embeddedEntity = (it as EmbeddedEntity)

        val courseId = embeddedEntity.properties?.get("CourseId")
        val termNumber = embeddedEntity.properties?.get("TermNumber")
        val detailsUri = embeddedEntity.links?.first()?.href

        if (courseId != null && termNumber != null && detailsUri != null)
            programmeOfferSummaryList.add(
                ProgrammeOfferSummary(
                    courseId = courseId.toInt(),
                    termNumber = termNumber.toInt(),
                    detailsUri = detailsUri
                )
            )
        else
            throw MappingFromSirenException("Cannot convert ${it} to ProgrammeOfferSummary")
    }

    if (id != null && acr != null && name != null && termSize != null) {
        return Programme(
            id = id.toInt(),
            name = name,
            acronym = acr,
            termSize = termSize.toInt(),
            programmeOffers = programmeOfferSummaryList
        )
    }
    throw MappingFromSirenException("Cannot convert ${this} to Programme")
}

fun SirenEntity.toProgrammeOffer(): ProgrammeOffer {
    val id = properties?.get("id")
    val acr = properties?.get("acronym")
    val termNumber = properties?.get("termNumber")
    val optional = properties?.get("optional")

    val detailsUri = (entities?.first() as EmbeddedEntity).links?.first()?.href

    if (id != null && acr != null && termNumber != null && optional != null && detailsUri != null) {
        return ProgrammeOffer(
            id = id.toInt(),
            acronym = acr,
            termNumber = termNumber.toInt(),
            optional = optional.toBoolean(),
            detailsUri = detailsUri
        )
    }
    throw MappingFromSirenException("Cannot convert ${this} to ProgrammeOffer")
}