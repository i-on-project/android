package org.ionproject.android.common

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import org.ionproject.android.R

fun NavController.navigateToHome() {
    while (popBackStack()) {
    }
    val navigateUp = NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_fade_enter)
        .build()
    navigate(R.id.navigation_home, null, navigateUp)
}