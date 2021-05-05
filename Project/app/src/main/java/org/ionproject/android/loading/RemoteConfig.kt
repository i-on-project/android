package org.ionproject.android.loading

import com.fasterxml.jackson.annotation.JsonProperty

data class RemoteConfig(
    @JsonProperty("api_link")
    var api_link: String
)