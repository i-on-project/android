package org.ionproject.android.common.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.ionproject.android.common.dto.enums.HttpMethod
import org.ionproject.android.common.dto.enums.MediaType
import org.ionproject.android.common.model.Root
import java.net.URI


/**
 * This type represents the JSON Home format. All objects were created from the specifications:
 * https://mnot.github.io/I-D/json-home/#api-home-documents
 */
data class JsonHome(
    val api: Api,
    val resources: HashMap<String, IResource>
) {
    fun getResourceByType(resourceType: ResourceType): IResource? =
        resources[resourceType.correctName]

    fun toRoot(): Root? {
        val iresourceProgrammes = getResourceByType(ResourceType.PROGRAMMES)
        val iresourceCalendarTerm = getResourceByType(ResourceType.CALENDAR_TERM)
        if (iresourceProgrammes != null && iresourceCalendarTerm != null) {
            val programmesResource = iresourceProgrammes as HrefTemplateResource
            val calendarTermsResource = iresourceCalendarTerm as HrefTemplateResource
            return Root(
                programmesResource.hrefTemplate.uri,
                calendarTermsResource.hrefTemplate.uri
            )
        }
        return null
    }
}

/**
 * Represents the keys of the resources within the JSON Home object
 * This way we avoid having hardcoded keys through out the project
 */
enum class ResourceType {
    PROGRAMMES {
        override val correctName: String = "programmes"
    },
    COURSES {
        override val correctName: String = "courses"
    },
    CALENDAR_TERM {
        override val correctName: String = "calendar-terms"
    };

    abstract val correctName: String
}

/**
 * Represents the Api object from JSON Home representation
 */
data class Api(
    val title: String?,
    val links: ApiLinks?
)


/**
 * Represents the Links object from Api object
 */
data class ApiLinks(
    val author: String?,
    val describedBy: String?,
    val license: String?
)

/**
 * Represents a resource from JSON home representation
 * it can be of two types, Href when the URI does not have query strings,
 * and HrefTemplate when there are query strings in the URI.
 */
@JsonDeserialize(using = ResourceDeserializer::class)
interface IResource {
    val hints: Hints?
}

@JsonDeserialize(using = JsonDeserializer.None::class)
data class HrefResource(
    val href: URI,
    override val hints: Hints?
) : IResource

@JsonDeserialize(using = JsonDeserializer.None::class)
data class HrefTemplateResource(
    val hrefTemplate: URITemplate,
    val hrefVars: HashMap<String, URI>,
    override val hints: Hints?
) : IResource

class ResourceDeserializer : StdDeserializer<IResource>(
    IResource::class.java
) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): IResource {
        val node: TreeNode = p.readValueAsTree()

        /**
         * Deserialize to HrefResource only if the JSON object contains the property href
         * else deserialize to HrefTemplateResource
         */
        return p.codec.treeToValue(
            node,
            if (node.get("href") == null)
                HrefTemplateResource::class.java
            else HrefResource::class.java
        )
    }

}

/**
 * Represents the hints of a Resource as specified in the JSON Home Docs
 */
data class Hints(
    val allow: List<HttpMethod>?,
    val formats: HashMap<MediaType, Nothing>?, //Docs say that for now formats has empty objects for its values
    val acceptPatch: List<MediaType>?,
    val acceptPost: List<MediaType>?,
    val acceptPut: List<MediaType>?,
    val acceptRanges: List<String>?,
    val acceptPrefer: List<String>?,
    val docs: URI?,
    val preconditionRequired: List<String>?,
    val authSchemes: List<AuthScheme>?,
    val status: Status?
)

/**
 * Represents an authentication scheme
 */
data class AuthScheme(
    val scheme: String,
    val realms: List<String>?
)

/**
 * Represents the status of a resource
 */
enum class Status {
    //Meaning: Use of the resource is not recommended, but it is still available
    deprecated,

    /*Meaning: The resource is no longer available; i.e., it will return a 404 (Not Found)
     or 410 (Gone) HTTP status code if accessed
     */
    gone;
}










