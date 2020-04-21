package org.ionproject.android.calendar.JDCalendar

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent


abstract class GestureListener : SimpleOnGestureListener() {

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var result = false
        try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(
                        velocityX
                    ) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    result = true
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return result
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    abstract fun onSwipeRight()

    abstract fun onSwipeLeft()
}