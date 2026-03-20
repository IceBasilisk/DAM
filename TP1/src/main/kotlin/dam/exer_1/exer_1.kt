package dam.exer_1

class Exer1 {

    fun run() {
        // Construtor IntArray
        val arrayA = IntArray(50) { i ->
            val number = i + 1
            number * number
        }

        // Range and map()
        val arrayB = (1..50)
            .map { it * it }
            .toIntArray()

        // Array with constructor
        val arrayC = Array(50) { i ->
            val number = i + 1
            number * number
        }

        println("a) " + arrayA.joinToString())
        println("b) " + arrayB.joinToString())
        println("c) " + arrayC.joinToString())

    }

}