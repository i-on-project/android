package org.ionproject.android.common.db

import androidx.room.TypeConverter
import org.ionproject.android.calendar.jdcalendar.fromMilis
import org.ionproject.android.common.model.Moment
import org.ionproject.android.common.model.WeekDay
import java.net.URI
import java.util.*

class URIConverter {

    @TypeConverter
    fun fromString(uri: String) = URI(uri)

    @TypeConverter
    fun toString(uri: URI?) = uri?.toString() ?: ""
}

class CalendarConverter {

    @TypeConverter
    fun fromLong(calendarMillis: Long): Calendar = Calendar.getInstance().fromMilis(calendarMillis)

    @TypeConverter
    fun toLong(calendar: Calendar): Long = calendar.timeInMillis

}

class MomentConverter {

    @TypeConverter
    fun fromString(moment: String): Moment {
        val hoursMinutes = moment.split(":")
        return Moment(hoursMinutes[0].toInt(), hoursMinutes[1].toInt())
    }

    @TypeConverter
    fun toString(moment: Moment): String = "${moment.hours}:${moment.minutes}"
}

class WeekDayConverter {

    @TypeConverter
    fun fromInt(weekDay: Int) = WeekDay.values()[weekDay]

    @TypeConverter
    fun toInt(weekDay: WeekDay) = weekDay.ordinal
}
