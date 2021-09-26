package com.gerasimovd.rickmorty.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStartFragment()


    }

    private fun setStartFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CharactersFragment())
            .commit()

        /*disableClick(binding.bottomNavigation.menu[0])*/
    }


}