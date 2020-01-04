package org.wit.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Parcelize
data class HillfortModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var visited: Boolean = false,
    var dateVisited: String = "",
    var rating: Short = 0,
    var notes: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable