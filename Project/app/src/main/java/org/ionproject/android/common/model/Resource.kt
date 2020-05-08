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
 *
 * Needs to exist so that a worker can be obtained without knowing its id
 * this way it can be obtained by using the resourceId + resourceType
 * The resourceId is not enough by itself because there might be resource with the same
 * id from different tables which means the query would return multiple workers.
 */
enum class ResourceType {
    PROGRAMME,
    PROGRAMME_SUMMARY,
    PROGRAMME_OFFER,
    COURSE,
    COURSE_SUMMARY,
    CLASS,
    CLASS_SUMMARY,
    CALENDAR_TERM
}