package com.albatros.notable.domain

import androidx.room.TypeConverter
import java.util.Date

interface Converter<T, V> {
    fun convertFrom(value: T): V
    fun convertTo(value: V): T
}

object DateConverter : Converter<Long, Date> {

    @TypeConverter
    override fun convertFrom(value: Long): Date = Date(value)

    @TypeConverter
    override fun convertTo(value: Date) = value.time

}