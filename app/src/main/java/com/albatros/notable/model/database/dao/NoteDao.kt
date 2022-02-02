package com.albatros.notable.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.albatros.notable.model.data.Note

@Dao
interface NoteDao : BaseDao<Note> {

    @Query("Select * From note Order By date Desc")
    suspend fun getAll(): List<Note>
}