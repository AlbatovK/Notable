package com.albatros.notable.model.database.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.database.dao.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}