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

    val course = properties?.get("course")
    val clazz = properties?.get("class")
    val name = properties?.get("id")

    if (properties != null && course != null && clazz != null && name != null) {
        //Using double bang operator because we are sure this properties cannot be null here
        return ClassSection(
            course = course,
            calendarTerm = clazz,
            name = name,
            calendarURI = calendarURI
        )
    }
    throw MappingFromSirenException("Cannot convert ${this} to ClassSection")
}
