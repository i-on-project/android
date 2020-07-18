package org.ionproject.android.common

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.CalendarTerm

fun SirenEntity.toCalendarTermList(): List<CalendarTerm> {

    val calendarTerms = mutableListOf<CalendarTerm>()

    if (clazz != null && clazz.contains("calendar-term") && clazz.contains("collection")) {
        entities?.forEach {
            val embeddedEntity = it as EmbeddedEntity
            val name = embeddedEntity.properties?.get("name")

            // Here we are assuming the format of calendar term is for example 1920v
            // Data inserted by integration is unfortunatly not following this format
            // This approach should be reconsidered in the future if the data from integration
            // is not correct.
            if (name != null) {
                val year = name.substring(0, 4).toInt()
                val season = name.substring(4)
                calendarTerms.add(CalendarTerm(year, season))
            }
        }
        return calendarTerms
    }
    throw MappingFromSirenException("Cannot convert $this to a List of CalendarTerm")
}
