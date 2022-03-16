package com.imperium.accessabilityapp.Uils

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject



class SharedPreference @Inject constructor(private val preferences: SharedPreferences){
    fun getIsScheduled() = preferences.getBoolean(IS_SCHEDULE_CREATED, false)

    fun setIsScheduled(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(IS_SCHEDULE_CREATED, value)
        editor.apply()
    }

    fun setStartTime(time_12: String,time_24:String) {
        val editor = preferences.edit()
        editor.putString(START_TIME, time_12)
        editor.putString(START_TIME_24, time_24)
        editor.apply()
    }
    fun getStartTime12() = preferences.getString(START_TIME, "")
    fun getStartTime24() = preferences.getString(START_TIME_24, "")

    fun setEndTime(time_12: String, time_24:String) {
        val editor = preferences.edit()
        editor.putString(END_TIME, time_12)
        editor.putString(END_TIME_24, time_24)
        editor.apply()
    }
    fun getEndTime12() = preferences.getString(END_TIME, "")
    fun getEndTime24() = preferences.getString(END_TIME_24, "")


    fun setWhiteList(value:Boolean){
        val editor = preferences.edit()
        editor.putBoolean(WHITELIST, value)
        editor.apply()
    }

    fun getBlockedState()=preferences.getBoolean(WHITELIST, false)
}