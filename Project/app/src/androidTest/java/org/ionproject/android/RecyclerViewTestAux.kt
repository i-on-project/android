package org.ionproject.android

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import java.util.concurrent.CompletableFuture


fun ViewInteraction.waitForData(): ViewInteraction {
    val completableFuture = CompletableFuture<Boolean>()
    this.perform(AdapterAction {
        completableFuture.complete(true)
    })
    completableFuture.join()
    return this
}

class AdapterObserver(val onUpdate: () -> Unit) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        this.onUpdate()
    }

}

class AdapterAction(private val onUpdate: () -> Unit) : ViewAction {

    override fun getDescription(): String? = null

    override fun getConstraints(): Matcher<View> =
        CoreMatchers.allOf(ViewMatchers.isAssignableFrom(RecyclerView::class.java))

    override fun perform(uiController: UiController, view: View) {
        val adapter = (view as RecyclerView).adapter

        if (adapter != null) {
            if (adapter.itemCount > 0) {
                onUpdate()
            } else {
                adapter.registerAdapterDataObserver(
                    AdapterObserver {
                        onUpdate()
                    })
            }
        }

    }
}
