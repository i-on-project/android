package org.ionproject.android.calendar_terms

import org.ionproject.android.common.EmbeddedEntity
import org.ionproject.android.common.SirenEntity
import org.ionproject.android.common.model.CalendarTerm
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
        name = properties!!.name,
        classesURIs = classesUris
    )
}