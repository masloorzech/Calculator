package com.example.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.apply


class AboutMeActivity : AppCompatActivity() {

    private fun adjustPadding(){
        //Adjusting padding if system bars exists.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun saveState(state: String){
        getSharedPreferences("app_state", MODE_PRIVATE).edit().apply {
            putString("last_screen", state)
            apply()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saveState("aboutme")
        setContentView(R.layout.about_me)
        adjustPadding()


        val backButton = findViewById<Button>(R.id.buttonBack)

        backButton.setOnClickListener{
            saveState("menu")
            finish()
        }

    }
}