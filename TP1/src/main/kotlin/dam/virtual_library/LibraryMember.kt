package dam.virtual_library

data class LibraryMember(
    val name: String,
    val membershipId: Int,
    val borrowedBooks: List<String>
)
