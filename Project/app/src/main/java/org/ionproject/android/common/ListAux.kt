package org.ionproject.android.common


/**
 * Returns a list of items created by [createFunction]
 */
fun <T> listOf(count: Int, createFunction: () -> T): List<T> = (0..count).map {
        createFunction()
    }

