package org.ionproject.android.favorites

import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.dto.findByRel
import org.ionproject.android.common.dto.findEmbeddedEntitiesByRel
import org.ionproject.android.common.model.Favorite
import java.net.URI


fun SirenEntity.toFavoriteList(): List<Favorite> {
    val favoritesList = mutableListOf<Favorite>()

    val selfUri = this.links?.findByRel("self")
    if (selfUri != null) {
        entities?.findEmbeddedEntitiesByRel("item")?.forEach {
            val id = it.properties?.get("id") as? String
            val courseId = it.properties?.get("courseId") as? Int
            val courseAcr = it.properties?.get("courseAcr") as? String
            val calendarTerm = it.properties?.get("calendarTerm") as? String
            val sectionURI = it.links?.findByRel("self")

            if (id != null && courseAcr != null && courseId != null && calendarTerm != null && sectionURI != null)
                favoritesList.add(
                    Favorite(
                        id = id,
                        courseId = courseId,
                        courseAcronym = courseAcr,
                        calendarTerm = calendarTerm,
                        selfURI = sectionURI
                    )
                )
            else
                throw MappingFromSirenException("Cannot convert $this to List of Favorites")
        }
    }
    return favoritesList
}