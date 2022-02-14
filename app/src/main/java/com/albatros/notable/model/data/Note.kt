package com.albatros.notable.model.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.albatros.notable.domain.DateConverter
import com.albatros.notable.domain.getRandomColor
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "note")
@TypeConverters(DateConverter::class)
data class Note(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "data")
    var data: String = "",
    @ColumnInfo(name = "date")
    var date: Date = Date(),
    @ColumnInfo(name = "color")
    var color: Int = getRandomColor(),
    @ColumnInfo(name = "finished")
    var finished: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
) : Parcelable