package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.db.ClassCollectionDao
import org.ionproject.android.common.db.ClassSectionDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ClassCollection
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.course_details.toClassCollection
import java.net.URI

class ClassesRepository(
    private val ionWebAPI: IIonWebAPI,
    private val classSectionDao: ClassSectionDao,
    private val classCollectionDao: ClassCollectionDao,
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun forceGetClassSection(classSectionUri: URI): ClassSection =
        withContext(dispatcher) {
            val classSectionLocal = classSectionDao.getClassSectionByUri(classSectionUri)
            val classSectionServer =
                ionWebAPI.getFromURI(classSectionUri, SirenEntity::class.java)
                    .toClassSection()

            if (classSectionLocal == null) {
                val workerId = workerManagerFacade.enqueueWorkForClassSection(
                    classSectionServer,
                    WorkImportance.VERY_IMPORTANT
                )
                classSectionServer.workerId = workerId
                classSectionDao.insertClassSection(classSectionServer)
            } else {
                classSectionServer.workerId = classSectionLocal.workerId
                classSectionDao.updateClassSection(classSectionServer)
                workerManagerFacade.resetWorkerJobsByCacheable(classSectionServer)
            }
            classSectionServer
        }

    suspend fun getClassSection(classSectionUri: URI): ClassSection? =
        withContext(Dispatchers.IO) {
            var classSection = classSectionDao.getClassSectionByUri(classSectionUri)
            if (classSection == null) {
                classSection =
                    ionWebAPI.getFromURI(classSectionUri, SirenEntity::class.java)
                        .toClassSection()
                val workerId = workerManagerFacade.enqueueWorkForClassSection(
                    classSection,
                    WorkImportance.VERY_IMPORTANT
                )
                classSection.workerId = workerId
                classSectionDao.insertClassSection(classSection)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(classSection)
            }
            classSection
        }


    suspend fun getClassCollectionByUri(classesUri: URI): ClassCollection? =
        withContext(Dispatchers.IO) {
            var classCollection = classCollectionDao.getClassCollectionByUri(classesUri)
            if (classCollection == null) {
                classCollection = ionWebAPI.getFromURI(
                    classesUri,
                    SirenEntity::class.java
                ).toClassCollection()
                val workerId = workerManagerFacade.enqueueWorkForClassCollection(
                    classCollection,
                    WorkImportance.IMPORTANT
                )
                classCollection.fields.workerId = workerId
                classCollectionDao.insertClassCollection(classCollection)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(classCollection.fields)
            }
            //Some courses may not have classes in a certain term, in those cases we return an emptyList
            classCollection
        }
}
