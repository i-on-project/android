package org.ionproject.android.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This type represents a Calendar Term in the context of this application.
 */
@Entity
data class CalendarTerm(
    @PrimaryKey val name: String
) {
    override fun toString(): String {
        return name
    }
}

