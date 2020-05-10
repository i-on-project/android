package org.ionproject.android.common.db

import androidx.room.TypeConverter
import java.net.URI

class URIConverter {

    @TypeConverter
    fun fromString(uri: String): URI {
        return URI(uri)
    }

    @TypeConverter
    fun toString(uri: URI): String {
        return uri.toString()
    }

}
