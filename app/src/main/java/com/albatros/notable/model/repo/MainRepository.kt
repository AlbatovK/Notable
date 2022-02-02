package com.albatros.notable.model.repo

import com.albatros.notable.model.data.Note
import com.albatros.notable.model.database.dao.NoteDao

class MainRepository(private val dao: NoteDao) {

    suspend fun getNotes(): List<Note> = dao.getAll()

    suspend fun deleteNote(note: Note) = dao.delete(note)

    suspend fun insertNote(note: Note) = dao.insert(note)

    suspend fun updateNote(note: Note) = dao.update(note)
}
