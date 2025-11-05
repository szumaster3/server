package core.game.interaction

import core.game.event.InteractionEvent
import core.game.event.UseWithEvent
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location

object InteractionListeners {
    // Maps interaction keys to handler functions.
    private val listeners = HashMap<String, (Player, Node) -> Boolean>(1000)

    // Maps use-with combinations to handlers.
    private val useWithListeners = HashMap<String, (Player, Node, Node) -> Boolean>(1000)

    // Handlers that match any "used" item with a specific "with" item.
    private val useAnyWithListeners = HashMap<String, (Player, Node, Node) -> Boolean>(10)

    // Wildcard handlers based on predicates.
    private val useWithWildcardListeners = HashMap<Int, ArrayList<Pair<(Int, Int) -> Boolean, (Player, Node, Node) -> Boolean>>>(10)

    // Overrides destination logic for specific interactions.
    private val destinationOverrides = HashMap<String, (Entity, Node) -> Location>(100)

    // Equipment-related handlers.
    private val equipListeners = HashMap<String, (Player, Node) -> Boolean>(10)

    // Metadata for interaction scripting.
    private val interactions = HashMap<String, InteractionListener.InteractionMetadata>()

    // Metadata for use-with scripting.
    private val useWithInteractions = HashMap<String, InteractionListener.UseWithMetadata>()

    // Class names of handlers that should be executed instantly.
    val instantClasses = HashSet<String>()

    /**
     * Adds a basic interaction listener.
     */
    @JvmStatic
    fun add(id: Int, type: Int, option: Array<out String>, method: (Player, Node) -> Boolean) {
        for (opt in option) {
            val key = "$id:$type:${opt.lowercase()}"
            if (!validate(key)) {
                throw IllegalStateException(
                    "$opt for $id with type ${IntType.values()[type].name} already defined! Existing use: [${listeners[key]!!::class}]",
                )
            }
            listeners[key] = method
        }
    }

    private fun validate(key: String): Boolean = !listeners.containsKey(key) && !useWithListeners.containsKey(key)

    /**
     * Adds a listener for multiple ids.
     */
    @JvmStatic
    fun add(ids: IntArray, type: Int, option: Array<out String>, method: (Player, Node) -> Boolean) {
        for (id in ids) {
            add(id, type, option, method)
        }
    }

    /**
     * Adds a fallback interaction by option and type.
     */
    @JvmStatic
    fun add(option: String, type: Int, method: (Player, Node) -> Boolean) {
        val key = "$type:${option.lowercase()}"
        if (!validate(key)) {
            throw IllegalStateException(
                "Catchall listener for $option on type ${IntType.values()[type].name} already in use: ${listeners[key]!!::class}",
            )
        }
        listeners[key] = method
    }

    /**
     * Adds fallback listeners for multiple options.
     */
    @JvmStatic
    fun add(options: Array<out String>, type: Int, method: (Player, Node) -> Boolean) {
        for (opt in options) {
            add(opt.lowercase(), type, method)
        }
    }

    /**
     * Adds a use-with listener for two specific items.
     */
    @JvmStatic
    fun add(used: Int, with: Int, type: Int, method: (Player, Node, Node) -> Boolean) {
        val key = "$used:$with:$type"
        val altKey = "$with:$used:$type"
        if (!validate(key) || !validate(altKey)) {
            throw IllegalStateException(
                "Usewith using $used with $with for type ${IntType.values()[type]} already in use: [${(useWithListeners[key] ?: useWithListeners[altKey])!!::class}",
            )
        }
        useWithListeners[key] = method
    }

    /**
     * Adds use-with listeners for one item used with multiple others.
     */
    @JvmStatic
    fun add(type: Int, used: Int, with: IntArray, method: (Player, Node, Node) -> Boolean) {
        for (id in with) {
            add(used = used, with = id, type = type, method = method)
        }
    }

    /**
     * Adds a wildcard use-with handler with predicate filter.
     */
    @JvmStatic
    fun addWildcard(type: Int, predicate: (used: Int, with: Int) -> Boolean, handler: (player: Player, used: Node, with: Node) -> Boolean) {
        if (!useWithWildcardListeners.containsKey(type)) {
            useWithWildcardListeners[type] = ArrayList()
        }
        useWithWildcardListeners[type]!!.add(Pair(predicate, handler))
    }

    /**
     * Adds equip handler for an item.
     */
    @JvmStatic
    fun addEquip(id: Int, method: (Player, Node) -> Boolean) {
        equipListeners["equip:$id"] = method
    }

    /**
     * Adds unequip handler for an item.
     */
    @JvmStatic
    fun addUnequip(id: Int, method: (Player, Node) -> Boolean) {
        equipListeners["unequip:$id"] = method
    }

    /**
     * Returns equip handler for an item.
     */
    @JvmStatic
    fun getEquip(id: Int): ((Player, Node) -> Boolean)? = equipListeners["equip:$id"]

