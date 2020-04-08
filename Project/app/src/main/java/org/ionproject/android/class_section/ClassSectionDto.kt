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

    if (properties != null && properties["course"] != null && properties["class"] != null
        && properties["id"] != null
    ) {
        //Using double bang operator because we are sure this properties cannot be null here
        return ClassSection(
            course = properties["course"]!!,
            calendarTerm = properties["class"]!!,
            id = properties["id"]!!,
            calendarURI = calendarURI
        )
    }
    throw MappingFromSirenException("Cannot convert ${this} to ClassSection")
}
