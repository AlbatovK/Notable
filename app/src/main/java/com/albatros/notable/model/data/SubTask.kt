package com.albatros.notable.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtask")
data class SubTask(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "finished")
    var finished: Boolean = false,
    @ColumnInfo(name = "note_id")
    var note_id: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
)