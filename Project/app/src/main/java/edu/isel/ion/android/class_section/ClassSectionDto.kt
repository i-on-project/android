package edu.isel.ion.android.class_section

import edu.isel.ion.android.common.EmbeddedEntity
import edu.isel.ion.android.common.SirenEntity
import edu.isel.ion.android.common.model.ClassSection
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
 * Extension method to convert an [SirenEntity] to a [ClassSection]
 */
fun SirenEntity<ClassSectionProperties>.toClassSection(): ClassSection {
    var calendarURI: URI? = (entities!!.first() as EmbeddedEntity<*>).links?.first()?.href

    return ClassSection(
        course = properties!!.course,
        calendarTerm = properties.clazz,
        id = properties.id,
        calendarURI = calendarURI
    )
}