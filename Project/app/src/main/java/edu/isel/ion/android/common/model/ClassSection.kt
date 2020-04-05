package edu.isel.ion.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

/**
 *  This type represents a Class Section in the context of this application.
 */
@Entity
data class ClassSection(
    val course: String,
    @ColumnInfo(name = "calendar_term")val calendarTerm: String,
    @PrimaryKey val id: String,
    @ColumnInfo(name = "calendar_uri") val calendarURI: URI?
)
