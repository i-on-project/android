package org.ionproject.android.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This is a representation for a Calendar type, which is a iCalendar type.
 */
data class ICalendarDto(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: CalendarProperties,
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

data class EventProperties(
    val type: String,
    val properties: ComponentProperties
)

data class ComponentProperties(
    val uid: Value,
    val summary: Value,
    val description: Value,
    val categories: Value,
    val created: Value? = null,
    val dtstamp: Value,
    val dtstart: Value? = null,
    val dtend: Value? = null,
    val rrule: Rules? = null,
    val attachment: Value? = null,
    val due: Value? = null,
    val relatedTo: List<Related>? = null
)

data class Value(
    val parameters: EventParameter? = null,
    val value: List<String>
)

data class EventParameter(
    val language: String
)

data class Rules(
    val value: String
)

data class Related(
    val parameters: RelType? = null,
    val value: String
)

data class RelType(
    val reltype: String
)