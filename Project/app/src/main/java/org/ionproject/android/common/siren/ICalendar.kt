package org.ionproject.android.common.siren

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This is a representation for a Calendar type, which is a iCalendar type.
 */
data class ICalendar(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: CalendarProperties,
    val entities: List<SubEntity>,
    val actions: List<SirenAction>,
    val links: List<SirenLink>
)

data class CalendarProperties(
    val type: String,
    val properties: CalendarCreator,
    val subComponents: List<Properties>
)

data class CalendarCreator(
    val prodid: Value,
    val version: Value
)
