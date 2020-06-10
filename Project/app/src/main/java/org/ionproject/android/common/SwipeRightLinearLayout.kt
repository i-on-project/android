package org.ionproject.android.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * Only passes events to the children which are not swipe right
 */
class SwipeRightLinearLayout(ctx: Context, attrs: AttributeSet) : LinearLayout(ctx, attrs) {

    var startX = 0

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val touchedX = event.rawX.toInt()
        return when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = touchedX
                return super.onInterceptTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = touchedX - startX
                if(deltaX > 0) {
                    return true
                }
                return super.onInterceptTouchEvent(event)
            }
            else -> super.onInterceptTouchEvent(event)
        }
    }
}