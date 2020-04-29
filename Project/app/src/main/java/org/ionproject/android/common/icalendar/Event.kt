package org.ionproject.android.common.icalendar

import com.fasterxml.jackson.annotation.JsonProperty
import org.ionproject.android.common.siren.SirenLink
import org.ionproject.android.common.siren.SubEntity

/**
 * This is a representation for a Event type, which is a iCalendar type.
 */
data class Event(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: Properties,
    val entities: List<SubEntity>,
    val links: List<SirenLink>
)

data class Properties(
    val type: String? = null,
    val properties: EventProperties
)

data class EventProperties(
    val uid: Value,
    val summary: Value? = null,
    val description: Value? = null,
    val categories: Value? = null,
    val created: Value? = null,
    val dtstamp: Value? = null,
    val dtstart: Value? = null,
    val dtend: Value? = null,
    val duration: Value? = null
)

data class Value(
    val value: String
)

