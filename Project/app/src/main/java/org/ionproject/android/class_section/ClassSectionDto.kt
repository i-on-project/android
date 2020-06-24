package org.ionproject.android.class_section

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.ClassSection
import java.net.URI

/**
 * Extension method to convert from [SirenEntity] to [ClassSection]
 */
fun SirenEntity.toClassSection(): ClassSection {
    val calendarURI: URI? = (entities?.first() as EmbeddedEntity).links?.first()?.href
    val courseAcronym = properties?.get("courseAcr")
    val calendarTerm = properties?.get("calendarTerm")
    val id = properties?.get("id")
    val selfUri = links?.first()?.href

    if (courseAcronym != null && calendarTerm != null && id != null && selfUri != null) {
        return ClassSection(
            id = id,
            courseAcronym = courseAcronym,
            calendarTerm = calendarTerm,
            calendarURI = calendarURI,
            selfUri = selfUri
        )
    }

    throw MappingFromSirenException("Cannot convert $this to ClassSection")
}
