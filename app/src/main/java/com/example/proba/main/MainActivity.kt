package com.example.proba.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.proba.databinding.ActivityMainBinding
import com.example.proba.R
import com.example.proba.helper.LanguageHelper
import com.example.proba.main.first_fragment.SearchFragment
import com.example.proba.main.second_fragment.ShowFragment
import com.example.proba.main.third_fragment.SettingsFragment
import com.example.proba.room.database.AppDatabase
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(savedInstanceState==null)
            setCurrentFragment(SearchFragment())


        setListeners()

        val bundle = intent.getStringExtra(getString(R.string.snackbar_bundle))
        if (bundle != null){

            Snackbar.make(binding.root, R.string.language_changed, Snackbar.LENGTH_LONG).show()

        }
    }

    fun setListeners(){
        binding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.input_menu -> setCurrentFragment(SearchFragment())
                R.id.show_menu -> setCurrentFragment(ShowFragment())
                R.id.settings_menu -> setCurrentFragment(SettingsFragment())
            }
            true
        }


    }

    fun setCurrentFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentView.id,fragment)
            commit()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageHelper.wrapLanguage(newBase))
    }


}