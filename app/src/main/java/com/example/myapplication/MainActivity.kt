package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log


class MainActivity : AppCompatActivity() {


        private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
        private lateinit var textView : TextView
        private lateinit var layout: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         textView = findViewById<TextView>(R.id.text)
         layout= findViewById<LinearLayout>(R.id.layout)

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(30)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        // Set default values for Remote Config parameters
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        // Fetch the remote values
        fetchRemoteValues()
}
    private fun fetchRemoteValues() {

        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Apply fetched values to UI elements
                    applyRemoteValues()
                }else {
                    Log.e(MainActivity::javaClass.name, "fetchRemoteValues: ${task.exception} ", )
                }
            }
    }
    private fun applyRemoteValues() {
        // Get values from Remote Config
        val newText = mFirebaseRemoteConfig.getString("text_key")
        val backgroundColor = mFirebaseRemoteConfig.getString("background_color")

        // Apply values to TextView
        textView.text = newText

        // Apply background color to layout
        layout.setBackgroundColor(Color.parseColor(backgroundColor))
    }
}