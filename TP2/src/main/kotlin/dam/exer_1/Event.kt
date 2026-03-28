package dam.exer_1

/**
 * Event log system
 */
sealed class Event {
    /**
     * User login event
     */
    class Login(val username: String, val timestamp: Long) : Event() {}

    /**
     * Purchase event
     */
    class Purchase(val username: String, val amount: Double, val timestamp: Long) : Event() {}

    /**
     * User logout event
     */
    class Logout(val username: String, val timestamp: Long) : Event() {}
}

/**
 * Returns only the events associated with a specific user
 */
fun List<Event>.filterByUser(username: String): List<Event> {
    val userEvents = mutableListOf<Event>()

    // for each event
    for (event in this) {
        // check user's name
        val eventUsername = when (event) {
            is Event.Login -> event.username
            is Event.Purchase -> event.username
            is Event.Logout -> event.username
        }
        // add to list if the names are the same
        if (eventUsername == username) {
            userEvents.add(event)
        }
    }
    return userEvents
}

/**
 * Returns the total amount spent by that user across all Purchase events
 */
fun List<Event>.totalSpent(username: String): Double {
    var totalAmount = 0.0

    // for each event
    for (event in this) {
        val eventUsername = when (event) {
            is Event.Login -> event.username
            is Event.Purchase -> event.username
            is Event.Logout -> event.username
        }
        // sum the amount of all purchases
        if (eventUsername == username && event is Event.Purchase) {
            totalAmount += event.amount
        }
    }
    return totalAmount
}

/**
 * Applies the handler to each event on a list
 */
fun processEvents(events: List<Event>, handler: (Event) -> Unit) {
    for (event in events) {
        handler(event)
    }
}

/**
 * Human-readable description of each event
 */
fun describe(event: Event) {
    when (event) {
        is Event.Login -> println("[LOGIN]     ${event.username} logged in at t=${event.timestamp}")
        is Event.Purchase -> println("[PURCHASE]  ${event.username} spent \$${event.amount} in at t=${event.timestamp}")
        is Event.Logout -> println("[LOGOUT]    ${event.username} logged out at t=${event.timestamp}")
    }
}

fun main() {
    val events = listOf(
        Event.Login("alice", 1_000),
        Event.Purchase("alice", 49.99, 1_100),
        Event.Purchase("bob", 19.99, 1_200),
        Event.Login("bob", 1_050),
        Event.Purchase("alice", 15.00, 1_300),
        Event.Logout("alice", 1_400),
        Event.Logout("bob", 1_500)
    )
    println("=================================================")
    processEvents(events, ::describe)
    println()
    println("Total spent by alice : $${String.format("%.2f", events.totalSpent("alice"))}")
    println("Total spent by bob : $${String.format("%.2f", events.totalSpent("bob"))}")
    println()
    println("Events for alice:")
    for (event in events.filterByUser(username = "alice")) {
        when (event) {
            is Event.Login -> println("  Login(username=${event.username}, timestamp=${event.timestamp})")
            is Event.Purchase -> println("  Purchase(username=${event.username}, amount=${event.amount}, timestamp=${event.timestamp})")
            is Event.Logout -> println("  Logout (username=${event.username}, timestamp=${event.timestamp})")
        }
    }
    println("=================================================")
}