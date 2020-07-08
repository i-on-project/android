package org.ionproject.android.common.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

/**
 * This type represents a very simplified version of the
 * JsonHome resource
 */
@Entity
data class Root(
    val programmesUri: URI,
    val calendarTermsUri: URI,
    @PrimaryKey val rootUri: URI = URI("/"),
    override var workerId: Int = 0
) : Parcelable, ICacheable {

    // ------------------ Parcelable methods -----------------------
    constructor(parcel: Parcel) : this(
        URI(parcel.readString() ?: ""),
        URI(parcel.readString() ?: "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(programmesUri.toString())
        parcel.writeString(calendarTermsUri.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Root> {
        override fun createFromParcel(parcel: Parcel): Root {
            return Root(parcel)
        }

        override fun newArray(size: Int): Array<Root?> {
            return arrayOfNulls(size)
        }
    }
}