package com.example.calculator

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