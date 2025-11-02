package com.example.myburgerapp.models

import android.os.Parcel
import android.os.Parcelable


data class Burger(
    val id: Int,
    val name: String,
    val description: String,
    val basePrice: Double
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(basePrice)
    }


    override fun describeContents(): Int = 0


    companion object CREATOR : Parcelable.Creator<Burger> {
        override fun createFromParcel(parcel: Parcel): Burger = Burger(parcel)
        override fun newArray(size: Int): Array<Burger?> = arrayOfNulls(size)
    }
}
