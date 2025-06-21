package core.api.ui

import core.game.component.Component
import core.game.node.entity.player.Player
import core.net.packet.OutgoingContext
import core.net.packet.PacketRepository
import core.net.packet.out.MinimapState
import core.net.packet.out.RepositionChild

/**
 * Restores the player's interface tabs to their default state.
 *
 * @param player The player whose interface tabs are being restored.
 */
fun restoreTabs(player: Player) = player.interfaceManager.restoreTabs()

/**
 * Opens a specific interface tab for a player.
 *
 * @param player The player who will open the interface tab.
 * @param component The id of the component (interface tab) to open.
 */
fun openSingleTab(
    player: Player,
    component: Int,
) {
    player.interfaceManager.openSingleTab(Component(component))
}

/**
 * Sets the state of the player's minimap (visible or hidden).
 *
 * @param player The player whose minimap state is being updated.
 * @param state The state of the minimap. This could represent whether the minimap is visible or hidden.
 */
fun setMinimapState(
    player: Player,
    state: Int,
) = PacketRepository.send(MinimapState::class.java, OutgoingContext.MinimapState(player, state))

/**
 * Sends an interface configuration update to the player.
 *
 * @param player The player to whom the interface configuration is being sent.
 * @param interfaceId The interface id containing the child component.
 * @param childId The id of the specific child component within the interface.
 * @param hide If true, the child component will be hidden; if false, it will be shown.
 */
fun sendInterfaceConfig(
    player: Player,
    interfaceId: Int,
    childId: Int,
    hide: Boolean,
) {
    player.packetDispatch.sendInterfaceConfig(interfaceId, childId, hide)
}

/**
 * Repositions a child interface element for the player.
 *
 * @param player The player whose interface is being modified.
 * @param interfaceId The interface id.
 * @param childId The id of the element to move.
 * @param positionX The new X coordinate.
 * @param positionY The new Y coordinate.
 */
fun repositionChild(
    player: Player,
    interfaceId: Int,
    childId: Int,
    positionX: Int,
    positionY: Int,
) = PacketRepository.send(
    RepositionChild::class.java,
    OutgoingContext.ChildPosition(player, interfaceId, childId, positionX, positionY),
)

private class InterfaceAPI
