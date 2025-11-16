package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.sendMessage
import core.api.teleport
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery as Objects

/**
 * Handles Spirit Kyatt familiar interactions.
 */
@Initializable
class SpiritKyattOptionPlugin : OptionHandler() {

    companion object {
        private const val BRONZE_AXE = Items.BRONZE_AXE_1351
        private const val BRONZE_PICKAXE = Items.BRONZE_PICKAXE_1265
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCs.SPIRIT_KYATT_7365.let { NPCDefinition.forId(it).handlers["option:interact"] = this }
        Objects.TRAPDOOR_28741.let { SceneryDefinition.forId(it).handlers["option:open"] = this }
        Objects.LADDER_28743.let { SceneryDefinition.forId(it).handlers["option:climb-up"] = this }
        Objects.BRONZE_PICKAXE_14910.let { SceneryDefinition.forId(it).handlers["option:take"] = this }
        Objects.RANGE_14919.let { SceneryDefinition.forId(it).handlers["option:take"] = this }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (node.id) {
            NPCs.SPIRIT_KYATT_7365 -> {
                val npc = node as? NPC ?: return false
                player.dialogueInterpreter.open(npc.id)
            }

            Objects.TRAPDOOR_28741 -> {
                player.animate(Animation(Animations.HUMAN_BURYING_BONES_827))
                teleport(player, Location(2333, 10015), TeleportManager.TeleportType.INSTANT, 1)
            }

            Objects.LADDER_28743 -> {
                player.animate(Animation(Animations.HUMAN_CLIMB_STAIRS_828))
                teleport(player, Location(2328, 3646), TeleportManager.TeleportType.INSTANT, 1)
            }

            Objects.BRONZE_AXE_14912 -> takeItemFromScenery(player, node as Scenery, BRONZE_AXE, 14908)
            Objects.BRONZE_PICKAXE_14910 -> takeItemFromScenery(player, node as Scenery, BRONZE_PICKAXE, 14908)
        }
        return true
    }

    private fun takeItemFromScenery(player: Player, scenery: Scenery, item: Int, transformId: Int) {
        if (!player.inventory.add(Item(item, 1))) {
            sendMessage(player, "You don't have enough inventory space.")
            return
        }
        SceneryBuilder.replace(scenery, scenery.transform(transformId), 500)
    }
}