package com.example.devfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.devfeed.ui.BookmarksFragment
import com.example.devfeed.ui.ExploreFragment
import com.example.devfeed.ui.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_bar)
        bottomNavigationView.setOnItemSelectedListener{
          when(it.itemId){
            R.id.home -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, HomeFragment.newInstance()).commit()
        }
            R.id.explore -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, ExploreFragment.newInstance()).commit()
        }
            R.id.bookmarks-> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, BookmarksFragment.newInstance()).commit()}
        }
        true
        }
        bottomNavigationView.selectedItemId = R.id.home
    }
}