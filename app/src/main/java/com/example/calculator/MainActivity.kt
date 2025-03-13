package com.example.calculator

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.E
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
import androidx.core.content.edit


class MainActivity : AppCompatActivity() {

    private var output: Double? = null
    private lateinit var bufferScreen: TextView
    private lateinit var outputScreen: TextView
    private var operation: String? = null

    private var isBracket: Boolean = false
    private var isNegative: Boolean = false

    private val availableOperations = mapOf(
        "ADD" to { a: Double, b: Double -> a + b },
        "SUBTRACTION" to { a: Double, b: Double -> a - b },
        "MULTIPLICATION" to { a: Double, b: Double -> a * b },
        "DIVISION" to { a: Double, b: Double -> a / b },
        "XTOPOWERY" to { a: Double, b: Double -> a.pow(b) },
        "XTOPOWERMINUSY" to { a: Double, b: Double -> a.pow(-b) },
        "LOGXY" to { a: Double, b: Double -> log(b, a) },
        "XMODY" to { a: Double, b: Double -> a % b },
        "PERCENTAGE" to { a: Double, b: Double -> (a / 100.0) * b }
    )

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val prefs = getSharedPreferences("app_state", MODE_PRIVATE)
        val lastScreen = prefs.getString("last_screen", "menu")
        if (lastScreen=="menu"){
            return
        }

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
        val prefs = getSharedPreferences("app_state", MODE_PRIVATE)
        val lastScreen = prefs.getString("last_screen", "menu")
        if (lastScreen=="menu"){
            return
        }
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

    private fun configureCommonElements(){
        configureCommonDisplays()
        configureCommonButtons()
    }

    private fun configureCommonDisplays(){
        outputScreen = findViewById(R.id.calculationPanel)
        bufferScreen = findViewById(R.id.MemoryPanel)
    }

    private fun configureCommonButtons(){
        val backToMenuButton = findViewById<Button>(R.id.backToMenuButton)
        backToMenuButton.setOnClickListener{
            saveState("menu")
            openMenu()
        }

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

        mapOf(
            R.id.menuButtonSimpleCalculator to  { actionButtonACClick() },
            R.id.actionButtonAdd            to  { actionButtonAddClick() },
            R.id.actionButtonSub            to  { actionButtonSubClick() },
            R.id.actionButtonMultiply       to  { actionButtonMulClick() },
            R.id.actionButtonDivide         to  { actionButtonDivClick() },
            R.id.actionButtonChangeSign     to  { actionButtonChangeSign() },
            R.id.actionButtonEquals         to  { calculateExpression() },
            R.id.valueButtonDot             to  { actionButtonDotClick() },
            R.id.actionButtonPercentage     to  { actionButtonPercentageClick() },
            R.id.actionButtonBackspace      to  {actionButtonBackspaceClick() }
        ).forEach { (id, action) ->
            findViewById<Button>(id).apply {
                setOnClickListener { action() }
            }
        }

    }

    private fun configureScienceButtons(){
        mapOf(
            R.id.actionButtonXSquare    to      {actionButtonSquareClick()},
            R.id.actionButtonTenToX     to      {actionButton10ToX()},
            R.id.actionButtonXModY      to      {actionButtonXModYClick()},
            R.id.actionButtonXFactorial to      {actionButtonFactorialXClick()},
            R.id.actionButtonSqrt       to      {actionButtonSqrtClick()},
            R.id.actionButtonEToPowerX  to      {actionButtonEtoPowerXClick()},
            R.id.actionButtonLogX       to      {actionButtonLogX()},
            R.id.actionButtonLnx        to      {actionButtonLnXClick()},
            R.id.actionButtonLogNX      to      {actionButtonLogXYClick()},
            R.id.actionButtonXToPowerY  to      {actionButtonXToPowerYClick()},
            R.id.actionButtonXToPowerMinusY to  {actionButtonXToPowerMinusYClick()},
            R.id.actionButtonSinX       to      {trigonometryHandling("SIN")},
            R.id.actionButtonCosX       to      {trigonometryHandling("COS")},
            R.id.actionButtonTanX       to      {trigonometryHandling("TAN")},
            R.id.actionButtonCTanX       to      {trigonometryHandling("CTAN")},
            R.id.actionButtonASinX       to      {trigonometryHandling("ASIN")},
            R.id.actionButtonACosX       to      {trigonometryHandling("ACOS")},
            R.id.actionButtonATanX       to      {trigonometryHandling("ATAN")},
            R.id.actionButtonACtanX       to      {trigonometryHandling("ACTAN")}

            ).forEach{ (id,action)->findViewById<Button>(id).apply {
                setOnClickListener { action() }
        }}
    }


