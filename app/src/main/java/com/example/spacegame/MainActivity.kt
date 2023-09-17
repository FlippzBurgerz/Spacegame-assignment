package com.example.spacegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val jukebox = Jukebox(assets)
        findViewById<Button>(R.id.startGameButton)?.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            jukebox.play(SFX.start)
        }
    }
        override fun onResume() {
            super.onResume()
            val prefs = getSharedPreferences(GameConfig.PREFS, MODE_PRIVATE)
            val longestDistance = prefs.getInt(GameConfig.LONGEST_DIST, 0)
            val highscore = findViewById<TextView>(R.id.highscore)
            highscore.text = "${getString(R.string.longest_distance_traveled)} $longestDistance ${getString(R.string.distance_value)}"
        }
}