package org.ionproject.android.calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_jdcalendar.view.*
import org.ionproject.android.R
import java.util.*


class JDCalendar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    /**
     * ----------------------------------Public methods-----------------------------------
     */
    var adapter = JDCalendarAdapter()
        set(value) {
            field = value
            baseAdapter.setAdapter(value)
        }


    /*
    Inflating calendar layout, this has to be done before obtaining
    the items from the UI. Since it is above the properties, it is executed first.

    Obtaining the custom attributes values and assigning them to fields.
     */
    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_jdcalendar, this, true)

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
                monthTextSize = getDimension(
                    R.styleable.JDCalendar_monthTextSize,
                    30F
                )
                yearTextSize = getDimension(
                    R.styleable.JDCalendar_yearTextSize,
                    30F
                )
                weekDaysTextSize = getDimension(
                    R.styleable.JDCalendar_weekDaysTextSize,
                    14F
                )
                /*monthTextStyle = getResourceId(
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
                gridStyle = getResourceId(
                    R.styleable.JDCalendar_gridStyle,
                    TODO("Default Style")
                )*/
            } finally {
                recycle()
            }
        }
    }

    //Custom Attributes
    //Colors
    private var topSectionBackgroundColor: Int? = null
    private var weekDaysHeaderBackgroundColor: Int? = null
    private var monthDaysGridBackGroundColor: Int? = null
    private var monthTextColor: Int? = null
    private var yearTextColor: Int? = null
    private var weekDaysTextColor: Int? = null
    //Sizes
    private var monthTextSize: Float? = null
    private var yearTextSize: Float? = null
    private var weekDaysTextSize: Float? = null
    //Styles
    private var monthTextStyle: Int? = null
    private var yearTextStyle: Int? = null
    private var weekDaysTextStyle: Int? = null
    private var gridStyle: Int? = null

    private val calendar = CalendarWrapper()

    private val baseAdapter = BaseCalendarAdapter(adapter, calendar)

    //Constraint layout view components
    private val gridView = gridview_calendar
    private val topSection = layout_date_display
    private val calendarHeader = layout_calendar_header
    private val nextButton = imageview_calendar_next
    private val prevButton = imageview_calendar_prev
    private val exportButton = imageview_calendar_export
    private val monthTextView = textview_date_display_month
    private val yearTextView = textview_date_display_year
    private val monTextView = textview_calendar_header_monday
    private val tueTextView = textview_calendar_header_tuesday
    private val wedTextView = textview_calendar_header_wednesday
    private val thuTextView = textview_calendar_header_thursday
    private val friTextView = textview_calendar_header_friday
    private val satTextView = textview_calendar_header_saturday
    private val sunTextView = textview_calendar_header_sunday

    /**
     * --------------------------Private methods-----------------------------------
     */
    /**
     * Updates the view components with the values
     * from the custom properties (e.g textColot, textSize etc..)
     */
    private fun applyCustomProperties() {
        applyColors()
        applySizes()
        //applyStyle()
    }

    private fun applyColors() {
        topSectionBackgroundColor?.let { topSection.setBackgroundColor(it) }
        weekDaysHeaderBackgroundColor?.let { calendarHeader.setBackgroundColor(it) }
        monthTextColor?.let { monthTextView.setTextColor(it) }
        yearTextColor?.let { yearTextView.setTextColor(it) }
        weekDaysTextColor?.let {
            monTextView.setTextColor(it)
            tueTextView.setTextColor(it)
            wedTextView.setTextColor(it)
            thuTextView.setTextColor(it)
            friTextView.setTextColor(it)
            satTextView.setTextColor(it)
            sunTextView.setTextColor(it)
        }
    }

    private fun applySizes() {
        monthTextSize?.let { monthTextView.textSize = it }
        yearTextSize?.let { yearTextView.textSize = it }
        weekDaysTextSize?.let {
            monTextView.textSize = it
            tueTextView.textSize = it
            wedTextView.textSize = it
            thuTextView.textSize = it
            friTextView.textSize = it
            satTextView.textSize = it
            sunTextView.textSize = it
        }
    }

    private fun applyStyle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        applyCustomProperties()
        gridView.adapter = baseAdapter
        updateTopSection() //Setting current month
        nextButton.setOnClickListener {
            calendar.moveForwardMonth()
            baseAdapter.notifyDataSetChanged()
            updateTopSection()
        }
        prevButton.setOnClickListener {
            calendar.moveBackwardMonth()
            baseAdapter.notifyDataSetChanged()
            updateTopSection()
        }
    }

    private fun updateTopSection() {
        monthTextView.setText(calendar.getMonthName(context))
        yearTextView.setText("${calendar.year}")
    }



}