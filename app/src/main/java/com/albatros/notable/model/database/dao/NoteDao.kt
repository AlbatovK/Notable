package com.albatros.notable.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.albatros.notable.model.data.Note

@Dao
interface NoteDao : BaseDao<Note> {

    @Query("Select * From note Order By finished Asc, date Desc")
    suspend fun getAll(): List<Note>

    @Query("Select * From note Where id = :noteId Order By finished Asc, date Desc")
    suspend fun getById(noteId: Long): List<Note>
}