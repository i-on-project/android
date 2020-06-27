package org.ionproject.android.common.db

import androidx.room.*
import org.ionproject.android.common.model.ClassCollection
import org.ionproject.android.common.model.ClassCollectionFields
import org.ionproject.android.common.model.ClassSummary
import java.net.URI

@Dao
abstract class ClassCollectionDao {

    @Transaction
    @Query("SELECT * FROM ClassCollectionFields WHERE selfUri = :uri ")
    abstract suspend fun getClassCollectionByUri(uri: URI): ClassCollection?

    suspend fun insertClassCollection(classCollection: ClassCollection) {
        insertClassCollectionFields(classCollection.fields)
        insertClassSummaries(classCollection.classes)
    }
    suspend fun deleteClassCollection(classCollection: ClassCollection) {
        deleteClassCollectionFields(classCollection.fields)
        deleteClassSummaries(classCollection.classes)

    }
    suspend fun updateClassCollection(classCollection: ClassCollection) {
        updateClassCollectionFields(classCollection.fields)
        updateClassSummaries(classCollection.classes)
    }

    @Query("DELETE FROM ClassCollectionFields WHERE selfUri = :uri")
    abstract suspend fun deleteClassCollectionFieldsByUri(uri: URI)

    @Insert
    abstract suspend fun insertClassCollectionFields(classCollectionFields: ClassCollectionFields)

    @Delete
    abstract suspend fun deleteClassCollectionFields(classCollectionFields: ClassCollectionFields)

    @Update
    abstract suspend fun updateClassCollectionFields(classCollectionFields: ClassCollectionFields)

    @Insert
    abstract suspend fun insertClassSummaries(classSummaries: List<ClassSummary>)

    @Delete
    abstract suspend fun deleteClassSummaries(classSummaries: List<ClassSummary>)

    @Query("DELETE FROM ClassSummary WHERE courseAcronym = :courseAcronym AND calendarTerm = :calendarTerm ")
    abstract suspend fun deleteClassSummariesByCourseAndCalendarTerm(
        courseAcronym: String,
        calendarTerm: String
    )

    @Update
    abstract suspend fun updateClassSummaries(classSummaries: List<ClassSummary>)
}