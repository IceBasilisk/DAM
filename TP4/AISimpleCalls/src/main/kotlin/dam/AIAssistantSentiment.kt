package dam

import com.google.gson.Gson

class AIAssistantSentiment(private val delegate: AIAssistant) : AIAssistant by delegate {
    private val GSON = Gson()

    override fun buildPrompt(input: String): String {
        return """
            You are a sentiment analysis tool.
            Analyse the sentiment of the text provided by the user.
            You MUST respond ONLY with a valid JSON object, with NO extra text, NO markdown, NO backticks.
            The JSON object must have exactly two fields:
            - "rating": an integer from 1 to 7, where:
                1 = Very Negative
                2 = Negative
                3 = Slightly Negative
                4 = Neutral
                5 = Slightly Positive
                6 = Positive
                7 = Very Positive
            - "justification": a short string explaining the rating.
            Text to analyse: "$input"
        """.trimIndent()
    }

    // Must override processInput explicitly — delegation bypasses buildPrompt override
    override suspend fun processInput(input: String): String {
        val formattedPrompt = buildPrompt(input)
        return delegate.apiCallWithBackoff(formattedPrompt)
    }

    suspend fun analysesentiment(input: String): SentimentResult {
        val raw = processInput(input)

        val clean = raw
            .replace(Regex("```json\\s*"), "")
            .replace(Regex("```\\s*"), "")
            .trim()

        val jsonStart = clean.indexOf('{')
        val jsonEnd = clean.lastIndexOf('}')

        if (jsonStart == -1 || jsonEnd == -1) {
            throw Exception("No JSON object found in response: $clean")
        }

        return GSON.fromJson(clean.substring(jsonStart, jsonEnd + 1), SentimentResult::class.java)
    }
}