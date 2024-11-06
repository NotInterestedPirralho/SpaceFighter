package com.example.spacefighter

class Score {
    var points: Int = 0
        private set

    fun addPoints(value: Int) {
        points += value
    }

    fun reset() {
        points = 0
    }
}