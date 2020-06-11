package org.ionproject.android.common

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View

fun View.addGradientBackground(
    bottomColor: Int = Color.parseColor("#f8f2f1"),
    topColor: Int = Color.WHITE
) {
    val gradientDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(
            topColor,
            bottomColor
        )
    )
    gradientDrawable.cornerRadius = 0f
    this.background = gradientDrawable
}