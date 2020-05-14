package org.ionproject.android.common.dto

import java.net.URI

/**
 * Represents specifically an UriTemplate with query strings
 * ex: /v0/programmes{?page,limit}
 */
class URITemplate(uriTemplate: String) {

    var queryStrings: List<String> = emptyList()
        private set
    var uri: URI = URI("")
        private set

    init {
        // /v0/programmes{?page,limit} -> /v0/programmes & ?page,limit}
        val uriTemplateParts = uriTemplate.split("{")

        uri = URI(uriTemplateParts[0])
        queryStrings = uriTemplateParts[1]
            .replace("?", "")
            .replace("}", "")
            .split(",")
    }
}