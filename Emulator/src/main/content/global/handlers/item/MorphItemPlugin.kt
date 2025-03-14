package content.global.handlers.item

import core.api.closeInterface
import core.api.playAudio
import core.api.removeTabs
import core.api.toIntArray
import core.api.ui.openSingleTab
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

@Initializable
class MorphItemPlugin : Plugin<Any> {
    private val easterEggIds = (NPCs.EGG_3689..NPCs.EGG_3694).toIntArray()
    private val component = Component(Components.UNMORPH_375)

    override fun newInstance(arg: Any?): Plugin<Any> {
        with(ItemDefinition.forId(Items.EASTER_RING_7927).handlers) {
            this["equipment"] = this@MorphItemPlugin
        }
        with(ItemDefinition.forId(Items.RING_OF_STONE_6583).handlers) {
            this["equipment"] = this@MorphItemPlugin
        }
        ClassScanner.definePlugin(MorphInterfacePlugin())
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any {
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

    private fun morph(
        player: Player,
        item: Item,
    ) {
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
        player.appearance.sync()
        player.walkingQueue.reset()
    }

    private fun lockPlayerActions(player: Player) {
        val currentTicks = GameWorld.ticks
        player.locks.lockMovement(currentTicks + 900000000)
        player.locks.lockInteractions(currentTicks + 90000000)
        player.locks.lockTeleport(currentTicks + 900000000)
    }

    inner class MorphInterfacePlugin : ComponentPlugin() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            ComponentDefinition.forId(Components.UNMORPH_375).plugin = this
            return this
        }

        override fun handle(
            player: Player,
            component: Component,
            opcode: Int,
            button: Int,
            slot: Int,
            itemId: Int,
        ): Boolean {
            player.interfaceManager.closeSingleTab()
            return true
        }
    }
}
