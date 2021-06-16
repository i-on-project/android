package org.ionproject.android.userAPI.models

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
    val access_token: String, //the access token to refresh
    val refresh_token: String //the refresh token associated with the provided access token
)

