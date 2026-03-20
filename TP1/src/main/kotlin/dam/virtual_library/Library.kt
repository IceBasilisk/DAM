package dam.virtual_library

class Library(val name: String) {
    val books = mutableListOf<Book>()

    fun addBook(book: Book) {
        books.add(book)
        totalBooksCreated++
    }

    fun borrowBook(title: String) {
        for (book in books) {
            if (book.title == title) {
                if (book.availableCopies > 0) {
                    book.availableCopies--

                    println("Successfully borrowed '$title'. Copies remaining: ${book.availableCopies}")
                } else {
                    println("Warning: Book is now out of stock!")
                }
                return
            }
        }
        println("Book $title not found.")
    }

    fun returnBook(title: String) {
        for (book in books) {
            if (book.title == title) {
                book.availableCopies += 1
                println("Book \"$title\" returned successfully. Copies available: ${book.availableCopies}")
                return
            }
        }
    }


    fun showBooks() {
        println("\n--- Library Catalog ---")
        for (book in books) {
            println(book)
            println(book.getStorageInfo())
            println()
        }
    }

    fun searchByAuthor(author: String) {
        println("Books by $author:")
        for (book in books) {
            if (book.author == author) {
                println("- ${book.title} (${book.yearDescription}, ${book.availableCopies} copy available)")
            }
        }
    }

    companion object {
        private var totalBooksCreated: Int = 0

        fun getTotalBooksCreated(): Int {
            return totalBooksCreated
        }
    }
}