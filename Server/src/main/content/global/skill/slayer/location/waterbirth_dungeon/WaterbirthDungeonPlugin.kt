package content.global.skill.slayer.waterbirth_dungeon

import core.game.node.entity.player.link.WarningManager
import core.game.node.entity.player.link.Warnings
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Quests

/**
 * Handles the waterbirth dungeon ladders interactions.
 */
@Initializable
class WaterbirthDungeonPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(8966).handlers["option:climb"] = this
        SceneryDefinition.forId(10193).handlers["option:climb-up"] = this
        SceneryDefinition.forId(10177).handlers["option:climb"] = this
        SceneryDefinition.forId(10177).handlers["option:climb-up"] = this
        SceneryDefinition.forId(10177).handlers["option:climb-down"] = this
        SceneryDefinition.forId(10217).handlers["option:climb-up"] = this
        SceneryDefinition.forId(10229).handlers["option:climb-up"] = this
        SceneryDefinition.forId(10230).handlers["option:climb-down"] = this
        SceneryDefinition.forId(8929).handlers["option:enter"] = this
        SceneryDefinition.forId(8930).handlers["option:enter"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        if (player == null || node !is Scenery) return false
        when (node.id) {
            8929 -> player.properties.teleportLocation = Location.create(2442, 10147, 0)
            8930 -> player.properties.teleportLocation = Location.create(2545, 10143, 0)
            8966 -> player.properties.teleportLocation = Location.create(2523, 3740, 0)
            10193 -> player.properties.teleportLocation = Location(2545, 10143, 0)
            10177 -> when (option) {
                "climb" -> {
                    sendOptions(player, "Climb Up.", "Climb Down.")
                    addDialogueAction(player) { p, button ->
                        when (button) {
                            1 -> ClimbActionHandler.climb(
                                p,
                                ClimbActionHandler.CLIMB_UP,
                                Location.create(2544, 3741, 0)
                            )

                            2 -> ClimbActionHandler.climb(
                                p,
                                ClimbActionHandler.CLIMB_DOWN,
                                Location.create(1799, 4406, 3)
                            )
                            else -> closeDialogue(p)
                        }

                    }
                }

                "climb-down" -> ClimbActionHandler.climb(
                    player,
                    ClimbActionHandler.CLIMB_DOWN,
                    Location.create(1799, 4406, 3)
                )

                "climb-up" -> ClimbActionHandler.climb(
                    player,
                    ClimbActionHandler.CLIMB_UP,
                    Location.create(2544, 3741, 0)
                )
            }
            10217 -> {
                if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                    teleport(player, Location.create(1957, 4373, 1))
                } else {
                    sendMessage(player, "You need to have completed Horror from the Deep in order to do this.")
                }
            }

            10230 -> {
                if (!WarningManager.isWarningDisabled(player, Warnings.DAGANNOTH_KINGS_LADDER)) {
                    WarningManager.openWarningInterface(player, Warnings.DAGANNOTH_KINGS_LADDER)
                } else {
                    teleport(player, Location.create(2899, 4449, 0))
                }
            }

            10229 -> {
                teleport(player, Location.create(1912, 4367, 0))
            }
        }
        return true
    }

    override fun isWalk(): Boolean {
        return super.isWalk()
    }

}