package com.goomer.ps.domain.model

class MenuItem {
    @JvmField
    var id: Int = 0
    @JvmField
    var name: String? = null
    @JvmField
    var description: String? = null
    @JvmField
    var price: Double = 0.0
    @JvmField
    var imageUrl: String? = null
}