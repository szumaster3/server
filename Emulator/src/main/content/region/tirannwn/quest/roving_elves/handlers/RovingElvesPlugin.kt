package content.region.tirannwn.quest.roving_elves.handlers

import content.region.tirannwn.quest.roving_elves.RovingElves
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import org.rs.consts.Quests
import org.rs.consts.Scenery

class RovingElvesPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.FIRE_REMAINS_5252).handlers["option:search"] = this
        ItemDefinition.forId(RovingElves.CONSECRATION_SEED_CHARGED).handlers["option:plant"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.ROVING_ELVES)
        if (quest == null) {
            player.sendMessage("Error! RovingElves quest cannot be found, please contact an admin!")
            return true
        }
        when (node.id) {
            5252 ->
                player.dialogueInterpreter.sendDialogue(
                    "The firepit is still warm, there must be travellers about. Maybe I",
                    "should look for them.",
                )
            4206 ->
                if (Location.getDistance(Location(2603, 9911), player.location) <= 3 &&
                    quest.getStage(player) == 15
                ) {
                    if (!player.inventory.containsItem(Item(952, 1))) {
                        player.packetDispatch.sendMessage("You need a spade to plant the seed.")
                    } else {
                        player.animate(ANIMATION_DIG)
                        player.packetDispatch.sendMessage("You dig a small hole with your spade.")
                        Pulser.submit(
                            object : Pulse(1, player) {
                                var counter: Int = 0

                                override fun pulse(): Boolean {
                                    when (counter++) {
                                        3 -> {
                                            player.packetDispatch.sendMessage("You drop the crystal seed in the hole.")
                                            player.animate(ANIMATION_DROP)
                                            player.faceLocation(Location(2604, 9907, 0))
                                        }

                                        6 -> {
                                            player.inventory.remove(Item(RovingElves.CONSECRATION_SEED_CHARGED))
                                            quest.setStage(player, 20)
                                            player.packetDispatch.sendGlobalPositionGraphic(
                                                719,
                                                Location(2604, 9907, 0),
                                            )
                                            sendMessage(player, "The seed vanishes in a puff of smoke.")
                                        }
                                    }
                                    return false
                                }
                            },
                        )
                    }
                }
        }
        return true
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        return null
    }

    override fun isWalk(
        player: Player,
        node: Node,
    ): Boolean {
        return node !is Item
    }

    override fun isWalk(): Boolean {
        return false
    }

    companion object {
        val ANIMATION_DIG: Animation = Animation.create(830)
        val ANIMATION_DROP: Animation = Animation.create(827)
    }
}
