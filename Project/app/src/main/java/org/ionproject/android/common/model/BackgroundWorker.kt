package org.ionproject.android.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BackgroundWorker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val numberOfJobs: Int, // Represents the number of work a Worker has left to perform
    var currNumberOfJobs: Int = numberOfJobs // Current number of jobs
) {

    fun decrementNumberOfJobs() {
        --currNumberOfJobs
    }

    fun resetNumberOfJobs() {
        currNumberOfJobs = numberOfJobs
    }
}

/**
 * Implies that the type is managed by a worker
 * who will ensure that the resource that is
 * in the Db is always up to date
 */
interface ICacheable {
    val workerId: Int
}

