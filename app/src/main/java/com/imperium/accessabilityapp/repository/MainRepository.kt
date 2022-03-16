package com.imperium.accessabilityapp.repository

import com.imperium.accessabilityapp.Model.TimingModel
import com.imperium.accessabilityapp.Uils.DataState
import com.imperium.accessabilityapp.Uils.SharedPreference
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val sharedPref: SharedPreference,

    ){


    fun isEventCreated(): Boolean =sharedPref.getIsScheduled()


    fun addSchedule(startTime:String,endTime:String,startTime24:String,endTime24:String){
        sharedPref.setIsScheduled(true)
        sharedPref.setStartTime(startTime,startTime24)
        sharedPref.setEndTime(endTime,endTime24)
    }

    fun deleteSchedule(){
        sharedPref.setIsScheduled(false)
        sharedPref.setStartTime("","")
        sharedPref.setEndTime("","")
    }

    fun setSwitchState(value:Boolean){
        sharedPref.setWhiteList(value)
    }
    fun getSwitchState()=sharedPref.getBlockedState()

    fun getStartTime24()=sharedPref.getStartTime24()

    fun getEndTime24()=sharedPref.getEndTime24()

    fun getScheduledDates():DataState<TimingModel>{
        val startTime=sharedPref.getStartTime12()
        val endTime=sharedPref.getEndTime12()
        DataState.Loading
        return if (startTime!=null && endTime!=null){
            DataState.Success(data = TimingModel(startTime = startTime, endTime = endTime))
        }else{
            DataState.Error(exception = "No dates Found")
        }

    }
}