package org.ionproject.android.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This is a representation for a Calendar type, which is a iCalendar type.
 */
data class ICalendarDto(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: CalendarProperties,
    val entities: List<SubEntity>,
    val actions: List<SirenAction>,
    val links: List<SirenLink>
)

data class CalendarProperties(
    val type: String,
    val properties: CalendarCreator,
    val subComponents: List<EventProperties>
)

data class CalendarCreator(
    val prodid: Value,
    val version: Value
)