    private fun openScienceCalculator(){
        //Setting content view to science calculator
        setContentView(R.layout.science_calculator)
        //Adjusting padding if view is available
        adjustPadding()
        //Configuring common elements, because science calculator uses the same resources as simple, just adding its own to layout.
        configureCommonElements()
        //Configure science buttons only for science calculator
        configureScienceButtons();

    }

    private fun openSimpleCalculator(){
        //Setting content view to simple calculator.
        setContentView(R.layout.activity_main)
        //Adjusting padding if view is available.
        adjustPadding()
        //Configuring only common elements, because science calculator uses the same resources in it's core.
        configureCommonElements()
    }

    private fun adjustPadding(){
        //Adjusting padding if system bars exists.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun openMenu(){
        //Setting content view to manu layout
        setContentView(R.layout.menu)
        //Adjusting padding if view is available.
        adjustPadding()
        //Buttons in menu initialization.
        configureMenuButtons()
    }

    private fun saveState(state: String){
        getSharedPreferences("app_state", MODE_PRIVATE).edit() {
            putString("last_screen", state)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Setting starting state application in menu view.
        val prefs = getSharedPreferences("app_state", MODE_PRIVATE)
        val lastScreen = prefs.getString("last_screen", "menu")
        //Checking if screen is in menu or somewhere else.
        when (lastScreen) {
            "science" -> openScienceCalculator()
            "simple" -> openSimpleCalculator()
            else -> openMenu()
        }
    }

    private fun configureMenuButtons(){
        findViewById<Button>(R.id.menuButtonQuit).setOnClickListener {
            finishAffinity()
        }
        findViewById<Button>(R.id.menuButtonAboutMe).setOnClickListener{
            //Not implemented yet
            //TODO implement asap
        }
        findViewById<Button>(R.id.menuButtonSimpleCalculator).setOnClickListener{
            saveState("simple")
            openSimpleCalculator()
        }
        findViewById<Button>(R.id.menuButtonScienceCalculator).setOnClickListener{
            saveState("science")
            openScienceCalculator()
        }
    }

    private fun calculateExpression(){
        if (operation==null){
            return
        }
        val expression: String = outputScreen.text.toString()
        val numbers = mutableListOf<String>()
        val operators = mutableListOf<Char>()
        val currentNumber = StringBuilder()

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
            output = availableOperations[operation]?.invoke(firstNumber,secondNumber)
        }

        updateBuffer("")
        setTextToView(output.toString())
        isNegative = false
        isBracket = false
        operation = null
        val backspaceButton : Button = findViewById(R.id.actionButtonBackspace)
        backspaceButton.text = ">"
    }

    //Single click operations
    private fun possibleToComputate(): Boolean{
        return !(outputScreen.text.toString()=="" || operation != null || output!=null)
    }

    private fun actionButton10ToX(){
        if (!possibleToComputate()){
            return
        }
        output = 10.0.pow(outputScreen.text.toString().toDouble())
        updateBuffer("10^")
        setTextToView(output.toString());
    }

    private fun actionButtonLogX(){
        if (!possibleToComputate()){
            return
        }
        output = log10(outputScreen.text.toString().toDouble())
        updateBuffer("log")
        setTextToView(output.toString());
    }

    private fun actionButtonChangeSign() {
        if (!possibleToComputate()){
            return
        }
        isNegative = !isNegative
        if (isNegative){
            setTextToView("-"+outputScreen.text.toString())
        }else{
            setTextToView(outputScreen.text.toString().substring(1))
        }
    }

    private fun actionButtonLnXClick(){
        if (!possibleToComputate()){
            return
        }
        output = ln(outputScreen.text.toString().toDouble())
        updateBuffer("ln")
        setTextToView(output.toString())
    }

    private fun actionButtonEtoPowerXClick(){
        if (!possibleToComputate()){
            return
        }
        output = E.pow(outputScreen.text.toString().toDouble())
        updateBuffer("e^")
        setTextToView(output.toString())
    }

