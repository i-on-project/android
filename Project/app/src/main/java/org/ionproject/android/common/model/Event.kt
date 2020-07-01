package org.ionproject.android.common.model

import androidx.room.PrimaryKey

/**
 * This a class to be extended from class [Exam],[Lecture],[Journal] and from [Todo]
 */
abstract class Event(
    val eventType: String,
    @PrimaryKey val uid: String,
    val summary: String,
    val description: String
) {
    companion object type {
        const val type = "event"
    }
}