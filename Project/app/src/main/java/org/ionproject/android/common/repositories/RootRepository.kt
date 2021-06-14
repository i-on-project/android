package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.RootDao
import org.ionproject.android.common.dto.JsonHome
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.JSON_HOME_MEDIA_TYPE
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import java.net.URI


/**
 * Used to check the existence of i-on core Web API root endpoints. It does so by
 * checking the Root resource.
 */
class RootRepository(
    private val rootResourceDao: RootDao,
    private val ionWebAPI: IIonWebAPI,
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getJsonHome(uri: URI) =
        withContext(dispatcher) {
            // Verify if root resource is stored locally
            var rootResource = rootResourceDao.getRootResource()

            if (rootResource == null) {
                rootResource =
                    ionWebAPI.getFromURI(URI("$uri/api/"), JsonHome::class.java, JSON_HOME_MEDIA_TYPE)
                        .toRoot()
                if (rootResource != null) {
                    // Save root resource into local database and create a worker for it
                    val workerId = workerManagerFacade.enqueueWorkForRootResource(
                        WorkImportance.VERY_IMPORTANT,
                        rootResource
                    )
                    rootResource.workerId = workerId
                    rootResourceDao.insertRootResource(rootResource)
                }
            } else {
                // Reset the number of jobs for it's worker
                workerManagerFacade.resetWorkerJobsByCacheable(rootResource)
            }
            rootResource
        }

}