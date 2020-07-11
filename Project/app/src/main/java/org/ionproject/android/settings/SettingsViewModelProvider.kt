package org.ionproject.android.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class SettingsViewModelProvider : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            SettingsViewModel::class.java -> SettingsViewModel(
                IonApplication.preferences,
                IonApplication.calendarTermRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
}