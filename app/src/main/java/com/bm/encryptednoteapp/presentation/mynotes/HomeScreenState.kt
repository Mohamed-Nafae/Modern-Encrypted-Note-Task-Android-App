package com.bm.encryptednoteapp.presentation.mynotes

import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import com.bm.encryptednoteapp.domian.util.NoteOrder
import com.bm.encryptednoteapp.domian.util.OrderType


data class HomeScreenState(
    val notes : List<NoteWithTasks> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible : Boolean = false,
    val searchQuery: String = "",
    val selectedNote: NoteWithTasks? = null
)
