package edu.isel.ion.android.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.annotation.JsonSerialize
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
    val title: String? = null) : TypeReference<SirenEntity<T>>()

/**
 * Class whose instances represent links as they are represented in Siren.
 */
data class SirenLink(
    val rel: List<String>,
    val href: URI,
    val title: String? = null,
    val type: String? = null)

/**
 * Class whose instances represent actions that are included in a siren entity.
 */
data class SirenAction(
    val name: String,
    val href: URI,
    val title: String? = null,
    @JsonProperty("class") val clazz: List<String>? = null,
    val method: HttpMethod? = null,
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
sealed class SubEntity

data class EmbeddedLink(
    @JsonProperty("class") val clazz: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    @JsonSerialize(using = ToStringSerializer::class)
    val type: MediaType? = null,
    val title: String? = null
) : SubEntity()

data class EmbeddedEntity<T>(
    val rel: List<String>,
    @JsonProperty("class") val clazz: List<String>? = null,
    val properties: T? = null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: SirenAction? = null,
    val title: String? = null
) : SubEntity()


/*
    Represents an HTTP Method
 */
enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD
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

    JSON {
        override fun toString(): String {
            return super.addApplication(JSON_SUBTYPE)
        }
    },

    SIREN {
        override fun toString(): String {
            return super.addApplication(SIREN_SUBTYPE)
        }
    },

    JSON_HOME {
        override fun toString(): String {
            return super.addApplication(JSON_HOME_SUBTYPE)
        }
    },

    URL_ENCODED {
        override fun toString(): String {
            return super.addApplication(URL_ENCODED_SUBTYPE)
        }
    };

    /*
     Adds the application type to the media type string
     */
    private fun addApplication(mediaType : String) : String {
        return "${APPLICATION_TYPE}/${mediaType}"
    }
}





