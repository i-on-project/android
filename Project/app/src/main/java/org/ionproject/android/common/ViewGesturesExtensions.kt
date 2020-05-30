package org.ionproject.android.common

import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * Extensions functions that add gestures to views. The gesture
 * also adds animation to the view so that it follows the recommendations from material.io:
 * https://material.io/design/interaction/gestures.html#properties
 */

fun View.addSwipeRightGesture(
    onSwipeRight: () -> Unit
) {
    setOnTouchListener(object : View.OnTouchListener {

        var startX = 0
        var velocityTracker = VelocityTracker.obtain()
        val ALPHA_THRESHOLD = 0.1F // When this alpha is reached parameter functions are called
        val SWIPE_THRESHOLD =
            500 // The distance swiped at which the view turns completely transparent

        override fun onTouch(view: View, event: MotionEvent): Boolean {

            val touchedX = event.rawX.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> { // USER TOUCHES SCREEN
                    startX = touchedX
                }
                MotionEvent.ACTION_MOVE -> { // USER MOVES FINGER WHILE TOUCHING SCREEN
                    velocityTracker.addMovement(event) // Obtain speed of movement
                    velocityTracker.computeCurrentVelocity(1)

                    val deltaX = touchedX - startX

                    if (deltaX > 0) { // Has user moved finger to right?
                        if (velocityTracker.xVelocity >= 3) // Was it a fast swipe?
                            view.alpha = 0F
                        else {
                            val layoutParams =
                                view.layoutParams as FrameLayout.LayoutParams
                            layoutParams.marginStart = deltaX
                            layoutParams.marginEnd = -deltaX
                            view.layoutParams = layoutParams // Update view position
                            view.alpha =
                                (-abs(deltaX) + SWIPE_THRESHOLD).toFloat() / SWIPE_THRESHOLD // Update view opacity
                        }
                    }
                }
                MotionEvent.ACTION_UP -> { //USER LIFTS FINGER FROM SCREEN
                    if (view.alpha <= ALPHA_THRESHOLD) {
                        onSwipeRight()
                    } else { // User lifts finger but hasn't reached the threshold so return view to starting position
                        val layoutParams =
                            view.layoutParams as FrameLayout.LayoutParams
                        layoutParams.marginStart = 0
                        layoutParams.marginEnd = 0
                        view.layoutParams = layoutParams
                        view.alpha = 1F
                    }
                }
            }
            view.invalidate()
            return true
        }
    })
}

fun View.addSwipeUpGesture(
    onSwipeUp: () -> Unit
) {
    setOnTouchListener(object : View.OnTouchListener {

        var startY = 0
        var velocityTracker = VelocityTracker.obtain()
        val ALPHA_THRESHOLD = 0.1F // When this alpha is reached parameter functions are called
        val SWIPE_THRESHOLD =
            500 // The distance swiped at which the view turns completely transparent

        override fun onTouch(view: View, event: MotionEvent): Boolean {

            val touchedY = event.rawY.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> { // USER TOUCHES SCREEN
                    startY = touchedY
                }
                MotionEvent.ACTION_MOVE -> { // USER MOVES FINGER WHILE TOUCHING SCREEN
                    velocityTracker.addMovement(event) // Obtain speed of movement
                    velocityTracker.computeCurrentVelocity(1)

                    val deltaY = touchedY - startY

                    if (deltaY < 0) { // Has user moved finger up?
                        if (velocityTracker.yVelocity >= 3) // Was it a fast swipe?
                            view.alpha = 0F
                        else {
                            val layoutParams =
                                view.layoutParams as FrameLayout.LayoutParams
                            layoutParams.bottomMargin = -deltaY
                            layoutParams.topMargin = deltaY
                            view.layoutParams = layoutParams // Update view position
                            view.alpha =
                                (-abs(deltaY) + SWIPE_THRESHOLD).toFloat() / SWIPE_THRESHOLD // Update view opacity
                        }
                    }
                }
                MotionEvent.ACTION_UP -> { //USER LIFTS FINGER FROM SCREEN
                    if (view.alpha <= ALPHA_THRESHOLD) {
                        onSwipeUp()
                    } else { // User lifts finger but hasn't reached the threshold so return view to starting position
                        val layoutParams =
                            view.layoutParams as FrameLayout.LayoutParams
                        layoutParams.bottomMargin = 0
                        layoutParams.topMargin = 0
                        view.layoutParams = layoutParams
                        view.alpha = 1F
                    }
                }
            }
            view.invalidate()
            return true
        }
    })
}


