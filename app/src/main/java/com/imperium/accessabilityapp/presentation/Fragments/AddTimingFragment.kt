package com.imperium.accessabilityapp.presentation.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.imperium.accessabilityapp.MainActivity
import com.imperium.accessabilityapp.R
import com.imperium.accessabilityapp.databinding.FragmentAddTimingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class AddTimingFragment : BaseFragment<FragmentAddTimingBinding>() {
    override fun getViewBinding(): FragmentAddTimingBinding = FragmentAddTimingBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        (activity as MainActivity?)!!.supportActionBar!!.hide()
        binding.addTiming.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_addTimingFragment_to_timingFragment)

        }
    }
}