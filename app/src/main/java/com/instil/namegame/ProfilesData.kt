package com.instil.namegame

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profiles(
    val results : List<Profile>
) : Parcelable

@Parcelize
data class Profile(
    val firstName: String,
    val headshot: Headshot,
    val lastName: String,
    val id: String
) : Parcelable

@Parcelize
data class Headshot(
    val alt: String,
    val id: String,
    val mimeType : String? = null,
    val type: String,
    val url: String? = null
) : Parcelable //this is a super type

