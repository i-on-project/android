package org.ionproject.android.common.model

import androidx.room.PrimaryKey

/**
 * This a class to be extended from classes [Exam],[Lecture],[Journal] and [Todo]
 */
abstract class Event(
    var eventType: String,
    @PrimaryKey val uid: String,
    val summary: String,
    val description: String
) {
    companion object type {
        const val type = "event"
    }
}