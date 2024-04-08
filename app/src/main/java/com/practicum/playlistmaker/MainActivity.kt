package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFirst: Button = findViewById(R.id.search_button)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
        btnFirst.setOnClickListener(buttonClickListener)

        findViewById<Button>(R.id.media_button).setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }
    }
    fun onBtnPress(v: View) = startActivity(Intent(this, SettingsActivity::class.java))

}