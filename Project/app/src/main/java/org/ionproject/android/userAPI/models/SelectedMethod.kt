package org.ionproject.android.userAPI.models

/**
 * body of the request to /api/auth/methods to continue the authentication procedure
 * after choosing the auth method
 */
data class SelectedMethod(
    val scope: String,
    val acr_values: String,
    val login_hint: String,
    val client_id: String
)