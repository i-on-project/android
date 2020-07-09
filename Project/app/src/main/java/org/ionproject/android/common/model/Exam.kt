package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Event to be called as [Exam]
 */
class Exam(
    uid: String,
    summary: String,
    description: String,
    val created: Calendar,
    val stamp: Calendar,
    val startDate: Calendar,
    val endDate: Calendar,
    // This is nullable because the exam date might have been annouced but the room hasn't
    val location: String?
) : Event(type, uid, summary, description) {

    companion object {
        const val type = "Exam" //Type of the event
        const val color = "red" //Color to be printed on calendar display as a small dot
    }
}

/**
 * Creates an [Exam] event
 */
fun createExam(properties: ComponentProperties): Exam {
    val uid = properties.uid.value.firstOrNull()
    val summary = properties.summary.value.firstOrNull()
    val description = properties.description.value.firstOrNull()
    val created = properties.created?.value?.firstOrNull()?.toCalendar()
    val stamp = properties.dtstamp.value.firstOrNull().toCalendar()
    val startDate = properties.dtstart?.value?.firstOrNull()?.toCalendar()
    val endDate = properties.dtend?.value?.firstOrNull()?.toCalendar()
    val location = properties.location?.value?.firstOrNull()

    if (uid != null && summary != null && description != null && created != null && stamp != null
        && startDate != null && endDate != null && location != null
    ) {
        return Exam(
            uid,
            summary,
            description,
            created,
            stamp,
            startDate,
            endDate,
            location
        )
    }
    throw IllegalArgumentException("Found a null field while creating an exam")
}


private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

/**
 * Extension method in order to parse information about an event's date to a Calendar instance.
 * Event's date information should be in the following format: "yyyy-MM-ddTHH:mm:ssZ",
 * so we need a SimpleDateFormat in order to parse this information.
 * This method should a null if there is an error trying to parse this or any other error.
 */
fun String?.toCalendar(): Calendar? {
    if (this.isNullOrEmpty())
        return null

    val calendar = Calendar.getInstance()
    val date: Date?
    try {
        date = formatter.parse(this)
    } catch (ex: ParseException) {
        return null
    }

    if (date != null) {
        calendar.time = date
        return calendar
    }

    return null
}

