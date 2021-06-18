package org.ionproject.android.userAPI.models

data class PollBody(
    val grant_type: String = "urn:openid:params:grant-type:ciba",
    val auth_req_id : String,
    val client_id: String
)