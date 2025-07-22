package core.plugin

/**
 * Marks a class as content that should be initialized at startup.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Initializable