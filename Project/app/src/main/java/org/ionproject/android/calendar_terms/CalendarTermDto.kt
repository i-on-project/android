package org.ionproject.android.calendar_terms

import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

data class CalendarTermProperties(
    val name: String
)

fun SirenEntity<CalendarTermProperties>.toCalendarTerm(): CalendarTerm {
    val classesUris: MutableList<URI> = mutableListOf()

    if (entities != null) {
        for (entity in entities) {
            val embeddedEntity = entity as EmbeddedEntity<*>
            embeddedEntity.links?.first()?.href?.apply {
                classesUris.add(this)
            }
        }
    }

    return CalendarTerm(
        name = properties?.name ?: "1920v",
        classesURIs = classesUris
    )
}