package org.ionproject.android.userAPI.models

/**
 * Response from /api/auth/methods with the available auth options
 */
data class AuthMethod(
    val allowed_domains: List<String>,
    val type: String,
    val create: Boolean
)
