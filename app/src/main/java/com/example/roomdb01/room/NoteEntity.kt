package com.example.roomdb01.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoteEntity")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var noteIdx : Long? = null,
    var noteTitle : String,
    var noteContent: String,
    var noteImage: String?
)