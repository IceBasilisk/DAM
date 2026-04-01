package dam.exer_1

/**
 * Internally stores an ordered list of transformation steps
 * Each step is a List<String>
 */
class Pipeline {
    /**
     * List of functions (steps)
     */
    val stages: MutableMap<String, (List<String>) -> List<String>> = mutableMapOf()

    /**
     * Appends a named stage to the pipeline
     */
    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stages[name] = transform
    }

    /**
     * Runs the input through every stage in order and returns the final result
     */
    fun execute(input: List<String>): List<String> {

        var result: List<String> = mutableListOf()
        for (stage in stages.values) {
            result = stage(input)
        }
        return result
    }

    /**
     * Prints the name of each stage in order
     */
    fun describe() {
        println(stages.keys)
    }
}

/**
 *
 */
fun buildPipeline(steps: List<String>) {
    val pipeline = Pipeline()
    pipeline.addStage("Trim", { trim(steps) })
    pipeline.addStage("Filter errors", { filter(steps) })
    pipeline.addStage("Uppercase", { uppercase(steps) })
    pipeline.addStage("Add index", { add_index(steps) })
}

fun main() {
    val logs = listOf(
        "INFO: server started",
        "ERROR: disk full",
        "DEBUG: checking config",
        "ERROR: out of memory",
        "INFO: request received",
        "ERROR: connection timeout"
    )

    val pipeline = buildPipeline(logs)

    //pipeline.execute(logs)

}

fun trim(steps: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (step in steps) {
        result.add(step.trim())
    }
    return result
}

fun filter(steps: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (step in steps) {
        if (step.contains("ERROR"))
            result.add(step)
    }
    return result
}

fun uppercase(steps: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (step in steps) {
        result.add(step.uppercase())
    }
    return result
}

fun add_index(steps: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (i in 1..steps.size) {
        result.add("$i. ${steps[i]}")
    }
    return result
}