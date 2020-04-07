package edu.isel.ion.android.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import java.net.URI

/**
 *  Most of this code was written by Prof. Paulo Pereira
 *  but modified by João Silva and Diogo Santos
 */


/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */

data class SirenEntity<T>(
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: T? = null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: List<SirenAction>? = null,
    val title: String? = null
)


/**
 * Class whose instances represent links as they are represented in Siren.
 */
data class SirenLink(
    val rel: List<String>,
    val href: URI,
    val title: String? = null,
    val type: String? = null
)

/**
 * Class whose instances represent actions that are included in a siren entity.
 */
data class SirenAction(
    val name: String,
    val href: URI,
    val title: String? = null,
    val clazz: List<String>? = null,
    val method: HttpMethod? = null,
    val isTemplated: Boolean? = null,
    @JsonSerialize(using = ToStringSerializer::class)
    val type: MediaType? = null,
    val fields: List<Field>? = null
) {
    /**
     * Represents action's fields
     */
    data class Field(
        val name: String,
        val type: String? = null,
        val value: String? = null,
        val title: String? = null
    )
}

/**
 * Base class for admissible sub entities, namely, [EmbeddedLink] and [EmbeddedEntity].
 */

@JsonDeserialize(using = SubEntityDeserializer::class)
sealed class SubEntity

@JsonDeserialize(using = JsonDeserializer.None::class)
data class EmbeddedLink(
    @JsonProperty("class") val clazz: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    @JsonSerialize(using = ToStringSerializer::class)
    val type: MediaType? = null,
    val title: String? = null
) : SubEntity()

@JsonDeserialize(using = JsonDeserializer.None::class)
data class EmbeddedEntity<T>(
    val rel: List<String>,
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: T? = null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: SirenAction? = null,
    val title: String? = null
) : SubEntity()


class SubEntityDeserializer : StdDeserializer<SubEntity>(SubEntity::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): SubEntity {
        val node: TreeNode = p.readValueAsTree()

        //If property "properties" or "links" or "actions" exists then
        // it must be an EmbeddedEntity
        return p.codec.treeToValue(
            node,
            if (node.get("properties") != null ||
                node.get("links") != null ||
                node.get("actions") != null) EmbeddedEntity::class.java
            else EmbeddedLink::class.java
        )
    }

}

/*
    Represents an HTTP Method
 */
enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    PATCH
}


const val APPLICATION_TYPE = "application"
const val SIREN_SUBTYPE = "vnd.siren+json"
const val JSON_SUBTYPE = "json"
const val JSON_HOME_SUBTYPE = "json-home"
const val URL_ENCODED_SUBTYPE = "x-www-form-urlencoded"

/*
    Represents the media type of an http request
 */

enum class MediaType {

    @JsonProperty("${APPLICATION_TYPE}/${JSON_SUBTYPE}")
    JSON,

    @JsonProperty("${APPLICATION_TYPE}/${SIREN_SUBTYPE}")
    SIREN,

    @JsonProperty("${APPLICATION_TYPE}/${JSON_HOME_SUBTYPE}")
    JSON_HOME,

    @JsonProperty("${APPLICATION_TYPE}/${URL_ENCODED_SUBTYPE}")
    URL_ENCODED

}





