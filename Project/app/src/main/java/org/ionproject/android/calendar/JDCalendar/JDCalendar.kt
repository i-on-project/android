package org.ionproject.android.calendar.jdcalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_jdcalendar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.ionproject.android.R
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * This type represents a calendar and exposes some properties and methods for customization
 *
 * The instantiation logic is as follows:
 * 1ª - first init block inflates the layout and obtain the custom attributes
 * 2ª - view properties are instantiated now that the layout has been inflated
 * 3ª - second init block applies custom attributes and behaviour to the views
 * that have now been instantiated
 */
class JDCalendar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs),
    CoroutineScope {

    //----------------------------------Public methods-----------------------------------
    var adapter: CalendarAdapter<*> = JDCalendarAdapter()
        set(value) {
            field = value
            BaseCalendarAdapter(value).let {
                // Calendar has to be updated in case user advanced months before adapter is set
                it.calendar = baseAdapter.calendar
                baseAdapter = it
                gridView.adapter = it
            }
        }


    // Inflating calendar layout, this has to be done before obtaining
    // the items from the UI. Since it is above the properties, it is executed first.
    // Obtaining the custom attributes values and assigning them to fields.
    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_jdcalendar, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JDCalendar,
            0, 0
        ).apply {

            try {
                // Colors
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

                // Dimensions
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

    // Custom Attributes
    // Colors
    private var topSectionBackgroundColor: Int? = null
    private var weekDaysHeaderBackgroundColor: Int? = null
    private var monthDaysGridBackGroundColor: Int? = null
    private var monthTextColor: Int? = null
    private var yearTextColor: Int? = null
    private var weekDaysTextColor: Int? = null
    private var nextButtonColor: Int? = null
    private var prevButtonColor: Int? = null

    // Sizes
    private var monthTextSize: Float? = null
    private var yearTextSize: Float? = null
    private var weekDaysTextSize: Float? = null

    // Position
    private var topSectionElevation: Float? = null

    // Styles
    private var monthTextStyle: Int? = null
    private var yearTextStyle: Int? = null
    private var weekDaysTextStyle: Int? = null
    private var gridStyle: Int? = null

    // Constraint layout view components
    private val gridView = gridview_calendar
    private val topSection = constraintlayout_calendar_top_section
    private val calendarHeader = linearlayout_calendar_header

    // Public properties
    val nextButton = imageview_calendar_next
    val prevButton = imageview_calendar_prev
    val monthTextView = textview_date_display_month
    val yearTextView = textview_date_display_year
    val mondayTextView = textview_calendar_header_monday
    val tuesdayTextView = textview_calendar_header_tuesday
    val wednesday = textview_calendar_header_wednesday
    val thursdayTextView = textview_calendar_header_thursday
    val fridayTextView = textview_calendar_header_friday
    val saturdayTextView = textview_calendar_header_saturday
    val sundayTextView = textview_calendar_header_sunday

    /**
     * Based adapter which is used by the gridview to instantiate the child views.
     * Also holds the calendar instance.
     */
    private var baseAdapter: BaseCalendarAdapter<*> = BaseCalendarAdapter(adapter)

    /**
     * Applies all custom attributes to its respective view elements
     * Adds behaviour to each view element.
     */
    init {
        applyCustomAttributes()
        gridView.adapter = baseAdapter
        updateTopSection(baseAdapter.calendar) //Setting current month

        nextButton.setOnClickListener {
            launchedCoroutines.add(launch {
                val calendar = baseAdapter.advanceMonths(1)
                updateTopSection(calendar)
            })
        }
        prevButton.setOnClickListener {
            launchedCoroutines.add(launch {
                val calendar = baseAdapter.advanceMonths(-1)
                updateTopSection(calendar)
            })
        }
    }


    //--------------------------Private methods-----------------------------------

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
            mondayTextView.setTextColor(it)
            tuesdayTextView.setTextColor(it)
            wednesday.setTextColor(it)
            thursdayTextView.setTextColor(it)
            fridayTextView.setTextColor(it)
            saturdayTextView.setTextColor(it)
            sundayTextView.setTextColor(it)
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
            mondayTextView.textSize = it
            tuesdayTextView.textSize = it
            wednesday.textSize = it
            thursdayTextView.textSize = it
            fridayTextView.textSize = it
            saturdayTextView.textSize = it
            sundayTextView.textSize = it
        }
    }


    /**
     * Applies all style custom attributes to its respective views
     * (e.g TextSize)
     */
    private fun applyStyle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Updates the text of month and year of the calendar.
     */
    private fun updateTopSection(calendar: Calendar) {
        monthTextView.text = calendar.getMonthName(context)
        yearTextView.text = "${calendar.year}"
    }

    private val launchedCoroutines = mutableListOf<Job>()

    /**
     * Goes through all created coroutines and cancels the ones which
    are active.

    MUST be called
     */
    fun destroy() {
        launchedCoroutines.forEach {
            if (it.isActive)
                it.cancel()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}