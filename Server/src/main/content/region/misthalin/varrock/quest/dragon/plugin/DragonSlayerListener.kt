package content.region.misthalin.varrock.quest.dragon.plugin

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import shared.consts.*

class DragonSlayerListener : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles interaction with the locked cell door.
         */

        on(Scenery.CELL_DOOR_40184, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "It's locked tight.")
            return@on true
        }

        /*
         * Handles interaction with Wormbrain.
         * Adjusts the player's location based on their proximity to the NPC.
         */

        setDest(IntType.NPC, intArrayOf(NPCs.WORMBRAIN_745), "talk-to") { player, node ->
            val npcLocation = node.asNpc().location
            val playerLocation = player.asPlayer().location
            val isWithinDistance = playerLocation.withinMaxnormDistance(npcLocation, 1)
            val targetLocation = if (isWithinDistance) playerLocation else npcLocation
            return@setDest Location.create(targetLocation.x, targetLocation.y, 0)
        }

        /*
         * Handles diary task.
         */

        on(Scenery.CLIMBING_ROPE_25213, IntType.SCENERY, "climb") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location(2834, 3258, 0))
            finishDiaryTask(player, DiaryType.KARAMJA, 1, 2)
            return@on true
        }

        /*
         * Handles entrance from crandor to elvarg dungeon.
         */

        on(Scenery.HOLE_25154, IntType.SCENERY, "enter") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(2833, 9658, 0))
            return@on true
        }

        /*
         * Handles the quest NPC interaction (Dragon slayer).
         */

        on(NPCs.ELVARG_742, IntType.NPC, "attack") { player, node ->
            when {
                getQuestStage(player, Quests.DRAGON_SLAYER) == 40 && inInventory(player, DragonSlayer.ELVARG_HEAD.id) -> {
                    sendMessage(player, "You have already slain the dragon. Now you just need to return to Oziach for your reward!")
                }
                getQuestStage(player, Quests.DRAGON_SLAYER) > 40 -> {
                    sendMessage(player, "You have already slain Elvarg the dragon.")
                }
                else -> {
                    player.attack(node)
                    face(player, node, 3)
                }
            }
            return@on true
        }

        /*
         * Handles climbing over a wall to elvarg NPC.
         */

        on(Scenery.WALL_25161, IntType.SCENERY, "climb-over") { player, _ ->
            if (player.location.x >= 2847) {
                if (getQuestStage(player, Quests.DRAGON_SLAYER) == 40 && !inInventory(player, DragonSlayer.ELVARG_HEAD.id)) {
                    if (player.location.x <= 2845) {
                        val npcs = RegionManager.getLocalNpcs(player)
                        for (n in npcs) {
                            if (n.id == NPCs.ELVARG_742) {
                                n.properties.combatPulse.attack(player)
                                return@on true
                            }
                        }
                    }
                }
                forceMove(
                    player,
                    player.location,
                    player.location.transform(if (player.location.x == 2845) 2 else -2, 0, 0),
                    30,
                    90,
                    null,
                    Animations.CLIMB_OVER_ROCK_10573
                )
                return@on true
            }
            if (inInventory(player, DragonSlayer.ELVARG_HEAD.id)) {
                sendMessage(player, "You have already slain the dragon. Now you just need to return to Oziach for your reward!")
                return@on true
            }
            if (isQuestComplete(player, Quests.DRAGON_SLAYER)) {
                sendMessage(player, "You have already slain Elvarg the dragon.")
                return@on true
            }
            return@on true
        }

        /*
         * Handles creating a crandor map from pieces.
         */

        onUseWith(IntType.ITEM, mapPieces, *mapPieces) { player, _, _ ->
            if (!allInInventory(player, *mapPieces)) {
                sendMessage(player, "You don't have all the map pieces yet.")
                return@onUseWith false
            }
            if (!player.inventory.removeAll(mapPieces)) return@onUseWith false

            sendItemDialogue(
                player,
                Items.CRANDOR_MAP_1538,
                "You put the three pieces together and assemble a map that shows the route through the reefs to Crandor."
            )
            addItem(player, Items.CRANDOR_MAP_1538, 1)
            return@onUseWith true
        }

        /*
         * Handles interaction for each map pieces.
         */

        on(mapPieces, IntType.ITEM, "study") { player, node ->
            val dialogue = when (node.id) {
                Items.MAP_PART_1535 -> "This is a piece of map that you found in Melzar's Maze. You will need to join it to the other two map pieces before you can see the route to Crandor."
                Items.MAP_PART_1536 -> "This is a piece of map that you got from Wormbrain, the goblin thief. You will need to join it to the other two map pieces before you can see the route to Crandor."
                Items.MAP_PART_1537 -> "This is a piece of map that you found in a secret chest in the Dwarven Mine. You will need to join it to the other two map pieces before you can see the route to Crandor."
                else -> return@on false
            }
            if(node.id == 1537) {
                sendDoubleItemDialogue(player, -1, 1537, dialogue)
            } else {
              sendItemDialogue(player, node.id, dialogue)
            }
            return@on true
        }

        /*
         * Handles opening crandor map.
         */

        on(Items.CRANDOR_MAP_1538, IntType.ITEM, "study") { player, _ ->
            openInterface(player, Components.DRAGON_SLAYER_QIP_MAP_547)
            return@on true
        }

    }

    companion object {
        val mapPieces = intArrayOf(Items.MAP_PART_1537, Items.MAP_PART_1536, Items.MAP_PART_1535)
    }
}
