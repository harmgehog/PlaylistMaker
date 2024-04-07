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

        val btnFirst: Button = findViewById(R.id.button1)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                // Домашнее задание 2 реализация тоста через анон класс
                // Toast.makeText(this@MainActivity, "Нажали Поиск!", Toast.LENGTH_LONG).show()
                // Домашнее 3 реализация запуска интент на той же кнопке
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
        btnFirst.setOnClickListener(buttonClickListener)

        findViewById<Button>(R.id.button2).setOnClickListener {
            // Домашнее 3 реализация запуска интент через лямбду
            startActivity(Intent(this, MediaActivity::class.java))
        }
    }
    // Собственный способ
    fun onBtnPress(v: View) = startActivity(Intent(this, SettingsActivity::class.java))

}