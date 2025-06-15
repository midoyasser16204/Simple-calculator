package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView

    private var operand1: Double = 0.0
    private var operand2: Double = 0.0
    private var currentOperator: String = ""
    private var isNewCalculation: Boolean = true
    private var hasDecimal: Boolean = false

    private val decimalFormat = DecimalFormat("#.########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
    }

    private fun initializeViews() {
        tvDisplay = findViewById(R.id.tvDisplay)
    }

    private fun setupNumberButtons() {
        val numberButtons = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtons.forEachIndexed { index, buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                onNumberClick(index.toString())
            }
        }
    }

    private fun setupOperatorButtons() {
        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperatorClick("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperatorClick("÷") }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsClick() }
    }

    private fun setupFunctionButtons() {
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClearClick() }
        findViewById<Button>(R.id.btnDecimal).setOnClickListener { onDecimalClick() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { onPlusMinusClick() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercentClick() }
    }

    private fun onNumberClick(number: String) {
        val currentText = tvDisplay.text.toString()

        if (isNewCalculation || currentText == "0") {
            tvDisplay.text = number
            isNewCalculation = false
        } else {
            tvDisplay.text = currentText + number
        }
    }

    private fun onOperatorClick(operator: String) {
        val currentText = tvDisplay.text.toString()

        try {
            if (currentOperator.isNotEmpty() && !isNewCalculation) {
                // Perform the previous operation
                operand2 = currentText.toDouble()
                val result = performCalculation()
                tvDisplay.text = formatResult(result)
                operand1 = result
            } else {
                operand1 = currentText.toDouble()
            }

            currentOperator = operator
            isNewCalculation = true
            hasDecimal = false
        } catch (e: NumberFormatException) {
            tvDisplay.text = "Error"
            resetCalculator()
        }
    }

    private fun onEqualsClick() {
        val currentText = tvDisplay.text.toString()

        if (currentOperator.isNotEmpty() && !isNewCalculation) {
            try {
                operand2 = currentText.toDouble()
                val result = performCalculation()
                tvDisplay.text = formatResult(result)
                resetCalculator()
            } catch (e: NumberFormatException) {
                tvDisplay.text = "Error"
                resetCalculator()
            } catch (e: ArithmeticException) {
                tvDisplay.text = "Error"
                resetCalculator()
            }
        }
    }

    private fun performCalculation(): Double {
        return when (currentOperator) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "×" -> operand1 * operand2
            "÷" -> {
                if (operand2 == 0.0) {
                    throw ArithmeticException("Division by zero")
                }
                operand1 / operand2
            }
            else -> operand1
        }
    }

    private fun onClearClick() {
        tvDisplay.text = "0"
        resetCalculator()
    }

    private fun onDecimalClick() {
        val currentText = tvDisplay.text.toString()

        if (isNewCalculation) {
            tvDisplay.text = "0."
            isNewCalculation = false
            hasDecimal = true
        } else if (!hasDecimal) {
            tvDisplay.text = currentText + "."
            hasDecimal = true
        }
    }

    private fun onPlusMinusClick() {
        val currentText = tvDisplay.text.toString()

        try {
            val currentValue = currentText.toDouble()
            val newValue = -currentValue
            tvDisplay.text = formatResult(newValue)
        } catch (e: NumberFormatException) {
            // Do nothing if current text is not a valid number
        }
    }

    private fun onPercentClick() {
        val currentText = tvDisplay.text.toString()

        try {
            val currentValue = currentText.toDouble()
            val percentValue = currentValue / 100
            tvDisplay.text = formatResult(percentValue)
        } catch (e: NumberFormatException) {
            // Do nothing if current text is not a valid number
        }
    }

    private fun resetCalculator() {
        operand1 = 0.0
        operand2 = 0.0
        currentOperator = ""
        isNewCalculation = true
        hasDecimal = false
    }

    private fun formatResult(result: Double): String {
        // Check if the result is a whole number
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            decimalFormat.format(result)
        }
    }
}