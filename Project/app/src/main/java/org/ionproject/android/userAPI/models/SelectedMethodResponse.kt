package org.ionproject.android.userAPI.models

/**
 * Response from /api/auth/methods after sending a request with the [SelectedMethod] body
 */
data class SelectedMethodResponse(
    val auth_req_id: String,
    val expires_in: Int
)