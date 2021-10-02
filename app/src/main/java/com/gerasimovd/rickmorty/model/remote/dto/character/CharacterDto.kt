package com.gerasimovd.rickmorty.model.remote.dto.character

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class CharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: CharacterOriginDto,
    @SerializedName("location") val location: CharacterLocationDto,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episodes: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(CharacterOriginDto::class.java.classLoader)!!,
        parcel.readParcelable(CharacterLocationDto::class.java.classLoader)!!,
        parcel.readString() ?: "",
        parcel.createStringArrayList()?.toList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(species)
        parcel.writeString(type)
        parcel.writeString(gender)
        parcel.writeString(image)
        parcel.writeStringList(episodes)
        parcel.writeString(url)
        parcel.writeString(created)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CharacterDto> {
        override fun createFromParcel(parcel: Parcel): CharacterDto = CharacterDto(parcel)
        override fun newArray(size: Int): Array<CharacterDto?> = arrayOfNulls(size)
    }

}

