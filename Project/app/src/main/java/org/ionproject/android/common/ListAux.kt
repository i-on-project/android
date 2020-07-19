package org.ionproject.android.common


/**
 * Returns a list of items created by [createFunction]
 */
fun <T> listOf(count: Int, createFunction: () -> T): List<T> = (0..count).map {
    createFunction()
}

/**
 * Maps [this] into a new List by applying the [transform] function through
 * each element. Returns null if the transform function returns null.
 */
fun <T, R> List<T>.mapOrNull(transform: (T) -> R?): List<R>? {
    val newList = mutableListOf<R>()
    for (item in this) {
        val transformed = transform(item)
        if (transformed == null)
            return null
        newList.add(transformed)
    }
    return newList
}

