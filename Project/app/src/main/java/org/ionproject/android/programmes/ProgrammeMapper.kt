package org.ionproject.android.programmes

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.*

fun SirenEntity.toProgrammeSummaryList(): List<ProgrammeSummary> {
    val programmesList = mutableListOf<ProgrammeSummary>()

    entities?.forEach {
        val embeddedEntity = (it as EmbeddedEntity)

        val id = embeddedEntity.properties?.get("programmeId") as? Int
        val acr = embeddedEntity.properties?.get("acronym") as? String
        val detailsUri = embeddedEntity.links?.first()?.href
        val selfUri = this.links?.first()?.href

        if (id != null && acr != null && detailsUri != null && selfUri != null)
            programmesList.add(
                ProgrammeSummary(
                    id = id,
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
    val programmeId = properties?.get("id") as? Int
    val acr = properties?.get("acronym") as? String
    val name = properties?.get("name") as? String
    val termSize = properties?.get("termSize") as? Int
    val selfUri = links?.first()?.href

    val programmeOfferSummaryList = mutableListOf<ProgrammeOfferSummary>()

    if (programmeId != null && acr != null && termSize != null && selfUri != null) {
        entities?.forEach {
            val embeddedEntity = (it as EmbeddedEntity)

            val id = embeddedEntity.properties?.get("id") as? Int
            val courseId = embeddedEntity.properties?.get("courseId") as? Int
            val termNumbers = embeddedEntity.properties?.get("termNumber") as? ArrayList<Int>
            val detailsUri = embeddedEntity.links?.first()?.href

            if (id != null && courseId != null && termNumbers != null && detailsUri != null)
                programmeOfferSummaryList.add(
                    ProgrammeOfferSummary(
                        id = id,
                        courseId = courseId,
                        termNumbers = termNumbers,
                        detailsUri = detailsUri,
                        programmeId = programmeId
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
    val id = properties?.get("id") as? Int
    val acr = properties?.get("acronym") as? String
    val termNumbers = properties?.get("termNumber") as? ArrayList<Int>
    val optional = properties?.get("optional") as? Boolean
    val selfUri = links?.first()?.href

    val detailsUri = (entities?.first() as EmbeddedEntity).links?.first()?.href

    if (id != null && acr != null && termNumbers != null && optional != null && detailsUri != null && selfUri != null) {
        return ProgrammeOffer(
            id = id,
            courseID = courseID,
            acronym = acr,
            termNumbers = termNumbers,
            optional = optional,
            detailsUri = detailsUri,
            selfUri = selfUri
        )
    }
    throw MappingFromSirenException("Cannot convert $this to ProgrammeOffer")
}