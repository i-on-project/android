package org.ionproject.android.common.db

import androidx.room.TypeConverter
import org.ionproject.android.common.model.ResourceType
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


class ResourceTypeConverter {

    @TypeConverter
    fun fromString(resourceType: String): ResourceType {
        return ResourceType.valueOf(resourceType)
    }

    @TypeConverter
    fun toString(resourceType: ResourceType): String {
        return resourceType.name
    }
}