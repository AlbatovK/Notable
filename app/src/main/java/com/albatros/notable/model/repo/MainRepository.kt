package com.albatros.notable.model.repo

import com.albatros.notable.model.data.Note
import com.albatros.notable.model.data.SubTask
import com.albatros.notable.model.database.dao.NoteDao
import com.albatros.notable.model.database.dao.SubTaskDao

class MainRepository(private val noteDao: NoteDao, private val subTaskDao: SubTaskDao) {

    suspend fun getNotes(): List<Note> = noteDao.getAll()

    suspend fun getNoteById(note_id: Long) = noteDao.getById(note_id)

    suspend fun deleteNote(note: Note) = noteDao.delete(note)

    suspend fun insertNote(note: Note) = noteDao.insert(note)

    suspend fun updateNote(note: Note) = noteDao.update(note)

    suspend fun getTasksByNoteId(note_id: Long) = subTaskDao.getTasksByNoteId(note_id)

    suspend fun deleteTasksByNoteId(note_id: Long) = subTaskDao.deleteTasksByNoteId(note_id)

    suspend fun deleteSubTask(task: SubTask) = subTaskDao.delete(task)

    suspend fun insertSubTask(task: SubTask) = subTaskDao.insert(task)

    suspend fun updateSubTask(task: SubTask) = subTaskDao.update(task)

}
