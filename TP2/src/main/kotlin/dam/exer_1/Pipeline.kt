package dam.exer_1

/**
 * Internally stores an ordered list of transformation steps
 * Each step is a List<String>
 */
class Pipeline {
    val steps = mutableListOf<Event>()

    /**
     * Appends a named stage to the pipeline
     */
    fun addStage(name: String, transform: (List<String>) -> List<String>) {

    }

    /**
     * Runs the input through every stage in order and returns the final result
     */
    fun execute(input: List<String>): List<String> {

    }

    /**
     * Prints the name of each stage in order
     */
    fun describe() {

    }
}

/**
 *
 */
fun buildPipeline() {

}

fun main() {
    val logs = listOf (
        "INFO: server started",
        "ERROR: disk full",
        "DEBUG: checking config",
        "ERROR: out of memory",
        "INFO: request received",
        "ERROR: connection timeout"
    )


}