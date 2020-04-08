package org.ionproject.android.common.model

import java.net.URI

/*
    This type represents a Course in the context of this application.

    This type still contains the most relevant links which came from the
    siren representation which means we don't need to have hard-coded
    uris.

 */
data class Course(
    val acronym: String,
    val name: String,
    val classesUri: URI?, //This is the URI used to navigate to this course classes
    val eventsUri: URI? //This is the URI used to navigate to this course events
)


/**

This type represents the summary of a [Course] ,
which means it does not include all of its details

 */
data class CourseSummary(
    val acronym: String,
    val detailsUri: URI
)