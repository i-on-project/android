package org.ionproject.android.userAPI.models

/**
 * Body of the POLL response
 *
 * This request can throw an error, but since we are using the [FetchResult] class we don't need
 * to make another sealed class to deal with these errors
 */
data class PollSuccess(
    val access_token: String,
    val token_type:String,
    val refresh_token:String,
    val expires_in:String,
    val id_token:String
)
