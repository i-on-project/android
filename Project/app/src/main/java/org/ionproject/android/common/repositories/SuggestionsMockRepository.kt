package org.ionproject.android.common.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.AppDatabase
import org.ionproject.android.common.db.Suggestion

class SuggestionsMockRepository(private val db: AppDatabase) {

    fun getSuggestions() = db.suggestionDAO().getAllSuggestions()

    suspend fun insertSuggestionMocks() {
        val suggestionsToInsert: Array<Suggestion> = arrayOf(
            Suggestion(1, "LI11D-PG"),
            Suggestion(2, "LI12D-ALGA"),
            Suggestion(3, "LI1N-LSD")
        )

        withContext(Dispatchers.IO) {
            db.suggestionDAO().insertAll(*suggestionsToInsert)
        }
    }
}