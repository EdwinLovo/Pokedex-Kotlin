package com.example.pokedex_kotlin.models

class Pokemon {
    var name:String? = null
    var url:String? = null

    var number: Int = 0
        get() {
            val urlPartes = url!!.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            return Integer.parseInt(urlPartes[urlPartes.size - 1])
        }
}