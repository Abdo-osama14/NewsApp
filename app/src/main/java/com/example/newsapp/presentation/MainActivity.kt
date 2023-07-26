package com.example.newsapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.presentation.fragments.BreakingNewsFragment
import com.example.newsapp.presentation.fragments.SavedNewsFragment
import com.example.newsapp.presentation.fragments.SearchNewsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var viewModel:NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectNavWithFrag()
        viewModel=ViewModelProvider(this)[NewsViewModel::class.java]

    }

    private fun changeFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(fragment.javaClass.name)
            commit()
        }
    }
    private fun connectNavWithFrag() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.breakingNewsFragment2 -> changeFragment(BreakingNewsFragment())
                R.id.savedNewsFragment2 -> changeFragment(SavedNewsFragment())
                R.id.searchNewsFragment2 -> changeFragment(SearchNewsFragment())
            }
            true

        }
    }

}