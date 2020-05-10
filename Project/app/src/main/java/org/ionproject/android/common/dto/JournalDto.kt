package org.ionproject.android.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class JournalDto(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: ComponentProperties,
    val entities: List<SubEntity>,
    val links: List<Link>
)

data class Link(
    val rel: List<String>,
    val href: List<String>
)