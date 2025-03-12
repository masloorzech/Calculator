package com.example.calculator

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.sqrt
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var output: Double? = null
    private lateinit var bufferScreen: TextView
    private lateinit var outputScreen: TextView
    private var operation: String? = null

    private var isBracket: Boolean = false
    private var isNegative: Boolean = false

    private val availableOperations = listOf(
        "ADD", "SUBTRACTION",
        "MULTIPLICATION", "DIVISION",
        "XTOPOWERY", "XTOPOWERMINUSY",
        "LOGXY", "XMODY", "RANDXY", "PERCENTAGE"
    )

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (output != null) {
            outState.putDouble("output", output ?: 0.0)
        } else {
            outState.putDouble("output", Double.NaN)
        }
        outState.putString("bufferScreen", bufferScreen.text.toString())
        outState.putString("outputScreen", outputScreen.text.toString())
        outState.putString("operation", operation)
        outState.putBoolean("isBracket", isBracket)
        outState.putBoolean("isNegative", isNegative)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        output = if (savedInstanceState.getDouble("output", Double.NaN).isNaN()) {
            null
        } else {
            savedInstanceState.getDouble("output", 0.0)
        }

        bufferScreen.text = savedInstanceState.getString("bufferScreen", "")
        outputScreen.text = savedInstanceState.getString("outputScreen", "")
        operation = savedInstanceState.getString("operation", null)
        isBracket = savedInstanceState.getBoolean("isBracket", false)
        isNegative = savedInstanceState.getBoolean("isNegative", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        outputScreen = findViewById(R.id.textView2)
        bufferScreen = findViewById(R.id.textView)
        val valButtonsIds = listOf(
            R.id.valueButton0, R.id.valueButton1, R.id.valueButton2,
            R.id.valueButton3, R.id.valueButton4, R.id.valueButton5,
            R.id.valueButton6, R.id.valueButton7, R.id.valueButton8,
            R.id.valueButton9
        )

        valButtonsIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                valueButtonClick((it as Button).text.toString())
            }
        }

        val buttonAC: Button = findViewById(R.id.actionButtonAC)
        buttonAC.setOnClickListener {
            actionButtonACClick()
        }

        val buttonAdd: Button = findViewById(R.id.actionButtonAdd)
        buttonAdd.setOnClickListener {
            actionButtonAddClick()
        }

        val buttonSub: Button = findViewById(R.id.actionButtonSub)

        buttonSub.setOnClickListener {
            actionButtonSubClick()
        }

        val buttonMul: Button = findViewById(R.id.actionButtonMultiply)
        buttonMul.setOnClickListener {
            actionButtonMulClick()
        }

        val buttonDiv: Button = findViewById(R.id.actionButtonDivide)
        buttonDiv.setOnClickListener {
            actionButtonDivClick()
        }

        val buttonChangeSign: Button = findViewById(R.id.actionButtonChangeSign)
        buttonChangeSign.setOnClickListener {
            actionButtonChangeSign()
        }

        val buttonEquals: Button = findViewById(R.id.actionButtonEquals)
        buttonEquals.setOnClickListener {
            calculateExpression()
        }

        val buttonDot: Button = findViewById(R.id.valueButtonDot)
        buttonDot.setOnClickListener {
            actionButtonDotClick()
        }

        val buttonPercentage: Button = findViewById(R.id.actionButtonPercentage)
        buttonPercentage.setOnClickListener {
            actionButtonPercentageClick()
        }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val buttonlnx: Button = findViewById(R.id.actionButtonLnx)

            buttonlnx.setOnClickListener {
                actionButtonLnXClick()
            }

            val buttonsqrt: Button = findViewById(R.id.acctionButtonSqrt)

            buttonsqrt.setOnClickListener {
                actionButtonSqrtClick()
            }

            val buttonXToPowerY: Button = findViewById(R.id.acctionButtonXToPowerY)
            buttonXToPowerY.setOnClickListener {
                actionButtonXToPowerYClick()
            }

            val buttonXToPowerMinusY: Button = findViewById(R.id.acctionButtonXToPowerMinusY)
            println("here")
            buttonXToPowerMinusY.setOnClickListener {
                actionButtonXToPowerMinusYClick()
            }

            val buttonSin: Button = findViewById(R.id.acctionButtonSinX)
            buttonSin.setOnClickListener {
                actionButtonSinClick()
            }

            val buttonASin: Button = findViewById(R.id.acctionButtonASinX)
            buttonASin.setOnClickListener {
                actionButtonASinClick()
            }

            val buttonCos: Button = findViewById(R.id.acctionButtonCosX)
            buttonCos.setOnClickListener {
                actionButtonCosClick()
            }

            val buttonACos: Button = findViewById(R.id.acctionButtonACosX)
            buttonACos.setOnClickListener {
                actionButtonACosClick()
            }

            val buttonTan: Button = findViewById(R.id.acctionButtonTanX)
            buttonTan.setOnClickListener {
                actionButtonTanClick()
            }

            val buttonATan: Button = findViewById(R.id.acctionButtonATanX)
            buttonATan.setOnClickListener {
                actionButtonATanClick()
            }

            val buttonCTan: Button = findViewById(R.id.acctionButtonCTanX)
            buttonCTan.setOnClickListener {
                actionButtonCTanClick()
            }

            val buttonACTan: Button = findViewById(R.id.acctionButtonACtanX)
            buttonACTan.setOnClickListener {
                actionButtonACtanClick()
            }

            val buttonFactorial: Button = findViewById(R.id.acctionButtonXFactorial)
            buttonFactorial.setOnClickListener {
                actionButtonFactorialXClick()
            }

            val buttonSquare: Button = findViewById(R.id.acctionButtonXSquare)
            buttonSquare.setOnClickListener {
                actionButtonSquareClick()
            }

            val buttonLogXY: Button = findViewById(R.id.acctionButtonLogNX)
            buttonLogXY.setOnClickListener {

                actionButtonLogXYClick()
            }

            val buttonEToPowerX: Button = findViewById(R.id.acctionButtonEToPowerX)
            buttonEToPowerX.setOnClickListener {
                actionButtonEtoPowerXClick()
            }

            val buttonLogX: Button = findViewById(R.id.acctionButtonLogX)
            buttonLogX.setOnClickListener {
                actionButtonLogX()
            }

            val buttonXmodY: Button = findViewById(R.id.acctionButtonXModY)
            buttonXmodY.setOnClickListener {
                actionButtonXModYClick()
            }

            val buttonRandXY: Button = findViewById(R.id.acctionButtonRandomFromXToY)
            buttonRandXY.setOnClickListener {
                actionButtonRandXY()
            }

            val button10ToX: Button = findViewById(R.id.acctionButtonTenToX)
            button10ToX.setOnClickListener {
                actionButton10ToX()
            }
        }

    }

    private fun calculateExpression(){
        if (operation==null){
            return
        }
        val expression: String = outputScreen.text.toString()
        val numbers = mutableListOf<String>()
        val operators = mutableListOf<Char>()
        var currentNumber = StringBuilder()

        for (char in expression) {
            if (char.isDigit() || char == '.' || char == 'e' || char == 'E') {
                currentNumber.append(char)
            } else {
                if (currentNumber.isNotEmpty()) {
                    numbers.add(currentNumber.toString())
                    currentNumber.clear()
                }
                operators.add(char)
            }
        }

        if (currentNumber.isNotEmpty()) {
            numbers.add(currentNumber.toString())
        }
        if (numbers.size<2){
            return
        }else {

            var firstNumber: Double = numbers[0].toDoubleOrNull() ?: return
            val secondNumber: Double = numbers[1].toDoubleOrNull() ?: return

            if (isNegative) {
                firstNumber*=-1
            }
            if (operation == availableOperations[0]) {
                output = firstNumber + secondNumber
            }
            if (operation == availableOperations[1]) {
                output = firstNumber- secondNumber
            }
            if (operation == availableOperations[2]) {
                output = firstNumber* secondNumber
            }
            if (operation == availableOperations[3]) {
                output = firstNumber/ secondNumber
            }
            if (operation == availableOperations[4]){
                output = firstNumber.pow(secondNumber)
            }
            if (operation == availableOperations[5]){
                output = firstNumber.pow(-secondNumber)
            }
            if (operation == availableOperations[6]){
                output = log(secondNumber,firstNumber)
            }
            if (operation == availableOperations[7]){
                output = firstNumber%secondNumber
            }
            if (operation == availableOperations[8]){
                if (firstNumber==secondNumber){
                    output=firstNumber
                }else if (firstNumber>secondNumber){
                    output = Random.nextDouble(secondNumber,firstNumber)
                }else {
                    output = Random.nextDouble(firstNumber, secondNumber)
                }
            }
            if (operation==availableOperations[9]){
                output = (firstNumber/100.0) * secondNumber
            }
        }

        updateBuffer("")
        setTextToView(output.toString())
        isNegative = false
        isBracket = false
        operation = null
    }
    private fun actionButton10ToX(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = 10.0.pow(outputScreen.text.toString().toDouble())
        updateBuffer("10^")
        setTextToView(output.toString());
    }
    private fun actionButtonLogX(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = log10(outputScreen.text.toString().toDouble())
        updateBuffer("log")
        setTextToView(output.toString());
    }
    private fun actionButtonRandXY(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        operation = availableOperations[8]
        isBracket = true
        setTextToView("rand("+outputScreen.text.toString()+", ")
    }
    private fun actionButtonChangeSign() {
        if (outputScreen.text.toString() == "" || operation != null || output != null) {
            return
        }
        isNegative = !isNegative
        if (isNegative){
            setTextToView("-"+outputScreen.text.toString())
        }else{
            setTextToView(outputScreen.text.toString().substring(1))
        }
    }
    private fun actionButtonAddClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "+"
        operation = availableOperations[0]
        addTextToView(sign)
    }
    private fun actionButtonXModYClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "mod"
        operation = availableOperations[7]
        addTextToView(sign)
    }
    private fun actionButtonLnXClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = ln(outputScreen.text.toString().toDouble())
        updateBuffer("ln")
        setTextToView(output.toString())
    }
    private fun actionButtonEtoPowerXClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = E.pow(outputScreen.text.toString().toDouble())
        updateBuffer("e^")
        setTextToView(output.toString())
    }
    private fun factorial(x: Int): Double{
        if (x<0){
            return Double.NaN
        }
        var result: Double = 1.0
        for (i in 1..x) {
            result *= i
        }
        return result
    }
    private fun actionButtonFactorialXClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = factorial(outputScreen.text.toString().toInt())
        updateBuffer("!")
        setTextToView(output.toString())
    }
    private fun actionButtonSquareClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = outputScreen.text.toString().toDouble()*outputScreen.text.toString().toDouble()
        updateBufferEnd("²")
        setTextToView(output.toString())
    }
    private fun actionButtonSinClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = sin(outputScreen.text.toString().toDouble())
        updateBuffer("sin")
        setTextToView(output.toString());
    }
    private fun actionButtonASinClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = asin(outputScreen.text.toString().toDouble())
        updateBuffer("asin")
        setTextToView(output.toString());
    }
    private fun actionButtonCosClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = cos(outputScreen.text.toString().toDouble())
        updateBuffer("cos")
        setTextToView(output.toString());
    }
    private fun actionButtonACosClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = acos(outputScreen.text.toString().toDouble())
        updateBuffer("acos")
        setTextToView(output.toString());
    }
    private fun actionButtonTanClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = tan(outputScreen.text.toString().toDouble())
        updateBuffer("tan")
        setTextToView(output.toString());
    }
    private fun actionButtonATanClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = atan(outputScreen.text.toString().toDouble())
        updateBuffer("atan")
        setTextToView(output.toString());
    }
    private fun actionButtonCTanClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = cos(outputScreen.text.toString().toDouble())/sin(outputScreen.text.toString().toDouble())
        updateBuffer("ctan")
        setTextToView(output.toString());
    }
    private fun actionButtonACtanClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = (PI/2) - atan(outputScreen.text.toString().toDouble())
        updateBuffer("actan")
        setTextToView(output.toString());
    }
    private fun actionButtonSqrtClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        output = sqrt(outputScreen.text.toString().toDouble())
        updateBuffer("√")
        setTextToView(output.toString());
    }
    private fun actionButtonXToPowerYClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "^"
        operation = availableOperations[4]
        addTextToView(sign)
    }
    private fun actionButtonXToPowerMinusYClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "^-"
        operation = availableOperations[5]
        addTextToView(sign)
    }
    private fun actionButtonDotClick(){
        if (outputScreen.text.toString()=="" || output!=null){
            return
        }
        if (operation==null && outputScreen.text.toString().contains(".")){
            return
        }
        if (operation!=null){
            val count = outputScreen.text.toString().count{
                it == '.'
            }
            if (count==2){
                return
            }
        }
        val sign: String = "."
        addTextToView(sign)
    }
    private fun actionButtonSubClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "-"
        operation = availableOperations[1]
        addTextToView(sign)
    }
    private fun actionButtonPercentageClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "%"
        operation = availableOperations[9]
        addTextToView(sign)
    }
    private fun actionButtonMulClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "×"
        operation = availableOperations[2]
        addTextToView(sign)
    }
    private fun actionButtonDivClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        val sign: String = "÷"
        operation = availableOperations[3]
        addTextToView(sign)
    }
    private fun actionButtonLogXYClick(){
        if (outputScreen.text.toString()=="" || operation != null || output!=null){
            return
        }
        operation = availableOperations[6]
        isBracket = true
        setTextToView("㏒("+outputScreen.text.toString()+", ")
    }
    private fun actionButtonACClick(){
        output = null
        bufferScreen.text = ""
        outputScreen.text = ""
        operation = null
        isBracket = false
    }

    private fun addTextToView(text: String){
        var buffer = outputScreen.text.toString()
        if (isBracket){
            buffer = buffer.dropLast(1)
        }
        buffer+=text
        if (isBracket){
            buffer+=")"
        }
        outputScreen.text = buffer
    }

    private fun setTextToView(text:String){
        outputScreen.text = text
    }

    @SuppressLint("SetTextI18n")
    private fun updateBuffer(text:String){
        val buffer: String = getString(R.string.memory_description)
        bufferScreen.text = buffer +" "+text+ outputScreen.text.toString()
    }
    @SuppressLint("SetTextI18n")
    private fun updateBufferEnd(text:String){
        val buffer: String = getString(R.string.memory_description)
        bufferScreen.text = buffer +" "+ outputScreen.text.toString()+ text
    }


    private fun valueButtonClick(value: String){
        if (output!=null){
            val toBuffer : String = getString(R.string.memory_description) +" "+outputScreen.text.toString()
            actionButtonACClick()
            bufferScreen.text = toBuffer
        }
        addTextToView(value)
    }
}