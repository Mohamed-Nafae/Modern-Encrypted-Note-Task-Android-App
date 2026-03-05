package com.bm.encryptednoteapp.domian.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}