package org.ionproject.android.calendar.JDCalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.view_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.JDCalendarAdapter


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
                nextButtonColor = getColor(
                    R.styleable.JDCalendar_nextButtonColor,
                    Color.BLACK
                )
                prevButtonColor = getColor(
                    R.styleable.JDCalendar_prevButtonColor,
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
    private var nextButtonColor: Int? = null
    private var prevButtonColor: Int? = null
    //Sizes
    private var monthTextSize: Float? = null
    private var yearTextSize: Float? = null
    private var weekDaysTextSize: Float? = null
    //Styles
    private var monthTextStyle: Int? = null
    private var yearTextStyle: Int? = null
    private var weekDaysTextStyle: Int? = null
    private var gridStyle: Int? = null

    private val baseAdapter =
        BaseCalendarAdapter(
            adapter
        )

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
     * from the custom attributes (e.g textColot, textSize etc..)
     */
    private fun applyCustomAttributes() {
        applyColors()
        applySizes()
        //applyStyle()
    }

    /**
     * Applies all color custom attributes to its respective views
     * (e.g TextColor or TextBackgroundColor)
     */
    private fun applyColors() {
        topSectionBackgroundColor?.let { topSection.setBackgroundColor(it) }
        weekDaysHeaderBackgroundColor?.let { calendarHeader.setBackgroundColor(it) }
        monthTextColor?.let { monthTextView.setTextColor(it) }
        yearTextColor?.let { yearTextView.setTextColor(it) }
        nextButtonColor?.let { nextButton.setColorFilter(it) }
        prevButtonColor?.let { prevButton.setColorFilter(it) }
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

    /**
     * Applies all dimension custom attributes to its respective views
     * (e.g TextSize)
     */
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

    /**
     * Applies all style custom attributes to its respective views
     * (e.g TextSize)
     */
    private fun applyStyle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        applyCustomAttributes()
        gridView.adapter = baseAdapter
        updateTopSection() //Setting current month
        nextButton.setOnClickListener {
            baseAdapter.calendar = baseAdapter.calendar.monthsFromNow(1)
            updateTopSection()
        }
        prevButton.setOnClickListener {
            baseAdapter.calendar = baseAdapter.calendar.monthsFromNow(-1)
            updateTopSection()
        }

        val gestor = GestureDetectorCompat(context, object : GestureListener() {
            override fun onSwipeRight() {
                baseAdapter.calendar = baseAdapter.calendar.monthsFromNow(-1)
                updateTopSection()
            }

            override fun onSwipeLeft() {
                baseAdapter.calendar = baseAdapter.calendar.monthsFromNow(1)
                updateTopSection()
            }


        })

        gridView.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                gestor.onTouchEvent(event)
                return true
            }
        })


    }

    private fun updateTopSection() {
        monthTextView.text = baseAdapter.calendar.getMonthName(context)
        yearTextView.text = "${baseAdapter.calendar.year}"
    }


}