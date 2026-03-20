package dam.virtual_library

abstract class Book(val title: String, val author: String, val publicationYear: Int, availableCopies: Int) {

    val yearDescription: String
        get() {
            return when {
                publicationYear < 1980 -> "Classic"
                publicationYear in 1980..2010 -> "Modern"
                else -> "Contemporary"
            }
        }

    var availableCopies: Int = availableCopies
        set(value) {
            require(value >= 0) { "Available copies must be greater than zero" }
            field = value
        }

    init {
        println("Book \"$title\" by $author was added to the database.")
    }

    override fun toString(): String {
        return "Title: $title, Author: $author, Era: $yearDescription, Available: $availableCopies copies"
    }

    abstract fun getStorageInfo(): String
}