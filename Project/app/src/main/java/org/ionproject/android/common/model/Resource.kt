package org.ionproject.android.common.model

import java.net.URI

/**
 * This is an interface so that the models can
 * extend from another class and implement other interfaces if necessary
 */
interface IResource {
    val id: Int
    val type: ResourceType
    val selfUri: URI
}

/**
 * This enum represents the resources
 * from the WebAPI
 */
enum class ResourceType{
    PROGRAMME,
    PROGRAMME_SUMMARY,
    COURSE,
    COURSE_SUMMARY,
    CLASS,
    CLASS_SUMMARY,
    CALENDAR_TERM
}