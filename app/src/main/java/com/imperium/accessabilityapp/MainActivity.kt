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


        setUpNavController()
    }
    /*i have only tested it in android 9 please do excuse if any crashes occur in the below versions*/



    fun setUpNavController(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        navController = Navigation.findNavController(this, R.id.main);
       // NavigationUI.setupActionBarWithNavController(this, navController!!)
    }
}