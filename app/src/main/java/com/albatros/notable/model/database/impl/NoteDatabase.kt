package com.albatros.notable.model.database.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.data.SubTask
import com.albatros.notable.model.database.dao.NoteDao
import com.albatros.notable.model.database.dao.SubTaskDao

@Database(entities = [Note::class, SubTask::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    abstract fun getSubTaskDao(): SubTaskDao
}