package org.ionproject.android.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

/**
 * This type represents a Course in the context of this application.
 *
 * This type still contains the most relevant links which came from the
 * siren representation which means we don't need to have hard-coded
 * uris.
 */
@Entity
data class Course(
    @PrimaryKey val id: Int,
    val acronym: String,
    val name: String,
    val classesUri: URI?, //This is the URI used to navigate to this course classes
    val eventsUri: URI?, //This is the URI used to navigate to this course events
    val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable

