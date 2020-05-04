package org.ionproject.android.search

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import org.ionproject.android.common.IonApplication
import java.util.*

class SearchCustomSuggestionsProvider : ContentProvider() {

    private val columns = arrayOf(
        "_id", // Must include this column
        SearchManager.SUGGEST_COLUMN_TEXT_1, // The string that is presented as a suggestion
        SearchManager.SUGGEST_COLUMN_INTENT_DATA // Data that is used when forming the suggestion's intent
    )

    override fun onCreate(): Boolean {
        return true
    }

    /**
     * Query the database for all suggestions started with [query].
     *
     * @return [cursor] All suggestions retrieved from the database.
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val query = uri.lastPathSegment?.toLowerCase(Locale.ROOT) ?: ""
        val suggestions = IonApplication.db.suggestionDAO().getSuggestions("$query%")

        val cursor = MatrixCursor(columns)

        for (suggestion in suggestions)
            cursor.addRow(arrayOf(suggestion._ID, suggestion.className, suggestion.className))

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }
}