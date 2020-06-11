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
    val categories: String,
    val created: Calendar,
    val stamp: Calendar,
    val startDate: Calendar,
    val endDate: Calendar
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
    val uid = properties.uid.value[0]
    val summary = properties.summary?.value?.get(0)
    val description = properties.description?.value?.get(0)
    val categories = properties.categories?.value?.get(0)
    val created = properties.created?.value?.get(0)?.toCalendar()
    val stamp = properties.dtstamp?.value?.get(0)?.toCalendar()
    val startDate = properties.dtstart?.value?.get(0)?.toCalendar()
    val endDate = properties.dtend?.value?.get(0)?.toCalendar()

    if (summary != null && description != null && categories != null && created != null && stamp != null && startDate != null && endDate != null) {
        return Exam(
            uid,
            summary,
            description,
            categories,
            created,
            stamp,
            startDate,
            endDate
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

/*fun String?.toMoment(): Moment? {
    if (this.isNullOrEmpty())
        return null

    val calendar = Calendar.getInstance()
    val date = formatter.parse(this)

    if (date != null) {
        calendar.time = date
        return Moment(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }

    return null
}*/
