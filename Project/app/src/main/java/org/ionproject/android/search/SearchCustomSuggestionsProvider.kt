package org.ionproject.android.search

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.db.AppDatabase
import org.ionproject.android.common.db.SuggestionDAO

private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI(
        SearchCustomSuggestionsProvider.AUTHORITY,
        SearchCustomSuggestionsProvider.PATH,
        1
    )
}

class SearchCustomSuggestionsProvider : ContentProvider() {

    private lateinit var db: AppDatabase
    private lateinit var suggestionsDAO: SuggestionDAO

    companion object {
        const val AUTHORITY = "org.ionproject.android.search.SearchCustomSuggestionsProvider"
        const val PATH = "suggestions"
    }

    override fun onCreate(): Boolean {
        db = Room
            .databaseBuilder(context!!, AppDatabase::class.java, "database-name")
            .build()
        suggestionsDAO = db.suggestionDAO()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var localSortOrder: String = sortOrder ?: ""
        var localSelection: String = selection ?: ""

        when (sUriMatcher.match(uri)) {
            1 -> {
                if (localSortOrder.isEmpty()) {
                    localSortOrder = "_ID ASC"
                }
            }
            else -> throw IllegalArgumentException("Invalid URI")
        }
        TODO("Not uet implemented")
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
        return "vnd.org.ionproject.android.dir/$AUTHORITY.$PATH"
    }
}