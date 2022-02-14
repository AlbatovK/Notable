package com.albatros.notable.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.albatros.notable.model.data.SubTask

@Dao
interface SubTaskDao : BaseDao<SubTask> {

    @Query("Select * From subtask Where note_id = :this_id")
    suspend fun getTasksByNoteId(this_id: Long): List<SubTask>

    @Query("Delete From subtask Where note_id = :this_id")
    suspend fun deleteTasksByNoteId(this_id: Long)
}