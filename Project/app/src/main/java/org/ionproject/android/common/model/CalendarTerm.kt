package org.ionproject.android.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This type represents a Calendar Term in the context of this application.
 */
@Entity
data class CalendarTerm(
    val years: Int,
    val season: String,
    @PrimaryKey val name: String = "$years$season",
    override var workerId: Int = 0
) : ICacheable {
    override fun toString(): String {
        return name
    }
}
