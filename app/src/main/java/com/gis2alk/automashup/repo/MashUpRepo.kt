package com.gis2alk.automashup.repo

import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.services.MashUpHistoryDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MashUpRepo(private val mashUpHistoryDAO: MashUpHistoryDAO) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun addOne(data: MashUpHistoryDTO) {
        coroutineScope.launch {
            mashUpHistoryDAO.addOne(data)
        }
    }

    suspend fun getLastOne() = withContext(Dispatchers.IO) {
        return@withContext mashUpHistoryDAO.findLastOne()
    }

    fun deleteOne(historyDTO: MashUpHistoryDTO) {
        coroutineScope.launch {
            mashUpHistoryDAO.deleteOne(historyDTO)
        }
    }

    fun increaseCompleted(id: Int) {
        coroutineScope.launch {
            mashUpHistoryDAO.increaseCompleted(id)
        }
    }
}