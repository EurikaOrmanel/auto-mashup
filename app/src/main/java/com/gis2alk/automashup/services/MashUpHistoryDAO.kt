package com.gis2alk.automashup.services

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gis2alk.automashup.models.MashUpHistoryDTO

@Dao
interface MashUpHistoryDAO {
    @Insert
    fun addOne(mashUpHistoryDTO: MashUpHistoryDTO)

    @Delete
    fun deleteOne(mashUpHistoryDTO: MashUpHistoryDTO)

    @Query("SELECT * FROM mashupHistory")
    fun getAll(): LiveData<List<MashUpHistoryDTO>>

    @Query("UPDATE mashupHistory SET completed = completed +1 WHERE id= :id")
    fun increaseCompleted(id: Int)

    @Query("SELECT * FROM mashupHistory ORDER BY id ASC LIMIT 1 ")
    fun findLastOne(): MashUpHistoryDTO
}