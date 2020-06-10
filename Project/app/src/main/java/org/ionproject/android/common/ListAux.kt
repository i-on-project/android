package org.ionproject.android.common

fun <T> listOf(count: Int, createFunc: () -> T): List<T> = (0..count).map {
        createFunc()
    }

