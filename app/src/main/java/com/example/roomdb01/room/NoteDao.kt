package com.example.roomdb01.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomdb01.room.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(vararg notes : NoteEntity)

    @Query("SELECT * FROM NoteEntity WHERE noteIdx = :noteIdx")
    fun selectNote(noteIdx : Long) : NoteEntity?

    @Query("SELECT * FROM NoteEntity")
    fun selectNotes() : LiveData<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity WHERE noteIdx = :noteIdx")
    fun selectLiveNote(noteIdx : Long): LiveData<NoteEntity>

    @Update
    fun updateNote(vararg notes : NoteEntity)

    @Delete
    fun deleteNotes(vararg note : NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE noteIdx = :noteIdx")
    fun deleteNote(noteIdx : Long)
}