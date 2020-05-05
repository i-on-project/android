package org.ionproject.android.class_section

import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.MappingFromSirenException
import org.ionproject.android.common.siren.SirenEntity
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
