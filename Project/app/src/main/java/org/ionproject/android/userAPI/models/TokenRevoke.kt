package org.ionproject.android.userAPI.models

import org.ionproject.android.common.ionwebapi.CLIENT_ID

data class TokenRevoke(
    val access_token: String,
    val client_id: String = CLIENT_ID,
    val client_secret: String? = null
)