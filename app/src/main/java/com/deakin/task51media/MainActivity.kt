package com.deakin.task51media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the main layout with Fragment container
        setContentView(R.layout.activity_main)

        // Load StartFragment only when app launches first time
        if (savedInstanceState == null) {

            // Replace container with StartFragment (navigation entry)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartFragment())
                .commit()
        }
    }
}