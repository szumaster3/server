package content.global.plugin.item

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

/**
 * Represents a plugin that handles the transformation effects for morph items.
 */
@Initializable
class MorphItemPlugin : Plugin<Any> {
    /**
     * List of NPC ids the player can morph into when using the easter ring.
     */
    private val easterEggIds = (NPCs.EGG_3689..NPCs.EGG_3694).toIntArray()

    /**
     * The interface component used for the unmorph option.
     */
    private val component = Component(Components.UNMORPH_375)

    /**
     * Registers this plugin.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        with(ItemDefinition.forId(Items.EASTER_RING_7927).handlers) {
            this["equipment"] = this@MorphItemPlugin
        }
        with(ItemDefinition.forId(Items.EASTER_RING_10729).handlers) {
            this["equipment"] = this@MorphItemPlugin
        }
        with(ItemDefinition.forId(Items.RING_OF_STONE_6583).handlers) {
            this["equipment"] = this@MorphItemPlugin
        }
        ClassScanner.definePlugin(MorphInterfacePlugin())
        return this
    }

    /**
     * Handles the "equip" event.
     *
     * @param identifier The event type (e.g., "equip").
     * @param args Arguments including [Player] and [Item].
     * @return `false` to cancel the original "equip" behavior, `true` otherwise.
     */
    override fun fireEvent(identifier: String, vararg args: Any, ): Any {
        val player = args[0] as Player
        val item = args[1] as Item
        return when (identifier) {
            "equip" -> {
                morph(player, item)
                false
            }

            else -> true
        }
    }

    /**
     * Transforms the player into a morph NPC.
     *
     * @param player The player to transform.
     * @param item The item being used for the transformation.
     */
    private fun morph(player: Player, item: Item, ) {
        val morphId =
            if (item.id == Items.RING_OF_STONE_6583) {
                NPCs.ROCKS_2626
            } else {
                easterEggIds[RandomFunction.random(easterEggIds.size)]
            }
        playAudio(player, Sounds.EASTER06_HUMAN_INTO_EGG_1520)
        closeInterface(player)
        player.appearance.transformNPC(morphId)
        removeTabs(player, 0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
        lockPlayerActions(player)
        openSingleTab(player, component.id)
        refreshAppearance(player)
        player.walkingQueue.reset()
    }

    /**
     * Locks the player.
     *
     * @param player The player to lock.
     */
    private fun lockPlayerActions(player: Player) {
        val currentTicks = GameWorld.ticks
        player.locks.lockMovement(currentTicks + 900000000)
        player.locks.lockInteractions(currentTicks + 90000000)
        player.locks.lockTeleport(currentTicks + 900000000)
    }

    /**
     * Inner plugin that handles the component interface.
     */
    inner class MorphInterfacePlugin : ComponentPlugin() {

        /**
         * Registers this component plugin to handle the unmorph interface.
         */
        override fun newInstance(arg: Any?): Plugin<Any> {
            ComponentDefinition.forId(Components.UNMORPH_375).plugin = this
            return this
        }

        /**
         * Handles the unmorph button click.
         *
         * @return Always returns `true` after closing the interface.
         */
        override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int, ): Boolean {
            player.interfaceManager.closeSingleTab()
            return true
        }
    }
}
