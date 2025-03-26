package content.global.skill.summoning.familiar.dialogue.spirit

import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * The type Spirit kyatt option plugin.
 */
@Initializable
class SpiritKyattOptionPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.SPIRIT_KYATT_7365).handlers["option:interact"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.TRAPDOOR_28741).handlers["option:open"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.LADDER_28743).handlers["option:climb-up"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.BRONZE_PICKAXE_14910).handlers["option:take"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.RANGE_14919).handlers["option:take"] = this
        return this
    }

    private val BRONZE_AXE = Item(1351, 1)
    private val BRONZE_PICKAXE = Item(1265, 1)

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (node.id) {
            7365 -> player.dialogueInterpreter.open(7365, node.asNpc())
            28741 -> {
                player.animate(Animation(827))
                player.teleport(Location(2333, 10015))
            }

            28743 -> {
                player.animate(Animation(828))
                player.teleport(Location(2328, 3646))
            }

            14912 -> {
                if (!player.inventory.add(BRONZE_AXE)) {
                    player.packetDispatch.sendMessage("You don't have enough inventory space.")
                    return true
                }
                SceneryBuilder.replace((node as Scenery), node.transform(14908), 500)
            }

            14910 -> {
                if (!player.inventory.add(BRONZE_PICKAXE)) {
                    player.packetDispatch.sendMessage("You don't have enough inventory space.")
                    return true
                }
                SceneryBuilder.replace((node as Scenery), node.transform(14908), 500)
            }
        }
        return true
    }
}
