package com.goomer.ps.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val id: Int = 0,
    @JvmField val name: String? = null,
    @JvmField val description: String? = null,
    @JvmField val price: Double = 0.0,
    val imageUrl: String? = null,
) : Parcelable
