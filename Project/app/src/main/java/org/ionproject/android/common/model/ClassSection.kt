package org.ionproject.android.common.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.net.URI

/**
 *  This type represents a Class Section in the context of this application.
 */
@Entity(primaryKeys = ["id", "courseAcronym", "calendarTerm"])
data class ClassSection(
    val id: String,
    val courseAcronym: String,
    val calendarTerm: String,
    val classId: Int,
    val calendarURI: URI?,
    val selfUri: URI,
    val upURI: URI,
    override var workerId: Int = 0
) : ICacheable

/**
 * This type represents a class list from
 * a course in a certain calendar term
 */
@Entity
data class Classes(
    val courseId: Int,
    val calendarTerm: String,
    val id: Int,
    @PrimaryKey val selfUri: URI,
    val upUri: URI, // represents the list of classes in which this classes obj is contained,
    override var workerId: Int = 0
) : ICacheable {
    override fun toString() = calendarTerm
}

/**
 * This type represents the information that is common
 * among all class summaries of a specific course and calendar term
 */
@Entity
data class ClassCollectionFields(
    val courseId: Int,
    val courseAcronym: String,
    val calendarTerm: String,
    val calendarURI: URI?,
    val selfUri: URI,
    // The sole purpose of this property is to establish a relation between ClassCollection and ClassSummary
    @PrimaryKey val courseCalendarTerm: String = "$courseAcronym$calendarTerm",
    override var workerId: Int = 0
) : ICacheable

/**
 * This type represents a collection of ClassSummaries
 */
data class ClassCollection(
    @Embedded val fields: ClassCollectionFields,
    @Relation(
        entityColumn = "courseCalendarTerm",
        parentColumn = "courseCalendarTerm",
        entity = ClassSummary::class
    )
    val classes: List<ClassSummary>
)

@Entity(primaryKeys = ["id", "courseAcronym", "calendarTerm"])
data class ClassSummary(
    val id: String,
    val courseAcronym: String,
    val calendarTerm: String,
    val detailsUri: URI,
    override var workerId: Int = 0,
    // The sole purpose of this property is to establish a relation between ClassCollection and ClassSummary
    val courseCalendarTerm: String = "$courseAcronym$calendarTerm"
) : ICacheable {
    override fun equals(other: Any?): Boolean {
        val otherClassSummary = other as ClassSummary
        if (id == otherClassSummary.id && courseAcronym == otherClassSummary.courseAcronym && calendarTerm == otherClassSummary.calendarTerm
            && detailsUri == otherClassSummary.detailsUri
        ) {
            return true
        }
        return false
    }

    /**
     * Generated by the IDE
     */
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + courseAcronym.hashCode()
        result = 31 * result + calendarTerm.hashCode()
        result = 31 * result + detailsUri.hashCode()
        result = 31 * result + workerId
        return result
    }
}






