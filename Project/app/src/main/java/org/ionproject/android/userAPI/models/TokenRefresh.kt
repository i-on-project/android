package org.ionproject.android.userAPI.models

import org.ionproject.android.common.ionwebapi.CLIENT_ID

/**
 * Body of the request to refresh the auth token
 *
 * This request provides a response with the same body as the [PollSuccess] or
 * an error
 *
 * Much like in [Poll] we will be using the [FetchResult] class, so we don't need to
 * create the data class to parse the error
 */
data class TokenRefresh(
    val refresh_token: String, //the refresh token associated with the provided access token
    val grant_type: String = "refresh_token",
    val client_id: String = CLIENT_ID //the access token to refresh
)

