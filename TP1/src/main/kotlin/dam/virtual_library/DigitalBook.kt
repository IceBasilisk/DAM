package dam.virtual_library

class DigitalBook(
    title: String,
    author: String,
    publicationYear: Int,
    availableCopies: Int,
    val fileSize: Double,
    val format: String
) :
    Book(title, author, publicationYear, availableCopies) {
    init {
        if (format !in listOf("PDF", "EPUB", "MOBI")) {
            throw IllegalArgumentException("Invalid format: $format")
        }
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun getStorageInfo(): String {
        return "Storage: Stored digitally: $fileSize MB, Format: $format"
    }
}