package org.ionproject.android.common

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.children
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

/**
 * Finds the [ProgressBar] contained in the ViewGroup, makes it visible.
 * The rest of the views become invisible
 *
 * @throws IllegalStateException if the [this] does not contain a progress bar
 */
fun ViewGroup.startLoading() {
    val progressBar = this.children.find {
        it is ProgressBar
    }
        ?: throw IllegalStateException("$this does not contain a ProgressBar which means it cannot support loading")

    if(progressBar.visibility == View.VISIBLE)
        return

    progressBar.visibility = View.VISIBLE

    this.children.forEach {
        if (it !is ProgressBar) it.visibility = View.GONE
    }
}

/**
 * Finds the [ProgressBar] contained in the ViewGroup, makes it invisible.
 * The rest of the views become Visible
 *
 * @throws IllegalStateException if the [this] does not contain a progress bar
 */
fun ViewGroup.stopLoading() {
    val progressBar = this.children.find {
        it is ProgressBar
    }
        ?: throw IllegalStateException("$this does not contain a ProgressBar which means it cannot support loading")

    if(progressBar.visibility == View.GONE)
        return

    progressBar.visibility = View.GONE

    this.children.forEach {
        if (it !is ProgressBar) it.visibility = View.VISIBLE
    }
}