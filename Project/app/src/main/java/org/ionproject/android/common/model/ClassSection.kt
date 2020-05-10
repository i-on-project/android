package org.ionproject.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URI

/**
 *  This type represents a Class Section in the context of this application.
 */
@Entity(primaryKeys = ["id", "courseAcronym", "calendar_term"])
data class ClassSection(
    val id: String,
    val courseAcronym: String,
    @ColumnInfo(name = "calendar_term") val calendarTerm: String, //Should be a foreign key in the future
    @ColumnInfo(name = "calendar_uri") val calendarURI: URI?,
    val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable

/**
 *  Represents a class summary, and when saved it represents a favorite,
 *  because the information held is exactly the same,
 *  which means there is no need to have separate types.
 */
@Entity(tableName = "Favorite", primaryKeys = ["id", "courseAcronym", "calendar_term"])
data class ClassSummary(
    val id: String,
    val courseAcronym: String,
    @ColumnInfo(name = "calendar_term") val calendarTerm: String, //Should be a foreign key in the future
    @ColumnInfo(name = "details_uri") val detailsUri: URI,
    val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable

