package core.api

/**
 * A builder class used to generate interface settings hashes for player interfaces.
 * It allows configuring various interface behaviors like enabling right-click options,
 * setting use-on settings, slot switching, and interface event depth.
 *
 * @author Ceikry
 */
class IfaceSettingsBuilder {
    /**
     * The value that will be sent in the access mask packet.
     * This value is incrementally updated as various settings are applied.
     */
    private var value = 0

    /**
     * Enables a specific right-click option for the interface.
     * If the option is disallowed, it might not appear in the context menu or may cause an error if clicked.
     *
     * @param optionId The index of the option to be enabled (0-9).
     * @return The builder instance for method chaining.
     * @throws IllegalArgumentException If the optionId is not in the range 0-9.
     */
    fun enableOption(optionId: Int): IfaceSettingsBuilder {
        require(!(optionId < 0 || optionId > 9)) { "Option index must be 0-9." }
        value = value or (0x1 shl optionId + 1)
        return this
    }

    /**
     * Enables multiple right-click options for the interface.
     *
     * @param ids The indices of the options to be enabled.
     * @return The builder instance for method chaining.
     */
    fun enableOptions(vararg ids: Int): IfaceSettingsBuilder {
        for (i in ids.indices) {
            enableOption(ids[i])
        }
        return this
    }

    /**
     * Enables right-click options for a given range of option indices.
     *
     * @param ids The range of option indices to be enabled.
     * @return The builder instance for method chaining.
     */
    fun enableOptions(ids: IntRange): IfaceSettingsBuilder {
        for (i in ids.start..ids.endInclusive) {
            enableOption(i)
        }
        return this
    }

    /**
     * Enables all right-click options (0-9).
     *
     * @return The builder instance for method chaining.
     */
    fun enableAllOptions(): IfaceSettingsBuilder {
        for (i in 0..9) {
            enableOption(i)
        }
        return this
    }

    /**
     * Enables multiple right-click options using string indices.
     *
     * @param options The string indices representing the options to be enabled.
     * @return The builder instance for method chaining.
     */
    fun enableOptions(vararg options: String?): IfaceSettingsBuilder {
        for (i in options.indices) {
            enableOption(i)
        }
        return this
    }

    /**
     * Sets the "use on" option for various interface components.
     * This determines whether the 'use' option will appear for ground items, NPCs, objects, players, etc.
     *
     * @param groundItems Whether ground items are eligible for the 'use' option.
     * @param npcs Whether NPCs are eligible for the 'use' option.
     * @param objects Whether objects are eligible for the 'use' option.
     * @param otherPlayer Whether other players are eligible for the 'use' option.
     * @param selfPlayer Whether the current player is eligible for the 'use' option.
     * @param component Whether the component itself is eligible for the 'use' option.
     * @return The builder instance for method chaining.
     */
    fun setUseOnSettings(
        groundItems: Boolean,
        npcs: Boolean,
        objects: Boolean,
        otherPlayer: Boolean,
        selfPlayer: Boolean,
        component: Boolean,
    ): IfaceSettingsBuilder {
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
     * Sets the depth of interface events.
     * The depth determines how events propagate to other interfaces. For example, an inventory interface
     * with depth 1 allows clicks in the inventory to trigger events in the game frame interface.
     *
     * @param depth The depth value (0-7) indicating how deep events should propagate.
     * @return The builder instance for method chaining.
     * @throws IllegalArgumentException If the depth is not within the range 0-7.
     */
    fun setInterfaceEventsDepth(depth: Int): IfaceSettingsBuilder {
        require(!(depth < 0 || depth > 7)) { "depth must be 0-7." }
        value = value and (0x7 shl 18).inv()
        value = value or (depth shl 18)
        return this
    }

    /**
     * Enables slot switching for the interface container, allowing items to be rearranged.
     *
     * @return The builder instance for method chaining.
     */
    fun enableSlotSwitch(): IfaceSettingsBuilder {
        value = value or (1 shl 21)
        return this
    }

    /**
     * Enables the "Use" option for items in the interface container.
     *
     * @return The builder instance for method chaining.
     */
    fun enableUseOption(): IfaceSettingsBuilder {
        value = value or (1 shl 17)
        return this
    }

    /**
     * Enables the "Examine" option for items in the interface container.
     *
     * @return The builder instance for method chaining.
     */
    fun enableExamine(): IfaceSettingsBuilder {
        value = value or (1 shl 9)
        return this
    }

    /**
     * Allows the component to have items used on it (e.g., use items directly on the component).
     *
     * @return The builder instance for method chaining.
     */
    fun enableUseOnSelf(): IfaceSettingsBuilder {
        value = value or (1 shl 22)
        return this
    }

    /**
     * Allows switching items with empty (null) slots in the interface container.
     *
     * @return The builder instance for method chaining.
     */
    fun enableNullSlotSwitch(): IfaceSettingsBuilder {
        value = value or (1 shl 23)
        return this
    }

    /**
     * Builds and returns the final value representing the configured interface settings.
     *
     * @return The generated settings value.
     */
    fun build(): Int {
        return value
    }
}
