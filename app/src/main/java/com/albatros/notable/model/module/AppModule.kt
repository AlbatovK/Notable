package com.albatros.notable.model.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.albatros.notable.model.database.impl.NoteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val dbName = "notes-database"

private fun provideDatabase(context: Context) =
    Room.databaseBuilder(context, NoteDatabase::class.java, dbName)
        .setJournalMode(RoomDatabase.JournalMode.AUTOMATIC).allowMainThreadQueries().build()

private fun provideNoteDao(db: NoteDatabase) =
    db.getNoteDao()

val appModule = module {
    single { provideDatabase(androidContext()) }
    single { provideNoteDao(get()) }
}

