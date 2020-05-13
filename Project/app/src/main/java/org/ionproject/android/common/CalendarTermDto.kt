package org.ionproject.android.common

import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.MappingFromSirenException
import org.ionproject.android.common.siren.SirenEntity

fun SirenEntity.toCalendarTermList(): List<CalendarTerm> {

    val calendarTerms = mutableListOf<CalendarTerm>()

    if (clazz != null && clazz.contains("calendar-term") && clazz.contains("collection")) {
        entities?.forEach {
            val embeddedEntity = it as EmbeddedEntity
            val name = embeddedEntity.properties?.get("name")
            if (name != null) {
                calendarTerms.add(
                    CalendarTerm(
                        name
                    )
                )
            }
        }
        return calendarTerms
    }
    throw MappingFromSirenException("Cannot convert ${this} to a List of CalendarTerm")
}