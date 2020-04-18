package edu.isel.ion.android.calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import edu.isel.ion.android.R
import kotlinx.android.synthetic.main.view_calendar.view.*


class JDCalendar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    /*
    Inflating calendar layout, this has to be done before obtaining
    the items from the UI. Since it is above the properties, it is executed first.

    Obtaining the custom attributes values and assigning them to fields.
     */
    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_calendar, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JDCalendar,
            0, 0
        ).apply {

            try {
                topSectionBackgroundColor = getColor(
                    R.styleable.JDCalendar_topSectionBackgroundColor,
                    Color.WHITE
                )
                weekDaysHeaderBackgroundColor = getColor(
                    R.styleable.JDCalendar_weekDaysHeaderBackgroundColor,
                    Color.WHITE
                )
                monthDaysGridBackGroundColor = getColor(
                    R.styleable.JDCalendar_monthDaysGridBackgroundColor,
                    Color.WHITE
                )
                monthTextColor = getColor(
                    R.styleable.JDCalendar_monthTextColor,
                    Color.BLACK
                )
                yearTextColor = getColor(
                    R.styleable.JDCalendar_yearTextColor,
                    Color.BLACK
                )
                weekDaysTextColor = getColor(
                    R.styleable.JDCalendar_weekDaysTextColor,
                    Color.BLACK
                )
                monthDaysTextColor = getColor(
                    R.styleable.JDCalendar_monthDaysTextColor,
                    Color.BLACK
                )
                monthTextSize = getDimension(
                    R.styleable.JDCalendar_monthTextSize,
                    24F
                )
                yearTextSize = getDimension(
                    R.styleable.JDCalendar_yearTextSize,
                    24F
                )
                weekDaysTextSize = getDimension(
                    R.styleable.JDCalendar_weekDaysTextSize,
                    24F
                )
                monthDaysTextSize = getDimension(
                    R.styleable.JDCalendar_monthDaysTextSize,
                    24F
                )
                monthTextStyle = getResourceId(
                    R.styleable.JDCalendar_monthTextStyle,
                    TODO("Default Style")
                )
                yearTextStyle = getResourceId(
                    R.styleable.JDCalendar_yearTextStyle,
                    TODO("Default Style")
                )
                weekDaysTextStyle = getResourceId(
                    R.styleable.JDCalendar_weekDaysTextStyle,
                    TODO("Default Style")
                )
                monthDaysTextStyle = getResourceId(
                    R.styleable.JDCalendar_monthDaysTextStyle,
                    TODO("Default Style")
                )
                gridStyle = getResourceId(
                    R.styleable.JDCalendar_gridStyle,
                    TODO("Default Style")
                )
            } finally {
                recycle()
            }
        }

        applyCustomProperties()

    }

    //Custom Attributes
    //Colors
    private var topSectionBackgroundColor: Int? = null
    private var weekDaysHeaderBackgroundColor: Int? = null
    private var monthDaysGridBackGroundColor: Int? = null
    private var monthTextColor: Int? = null
    private var yearTextColor: Int? = null
    private var weekDaysTextColor: Int? = null
    private var monthDaysTextColor: Int? = null
    //Sizes
    private var monthTextSize: Float? = null
    private var yearTextSize: Float? = null
    private var weekDaysTextSize: Float? = null
    private var monthDaysTextSize: Float? = null
    //Styles
    private var monthTextStyle: Int? = null
    private var yearTextStyle: Int? = null
    private var weekDaysTextStyle: Int? = null
    private var monthDaysTextStyle: Int? = null
    private var gridStyle: Int? = null


    //Contains the days of the month
    private val gridView = gridview_calendar
    private val nextButton = imageview_calendar_next
    private val prevButton = imageview_calendar_prev
    private val exportButton = imageview_calendar_export
    private val monthTextView = textview_date_display_month
    private val yearTextView = textview_date_display_year

    /**
     * Updates the view components with the values
     * from the custom properties (e.g textColot, textSize etc..)
     */
    private fun applyCustomProperties() {
        TODO()
    }

    /**
     * TODO Calendar methods
     */

}