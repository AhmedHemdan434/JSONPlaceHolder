package com.hemdan.jsonplaceholder.presentation

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.hemdan.jsonplaceholder.R
import com.hemdan.jsonplaceholder.presentation.postList.PostListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.postListFragment)
//        supportActionBar?.hide()
        // ensures fragments already created will not be created
        if (fragment == null) {
            fragment = PostListFragment()
            // create and commit a fragment transaction
            fm.beginTransaction()
                .add(R.id.postListFragment, fragment)
                .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}