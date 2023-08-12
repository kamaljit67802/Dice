package com.example.dice

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var customDiceSidesEditText: EditText
    private lateinit var nightModeToggleButton: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        customDiceSidesEditText = findViewById(R.id.customDiceSidesEditText)
        nightModeToggleButton = findViewById(R.id.nightModeToggleButton)

        val diceOptions = resources.getStringArray(R.array.dice_options)
        val diceSpinner: Spinner = findViewById(R.id.diceSpinner)
        val rollButton: Button = findViewById(R.id.rollButton)
        val rollTwiceButton: Button = findViewById(R.id.rollTwiceButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diceOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        diceSpinner.adapter = adapter

        nightModeToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                applyNightMode()
            } else {
                applyDayMode()
            }
        }

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
            max(1, selectedDiceText.toInt())
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

    private fun applyNightMode() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setBackgroundColor(resources.getColor(R.color.colorNightBackground))
        // Adjust other styling elements for night mode
    }

    private fun applyDayMode() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setBackgroundColor(resources.getColor(R.color.colorDayBackground))
        // Reset styling changes made for night mode
    }
}