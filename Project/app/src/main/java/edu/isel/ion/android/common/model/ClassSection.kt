package edu.isel.ion.android.common.model

import com.squareup.moshi.Json

/*

    This type represents a Class Section in the context of this application.

 */
data class ClassSection (
    val course : String,
    @Json(name = "class") val clazz : String,
    val id : String
)
