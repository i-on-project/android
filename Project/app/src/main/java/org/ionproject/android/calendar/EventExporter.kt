package org.ionproject.android.calendar

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import org.ionproject.android.R
import org.ionproject.android.calendar.jdcalendar.weekDaysUntil
import org.ionproject.android.common.model.Exam
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.model.Todo
import java.util.*

/**
 * Exports events to another calendar application via Intent
 * https://developer.android.com/guide/topics/providers/calendar-provider#intents
 */
fun Lecture.export(context: Context) {
    val duration = this.duration
    val endDate = this.endDate
    val start = this.start
    val weekDay = this.weekDay

    val startMillis = start.timeInMillis
    val numberOfLectures = start.weekDaysUntil(endDate)
    if (numberOfLectures > 0) {
        val endMillis = Calendar.getInstance().run {
            this.time = start.time
            this.add(Calendar.HOUR, duration.hours)
            this.add(Calendar.MINUTE, duration.minutes)
            this.timeInMillis
        }
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(
                CalendarContract.Events.RRULE,
                "FREQ=WEEKLY;COUNT=$numberOfLectures;WKST=$weekDay"
            )
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, this.summary)
            .putExtra(CalendarContract.Events.DESCRIPTION, this.description)
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )
        startActivity(context, intent, null)
    } else {
        Toast.makeText(
            context,
            context.resources.getString(R.string.warning_no_more_classes),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Todo.export(context: Context) {
    if (this.due.after(Calendar.getInstance())) {
        val startEndMillis = this.due.timeInMillis
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startEndMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startEndMillis)
            .putExtra(CalendarContract.Events.TITLE, this.summary)
            .putExtra(CalendarContract.Events.DESCRIPTION, this.description)
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )
        startActivity(context, intent, null)
    } else {
        Toast.makeText(
            context,
            context.resources.getString(R.string.warning_due_date_has_passed),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Exam.export(context: Context) {
    if (this.startDate.after(Calendar.getInstance())) {
        val startMillis = this.startDate.timeInMillis
        val endMillis = this.endDate.timeInMillis
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, this.summary)
            .putExtra(CalendarContract.Events.DESCRIPTION, this.description)
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )
        startActivity(context, intent, null)
    } else {
        Toast.makeText(
            context,
            context.resources.getString(R.string.warning_start_date_has_passed),
            Toast.LENGTH_SHORT
        ).show()
    }
}

