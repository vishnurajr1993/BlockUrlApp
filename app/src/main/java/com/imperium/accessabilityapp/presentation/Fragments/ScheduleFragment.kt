package com.imperium.accessabilityapp.presentation.Fragments

import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.imperium.accessabilityapp.MainActivity
import com.imperium.accessabilityapp.R
import com.imperium.accessabilityapp.Uils.DataState
import com.imperium.accessabilityapp.Uils.checkIsTimeBetween
import com.imperium.accessabilityapp.databinding.FragmentSceduleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentSceduleBinding>(){

    private val mainDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            vm.getScheduledTime()
        }
    override fun getViewBinding(): FragmentSceduleBinding = FragmentSceduleBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        (activity as MainActivity?)!!.supportActionBar!!.hide()
        vm.getScheduledTime()
        initViews()

    }
    fun initViews(){

        binding.sWidgetSwitchBtn.isChecked=vm.getSwitchState()
        setContent(vm.getSwitchState())
        binding.edit.setOnClickListener {
            findNavController().apply {
                navigate(R.id.action_scheduleFragment_to_timingFragment)
                addOnDestinationChangedListener(mainDestinationChangedListener)
            }
        }

        binding.delete.setOnClickListener {
            vm.deleteSchedule()
            findNavController().navigate(R.id.action_scheduleFragment_to_addTimingFragment)
        }


        binding.sWidgetSwitchBtn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            vm.setSwitchState(isChecked)
            setContent(isChecked)
        })
    }

    fun setContent(value:Boolean){
        if(value){
            binding.content.text=resources.getString(R.string.black_list)
        }else{
            binding.content.text=resources.getString(R.string.white_list)
        }
    }
    override fun observeView() {
        super.observeView()
        Log.d("TAG", "observeView: ")
        vm.getScheduledTime()
    }


    override fun observeData() {
        super.observeData()

        lifecycleScope.launchWhenCreated{

            vm.scheduleTimeState.collectLatest {
                when(it){
                    is DataState.Loading->{

                    }
                    is DataState.Success->{
                        binding.starts.text=it.data.startTime
                        binding.ends.text=it.data.endTime
                    }
                    is DataState.Error->{

                    }
                }
            }
        }
    }


}