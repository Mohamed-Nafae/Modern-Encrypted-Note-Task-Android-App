package com.bm.encryptednoteapp.domian.use_cases.note_usecases

import com.bm.encryptednoteapp.domian.TaskRepository.NoteRepository
import com.bm.encryptednoteapp.domian.models.NoteWithTasks
import com.bm.encryptednoteapp.domian.util.NoteOrder
import com.bm.encryptednoteapp.domian.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesWithTasks (
    private val noteRepository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder
    ): Flow<List<NoteWithTasks>>
    {

        val notesOrdered = noteRepository.getNotesWithTasks().map { notes ->
            when(noteOrder.orderType){
                is OrderType.Ascending->{
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.note.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.note.createdAt }
                       is NoteOrder.Color ->  notes.sortedBy { it.note.color }
                        is NoteOrder.Gender -> notes.sortedBy { it.note.tag.name }
                    }
                }
                is OrderType.Descending->{
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.note.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.note.createdAt }
                        is NoteOrder.Color -> notes.sortedByDescending { it.note.color }
                        is NoteOrder.Gender -> notes.sortedByDescending { it.note.tag.name }
                    }
                }
            }
        }
        return notesOrdered.map { notes ->
            notes.map { note ->
                note.copy(tasks = note.tasks.sortedBy { it.position })
            }
        }
    }
}
