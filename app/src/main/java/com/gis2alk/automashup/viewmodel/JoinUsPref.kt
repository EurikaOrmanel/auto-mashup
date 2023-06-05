package com.gis2alk.automashup.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

class JoinUsPref(val sharedPreferences: SharedPreferences) : ViewModel() {
    private val userHasJoinedChannelKey = "user_j"
    fun userJoinedCheck(): Boolean {
        return sharedPreferences.getBoolean(userHasJoinedChannelKey, false)
    }

    fun markasJoined() {
        with(sharedPreferences.edit()) {
            putBoolean(userHasJoinedChannelKey, true)
            apply()
        }
    }


}