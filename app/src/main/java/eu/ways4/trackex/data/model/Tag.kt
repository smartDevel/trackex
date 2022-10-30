package eu.ways4.trackex.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(val id: String, val name: String): Parcelable