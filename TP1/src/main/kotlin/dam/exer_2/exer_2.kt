package dam.exer_2

import java.util.*

class Exer2 {
    fun run() {
        println("===== Kotlin Console Calculator =====")
        println("1 -> Addition (+)")
        println("2 -> Subtraction (-)")
        println("3 -> Multiplication (*)")
        println("4 -> Division (/)")
        println("5 -> Boolean AND (&&)")
        println("6 -> Boolean OR (||)")
        println("7 -> Boolean NOT (!)")
        println("8 -> Left Shift (shl)")
        println("9 -> Right Shift (shr)")
        println("0 -> Exit")
        while (true) {
            try {
                println("\nSelect operation:")
                val choice = readLine()?.toIntOrNull()
                    ?: throw InputMismatchException("Invalid menu option")

                if (choice == 0) {
                    println("Exiting calculator...")
                    break
                }

                when (choice) {
                    1, 2, 3, 4 -> {
                        print("Enter first number: ")
                        val a = readLine()?.toDoubleOrNull()
                            ?: throw NumberFormatException("Invalid number")
                        print("Enter second number: ")
                        val b = readLine()?.toDoubleOrNull()
                            ?: throw NumberFormatException("Invalid number")

                        val result = when (choice) {
                            1 -> a + b
                            2 -> a - b
                            3 -> a * b
                            4 -> {
                                if (b == 0.0) throw ArithmeticException("Division by 0")
                                a / b
                            }

                            else -> 0.0
                        }
                        println("\nResult: $result")
                    }

                    5, 6 -> {
                        print("Enter first boolean (true/false): ")
                        val a = readLine()?.toBooleanStrictOrNull()
                            ?: throw NumberFormatException("Invalid boolean")

                        print("Enter second boolean (true/false): ")
                        val b = readLine()?.toBooleanStrictOrNull()
                            ?: throw NumberFormatException("Invalid boolean")

                        val result = when (choice) {
                            5 -> a && b
                            6 -> a || b
                            else -> false
                        }
                        println("\nResult: $result")
                    }

                    7 -> {
                        print("Enter boolean (true/false): ")
                        val a = readLine()?.toBooleanStrictOrNull()
                            ?: throw NumberFormatException("Invalid boolean")

                        val result = !a
                        println("\nResult: $result")
                    }

                    8, 9 -> {
                        print("Enter integer number: ")
                        val a = readLine()?.toIntOrNull()
                            ?: throw NumberFormatException("Invalid integer")

                        print("Enter shift amount: ")
                        val shift = readLine()?.toIntOrNull()
                            ?: throw NumberFormatException("Invalid shift value")

                        val result = when (choice) {
                            8 -> a shl shift
                            9 -> a shr shift
                            else -> 0
                        }
                        println("\nResult: $result")
                    }

                    else -> println("Invalid option. Please try again.")
                }
            } catch (e: ArithmeticException) {
                println("Error: ${e.message}")
            } catch (e: NumberFormatException) {
                println("Error: ${e.message}")
            } catch (e: InputMismatchException) {
                println("Error: ${e.message}")
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
            }
        }
    }
}