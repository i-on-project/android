package org.ionproject.android.common.dto.enums

import com.fasterxml.jackson.annotation.JsonProperty
import org.ionproject.android.common.dto.*

/**
 * Represents the media type of an http request
 */
enum class MediaType {
    @JsonProperty("$APPLICATION_TYPE/$JSON_SUBTYPE")
    JSON,

    @JsonProperty("$APPLICATION_TYPE/$SIREN_SUBTYPE")
    SIREN,

    @JsonProperty("$APPLICATION_TYPE/$JSON_HOME_SUBTYPE")
    JSON_HOME,

    @JsonProperty("$APPLICATION_TYPE/$URL_ENCODED_SUBTYPE")
    URL_ENCODED,

    @JsonProperty("*/*")
    ANY_TYPE
}