package com.example.wistcookapp

import android.os.Parcel
import android.os.Parcelable

data class Resep(
    val namaResep: String = "",
    val deskripsi: String = "",
    val kategori: String = "",
    val fotoMasakan: String = "",
    val bahanResep: String = "",
    val caraMasakResep: String = "",
    var isFavorited: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte() // Convert byte to Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(namaResep)
        parcel.writeString(deskripsi)
        parcel.writeString(kategori)
        parcel.writeString(fotoMasakan)
        parcel.writeString(bahanResep)
        parcel.writeString(caraMasakResep)
        parcel.writeByte(if (isFavorited) 1 else 0) // Write Boolean as byte
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Resep> {
        override fun createFromParcel(parcel: Parcel): Resep {
            return Resep(parcel)
        }

        override fun newArray(size: Int): Array<Resep?> {
            return arrayOfNulls(size)
        }
    }
}

