package com.gis2alk.automashup.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "mashupHistory")
data class MashUpHistoryDTO(
    @PrimaryKey val id: Int? = null,
    val total: Int,
    val completed: Int,
    val timestamp: Date
)
