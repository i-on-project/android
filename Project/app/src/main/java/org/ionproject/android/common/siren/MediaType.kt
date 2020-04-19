package org.ionproject.android.common.siren

import com.fasterxml.jackson.annotation.JsonProperty

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
    URL_ENCODED
}