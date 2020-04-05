package edu.isel.ion.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


/**
 *  This type represents a class section selected by the user in the context of this application.
 */
@Entity(primaryKeys = ["calendar_term","class_section"])
@ForeignKey(
    entity = ClassSection::class,
    parentColumns = ["id"],
    childColumns = ["class_section"]
)
data class Favorite(
    @ColumnInfo(name = "calendar_term") val calendarTerm: CalendarTerm,
    @ColumnInfo(name = "class_section") val classSection : String
)

