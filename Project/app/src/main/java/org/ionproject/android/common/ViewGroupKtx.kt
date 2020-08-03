package org.ionproject.android.common

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.children

/**
 * Finds the [ProgressBar] contained in the ViewGroup, makes it visible.
 * The rest of the views become invisible
 *
 * To use this method the [ViewGroup] must contain a progress bar otherwise
 * and exception is thrown
 *
 * @throws IllegalStateException if the [this] does not contain a progress bar
 */
fun ViewGroup.startLoading() {
    val progressBar = this.children.find {
        it is ProgressBar
    }
        ?: throw IllegalStateException("$this does not contain a ProgressBar which means it cannot support loading")

    progressBar.visibility = View.VISIBLE

    this.children.forEach {
        if (it !is ProgressBar) it.visibility = View.GONE
    }
}

/**
 * Finds the [ProgressBar] contained in the ViewGroup, makes it invisible.
 * The rest of the views become Visible
 *
 * To use this method the [ViewGroup] must contain a progress bar otherwise
 * and exception is thrown
 *
 * @throws IllegalStateException if the [this] does not contain a progress bar
 */
fun ViewGroup.stopLoading() {
    val progressBar = this.children.find {
        it is ProgressBar
    }
        ?: throw IllegalStateException("$this does not contain a ProgressBar which means it cannot support loading")

    // Since this method is usually called within an observer, it may be called multiple times, which would lead to multiple children traversals
    if (progressBar.visibility == View.GONE)
        return

    progressBar.visibility = View.GONE

    this.children.forEach {
        if (it !is ProgressBar) it.visibility = View.VISIBLE
    }
}