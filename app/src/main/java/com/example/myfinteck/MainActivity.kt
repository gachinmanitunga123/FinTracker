package com.example.myfinteck

import android.R.attr.fragment
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myfinteck.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter : TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Default fragment
        replaceDBFragment(DashboardFragment())

        binding.btnNavigation.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.nav_dashboard -> replaceFragment(DashboardFragment())

                R.id.nav_transactions -> replaceFragment(TransactionFragment())

                R.id.nav_settings -> replaceFragment(SettingsFragment())
            }
            true
        }



    }

    private fun replaceFragment(fragment: Fragment) {
         supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun replaceDBFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}