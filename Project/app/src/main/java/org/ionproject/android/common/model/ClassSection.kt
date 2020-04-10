package org.ionproject.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.net.URI

/**
 *  This type represents a Class Section in the context of this application.
 */
@Entity
data class ClassSection(
    val course: String,
    @ColumnInfo(name = "calendar_term") val calendarTerm: String,
    @PrimaryKey val id: String,
    @ColumnInfo(name = "calendar_uri") val calendarURI: URI?
)

data class ClassSummary(
    val id: String?,
    val detailsUri: URI
)

class URIConverter {

    @TypeConverter
    fun fromString(uri: String): URI {
        return URI(uri)
    }

    @TypeConverter
    fun uriToString(uri: URI): String {
        return uri.toString()
    }

}
