package annotations

// Function only annotation
@Target(AnnotationTarget.FUNCTION)
// Only used for processing during compilation time
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)