package com.gis2alk.automashup.services

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.utils.Converters


@Database(entities = [MashUpHistoryDTO::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class RoomDBHelper : RoomDatabase() {
    abstract fun mashupHistoryDAO(): MashUpHistoryDAO
}