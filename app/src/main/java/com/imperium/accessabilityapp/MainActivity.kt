package com.imperium.accessabilityapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.imperium.accessabilityapp.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    @Inject
    lateinit var  main: MainRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!checkAccessibilityPermission()){
            showAlertDialog()
        }

        setUpNavController()
    }

    fun showAlertDialog(){
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Please Enable Accessibility Permission")
            .setMessage("Do you want to enable permission")
            .setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener { dialogInterface, i -> //set what would happen when positive button is clicked
                    /*if(!checkAccessibilityPermission()){
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }*/
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
    fun checkAccessibilityPermission(): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled =
                Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return accessEnabled != 0
    }

    fun setUpNavController(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        navController = Navigation.findNavController(this, R.id.main);
        NavigationUI.setupActionBarWithNavController(this, navController!!)
    }
}