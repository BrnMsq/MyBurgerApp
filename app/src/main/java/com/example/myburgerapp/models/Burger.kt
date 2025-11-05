package com.example.myburgerapp.modelsimport

import android.os.Parcel


import android.os.Parcelable
import androidx.annotation.DrawableRes


data class Burger(
    val id: Int,
    val name: String,
    val description: String,
    val basePrice: Double,
    @DrawableRes val image: Int
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    // 3. Escribimos el nuevo campo 'image' en el parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(basePrice)
        parcel.writeInt(image)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Burger> {
        override fun createFromParcel(parcel: Parcel): Burger = Burger(parcel)
        override fun newArray(size: Int): Array<Burger?> = arrayOfNulls(size)
    }
}