    /**
     * Returns unequip handler for an item.
     */
    @JvmStatic
    fun getUnequip(id: Int): ((Player, Node) -> Boolean)? = equipListeners["unequip:$id"]

    /**
     * Gets a use-with handler for two items.
     */
    @JvmStatic
    fun get(used: Int, with: Int, type: Int): ((Player, Node, Node) -> Boolean)? {
        val method = useWithListeners["$used:$with:$type"] ?: useAnyWithListeners["$with:$type"]
        if (method != null) {
            return method
        }
        val handlers = useWithWildcardListeners[type] ?: return null
        for (pair in handlers) {
            if (pair.first(used, with)) {
                return pair.second
            }
        }
        return null
    }

    /**
     * Gets a handler for a specific node interaction.
     */
    @JvmStatic
    fun get(id: Int, type: Int, option: String): ((Player, Node) -> Boolean)? = listeners["$id:$type:${option.lowercase()}"]

    /**
     * Gets a fallback handler for an interaction type and option.
     */
    @JvmStatic
    fun get(option: String, type: Int): ((Player, Node) -> Boolean)? = listeners["$type:${option.lowercase()}"]

    /**
     * Adds destination override for a type + id.
     */
    @JvmStatic
    fun addDestOverride(type: Int, id: Int, method: (Entity, Node) -> Location) {
        destinationOverrides["$type:$id"] = method
    }

    /**
     * Adds destination overrides by string options.
     */
    @JvmStatic
    fun addDestOverrides(type: Int, options: Array<out String>, method: (Entity, Node) -> Location) {
        for (opt in options) {
            destinationOverrides["$type:${opt.lowercase()}"] = method
        }
    }

    /**
     * Adds destination overrides for multiple ids and options.
     */
    @JvmStatic
    fun addDestOverrides(type: Int, ids: IntArray, options: Array<out String>, method: (Entity, Node) -> Location) {
        for (id in ids) {
            for (opt in options) {
                destinationOverrides["$type:$id:${opt.lowercase()}"] = method
            }
        }
    }

    /**
     * Gets override for specific type, id, and option.
     */
    @JvmStatic
    fun getOverride(type: Int, id: Int, option: String): ((Entity, Node) -> Location)? = destinationOverrides["$type:$id:${option.lowercase()}"]

    /**
     * Gets override for specific type and id.
     */
    @JvmStatic
    fun getOverride(type: Int, id: Int): ((Entity, Node) -> Location)? = destinationOverrides["$type:$id"]

    /**
     * Gets override for specific type and string option.
     */
    @JvmStatic
    fun getOverride(type: Int, option: String): ((Entity, Node) -> Location)? = destinationOverrides["$type:$option"]

    /**
     * Runs equip or unequip logic.
     */
    @JvmStatic
    fun run(id: Int, player: Player, node: Node, isEquip: Boolean): Boolean {
        player.dispatch(InteractionEvent(node, if (isEquip) "equip" else "unequip"))
        if (isEquip) {
            return equipListeners["equip:$id"]?.invoke(player, node) ?: true
        } else {
            return equipListeners["unequip:$id"]?.invoke(player, node) ?: true
        }
    }

    /**
     * Runs a use-with interaction between two nodes.
     */
    @JvmStatic
    fun run(used: Node, with: Node, type: IntType, player: Player): Boolean {
        val flag = when (type) {
            IntType.NPC, IntType.PLAYER -> DestinationFlag.ENTITY
            IntType.SCENERY -> DestinationFlag.OBJECT
            IntType.GROUND_ITEM -> DestinationFlag.ITEM
            else -> DestinationFlag.OBJECT
        }

        if (player.locks.isInteractionLocked()) return false

        var flipped = false

        val method = if (with is Player) {
            get(-1, used.id, 4) ?: return false
        } else {
            get(used.id, with.id, type.ordinal) ?: if (type == IntType.ITEM) {
                get(with.id, used.id, type.ordinal).also { flipped = true } ?: return false
            } else {
                return false
            }
        }

        val destOverride = if (flipped) {
            getOverride(type.ordinal, used.id, "use") ?: getOverride(type.ordinal, with.id) ?: getOverride(
                type.ordinal,
                "use",
            )
        } else {
            getOverride(type.ordinal, with.id, "use") ?: getOverride(type.ordinal, used.id) ?: getOverride(
                type.ordinal,
                "use",
            )
        }

        if (type != IntType.ITEM && !isUseWithInstant(method)) {
            if (player.locks.isMovementLocked()) return false
            player.pulseManager.run(
                object : MovementPulse(player, with, flag, destOverride) {
                    override fun pulse(): Boolean {
                        if (player.zoneMonitor.useWith(used.asItem(), with)) {
                            return true
                        }
                        player.faceLocation(with.location)
                        if (flipped) {
                            player.dispatch(UseWithEvent(with.id, used.id))
                        } else {
                            player.dispatch(UseWithEvent(used.id, with.id))
                        }
                        if (flipped) {
                            method.invoke(player, with, used)
                        } else {
                            method.invoke(player, used, with)
                        }
                        return true
                    }
                },
            )
        } else {
            if (flipped) {
                player.dispatch(UseWithEvent(with.id, used.id))
            } else {
                player.dispatch(UseWithEvent(used.id, with.id))
            }
            if (flipped) {
                return method.invoke(player, with, used)
            } else {
                return method.invoke(player, used, with)
            }
        }
        return true
    }

