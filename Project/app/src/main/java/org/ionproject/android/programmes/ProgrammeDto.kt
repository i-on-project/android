package org.ionproject.android.programmes

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.EmbeddedLink
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.*

fun SirenEntity.toProgrammeSummaryList(): List<ProgrammeSummary> {
    val programmesList = mutableListOf<ProgrammeSummary>()

    entities?.forEach {
        val embeddedEntity = (it as EmbeddedEntity)

        val id = embeddedEntity.properties?.get("programmeId")
        val acr = embeddedEntity.properties?.get("acronym")
        val detailsUri = embeddedEntity.links?.first()?.href
        val selfUri = this.links?.first()?.href

        if (id != null && acr != null && detailsUri != null && selfUri != null)
            programmesList.add(
                ProgrammeSummary(
                    id = id.toInt(),
                    acronym = acr,
                    detailsUri = detailsUri,
                    selfUri = selfUri
                )
            )
        else
            throw MappingFromSirenException("Cannot convert $this to List of ProgrammeSummary")
    }

    return programmesList
}

fun SirenEntity.toProgramme(): ProgrammeWithOffers {
    val programmeId = properties?.get("id")
    val acr = properties?.get("acronym")
    val name = properties?.get("name")
    val termSize = properties?.get("termSize")
    val selfUri = links?.first()?.href

    val programmeOfferSummaryList = mutableListOf<ProgrammeOfferSummary>()

    if (programmeId != null && acr != null && name != null && termSize != null && selfUri != null) {
        entities?.forEach {
            val embeddedEntity = (it as EmbeddedEntity)

            val id = embeddedEntity.properties?.get("id")
            val courseId = embeddedEntity.properties?.get("courseId")
            val termNumber = embeddedEntity.properties?.get("termNumber")
            val detailsUri = embeddedEntity.links?.first()?.href

            if (id != null && courseId != null && termNumber != null && detailsUri != null)
                programmeOfferSummaryList.add(
                    ProgrammeOfferSummary(
                        id = id.toInt(),
                        courseId = courseId.toInt(),
                        termNumber = termNumber.toInt(),
                        detailsUri = detailsUri,
                        programmeId = programmeId.toInt()
                    )
                )
            else
                throw MappingFromSirenException("Cannot convert $it to ProgrammeOfferSummary")
        }
        return ProgrammeWithOffers(
            programme = Programme(
                id = programmeId.toInt(),
                name = name,
                acronym = acr,
                termSize = termSize.toInt(),
                selfUri = selfUri
            ),
            programmeOffers = programmeOfferSummaryList
        )
    }
    throw MappingFromSirenException("Cannot convert $this to Programme")
}

fun SirenEntity.toProgrammeOffer(courseID: Int): ProgrammeOffer {
    val id = properties?.get("id")
    val acr = properties?.get("acronym")
    val termNumber = properties?.get("termNumber")
    val optional = properties?.get("optional")
    val selfUri = links?.first()?.href

    val detailsUri = (entities?.first() as EmbeddedEntity).links?.first()?.href

    if (id != null && acr != null && termNumber != null && optional != null && detailsUri != null && selfUri != null) {
        return ProgrammeOffer(
            id = id.toInt(),
            courseID = courseID,
            acronym = acr,
            termNumber = termNumber.toInt(),
            optional = optional.toBoolean(),
            detailsUri = detailsUri,
            selfUri = selfUri
        )
    }
    throw MappingFromSirenException("Cannot convert ${this} to ProgrammeOffer")
}