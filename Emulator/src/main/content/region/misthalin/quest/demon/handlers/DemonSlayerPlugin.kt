package content.region.misthalin.quest.demon.handlers

import content.region.misthalin.quest.demon.cutscene.DemonSlayerCutscenePlugin
import core.api.removeAttribute
import core.api.setVarp
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.ClimbActionHandler.climbLadder
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import org.rs.consts.Quests

class DemonSlayerPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(881).handlers["option:open"] = this
        SceneryDefinition.forId(882).handlers["option:close"] = this
        SceneryDefinition.forId(882).handlers["option:climb-down"] = this
        SceneryDefinition.forId(DRAIN_ID).handlers["option:search"] = this
        SceneryDefinition.forId(17429).handlers["option:take"] = this
        NPCDefinition.forId(DemonSlayerCutscenePlugin.DELRITH).handlers["option:attack"] = this
        NPCDefinition.forId(DemonSlayerCutscenePlugin.WEAKENED_DELRITH).handlers["option:banish"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.DEMON_SLAYER)
        val id =
            if (node is Scenery) {
                node.getId()
            } else if (node is Item) {
                node.id
            } else {
                node.id
            }
        when (id) {
            880 -> player.dialogueInterpreter.open(8427322)
            879 -> {
                if (!player.equipment.containsItem(DemonSlayerUtils.SILVERLIGHT)) {
                    player.packetDispatch.sendMessage("I'd better wield Silverlight first.")
                    return true
                }
                player.face((node as NPC))
                player.properties.combatPulse.attack(node)
                return true
            }

            DRAIN_ID -> {
                if (quest.getStage(player) == 20) {
                    player.dialogueInterpreter.open(883, 883, "key")
                    return true
                } else {
                    player.sendMessage("You search the castle drain and find nothing of value.")
                }
                return true
            }

            881 -> SceneryBuilder.replace((node as Scenery), node.transform(882))
            882 ->
                when (option) {
                    "climb-down" ->
                        if (node.location == Location(3237, 3458, 0)) {
                            climb(player, Animation(828), SEWER_LOCATION)
                        } else {
                            climbLadder(player, node as Scenery, option)
                        }

                    "close" -> SceneryBuilder.replace((node as Scenery), node.transform(881))
                }

            17429 ->
                if (quest.getStage(player) == 20 && player.inventory.add(DemonSlayerUtils.FIRST_KEY)) {
                    setVarp(player, 222, 4757762, true)
                    removeAttribute(player, "demon-slayer:poured")
                    removeAttribute(player, "demon-slayer:just-poured")
                    player.dialogueInterpreter.sendItemMessage(
                        DemonSlayerUtils.FIRST_KEY.id,
                        "You pick up an old rusty key.",
                    )
                } else {
                    if (player.inventory.freeSlots() == 0) {
                        player.packetDispatch.sendMessage("Not enough inventory space.")
                        return true
                    }
                }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is NPC) {
            return n.getLocation().transform(2, 0, 0)
        }
        if (n.id == DRAIN_ID) {
            return Location.create(3226, 3496, 0)
        }
        return null
    }

    companion object {
        const val DRAIN_ID: Int = 17424

        private val SEWER_LOCATION = Location(3237, 9858, 0)
    }
}
