package org.ionproject.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URI

@Entity(primaryKeys = ["id", "courseAcronym", "calendar_term"])
data class Favorite(
    val id: String,
    val courseAcronym: String,
    @ColumnInfo(name = "calendar_term") val calendarTerm: String, //Should be a foreign key in the future
    @ColumnInfo(name = "details_uri") val detailsUri: URI,
    val selfURI: URI
) {
    fun toClassSummary() = ClassSummary(
        id,
        courseAcronym,
        calendarTerm,
        detailsUri,
        selfURI
    )
}