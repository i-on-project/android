package org.ionproject.android.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity

/**
 * This type represents a Calendar Term in the context of this application.
 */
@Entity
data class CalendarTerm(
    @PrimaryKey val name: String,
    override var workerId: Int = 0
) : ICacheable {
    override fun toString(): String {
        return name
    }
}

fun SirenEntity.toCalendarTermList(): List<CalendarTerm> {

    val calendarTerms = mutableListOf<CalendarTerm>()

    if (clazz != null && clazz.contains("calendar-term") && clazz.contains("collection")) {
        entities?.forEach {
            val embeddedEntity = it as EmbeddedEntity
            val name = embeddedEntity.properties?.get("name")
            if (name != null) {
                calendarTerms.add(CalendarTerm(name))
            }
        }
        return calendarTerms
    }
    throw MappingFromSirenException("Cannot convert $this to a List of CalendarTerm")
}
