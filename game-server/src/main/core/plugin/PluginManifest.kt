package core.plugin

/**
 * Annotation for marking plugin metadata.
 *
 * @property type The plugin type.
 * @property name The plugin name.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class PluginManifest(
    val type: PluginType = PluginType.ACTION,
    val name: String = ""
)