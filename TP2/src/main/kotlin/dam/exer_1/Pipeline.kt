package dam.exer_1

import com.sun.org.apache.xml.internal.serialize.Printer

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

        var result = input
        for (stage in stages.values) {
            result = stage(result)
        }
        return result


    }

    /**
     * Prints the name of each stage in order
     */
    fun describe() {
        var index = 0
        println("Pipeline stages:")
        stages.forEach(
            { println("   ${++index}. ${it.key}") }
        )
    }
}

/**
 *
 */
fun buildPipeline(transform: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline()
    pipeline.transform()
    return pipeline
}

fun main() {
    val logs = listOf(
        " INFO: server started ",
        " ERROR: disk full ",
        " DEBUG: checking config ",
        " ERROR: out of memory ",
        " INFO: request received ",
        " ERROR: connection timeout "
    )

    val pipeline = buildPipeline {
        addStage("Trim") { it.map { it.trim() } }
        addStage("Filter errors") { it.filter { "ERROR" in it } }
        addStage("Uppercase") { it.map { it.uppercase() } }
        addStage("Add index") {
            var index = 0
            it.map { "${++index}. $it" }
        }
    }

    pipeline.describe()
    println("\nResult: ")
    pipeline.execute(logs).forEach {
        println("   $it")
    }
}