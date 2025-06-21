package core.game.component

import core.api.Container
import core.game.container.Slot
import core.game.interaction.InterfaceListeners.runClose
import core.game.interaction.InterfaceListeners.runOpen
import core.game.interaction.InterfaceListeners.runSlotSwitch
import core.game.node.entity.player.Player
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.Interface

/**
 * Represents a UI component that can be opened, closed, and interacted with by a player.
 */
open class Component {
    /**
     * The unique identifier of this component.
     */
    @JvmField var id: Int

    /**
     * The child identifier of this component.
     */
    @JvmField var childId: Int = -1

    private val slots: MutableList<Slot> = ArrayList()

    /**
     * The definition of this component.
     */
    @JvmField
    val definition: ComponentDefinition?

    /**
     * The event triggered when the component is closed.
     */
    var closeEvent: CloseEvent? = null

    /**
     * The associated plugin for this component.
     */
    var plugin: ComponentPlugin?

    /**
     * Whether this component is hidden.
     */
    var isHidden: Boolean = false

    /**
     * Creates a new component with the specified ID.
     *
     * @param id The unique identifier of the component.
     */
    constructor(id: Int) {
        this.id = id
        this.definition = ComponentDefinition.forId(id)
        this.plugin = definition?.plugin
    }

    /**
     * Creates a new component with the specified ID and child ID.
     *
     * @param id      The unique identifier of the component.
     * @param childId The child identifier of the component.
     */
    constructor(id: Int, childId: Int) {
        this.id = id
        this.childId = childId
        this.definition = ComponentDefinition.forId(id)
        this.plugin = definition?.plugin
    }

    /**
     * Opens this component for the specified player.
     *
     * @param player The player opening the component.
     */
    open fun open(player: Player) {
        val manager = player.interfaceManager
        runOpen(player, this)

        if (definition == null) {
            PacketRepository.send(
                Interface::class.java,
                OutgoingContext.InterfaceContext(
                    player,
                    manager.windowPaneId,
                    manager.defaultChildId,
                    id,
                    false,
                ),
            )
            plugin?.open(player, this)
            return
        }

        if (definition.type == InterfaceType.WINDOW_PANE) {
            return
        }

        if (definition.type == InterfaceType.TAB) {
            PacketRepository.send(
                Interface::class.java,
                OutgoingContext.InterfaceContext(
                    player,
                    definition.getWindowPaneId(manager.isResizable),
                    definition.getChildId(manager.isResizable) + definition.tabIndex,
                    id,
                    definition.isWalkable,
                ),
            )
            plugin?.open(player, this)
            return
        }

        PacketRepository.send(
            Interface::class.java,
            OutgoingContext.InterfaceContext(
                player,
                definition.getWindowPaneId(manager.isResizable),
                definition.getChildId(manager.isResizable),
                id,
                definition.isWalkable,
            ),
        )
        plugin?.open(player, this)
    }

    /**
     * Closes this component for the specified player.
     *
     * @param player The player closing the component.
     * @return `true` if the component was closed successfully, otherwise `false`.
     */
    open fun close(player: Player): Boolean = (closeEvent == null || closeEvent!!.close(player, this)) && runClose(player, this)

    /**
     * Handles slot switching within the component.
     *
     * @param player     The player interacting with the slots.
     * @param sourceSlot The source slot index.
     * @param destSlot   The destination slot index.
     * @return `true` if the slot switch was handled successfully, otherwise `false`..
     */
    fun handleSlotSwitch(
        player: Player,
        sourceSlot: Int,
        destSlot: Int,
    ): Boolean = runSlotSwitch(player, this, sourceSlot, destSlot)

    /**
     * Adds a new slot to this component.
     *
     * @param slotId    The slot identifier.
     * @param container The container associated with the slot.
     */
    fun addSlot(
        slotId: Int,
        container: Container,
    ) {
        slots.add(Slot(slotId, container))
    }

    /**
     * Retrieves the list of slots associated with this component.
     *
     * @return A list of slots.
     */
    fun getSlots(): List<Slot> = slots

    /**
     * Sets the close event for this component.
     *
     * @param closeEvent The close event to set.
     * @return This component instance.
     */
    fun setUncloseEvent(closeEvent: CloseEvent?): Component {
        this.closeEvent = closeEvent
        return this
    }

    companion object {
        /**
         * Marks a component as unclosable.
         *
         * @param player    The player for whom the component should be unclosable.
         * @param component The component to mark as unclosable.
         */
        fun setUnclosable(
            player: Player,
            component: Component,
        ) {
            player.setAttribute("close_c_", true)
            component.setUncloseEvent { player, _ ->
                !player.getAttribute("close_c_", false)
            }
        }
    }
}
