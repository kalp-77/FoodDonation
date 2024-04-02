package com.example.foodapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.helpToolbar)
        binding.helpToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}