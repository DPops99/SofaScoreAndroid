package com.example.proba.aboutMe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proba.R
import com.example.proba.databinding.ActivityAboutMeBinding

class AboutMeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutMeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutMeBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        setView();
    }

    fun setView(){

        setSupportActionBar(binding.aboutToolbar)

        supportActionBar?.apply {

            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.aboutApp.amTitle.text = getString(R.string.about_name)
        binding.aboutApp.amValue.text = getString(R.string.app_name)

        binding.aboutApi.amTitle.text = getString(R.string.abour_api)
        binding.aboutApi.amValue.text = getString(R.string.api_credit)

        binding.aboutDev.amTitle.text = getString(R.string.developer)
        binding.aboutDev.amValue.text = getString(R.string.dev_name)
    }
}