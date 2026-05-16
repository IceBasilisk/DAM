package dam

data class SentimentResult(val rating: Int, val justification: String) {
    fun label(): String = when (rating) {
        1 -> "Very Negative"
        2 -> "Negative"
        3 -> "Slightly Negative"
        4 -> "Neutral"
        5 -> "Slightly Positive"
        6 -> "Positive"
        7 -> "Very Positive"
        else -> "Unknown"
    }

    override fun toString(): String =
        "Rating: $rating/7 - ${label()}\nJustification: $justification"
}