package org.ionproject.android.class_section

import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

/**
 * Represents an class section properties from a [SirenEntity]
 */
data class ClassSectionProperties(
    val course: String,
    val clazz: String,
    val id: String
)

/**
 * Represents a class list information when searching for all classes in
 * a course details presentation
 */
data class ClassListProperties(
    val course: String,
    val calendarTerm: String
)

/**
 * Extension method to convert an [SirenEntity] to a [ClassSection]
 */
fun SirenEntity<ClassSectionProperties>.toClassSection(): ClassSection {
    val calendarURI: URI? = (entities?.first() as EmbeddedEntity<*>).links?.first()?.href
    properties as LinkedHashMap<String, String>

    return ClassSection(
        course = properties["course"],
        calendarTerm = properties["class"],
        id = properties["id"],
        calendarURI = calendarURI
    )
}
