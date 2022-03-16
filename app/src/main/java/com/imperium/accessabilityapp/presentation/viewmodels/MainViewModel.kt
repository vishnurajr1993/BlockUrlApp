package com.imperium.accessabilityapp.presentation.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.imperium.accessabilityapp.Model.TimingModel
import com.imperium.accessabilityapp.Uils.DataState
import com.imperium.accessabilityapp.repository.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel@ViewModelInject
constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private  val _splashTimer: MutableLiveData<Boolean> = MutableLiveData()
    val splashTimer: LiveData<Boolean>
        get()=_splashTimer


    private val _scheduleState = MutableSharedFlow<DataState<Boolean>>()
    val scheduleState: SharedFlow<DataState<Boolean>>
        get() = _scheduleState.asSharedFlow()


    private val _scheduleTimeState = MutableStateFlow<DataState<TimingModel>>(DataState.Loading)
    val scheduleTimeState: StateFlow<DataState<TimingModel>>
        get() = _scheduleTimeState.asStateFlow()

    fun initSplashScreen() {
        viewModelScope.launch {
            delay(2000)
            updateSplashTimer()
        }
    }

    private fun updateSplashTimer() {
        _splashTimer.value = true
    }

    fun isEventCreated():Boolean=mainRepository.isEventCreated()

    fun setSchedule(startTime:String,endTime:String,startTime24:String,endTime24:String){
        viewModelScope.launch {
            _scheduleState.emit(DataState.Loading)
            when {
                startTime.isEmpty() -> {
                    _scheduleState.emit(DataState.Error(exception = "Select Start Date"))
                }
                endTime.isEmpty() -> {
                    _scheduleState.emit(DataState.Error(exception = "Select End Date"))
                }
                else -> {
                    mainRepository.addSchedule(startTime,endTime,startTime24,endTime24)
                    _scheduleState.emit(DataState.Success(data = true))
                }
            }

        }

    }

    fun getScheduledTime(){
            _scheduleTimeState.value=mainRepository.getScheduledDates()
    }

    fun deleteSchedule()=mainRepository.deleteSchedule()


    fun setSwitchState(value:Boolean){
        mainRepository.setSwitchState(value)
    }

    fun getSwitchState()=mainRepository.getSwitchState()
}