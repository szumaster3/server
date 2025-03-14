package core.api.ui

import core.game.component.Component
import core.game.node.entity.player.Player
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import core.net.packet.out.RepositionChild

/**
 * Sets the text of a specified interface component for a player.
 *
 * @param player The player whose interface text is being updated.
 * @param string The text to display in the interface component.
 * @param interfaceID The interface id where the text will be displayed.
 * @param child The child id within the interface where the text will be shown.
 */
fun setInterfaceText(
    player: Player,
    string: String,
    interfaceID: Int,
    child: Int,
) {
    player.packetDispatch.sendString(string, interfaceID, child)
}

/**
 * Restores the player's interface tabs to their default state.
 *
 * @param player The player whose interface tabs are being restored.
 */
fun restoreTabs(player: Player) {
    return player.interfaceManager.restoreTabs()
}

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
) {
    return PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, state))
}

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
) {
    return PacketRepository.send(
        RepositionChild::class.java,
        ChildPositionContext(player, interfaceId, childId, positionX, positionY),
    )
}

private class InterfaceAPI
