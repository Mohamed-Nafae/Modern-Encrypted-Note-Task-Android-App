package com.bm.encryptednoteapp.Common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.bm.encryptednoteapp.domian.models.NoteTag

public class Converters {
    @TypeConverter
    fun fromNoteTag(tag: NoteTag): String = tag.name
    @TypeConverter
    fun toTag(tagName:String): NoteTag = NoteTag.fromName(tagName)
}