package dam.exer_1

import kotlin.math.sqrt

data class Vec2(val x: Double, val y: Double) : Comparable<Vec2> {

    operator fun plus(other: Vec2): Vec2 {
        return Vec2(x + other.x, y + other.y)
    }

    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y)
    }

    operator fun times(scalar: Double): Vec2 {
        return Vec2(x * scalar, y * scalar)
    }

    operator fun unaryMinus(): Vec2 {
        return Vec2(-x, -y)
    }

    override operator fun compareTo(other: Vec2): Int {
        val mag1 = this.magnitude()
        val mag2 = other.magnitude()
        return mag1.compareTo(mag2)
    }


    fun magnitude(): Double {
        return sqrt(x * x + y * y)
    }

    fun dot(other: Vec2): Double {
        return x * other.x + y * other.y
    }

    fun normalized(): Vec2 {
        if (this.magnitude() == 0.0)
            throw IllegalStateException("This is the zero vector.")
        return Vec2(x / this.magnitude(), y / this.magnitude())
    }

    operator fun get(index: Int): Double {
        if (index == 0) return this.x
        else if (index == 1) return this.y
        else
            throw IndexOutOfBoundsException("Value must either be 0 or 1.")

    }
}

fun main() {
    val a = Vec2(3.0, 4.0)
    val b = Vec2(1.0, 2.0)
    println("a = $a") // a = Vec2 (x=3.0 , y =4.0)
    println("b = $b") // b = Vec2 (x=1.0 , y =2.0)
    println("a + b = ${a + b}") // a + b = Vec2 (x=4.0 , y =6.0)
    println("a - b = ${a - b}") // a - b = Vec2 (x=2.0 , y =2.0)
    println("a * 2.0 = ${a * 2.0} ") // a * 2.0 = Vec2 (x=6.0 , y =8.0)
    println("-a = ${-a}") // -a = Vec2 (x= -3.0 , y= -4.0)
    println("|a| = ${a.magnitude()}") // |a| = 5.0
    println("a dot b = ${a.dot(b)}") // a dot b = 11.0
    println("norm (a) = ${a.normalized()}") // norm (a) = Vec2 (x=0.6 , y =0.8)
    println("a[0] = ${a[0]}") // a[0] = 3.0
    println("a[1] = ${a[1]}") // a[1] = 4.0
    println("a > b = ${a > b}") // a > b = true
    println("a < b = ${a < b}") // a < b = false
    val vectors = listOf(Vec2(1.0, 0.0), Vec2(3.0, 4.0), Vec2(0.0, 2.0))
    println("Longest = ${vectors.max()}") // Longest = Vec2 (x=3.0 , y = 4.0)
    println("Shortest = ${vectors.min()}") // Shortest = Vec2 (x=1.0 , y = 0.0)
}