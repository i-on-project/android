package org.ionproject.android.common.siren

import com.fasterxml.jackson.annotation.JsonProperty

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
    val properties: ComponentProperties
)

data class ComponentProperties(
    val uid: Value,
    val summary: Value? = null,
    val description: Value? = null,
    val categories: Value? = null,
    val created: Value? = null,
    val dtstamp: Value? = null,
    val dtstart: Value? = null,
    val dtend: Value? = null,
    val duration: Value? = null,
    val rrule: Rules? = null,
    val attachment: Value? = null,
    val due: Value? = null,
    val relatedTo: List<Related>? = null
)

data class Value(
    val value: List<String>
)

data class Rules(
    val value: RuleValues
)

data class RuleValues(
    val freq: String,
    val until: String,
    val byDay: List<String>
)

data class Related(
    val parameters: RelType? = null,
    val value: String
)

data class RelType(
    val reltype: String
)
