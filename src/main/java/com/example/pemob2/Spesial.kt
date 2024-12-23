package com.example.wistcookapp

import android.os.Parcel
import android.os.Parcelable

data class Spesial(
    val name: String,
    val description: String,
    val imageResId: Int,
    val ingredients: String,
    val steps: String,
    var isFavorited: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte() // Read the isFavorited boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(imageResId)
        parcel.writeString(ingredients)
        parcel.writeString(steps)
        parcel.writeByte(if (isFavorited) 1 else 0) // Write the isFavorited boolean
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Spesial> {
        override fun createFromParcel(parcel: Parcel): Spesial {
            return Spesial(parcel)
        }

        override fun newArray(size: Int): Array<Spesial?> {
            return arrayOfNulls(size)
        }
    }
}
