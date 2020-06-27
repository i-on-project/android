package org.ionproject.android.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This type represents the Siren specification mixed with the ICalendar specification.
 * It was invented by the i-on core members. For details regarding this specification check:
 * https://github.com/i-on-project/core/blob/master/docs/api/events.md#categories
 */
data class SirenICalendar(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: ICalendarProperties,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: List<SirenAction>? = null,
    val title: String? = null
)

data class ICalendarProperties(
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
    val categories: List<Value>,
    val created: Value? = null,
    val dtstamp: Value,
    val dtstart: Value? = null,
    val dtend: Value? = null,
    val rrule: Rules? = null,
    //val attach: Value? = null,
    val due: Value? = null,
    val relatedTo: List<Related>? = null,
    val location: Value? = null
)

data class Value(
    val parameters: EventParameter? = null,
    val value: List<String>
)

data class EventParameter(
    val language: Language
)

data class Rules(
    private val value: String
) {
    private val ruleMap = mutableMapOf<String, String>()

    /**
     * Processes the rules.
     * Logic:
     * 1ª Separates each rule in between the ;
     * 2ª Separates each rule from NAME=VALUE in pairs Name, Value
     * 3ª Adds the pair to the map
     */
    init {
        value.split(";").forEach {
            val rulePair = it.split("=")
            ruleMap.put(rulePair[0], rulePair[0])
        }
    }

    /**
     * @param rname is the name of the rule
     * @return the value of the rule or null if rule does not exist
     */
    fun getRuleByName(rname: String): String? = ruleMap.get(rname)
}

data class Related(
    val parameters: RelType? = null,
    val value: String
)

data class RelType(
    val reltype: String
)

enum class Language {
    @JsonProperty("en-GB")
    EN_GB,

    @JsonProperty("en-US")
    EN_US,

    @JsonProperty("pt-PT")
    PT_PT
}