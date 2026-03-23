package dam.exer_1

/**
 * Stage of pipeline
 */
data class Stage(val name: String,
                 val transform: (List<String>) -> List<String>)