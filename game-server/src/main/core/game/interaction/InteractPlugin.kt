package core.game.interaction

import core.api.log
import core.game.container.Container
import core.game.event.InteractionEvent
import core.game.interaction.InteractionListeners.run
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.net.packet.OutgoingContext.InteractionOptionContext
import core.net.packet.PacketRepository.send
import core.net.packet.out.InteractionOption
import core.tools.Log
import java.util.*

/**
 * Handles interaction logic for a node.
 */
class InteractPlugin(private val node: Node) {

    /**
     * The list of available options for this node.
     */
    var options: Array<Option?> = arrayOfNulls(8)
        private set

    var isInitialized: Boolean = false

    /**
     * Handles a general interaction option click.
     *
     * @param player the player interacting
     * @param option the selected interaction option
     */
    fun handle(player: Player, option: Option?) {
        try {
            if (player.locks.isInteractionLocked() || option == null) return

            player.debug("Received interaction request: ${option.name}")
            val handler = option.handler
            val shouldWalk = handler?.isWalk == true || handler?.isWalk(player, node) == true

            when {
                handler == null || shouldWalk -> handleWalkOption(player, option, PulseType.STANDARD)
                else -> {
                    player.debug("Option handler used: ${handler.javaClass.simpleName}")
                    handleDefaultOption(player, option, PulseType.STANDARD)
                }
            }

            player.dispatch(InteractionEvent(node, option.name.lowercase(Locale.getDefault())))

        } catch (e: Exception) {
            e.printStackTrace()
            log(javaClass, Log.ERR, "${javaClass.name}: ${e.message}")
        }
    }

