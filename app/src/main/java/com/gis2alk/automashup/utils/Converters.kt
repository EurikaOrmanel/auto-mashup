package com.gis2alk.automashup.utils

import androidx.room.TypeConverter
import java.util.*


class Converters {
    @TypeConverter
    fun toDateTime(input: Long?): Date? = input?.let { Date(it) };

    @TypeConverter
    fun fromDateTime(data: Date?): Long? = data?.time?.toLong()
}