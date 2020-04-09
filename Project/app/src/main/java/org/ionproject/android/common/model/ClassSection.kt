package org.ionproject.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

/**
 *  This type represents a Class Section in the context of this application.
 */
data class ClassSection(
    val course: String,
    val calendarTerm: String,
    val id: String,
    val calendarURI: URI?
)

data class ClassSummary(
    val id: String?,
    val detailsUri: URI
)
