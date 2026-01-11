package content.global.skill.slayer.dungeon.waterbirth_dungeon

import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.WarningManager
import core.game.node.entity.player.link.Warnings
import core.game.world.map.Location
import shared.consts.Quests
import shared.consts.Scenery

class WaterbirthDungeonListener : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles enter to waterbirth dungeon.
         */

        on(intArrayOf(Scenery.CAVE_ENTRANCE_8929, Scenery.CAVE_ENTRANCE_8930), SCENERY, "enter") { player, scenery ->
            val destination = when (scenery.id) {
                Scenery.CAVE_ENTRANCE_8929 -> Location(2442, 10147, 0)
                Scenery.CAVE_ENTRANCE_8930 -> Location(2545, 10143, 0)
                else -> return@on true
            }
            teleport(player, destination)
            return@on true
        }

        /*
         * Handles ladder exiting from waterbirth dungeon.
         */

        on(Scenery.STEPS_8966, SCENERY, "climb") { player, _ ->
            teleport(player, Location(2523, 3740, 0))
            return@on true
        }

        /*
         * Handles multi-option ladder.
         */

        on(Scenery.IRON_LADDER_10177, SCENERY, "climb") { player, _ ->
            setTitle(player, 2)
            sendOptions(player, "Climb up or down the ladder?", "Climb up.", "Climb down.")
            addDialogueAction(player) { p, button ->
                when (button) {
                    2 -> ClimbActionHandler.climb(
                        p,
                        ClimbActionHandler.CLIMB_UP,
                        Location(2544, 3741, 0)
                    )
                    3 -> ClimbActionHandler.climb(
                        p,
                        ClimbActionHandler.CLIMB_DOWN,
                        Location(1799, 4406, 3)
                    )
                    else -> closeDialogue(p)
                }
            }
            return@on true
        }

        on(Scenery.IRON_LADDER_10177, SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_UP,
                Location(2544, 3741, 0)
            )
            return@on true
        }

        on(Scenery.IRON_LADDER_10177, SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_DOWN,
                Location(1799, 4406, 3)
            )
            return@on true
        }

        /*
         * Handles the dungeon internal ladders.
         */

        on(Scenery.LADDER_10193, SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_UP,
                Location(2545, 10143, 0)
            )
            return@on true
        }

        /*
         * Handles horror from the Deep quest ladder.
         */

        on(Scenery.LADDER_10217, SCENERY, "climb-up") { player, _ ->
            if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(1957, 4373, 1))
            } else {
                sendMessage(player, "You need to have completed Horror from the Deep in order to do this.")
            }
            return@on true
        }

        /*
         * Handles dagannoth kings warning ladder.
         */

        on(Scenery.LADDER_10230, SCENERY, "climb-down") { player, _ ->
            if (!WarningManager.isWarningDisabled(player, Warnings.DAGANNOTH_KINGS_LADDER)) {
                WarningManager.openWarningInterface(player, Warnings.DAGANNOTH_KINGS_LADDER)
            } else {
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, Location(2899, 4449, 0))
            }
            return@on true
        }

        /*
         * Handles exit from dagannoth kings lair.
         */

        on(Scenery.LADDER_10229, SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(1912, 4367, 0))
            return@on true
        }
    }
}
