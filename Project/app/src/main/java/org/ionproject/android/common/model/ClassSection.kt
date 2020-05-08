package org.ionproject.android.common.model

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
    @ColumnInfo(name = "calendar_term") val calendarTerm: String, //Should be a foreign key in the future
    @PrimaryKey val id: String,
    @ColumnInfo(name = "calendar_uri") val calendarURI: URI?
)

/**
 *  Represents a class summary, and when saved it represents a favorite,
 *  because the information holded is exactly the same,
 *  which means there is no need to have separate types.
 */
@Entity(tableName = "Favorite", primaryKeys = ["id", "course", "calendar_term"])
data class ClassSummary(
    val id: String,
    val course: String,
    @ColumnInfo(name = "calendar_term") val calendarTerm: String, //Should be a foreign key in the future
    @ColumnInfo(name = "details_uri") val detailsUri: URI
)

