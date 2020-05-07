package org.ionproject.android.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity
data class BackgroundWorker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val numberOfJobs: Int, // Represents the number of work a Worker has left to perform
    var currNumberOfJobs: Int = numberOfJobs, // Current number of jobs
    val resourceId: Int, // Is the Id of the resource which the worker has to manage (Could be of any entity)
    val resourceType: ResourceType
) {

    fun decrementNumberOfJobs() {
        --currNumberOfJobs
    }

    fun resetNumberOfJobs() {
        currNumberOfJobs = numberOfJobs
    }

}

