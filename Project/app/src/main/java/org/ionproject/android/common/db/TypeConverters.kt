package org.ionproject.android.common.db

import androidx.room.TypeConverter
import java.net.URI

class URIConverter {

    @TypeConverter
    fun fromString(uri: String) = URI(uri)

    @TypeConverter
    fun toString(uri: URI?) = uri?.toString() ?: ""
}