    /**
     * Handles using an item on this node.
     *
     * @param player the player using the item
     * @param option the selected item option
     * @param container the item container (optional)
     */
    fun handleItemOption(player: Player, option: Option, container: Container?) {
        if (player.locks.isInteractionLocked()) return

        player.pulseManager.clear(PulseType.STANDARD)

        Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                try {
                    if (player.locks.isInteractionLocked() || player.zoneMonitor.interact(node, option)) return true

                    if (run(node.id, IntType.ITEM, option.name, player, node)) return true

                    val handler = option.handler
                    if (handler == null || !handler.handle(player, node, option.name.lowercase(Locale.getDefault()))) {
                        player.packetDispatch.sendMessage("Nothing interesting happens.")
                    } else {
                        player.debug("Using item handler: ${handler.javaClass.simpleName}")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    log(javaClass, Log.ERR, "${javaClass.name}: ${e.message}")
                }
                return true
            }
        })
    }

    /**
     * Handles walking toward the node and executing the option.
     *
     * @param player the player moving toward the node
     * @param option the interaction option
     * @param pulseType the pulse type to use
     */
    private fun handleWalkOption(player: Player, option: Option, pulseType: PulseType) {
        if (node.location == null) {
            player.pulseManager.runUnhandledAction(player, pulseType)
            return
        }
        if (player.locks.isMovementLocked()) {
            player.pulseManager.clear(pulseType)
            return
        }

        player.pulseManager.run(object : MovementPulse(player, node, option.handler) {
            override fun pulse(): Boolean {
                try {
                    player.faceLocation(node.getFaceLocation(player.location))
                    if (player.locks.isInteractionLocked() || player.zoneMonitor.interact(node, option)) return true

                    val handler = option.handler
                    if (handler == null || !handler.handle(player, node, option.name.lowercase(Locale.getDefault()))) {
                        player.packetDispatch.sendMessage("Nothing interesting happens.")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    log(javaClass, Log.ERR, "${javaClass.name}: ${e.message}")
                }
                return true
            }
        }, pulseType)
    }

    /**
     * Handles immediate (non-walk) interaction with a node.
     *
     * @param player the interacting player
     * @param option the selected option
     * @param pulseType the pulse type to use
     */
    private fun handleDefaultOption(player: Player, option: Option, pulseType: PulseType) {
        val handler = option.handler ?: return

        if (!handler.isDelayed(player)) {
            if (player.zoneMonitor.interact(node, option)) return

            player.properties.combatPulse.stop()
            if (!handler.handle(player, node, option.name.lowercase(Locale.getDefault()))) {
                player.pulseManager.runUnhandledAction(player, pulseType)
            }
        } else {
            player.pulseManager.run(object : Pulse(1, player, node) {
                override fun pulse(): Boolean {
                    if (player.locks.isInteractionLocked() || player.zoneMonitor.interact(node, option)) return true
                    if (!handler.handle(player, node, option.name.lowercase(Locale.getDefault()))) {
                        player.packetDispatch.sendMessage("Nothing interesting happens.")
                    }
                    return true
                }
            }, pulseType)
        }
    }

    /**
     * Initializes the interaction options for a given node id and option names.
     *
     * @param nodeId the ID of the node
     * @param names the names of interaction options
     */
    fun init(nodeId: Int, vararg names: String?) {
        options = arrayOfNulls(names.size)
        for (i in names.indices) {
            val name = names[i]
            if (!name.isNullOrBlank() && name != "null") {
                set(Option(name, i).setHandler(Option.defaultHandler(node, nodeId, name)))
            }
        }
    }

    /**
     * Automatically assigns default options based on node type.
     */
    fun setDefault() {
        if (isInitialized) return

        when (node) {
            is Player -> {
                for (i in options.indices) remove(i)
                set(Option.P_FOLLOW)
                set(Option.P_TRADE)
                set(Option.P_ASSIST)
            }
            is NPC -> init(node.id, *node.definition.options)
            is Scenery -> init(node.id, *node.definition.options)
            is Item -> {
                val definition = node.definition
                val options = if (node.location != null) definition.groundOptions else definition.inventoryOptions
                init(node.id, *options)
            }
            else -> throw IllegalStateException("Unsupported node type: $node")
        }

        isInitialized = true
    }

    /**
     * Sets a interaction option.
     *
     * @param option the option to set
     */
    fun set(option: Option) {
        options[option.index] = option
        sendOption(node, option.index, option.name)
    }

    /**
     * Removes an option instance.
     *
     * @param option the option to remove
     * @return true if it was removed, false otherwise
     */
    fun remove(option: Option): Boolean {
        if (options[option.index] == option) {
            remove(option.index)
            return true
        }
        return false
    }

    /**
     * Removes the option at the specified index.
     *
     * @param index the index to clear
     */
    fun remove(index: Int) {
        if (options[index] == null) return
        options[index] = null
        sendOption(node, index, "null")
    }

    /**
     * Gets the option at the given index.
     *
     * @param index the index to get
     * @return the option or null
     */
    operator fun get(index: Int): Option? = options.getOrNull(index)

    companion object {
        /**
         * Sends an interaction option to client.
         *
         * @param node the node (must be a player)
         * @param index the index of the option
         * @param name the option name
         */
        fun sendOption(node: Node?, index: Int, name: String) {
            if (node is Player) {
                send(InteractionOption::class.java, InteractionOptionContext(node, index, name, false))
            }
        }

        /**
         * Handles fallback behavior for invalid interaction.
         *
         * @param player the player
         * @param node the node being interacted with
         * @param option the selected option
         */
        fun handleInvalidInteraction(player: Player, node: Node?, option: Option?) {
            if (node?.location != null && !player.locks.isMovementLocked()) {
                player.pulseManager.run(object : MovementPulse(player, node) {
                    override fun pulse(): Boolean {
                        try {
                            player.faceLocation(node.getFaceLocation(player.location))
                            if (player.locks.isInteractionLocked() || player.zoneMonitor.interact(node, option)) {
                                return true
                            }
                            player.packetDispatch.sendMessage("Nothing interesting happens.")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            log(javaClass, Log.ERR, "${javaClass.name}: ${e.message}")
                        }
                        return true
                    }
                }, PulseType.STANDARD)
            } else {
                player.pulseManager.runUnhandledAction(player, PulseType.STANDARD)
            }
        }
    }
}
