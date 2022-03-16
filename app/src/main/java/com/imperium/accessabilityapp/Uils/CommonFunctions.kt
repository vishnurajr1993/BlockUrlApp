package com.imperium.accessabilityapp.Uils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun checkIsTimeBetween(startTime:String,endTime:String):Boolean{
    try {

        val time1: Date = SimpleDateFormat("HH:mm").parse(startTime)
        val calendar1: Calendar = Calendar.getInstance()
        calendar1.setTime(time1)
        calendar1.add(Calendar.DATE, 1)

        val time2: Date = SimpleDateFormat("HH:mm").parse(endTime)
        val calendar2: Calendar = Calendar.getInstance()
        calendar2.setTime(time2)
        calendar2.add(Calendar.DATE, 1)

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:")
        val currentTime = sdf.format(cal.time)
        val d: Date = SimpleDateFormat("HH:mm").parse(currentTime)
        val calendar3: Calendar = Calendar.getInstance()
        calendar3.setTime(d)
        calendar3.add(Calendar.DATE, 1)
        val x: Date = calendar3.getTime()
        return x.after(calendar1.getTime()) && x.before(calendar2.getTime())
    } catch (e: ParseException) {
        return false
        e.printStackTrace()
    }



}