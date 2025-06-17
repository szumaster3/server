package content.region.desert.quest.rescue.plugin

import core.api.inInventory
import core.api.openDialogue
import core.api.sendMessage
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PrinceAliRescuePlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.PRISON_DOOR_2881).handlers["option:open"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.DOOR_4639).handlers["option:open"] = this
        NPCDefinition.forId(NPCs.BORDER_GUARD_7912).handlers["option:talk-to"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE)
        val id = if (node is Scenery) {
            node.getId()
        } else if (node is NPC) {
            node.id
        } else {
            0
        }
        when (id) {
            NPCs.BORDER_GUARD_7912 -> openDialogue(player, NPCs.BORDER_GUARD_7912, node.asNpc())
            2881 -> when (quest.getStage(player)) {
                60 -> handleAutowalkDoor(player, node as Scenery)
                50 -> if (player.getAttribute("keli-gone", 0) > ticks) {
                    if (inInventory(player, Items.BRONZE_KEY_2418, 1)) {
                        sendMessage(player, "You unlock the door.")
                        handleAutowalkDoor(player, node as Scenery)
                    } else {
                        sendMessage(player, "The door is locked.")
                    }
                } else {
                    sendMessage(player, "You'd better get rid of Lady Keli before trying to go through there.")
                }

                else -> sendMessage(player, "You'd better get rid of Lady Keli before trying to go through there.")
            }
        }
        return true
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is NPC) {
            val npc = n
            if (npc.location == Location(3268, 3226, 0)) {
                return locs[0]
            } else if (npc.location == Location(3268, 3229, 0)) {
                return locs[1]
            } else if (npc.location == Location(3267, 3229, 0)) {
                return locs[2]
            } else if (npc.location == Location(3267, 3226, 0)) {
                return locs[3]
            }
        }
        return null
    }

    companion object {
        var locs: Array<Location> = arrayOf(
            Location(3268, 3227, 0),
            Location.create(3268, 3228, 0),
            Location.create(3267, 3228, 0),
            Location.create(3267, 3227, 0),
        )
    }
}
