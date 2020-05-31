package org.ionproject.android.calendar.jdcalendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

private const val PADDING = 5F
private const val SIZE = 30

class GridItemView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var eventCircleColor = Color.TRANSPARENT
    private var backgroundCircleColor = Color.TRANSPARENT
    private var day = Day(Calendar.getInstance(), true, true, false)

    fun populate(
        day: Day,
        backgroundCircleColor: Int = Color.TRANSPARENT,
        eventCicleColor: Int = Color.TRANSPARENT
    ) {
        this.day = day
        this.backgroundPaint.color = backgroundCircleColor
        this.eventPaint.color = eventCicleColor
        invalidate()
    }

    private val backgroundPaint = Paint().apply { color = backgroundCircleColor }
    private val eventPaint = Paint().apply { color = eventCircleColor }
    private val textPaint = Paint().apply { color = Color.BLACK; textSize = 14f; }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(
            day.value.day.toString(),
            width / 2f,
            height / 2f,
            textPaint
        )
    }
}