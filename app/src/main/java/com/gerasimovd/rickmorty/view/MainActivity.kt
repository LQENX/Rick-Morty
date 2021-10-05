package com.gerasimovd.rickmorty.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.databinding.ActivityMainBinding
import com.gerasimovd.rickmorty.utils.Constants
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.view.character.CharactersFragment
import com.gerasimovd.rickmorty.view.episode.EpisodesFragment
import com.gerasimovd.rickmorty.view.location.LocationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Time when the button was pressed in milliseconds
    private var backPressed: Long = 0

    // Receiver for network connection
    private var receiver: BroadcastReceiver? = null


    companion object {
        private const val BACK_PRESSED_OFFSET = 2000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_RickMorty)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListener()
        setStartFragment()
    }

    override fun onResume() {
        registerBroadcast()
        super.onResume()
    }

    override fun onPause() {
        unregisterBroadcast()
        super.onPause()
    }

    private fun registerBroadcast() {
        receiver = getReceiver()
        val filter = IntentFilter(Constants.NETWORK_INTENT_ACTION)
        this.registerReceiver(receiver, filter)
    }

    private fun unregisterBroadcast() {
        receiver?.let { this.unregisterReceiver(it) }
        receiver = null
    }

    private fun getReceiver(): BroadcastReceiver {
        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val connectivityManager =
                        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val isNetworkConnected = connectivityManager.activeNetworkInfo?.isConnected ?: false
                    setNetworkConnected(isNetworkConnected)
                    updateNetworkStatus(isNetworkConnected)
                }
            }
        }
        return receiver!!
    }

    private fun updateNetworkStatus(isConnected: Boolean) {
        binding.offlineStatus.isVisible = !isConnected
    }

    private fun setNetworkConnected(isConnected: Boolean) {
        NetworkManager.setNetworkStatus(isConnected)
    }

    private fun setBottomNavListener() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener())
    }

    private fun setStartFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CharactersFragment())
            .commit()
    }

    private fun navListener() = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when(menuItem.itemId) {
            R.id.nav_characters -> {
                var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (currentFragment is CharactersFragment)
                    currentFragment.scrollToTop()
                else {
                    currentFragment = CharactersFragment()
                    navigateTo(currentFragment)
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_locations -> {
                var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (currentFragment is LocationsFragment)
                    currentFragment.scrollToTop()
                else {
                    currentFragment = LocationsFragment()
                    navigateTo(currentFragment)
                }

                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_episodes -> {
                var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (currentFragment is EpisodesFragment)
                    currentFragment.scrollToTop()
                else {
                    currentFragment = EpisodesFragment()
                    navigateTo(currentFragment)
                }

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