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
 * in the local Db is always up to date.
 *
 * This is required so that the number of jobs associated with a worker
 * can be reset only by knowing the resource which it is associated with.
 */
interface ICacheable {
    val workerId: Int
}

