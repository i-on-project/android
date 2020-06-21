package org.ionproject.android.common.model

/**
 * This a class to be extended from class [Exam],[Lecture],[Journal] and from [Todo]
 */
abstract class Event(
    val eventType: String,
    val uid: String,
    val summary: String,
    val description: String
)