package com.gis2alk.automashup.viewmodel

import androidx.lifecycle.ViewModel
import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.repo.MashUpRepo
import com.gis2alk.automashup.services.MashUpHistoryDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MashUpHIstoryViewModel(private val mashUpHistoryDAO: MashUpHistoryDAO) : ViewModel() {
    val allHistory = mashUpHistoryDAO.getAll()
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val mashUpRepo = MashUpRepo(mashUpHistoryDAO)
    fun addOne(data: MashUpHistoryDTO) {
        coroutineScope.launch {
            mashUpRepo.addOne(data)
        }
    }

    fun increaseOne(id: Int) {
        coroutineScope.launch {
            mashUpRepo.increaseCompleted(id)
        }
    }
}