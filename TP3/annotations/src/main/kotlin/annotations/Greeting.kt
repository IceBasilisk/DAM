package annotations

// Function only annotation
@Target(AnnotationTarget.FUNCTION)
// Only used for processing during compilation time
@Retention(AnnotationRetention.SOURCE)
/**
 * @param message greeting message to argument
 */
annotation class Greeting(val message: String)