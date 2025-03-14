package core.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The interface Plugin manifest.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginManifest {

    /**
     * Type plugin type.
     *
     * @return the plugin type
     */
    PluginType type() default PluginType.ACTION;

    /**
     * Name string.
     *
     * @return the string
     */
    String name() default "";
}