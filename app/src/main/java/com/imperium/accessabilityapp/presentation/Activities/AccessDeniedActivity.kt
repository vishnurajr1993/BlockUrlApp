package com.imperium.accessabilityapp.presentation.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.imperium.accessabilityapp.databinding.ActivityAccessDeniedBinding

class AccessDeniedActivity : AppCompatActivity() {
    private var binding: ActivityAccessDeniedBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessDeniedBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)


    }
}