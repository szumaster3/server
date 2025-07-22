package core.game.component

import java.sql.SQLException

/**
 * Represents the definition of a UI component, including its type, behavior, and associated plugin.
 */
class ComponentDefinition {
    /**
     * The interface type of this component.
     */
    @JvmField
    var type: InterfaceType = InterfaceType.DEFAULT

    /**
     * Indicates whether this component is walkable.
     */
    var isWalkable: Boolean = false

    /**
     * The tab index of this component.
     */
    @JvmField
    var tabIndex: Int = -1

    /**
     * The plugin associated with this component.
     */
    @JvmField
    var plugin: ComponentPlugin? = null

    /**
     * Parses the component definition from string values.
     *
     * @param type     The type identifier as a string.
     * @param walkable Whether the component is walkable, as a string.
     * @param tabIndex The tab index as a string.
     * @return The parsed {@code ComponentDefinition} instance.
     * @throws SQLException If an error occurs while parsing values.
     */
    @Throws(SQLException::class)
    fun parse(
        type: String,
        walkable: String,
        tabIndex: String,
    ): ComponentDefinition {
        try {
            this.type = InterfaceType.values()[type.toInt()]
            this.isWalkable = walkable.toBoolean()
            this.tabIndex = tabIndex.toInt()
        } catch (e: NumberFormatException) {
            throw SQLException("Error parsing ComponentDefinition values", e)
        }
        return this
    }

    /**
     * Retrieves the window pane ID based on the resizable state.
     *
     * @param resizable Whether the interface is resizable.
     * @return The corresponding window pane ID.
     */
    fun getWindowPaneId(resizable: Boolean): Int = if (resizable) type.resizablePaneId else type.fixedPaneId

    /**
     * Retrieves the child ID based on the resizable state.
     *
     * @param resizable Whether the interface is resizable.
     * @return The corresponding child ID.
     */
    fun getChildId(resizable: Boolean): Int = if (resizable) type.resizableChildId else type.fixedChildId

    override fun toString(): String = "ComponentDefinition [type=$type, walkable=$isWalkable, tabIndex=$tabIndex, plugin=$plugin]"

    companion object {
        private val DEFINITIONS: MutableMap<Int, ComponentDefinition> = HashMap()

        /**
         * Retrieves the component definition for the given component ID.
         * If the definition does not exist, a new one is created and stored.
         *
         * @param componentId The component ID.
         * @return The corresponding {@code ComponentDefinition}.
         */
        @JvmStatic
        fun forId(componentId: Int): ComponentDefinition = DEFINITIONS.computeIfAbsent(componentId) { ComponentDefinition() }

        /**
         * Associates a plugin with a component definition.
         *
         * @param id     The component ID.
         * @param plugin The plugin to associate.
         */
        @JvmStatic
        fun put(
            id: Int,
            plugin: ComponentPlugin?,
        ) {
            forId(id).plugin = plugin
        }

        @JvmStatic
        fun put(
            id: Int,
            definition: ComponentDefinition,
        ) {
            DEFINITIONS[id] = definition
        }

        /**
         * Retrieves all component definitions.
         *
         * @return A map of component definitions.
         */
        val definitions: Map<Int, ComponentDefinition>
            get() = DEFINITIONS
    }
}
