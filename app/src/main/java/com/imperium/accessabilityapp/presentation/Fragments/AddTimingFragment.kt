package com.imperium.accessabilityapp.presentation.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
        //(activity as MainActivity?)!!.supportActionBar!!.hide()
        binding.addTiming.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_addTimingFragment_to_timingFragment)
        }
        if(!checkAccessibilityPermission()){
            showAlertDialog()
        }
    }
    fun checkAccessibilityPermission(): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled =
                Settings.Secure.getInt(activity?.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return accessEnabled != 0
    }
    fun showAlertDialog(){
        activity?.let {
            val alertDialog: AlertDialog = AlertDialog.Builder(it)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Please Enable Accessibility Permission")
                .setMessage("Do you want to enable permission")
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialogInterface, i -> //set what would happen when positive button is clicked

                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        // request permission via start activity for result
                        startActivity(intent)
                    })
                .setNegativeButton(
                    "No",
                    DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                .show()
        }

    }
}