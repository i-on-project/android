package edu.isel.ion.android.common.model

import java.net.URI

/*
    This type represents a Course in the context of this application.

    This type still contains the most relevant links which came from the
    siren representation which means we don't need to have hard-coded
    uris.

 */
data class Course (
    val acronym : String,
    val name : String,
    val classesUri : URI, //This is the URI used to navigate to this course classes
    val eventsUri : URI //This is the URI used to navigate to this course events
)