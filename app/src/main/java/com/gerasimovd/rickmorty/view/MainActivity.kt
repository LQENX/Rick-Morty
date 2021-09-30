package com.gerasimovd.rickmorty.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Indicates what a Fragment in BottomNavBar is currently selected
    // Helps to avoid re-click an already opened Fragment.
    private var currentFragmentOpened: MenuItem? = null

    // Time when the button was pressed in milliseconds
    private var backPressed: Long = 0

    companion object {
        private const val BACK_PRESSED_OFFSET = 2000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListener()
        setStartFragment()

    }

    private fun setBottomNavListener() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener())
    }

    private fun setStartFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CharactersFragment())
            .commit()

        disableClick(binding.bottomNavigation.menu[0])
    }

    private fun navListener() = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when(menuItem.itemId) {
            R.id.nav_characters -> {
                disableClick(menuItem)
                val fragment = CharactersFragment()
                navigateTo(fragment)

                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_locations -> {
                disableClick(menuItem)
                val fragment = LocationsFragment()
                navigateTo(fragment)

                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_episodes -> {
                disableClick(menuItem)
                val fragment = EpisodesFragment()
                navigateTo(fragment)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                fragment,
                fragment::class.qualifiedName
            )
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    // Helps to avoid re-click an already opened Fragment
    private fun disableClick(menuItem: MenuItem) {
        currentFragmentOpened?.isEnabled = true
        menuItem.isEnabled = false
        currentFragmentOpened = menuItem
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (backPressed + BACK_PRESSED_OFFSET > System.currentTimeMillis()) {
                finish()
            } else
                Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

}