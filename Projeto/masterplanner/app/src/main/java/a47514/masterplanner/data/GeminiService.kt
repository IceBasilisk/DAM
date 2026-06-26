package a47514.masterplanner.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {

    /**
     * API key for Google Generative AI.
     */
    private const val API_KEY = "AQ.Ab8RN6IQZbyqshAY3yqnZgwh9xrCXHU5rXxjfdDVOFt-8nUaKw"

    /**
     * Gemini model to use.
     */
    private const val MODEL = "gemini-2.5-flash-lite" // same model as AIAssistantGemini

    /**
     * HTTP client for making API requests.
     */
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Builds an HTTP request for the Gemini API. Mirrors AIAssistantGemini.buildRequest().
     */
    private fun buildRequest(prompt: String): Request {
        /**
         * Prompt.
         */
        val messagesArray = JSONArray()
            .put(
                JSONObject()
                    .put("role", "user")
                    .put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            )

        /**
         * Request body.
         */
        val requestBody = JSONObject()
            .put("contents", messagesArray)
            .toString()

        return Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$API_KEY")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
    }

    /**
     * Parses the Gemini "candidates" response structure. Mirrors AIAssistant.makeApiCall().
     */
    private fun parseResponse(responseBody: String): String {
        val json = JSONObject(responseBody)
        if (!json.has("candidates") || json.getJSONArray("candidates").length() == 0)
            return "[]"
        val content = json.getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
        val parts = content.getJSONArray("parts")
        return parts.getJSONObject(0).getString("text").trim()
    }

    /**
     * Given a roadmap title, returns a list of suggested task name strings.
     * Follows the same flow as AIAssistant.processInput() → buildPrompt() → makeApiCall().
     */
    suspend fun suggestTaskNames(roadmapTitle: String): List<String> = withContext(Dispatchers.IO) {
        try {
            // Mirrors AIAssistant.buildPrompt() — focused prompt for task suggestions
            val prompt = """
                You are helping a user plan tasks for their roadmap titled: "$roadmapTitle".
                Suggest exactly 6 short, actionable task names that would fit this roadmap.
                Respond ONLY with a JSON array of strings. No explanations, no markdown, no extra text.
                Example format: ["Task One", "Task Two", "Task Three", "Task Four", "Task Five", "Task Six"]
            """.trimIndent()

            val request = buildRequest(prompt)

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string() ?: return@withContext emptyList()
                val text = parseResponse(body)
                val jsonArray = JSONArray(text)
                List(jsonArray.length()) { jsonArray.getString(it) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}