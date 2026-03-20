package dam.exer_3

import kotlin.math.round

class Exer3 {
    val height: Double = 100.0
    val dropPercentage: Double = 0.6

    fun run()
    {
        val ballCourse = generateSequence(height){
            it * dropPercentage
        }

        ballCourse.takeWhile { it > 1 }.toList().forEach { println("${round(it*100)/100} m") }
    }
}