    /**
     * Runs a standard interaction on a node.
     */
    @JvmStatic
    fun run(id: Int, type: IntType, option: String, player: Player, node: Node): Boolean {
        val flag = when (type) {
            IntType.PLAYER -> DestinationFlag.ENTITY
            IntType.GROUND_ITEM -> DestinationFlag.ITEM
            IntType.NPC -> DestinationFlag.ENTITY
            IntType.SCENERY -> null
            else -> DestinationFlag.OBJECT
        }

        if (player.locks.isInteractionLocked()) return false

        val method = get(id, type.ordinal, option) ?: get(option, type.ordinal)

        player.setAttribute("interact:option", option.lowercase())
        player.dispatch(InteractionEvent(node, option.lowercase()))

        if (method == null) {
            val inter = interactions["${type.ordinal}:$id:${option.lowercase()}"]
                ?: interactions["${type.ordinal}:${option.lowercase()}"] ?: return false
            val script = Interaction(inter.handler, inter.distance, inter.persist)
            player.scripts.setInteractionScript(node, script)
            player.pulseManager.run(
                object : MovementPulse(player, node, flag) {
                    override fun pulse(): Boolean = true
                },
            )
            return true
        }

        val destOverride = getOverride(type.ordinal, id, option) ?: getOverride(type.ordinal, node.id) ?: getOverride(
            type.ordinal,
            option.lowercase(),
        )

        if (type != IntType.ITEM && !isInstant(method)) {
            if (player.locks.isMovementLocked()) return false
            player.pulseManager.run(
                object : MovementPulse(player, node, flag, destOverride) {
                    override fun pulse(): Boolean {
                        if (player.zoneMonitor.interact(node, Option(option, 0))) return true
                        player.faceLocation(node.location)
                        method.invoke(player, node)
                        return true
                    }
                },
            )
        } else {
            method.invoke(player, node)
        }
        return true
    }

    /**
     * Adds use-with handler for multiple pairs.
     */
    fun add(type: Int, used: IntArray, with: IntArray, handler: (Player, Node, Node) -> Boolean) {
        for (u in used) {
            for (w in with) {
                useWithListeners["$u:$w:$type"] = handler
            }
        }
    }

    /**
     * Adds a use-with handler for any "used" item with given "with" items.
     */
    fun add(type: Int, with: IntArray, handler: (Player, Node, Node) -> Boolean) {
        for (w in with) {
            useAnyWithListeners["$w:$type"] = handler
        }
    }

    /**
     * Returns true if the handler is marked as instant.
     */
    fun isInstant(handler: (Player, Node) -> Boolean): Boolean {
        val className = handler.javaClass.name.substringBefore("$")
        return instantClasses.contains(className)
    }

    /**
     * Returns true if the use-with handler is marked as instant.
     */
    fun isUseWithInstant(handler: (player: Player, used: Node, with: Node) -> Boolean): Boolean {
        val className = handler.javaClass.name.substringBefore("$")
        return instantClasses.contains(className)
    }

    /**
     * Adds metadata for interactions.
     */
    fun addMetadata(ids: IntArray, type: IntType, options: Array<out String>, metadata: InteractionListener.InteractionMetadata) {
        for (id in ids) {
            for (opt in options) {
                interactions["${type.ordinal}:$id:${opt.lowercase()}"] = metadata
            }
        }
    }

    /**
     * Adds metadata for interactions for one id.
     */
    fun addMetadata(id: Int, type: IntType, options: Array<out String>, metadata: InteractionListener.InteractionMetadata) {
        for (opt in options) {
            interactions["${type.ordinal}:$id:${opt.lowercase()}"] = metadata
        }
    }

    /**
     * Adds generic metadata for multiple options.
     */
    fun addGenericMetadata(options: Array<out String>, type: IntType, metadata: InteractionListener.InteractionMetadata) {
        for (opt in options) {
            interactions["${type.ordinal}:$opt"] = metadata
        }
    }

    /**
     * Adds metadata for a use-with interaction.
     */
    fun addMetadata(used: Int, with: IntArray, type: IntType, metadata: InteractionListener.UseWithMetadata) {
        for (id in with) {
            useWithInteractions["${type.ordinal}:$used:$with"] = metadata
        }
    }

    /**
     * Adds metadata for a specific use-with pair.
     */
    fun addMetadata(used: Int, with: Int, type: IntType, metadata: InteractionListener.UseWithMetadata) {
    }
}
