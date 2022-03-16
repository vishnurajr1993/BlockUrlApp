package com.imperium.accessabilityapp.presentation.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.imperium.accessabilityapp.MainActivity
import com.imperium.accessabilityapp.R
import com.imperium.accessabilityapp.databinding.FragmentSplashSceenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<FragmentSplashSceenBinding>() {
    override fun getViewBinding(): FragmentSplashSceenBinding =FragmentSplashSceenBinding.inflate(layoutInflater)


    override fun setUpViews() {
        super.setUpViews()
        //(activity as MainActivity?)!!.supportActionBar!!.hide()
    }
    override fun observeData() {
        super.observeData()
        observeSplashLiveData(binding.root)
    }
    private fun observeSplashLiveData(view: View) {
        vm.initSplashScreen()
        val observer = Observer<Boolean> {
            val isEventCreated=vm.isEventCreated()


            if(isEventCreated){
                NavHostFragment.findNavController(this).navigate(R.id.action_splashScreenFragment_to_scheduleFragment)
            }else {
                Navigation.findNavController(view)
                    .navigate(R.id.action_splashScreenFragment_to_addTimingFragment);
            }

        }
        vm.splashTimer.observe(this, observer)
    }
}