package org.ionproject.android.common

import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains

/**
 * Replaces [currentView] from ViewGroup for [newView]
 *
 * @throws IllegalArgumentException if current view does not belong to ViewGroup or if newView is already contained in a ViewGroup
 */
fun ViewGroup.replaceView(currentView: View, newView: View) {
    if (!this.contains(currentView)) throw IllegalArgumentException("$currentView does not belong to ViewGroup $this!")
    if (newView.parent != null) throw IllegalArgumentException("$newView is already contained in a ViewGroup. You must call removeView() on the newView parent first.")
    val idxView = this.indexOfChild(currentView)
    this.removeView(currentView)
    this.addView(newView, idxView)
}