package com.bm.encryptednoteapp.domian.models


enum class NoteTag(
    val displayName: String
) {
    WORK("Work"),
    PERSONAL("Personal"),
    STUDY("Study"),
    IDEAS("Ideas"),
    TASKS("Tasks"),
    PROJECT("Project"),
    REMINDER("Reminder"),
    NOTE("Note");

    companion object {
        fun fromName(name: String?): NoteTag {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: NOTE
        }
    }
}
