package com.bm.encryptednoteapp.domian.util

sealed class NoteOrder(val orderType: OrderType){
    class Title(orderType: OrderType):NoteOrder(orderType)
    class Date(orderType: OrderType):NoteOrder(orderType)
    class Color(orderType: OrderType):NoteOrder(orderType)
    class Gender(orderType: OrderType):NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when(this) {
            is Color -> Color(orderType)
            is Date -> Date(orderType)
            is Title -> Title(orderType)
            is Gender -> Gender(orderType)
            else -> throw IllegalStateException("Unknown NoteOrder type")
        }
    }
}


