package org.ionproject.android.favorites

import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.dto.findByRel
import org.ionproject.android.common.dto.findEmbeddedEntitiesByRel
import org.ionproject.android.common.model.Favorite


fun SirenEntity.toFavoriteList(): List<Favorite> {
    val favoritesList = mutableListOf<Favorite>()

    val selfUri = this.links?.findByRel("self")
    if (selfUri != null) {
        entities?.findEmbeddedEntitiesByRel("item")?.forEach {
            val id = it.properties?.get("id") as? String
            val courseId = it.properties?.get("courseId") as? Int
            val courseAcr = it.properties?.get("courseAcr") as? String
            val calendarTerm = it.properties?.get("calendarTerm") as? String

            if (id != null && courseAcr != null && courseId != null && calendarTerm != null)
                favoritesList.add(
                    Favorite(
                        id = id,
                        courseId = courseId,
                        courseAcronym = courseAcr,
                        calendarTerm = calendarTerm,
                        selfURI = selfUri
                    )
                )
            else
                throw MappingFromSirenException("Cannot convert $this to List of Favorites")
        }
    }
    return favoritesList
}