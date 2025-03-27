package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private fun adjustPadding(){
        //Adjusting padding if system bars exists.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun openMenu(){
        enableEdgeToEdge()
        //Setting content view to manu layout
        setContentView(R.layout.menu)
        adjustPadding()
        //Buttons in menu initialization.
        configureMenuButtons()
    }


    private fun saveState(state: String){
        getSharedPreferences("app_state", MODE_PRIVATE).edit().apply {
            putString("last_screen", state)
            apply()
        }
    }
    private fun openCalculatorsActivity(){
        val activity = Intent(this,CalculatorsActivity::class.java)
        startActivity(activity)
    }

    private fun openAboutMeActivity(){
        val activity = Intent(this, AboutMeActivity::class.java)
        startActivity(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("app_state", MODE_PRIVATE)
        openMenu()
        val lastScreen = prefs.getString("last_screen", "menu")
        when (lastScreen) {
            "science", "simple" -> openCalculatorsActivity()
            "aboutme" -> openAboutMeActivity()
        }
    }


    private fun configureMenuButtons(){
        Log.d("CalculatorApp", "Inicialised buttons")
        findViewById<Button>(R.id.menuButtonQuit).setOnClickListener {
            finishAffinity()
        }
        findViewById<Button>(R.id.menuButtonAboutMe).setOnClickListener{
            saveState("aboutme")
            openAboutMeActivity()
        }
        findViewById<Button>(R.id.menuButtonSimpleCalculator).setOnClickListener{
            saveState("simple")
            openCalculatorsActivity()
        }
        findViewById<Button>(R.id.menuButtonScienceCalculator).setOnClickListener{
            saveState("science")
            openCalculatorsActivity()
        }
    }

}