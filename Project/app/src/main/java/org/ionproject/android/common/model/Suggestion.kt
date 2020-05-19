package org.ionproject.android.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Suggestion(
    @PrimaryKey val _ID: Int,
    @ColumnInfo(name = "class_name") val className: String?
)

