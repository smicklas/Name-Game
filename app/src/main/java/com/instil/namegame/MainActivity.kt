package com.instil.namegame

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var startButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(this).get(DialogViewModel::class.java)

        startButton = findViewById<Button>(R.id.startButton)

        //Launch game activity
        startButton.setOnClickListener {
            val transaction = supportFragmentManager?.beginTransaction()
            transaction.add(R.id.container, GameFragment.newInstance(), "game")
            transaction.addToBackStack(null)
            transaction.commit()
        }

        var pickerButton = findViewById<Button>(R.id.difficultyButton)

        pickerButton.setOnClickListener {
            NumberPickerDialogFragment().show(supportFragmentManager, "NumberDialogPicker")
            //TODO - change color of space in the notification bar
        }
    }
}