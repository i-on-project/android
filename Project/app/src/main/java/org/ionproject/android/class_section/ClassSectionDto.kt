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
    val course = properties?.get("courseAcr")
    val calendarTerm = properties?.get("calendarTerm")
    val id = properties?.get("id")

    if (course != null && calendarTerm != null && id != null) {
        return ClassSection(course, calendarTerm, id, calendarURI)
    }

    throw MappingFromSirenException("Cannot convert $this to ClassSection")
}
