package com.example.dice

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var customDiceSidesEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        customDiceSidesEditText = findViewById(R.id.customDiceSidesEditText)

        val diceOptions = resources.getStringArray(R.array.dice_options)
        val diceSpinner: Spinner = findViewById(R.id.diceSpinner)
        val rollButton: Button = findViewById(R.id.rollButton)
        val rollTwiceButton: Button = findViewById(R.id.rollTwiceButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diceOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        diceSpinner.adapter = adapter

        rollButton.setOnClickListener {
            val selectedDice = getSelectedDice()
            val randomVal = (Math.random() * selectedDice + 1).toInt()
            resultTextView.text = randomVal.toString()

            saveValueToSharedPreferences(randomVal)
        }

        rollTwiceButton.setOnClickListener {
            val selectedDice = getSelectedDice()
            val randomVal1 = (Math.random() * selectedDice + 1).toInt()
            val randomVal2 = (Math.random() * selectedDice + 1).toInt()
            resultTextView.text = "$randomVal1, $randomVal2"

            saveValueToSharedPreferences(randomVal1, randomVal2)
        }
    }

    private fun getSelectedDice(): Int {
        val selectedDiceText = customDiceSidesEditText.text.toString().trim()
        return if (selectedDiceText.isNotEmpty()) {
            max(1, selectedDiceText.toInt()) // Ensure at least 1 side
        } else {
            val selectedSpinnerItem = (findViewById<Spinner>(R.id.diceSpinner)).selectedItem.toString()
            selectedSpinnerItem.toInt()
        }
    }

    private fun saveValueToSharedPreferences(vararg values: Int) {
        val valueString = values.joinToString(", ")
        val editor = sharedPreferences.edit()
        editor.putString("myvals", valueString)
        editor.apply()
    }
}
