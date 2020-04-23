package org.ionproject.android.common.icalendar

import com.fasterxml.jackson.annotation.JsonProperty
import org.ionproject.android.common.siren.SirenLink
import org.ionproject.android.common.siren.SubEntity

/**
 * This is a representation for a Event type, which is a iCalendar type.
 */
data class Event(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: EventProperties? = null,
    val entities: SubEntity,
    val links: SirenLink? = null
)

data class EventProperties(
    val uid: ParameterValue,
    val summary: ParameterValue? = null,
    val description: ParameterValue? = null,
    val categories: ParameterValue? = null,
    val created: ParameterValue,
    val dtstamp: ParameterValue? = null,
    val dtstart: ParameterValue,
    val dtend: ParameterValue? = null,
    val duration: ParameterValue? = null
)

data class ParameterValue(
    val parameters: Any,
    val value: Any
)

