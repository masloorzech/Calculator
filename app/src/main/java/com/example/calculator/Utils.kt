package com.example.calculator

import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

fun factorial(x: Int): Double{
    if (x<0){
        return Double.NaN
    }
    var result: Double = 1.0
    for (i in 1..x) {
        result *= i
    }
    return result
}
