package org.ionproject.android.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TodoDto(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: ComponentProperties,
    val entities: List<SubEntity>,
    val links: List<SirenLink>
)
