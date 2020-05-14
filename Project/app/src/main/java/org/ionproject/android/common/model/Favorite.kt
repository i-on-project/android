package org.ionproject.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URI

/**
 * Although this type is equal to class summary, they have to be separate types,
 * because the class summary is saved in the database for the sole purpose of caching which
 * means it can be deleted at any point in the future. The favorite on the other way cannot, it has
 * to still be in the db even if the user stops accessing it.
 */
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