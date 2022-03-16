package com.imperium.accessabilityapp.presentation.Fragments

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.imperium.accessabilityapp.R
import com.imperium.accessabilityapp.Uils.DataState
import com.imperium.accessabilityapp.databinding.FragmentTimingBinding
import com.imperium.accessabilityapp.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class TimingFragment : BottomSheetDialogFragment() {
    private val vm: MainViewModel by viewModels()
    lateinit var binding: FragmentTimingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimingBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        activity?.let { getTime(binding.fromDate, it) }
        activity?.let { getTime(binding.toDate, it) }
        setStartEndTime()

        binding.saveBtn.setOnClickListener {
            vm.setSchedule(
                startTime = binding.fromDate.text.toString().trim(),
                endTime = binding.toDate.text.toString().trim(),
                startTime24 = binding.fromDate.tag.toString().trim(),
                endTime24 = binding.toDate.tag.toString().trim()
            )
            vm.getScheduledTime()
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted{
            vm.scheduleState.collectLatest {
                when(it){
                    is DataState.Success->{
                        arguments?.let {
                            val source: String = requireArguments().getString("from").toString()
                            navigateToNextPage(source)
                        }


                    }
                    is DataState.Loading->{

                    }
                    is DataState.Error->{
                        Toast.makeText(activity,it.exception,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun setStartEndTime(){

        lifecycleScope.launchWhenCreated {
            vm.scheduleTimeState.collectLatest {
                when(it){
                    is DataState.Loading->{

                    }
                    is DataState.Success->{
                        if(it.data.startTime.isNotEmpty() && it.data.endTime.isNotEmpty()) {
                            binding.fromDate.setText(it.data.startTime)
                            binding.fromDate.setTag(it.data.startTime)
                            binding.toDate.setText(it.data.endTime)
                            binding.toDate.setTag(it.data.endTime)
                        }else{
                            val currentTimeHHMMA =
                                SimpleDateFormat("K:mm a", Locale.getDefault()).format(Date())
                            val currentTimeHHMM =
                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                            binding.fromDate.apply {
                                setText(currentTimeHHMMA)
                                setTag(currentTimeHHMM)
                            }
                            binding.toDate.apply {
                                setText(currentTimeHHMMA)
                                setTag(currentTimeHHMM)
                            }
                        }
                    }
                    is DataState.Error->{
                        Toast.makeText(activity, it.exception, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        vm.getScheduledTime()
    }

    private fun setHeader() {
            arguments?.let {
                val source: String = requireArguments().getString("from").toString()
                if(source=="add"){
                    binding.title.text=resources.getString(R.string.add_timing)
                }else{
                    binding.title.text=resources.getString(R.string.edit_timing)
                }
            }

    }
    private fun navigateToNextPage(fromRoute:String){
        if(fromRoute=="add"){
            findNavController().navigate(R.id.action_timingFragment_to_scheduleFragment)
            dismiss()
        }else{
            dismiss()
        }
    }

    private fun getTime(editText: EditText, context: Context){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            editText.setText(SimpleDateFormat("K:mm a").format(cal.time))
            editText.setTag(SimpleDateFormat("HH:mm").format(cal.time))
        }

        editText.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }
    }

}