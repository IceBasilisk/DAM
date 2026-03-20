package dam.virtual_library

class PhysicalBook(
    title: String,
    author: String,
    publicationYear: Int,
    availableCopies: Int,
    val weight: Int,
    val hasHardcover: Boolean = true
) :
    Book(title, author, publicationYear, availableCopies) {

    override fun toString(): String {
        return super.toString()
    }

    override fun getStorageInfo(): String {
        return "Storage: Physical book: ${weight}g, Hardcover: ${if (hasHardcover) "Yes" else "No"}"
    }
}