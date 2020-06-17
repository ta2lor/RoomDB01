package com.example.roomdb01.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(NoteEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun noteDao(): NoteDao

    companion object{
        private var database : AppDatabase? = null

        private const val ROOM_DB = "note.db"

        fun getDatabase(context: Context): AppDatabase{
            if(database == null){
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, ROOM_DB
                ).fallbackToDestructiveMigration().build()
            }
            return database!!
        }
    }
}