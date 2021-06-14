package org.ionproject.android.userAPI.models

/**
 * Body of the POLL response
 *
 * This request can throw an error, but since we are using the [FetchResult] class we don't need
 * to make another sealed class to deal with these errors
 */
data class PollResponse(
    var access_token: String,
    var token_type:String,
    var refresh_token:String,
    var expires_in:String,
    var id_token:String
)
