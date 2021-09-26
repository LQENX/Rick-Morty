package com.gerasimovd.rickmorty.model.server.dto.character

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class LocationDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<LocationDto> {
        override fun createFromParcel(parcel: Parcel): LocationDto = LocationDto(parcel)
        override fun newArray(size: Int): Array<LocationDto?> = arrayOfNulls(size)
    }
}
