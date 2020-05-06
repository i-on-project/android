package org.ionproject.android.common.siren

import com.fasterxml.jackson.annotation.JsonProperty

data class Todo(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: ComponentProperties,
    val entities: List<SubEntity>,
    val links: List<SirenLink>
)
