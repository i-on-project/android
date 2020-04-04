package edu.isel.ion.android.calendar_terms

import edu.isel.ion.android.common.EmbeddedEntity
import edu.isel.ion.android.common.SirenEntity
import edu.isel.ion.android.common.model.CalendarTerm
import java.net.URI

data class CalendarTermProperties(
    val name: String
)

fun SirenEntity<CalendarTermProperties>.toCalendarTerm() : CalendarTerm {
    val classesUris: MutableList<URI> = mutableListOf()

    if(entities != null){
        for (entity in entities){
            val embeddedEntity = entity as EmbeddedEntity<*>
            classesUris.add(embeddedEntity.links?.first()!!.href)
        }
    }

    return CalendarTerm(
        name = properties!!.name,
        classesURIs = classesUris
    )
}