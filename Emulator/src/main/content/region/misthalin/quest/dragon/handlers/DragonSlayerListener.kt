package content.region.misthalin.quest.dragon.handlers

import content.region.misthalin.quest.dragon.DragonSlayer
import core.api.*
import core.api.quest.getQuestStage
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class DragonSlayerListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CELL_DOOR_40184, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "It's locked tight.")
            return@on true
        }

        setDest(IntType.NPC, intArrayOf(NPCs.WORMBRAIN_745), "talk-to") { player, node ->
            val npcLocation = node.asNpc().location
            val playerLocation = player.asPlayer().location

            val isWithinDistance = playerLocation.withinMaxnormDistance(npcLocation, 1)
            val targetLocation = if (isWithinDistance) playerLocation else npcLocation

            return@setDest Location.create(targetLocation.x, targetLocation.y, 0)
        }

        on(Scenery.CLIMBING_ROPE_25213, IntType.SCENERY, "climb") { player, _ ->
            val animation = Animation(Animations.USE_LADDER_828)
            val location = Location(2834, 3258, 0)

            ClimbActionHandler.climb(player, animation, location)
            finishDiaryTask(player, DiaryType.KARAMJA, 1, 2)

            return@on true
        }

        on(Scenery.HOLE_25154, IntType.SCENERY, "enter") { player, _ ->
            val animation = Animation(Animations.USE_LADDER_828)
            val location = Location(2833, 9658, 0)

            ClimbActionHandler.climb(player, animation, location)

            return@on true
        }

        on(NPCs.ELVARG_742, IntType.NPC, "attack") { player, node ->
            if (getQuestStage(player, Quests.DRAGON_SLAYER) == 40 && inInventory(player, DragonSlayer.ELVARG_HEAD.id)) {
                sendMessage(player, "You have already slain the dragon. Now you just need to return to Oziach for")
                sendMessage(player, "your reward!")
                return@on true
            }
            if (getQuestStage(player, Quests.DRAGON_SLAYER) > 40) {
                sendMessage(player, "You have already slain Elvarg the dragon.")
                return@on true
            }

            player.attack(node)
            face(player, node, 3)
            return@on true
        }

        on(Scenery.WALL_25161, IntType.SCENERY, "climb-over") { player, _ ->
            if (player.location.x >= 2847) {
                if (getQuestStage(player, Quests.DRAGON_SLAYER) == 40 &&
                    !inInventory(
                        player,
                        DragonSlayer.ELVARG_HEAD.id,
                    )
                ) {
                    if (player.location.x <= 2845) {
                        val npcs = getLocalNpcs(player)
                        for (n in npcs) {
                            if (n.id == 742) {
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
                    10,
                    60,
                    null,
                    Animations.CLIMB_OVER_ROCK_10573,
                )
                return@on true
            }
            if (getQuestStage(player, Quests.DRAGON_SLAYER) == 40 && inInventory(player, DragonSlayer.ELVARG_HEAD.id)) {
                sendMessage(player, "You have already slain the dragon. Now you just need to return to Oziach for")
                sendMessage(player, "your reward!")
                return@on true
            }
            if (getQuestStage(player, Quests.DRAGON_SLAYER) > 40) {
                sendMessage(player, "You have already slain Elvarg the dragon.")
                return@on true
            }

            return@on true
        }

        onUseWith(IntType.ITEM, mapPieces, *mapPieces) { player, _, _ ->
            if (!inInventory(player, Items.MAP_PART_1537) ||
                !inInventory(player, Items.MAP_PART_1536) ||
                !inInventory(
                    player,
                    Items.MAP_PART_1535,
                )
            ) {
                sendMessage(player, "You don't have all the map pieces yet.")
            } else if (removeItem(player, Items.MAP_PART_1537) &&
                removeItem(player, Items.MAP_PART_1536) &&
                removeItem(
                    player,
                    Items.MAP_PART_1535,
                )
            ) {
                sendItemDialogue(
                    player,
                    Items.CRANDOR_MAP_1538,
                    "You put the three pieces together and assemble a map that shows the route through the reefs to Crandor.",
                )
                addItem(player, Items.CRANDOR_MAP_1538, 1)
            }
            return@onUseWith true
        }

        on(mapPieces + Items.CRANDOR_MAP_1538, IntType.ITEM, "study") { player, node ->
            val itemDialogues =
                mapOf(
                    Items.MAP_PART_1535 to
                        "This is a piece of map that you found in Melzar's Maze. You will need to join it to the other two map pieces before you can see the route to Crandor.",
                    Items.MAP_PART_1536 to
                        "This is a piece of map that you got from Wormbrain, the goblin thief. You will need to join it to the other two map pieces before you can see the route to Crandor.",
                    Items.MAP_PART_1537 to
                        "This is a piece of map that you found in a secret chest in the Dwarven Mine. You will need to join it to the other two map pieces before you can see the route to Crandor.",
                )

            itemDialogues[node.id]?.let { dialogue ->
                sendItemDialogue(player, node.id, dialogue)
            } ?: run {
                openInterface(player, Components.DRAGON_SLAYER_QIP_MAP_547)
            }
            return@on true
        }
    }

    companion object {
        val mapPieces = intArrayOf(Items.MAP_PART_1537, Items.MAP_PART_1536, Items.MAP_PART_1535)
    }
}