    private fun actionButtonFactorialXClick(){
        if (!possibleToComputate()){
            return
        }
        output = factorial(outputScreen.text.toString().toInt())
        updateBuffer("!")
        setTextToView(output.toString())
    }

    private fun actionButtonSquareClick(){
        if (!possibleToComputate()){
            return
        }
        output = outputScreen.text.toString().toDouble()*outputScreen.text.toString().toDouble()
        updateBufferEnd("²")
        setTextToView(output.toString())
    }

    private fun actionButtonSqrtClick(){
        if (!possibleToComputate()){
            return
        }
        output = sqrt(outputScreen.text.toString().toDouble())
        updateBuffer("√")
        setTextToView(output.toString());
    }

    private fun validateDotExpression(): Boolean{
        if (outputScreen.text.toString()=="" || output!=null){
            return false
        }
        if (operation==null && outputScreen.text.toString().contains(".")){
            return false
        }
        return true
    }

    private fun actionButtonDotClick(){
        if (!validateDotExpression()){
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

    private val trigonometryFunctions = mapOf(
        "SIN" to { output = sin(outputScreen.text.toString().toDouble()) },
        "ASIN" to { output = asin(outputScreen.text.toString().toDouble()) },
        "COS" to { output = cos(outputScreen.text.toString().toDouble()) },
        "ACOS" to { output = acos(outputScreen.text.toString().toDouble()) },
        "TAN" to { output = tan(outputScreen.text.toString().toDouble()) },
        "ATAN" to { output = atan(outputScreen.text.toString().toDouble()) },
        "CTAN" to { output = 1 / tan(outputScreen.text.toString().toDouble()) },
        "ACTAN" to { output = atan(outputScreen.text.toString().toDouble()) }
    )

    private fun trigonometryHandling(type: String) {
        if (!possibleToComputate()){
            return
        }
        // Check if the function exists in the map and then invoke it
        try {
            trigonometryFunctions[type]?.invoke() // Invoke the function corresponding to the type
            updateBuffer(type.lowercase())
            setTextToView(output.toString()) // Update output
        } catch (e: Exception) {
            // Handle the case when the function does not exist or there is an error
            return
        }
    }

    private fun actionButtonACClick(){
        output = null
        bufferScreen.text = ""
        outputScreen.text = ""
        operation = null
        isBracket = false
        val backspaceButton : Button = findViewById(R.id.actionButtonBackspace)
        backspaceButton.text = ">"
    }

    //Operations with more than one argument.

    private fun addOperationToScreen(sign:String, operationType:String){
        operation = operationType
        addTextToView(sign)
    }

    private fun actionButtonAddClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("+", "ADD")
    }

    private fun actionButtonSubClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("-", "SUBTRACTION")
    }

    private fun actionButtonXModYClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("mod", "XMODY")
    }

    private fun actionButtonXToPowerYClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("^", "XTOPOWERY")
    }

    private fun actionButtonXToPowerMinusYClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("^-", "XTOPOWERMINUSY")
    }

    private fun actionButtonPercentageClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("%","PERCENTAGE")
    }

    private fun actionButtonMulClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("×","MULTIPLICATION")
    }

    private fun actionButtonDivClick(){
        if (!possibleToComputate()){
            return
        }
        addOperationToScreen("÷","DIVISION")
    }

    private fun actionButtonLogXYClick(){
        if (!possibleToComputate()){
            return
        }

        val backspaceButton : Button = findViewById(R.id.actionButtonBackspace)
        backspaceButton.text = ""


        val buffer = outputScreen.text.toString()
        setTextToView("")
        addOperationToScreen("㏒($buffer, ", "LOGXY")
        isBracket = true
    }

    private fun actionButtonBackspaceClick(){
        if (operation=="LOGXY"){
            return
        }
        var chars_to_clear = 1
        val buffer : String = outputScreen.text.toString()
        val char: Char = buffer.lastOrNull() ?: return
        Log.e("Backspace", "Last character: $char")
        if (char == '-' && outputScreen.text.toString().length == 2){
            isNegative = false
        }else if(!char.isDigit() && char!='.' && char!='(' && char!=')'){
            if (operation=="XMODY"){
                chars_to_clear = 3
            }
            operation=null
        }
        outputScreen.text = outputScreen.text.toString().dropLast(chars_to_clear)
        if (outputScreen.text.toString().isEmpty()){
            operation=null
        }
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