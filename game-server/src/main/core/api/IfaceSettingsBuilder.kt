package core.api

/**
 * Builder for generating interface settings masks used in player interfaces.
 * Configures options such as right-click menu options, "use-on" settings,
 * slot switching, and event propagation depth.
 *
 * @author Ceikry
 */
class IfaceSettingsBuilder {
    /**
     * Bitmask value representing the combined interface settings.
     */
    private var value = 0

    /**
     * Enables a specific right-click option by its index (0-9).
     *
     * @param optionId The index of the right-click option to enable.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if optionId is out of range.
     */
    fun enableOption(optionId: Int): IfaceSettingsBuilder {
        require(optionId in 0..9) { "Option index must be 0-9." }
        value = value or (0x1 shl (optionId + 1))
        return this
    }

    /**
     * Enables multiple right-click options by their indices.
     *
     * @param ids Indices of the options to enable.
     * @return This builder for chaining.
     */
    fun enableOptions(vararg ids: Int): IfaceSettingsBuilder {
        ids.forEach { enableOption(it) }
        return this
    }

    /**
     * Enables right-click options within a range.
     *
     * @param ids Range of option indices to enable.
     * @return This builder for chaining.
     */
    fun enableOptions(ids: IntRange): IfaceSettingsBuilder {
        ids.forEach { enableOption(it) }
        return this
    }

    /**
     * Enables all right-click options (0 through 9).
     */
    fun enableAllOptions(): IfaceSettingsBuilder = enableOptions(0..9)

    /**
     * Enables right-click options using string indices (their indices in the vararg).
     *
     * @param options String representations of options.
     * @return This builder for chaining.
     */
    fun enableOptions(vararg options: String?): IfaceSettingsBuilder {
        options.indices.forEach { enableOption(it) }
        return this
    }

    /**
     * Configures "use-on" flags for interface components,
     * determining where the 'use' option is available.
     *
     * @param groundItems Include ground items.
     * @param npcs Include NPCs.
     * @param objects Include game objects.
     * @param otherPlayer Include other players.
     * @param selfPlayer Include self player.
     * @param component Include the component itself.
     * @return This builder for chaining.
     */
    fun setUseOnSettings(groundItems: Boolean, npcs: Boolean, objects: Boolean, otherPlayer: Boolean, selfPlayer: Boolean, component: Boolean): IfaceSettingsBuilder {
        var useFlag = 0
        if (groundItems) {
            useFlag = useFlag or 0x1
        }
        if (npcs) {
            useFlag = useFlag or 0x2
        }
        if (objects) {
            useFlag = useFlag or 0x4
        }
        if (otherPlayer) {
            useFlag = useFlag or 0x8
        }
        if (selfPlayer) {
            useFlag = useFlag or 0x10
        }
        if (component) {
            useFlag = useFlag or 0x20
        }
        value = value or (useFlag shl 11)
        return this
    }

    /**
     * Sets event propagation depth for the interface.
     * A higher depth allows events to propagate to parent interfaces.
     *
     * @param depth Depth value (0-7).
     * @return This builder for chaining.
     * @throws IllegalArgumentException if depth is out of range.
     */
    fun setInterfaceEventsDepth(depth: Int): IfaceSettingsBuilder {
        require(!(depth < 0 || depth > 7)) { "depth must be 0-7." }
        value = value and (0x7 shl 18).inv()
        value = value or (depth shl 18)
        return this
    }

    /**
     * Enables slot switching in the container (allows rearranging items).
     */
    fun enableSlotSwitch(): IfaceSettingsBuilder {
        value = value or (1 shl 21)
        return this
    }

    /**
     * Enables the "Use" option on items in the container.
     */
    fun enableUseOption(): IfaceSettingsBuilder {
        value = value or (1 shl 17)
        return this
    }

    /**
     * Enables the "Examine" option on items in the container.
     */
    fun enableExamine(): IfaceSettingsBuilder {
        value = value or (1 shl 9)
        return this
    }

    /**
     * Allows using items directly on the component itself.
     */
    fun enableUseOnSelf(): IfaceSettingsBuilder {
        value = value or (1 shl 22)
        return this
    }

    /**
     * Allows swapping items with empty (null) slots.
     */
    fun enableNullSlotSwitch(): IfaceSettingsBuilder {
        value = value or (1 shl 23)
        return this
    }

    /**
     * Builds the final bitmask representing all configured settings.
     */
    fun build(): Int = value
}
