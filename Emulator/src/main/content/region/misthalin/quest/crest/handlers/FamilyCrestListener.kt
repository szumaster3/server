package content.region.misthalin.quest.crest.handlers

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.net.packet.out.ClearScenery
import core.net.packet.out.ConstructScenery
import core.net.packet.out.UpdateAreaPosition
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FamilyCrestListener : InteractionListener {
    val POISON = intArrayOf(Items.ANTIPOISON4_2446, Items.ANTIPOISON3_175, Items.ANTIPOISON2_177, Items.ANTIPOISON1_179)
    val DOWN_ANIMATION = Animation(2140)
    val UP_ANIMATION = Animation(2139)

    val NORTH_LEVER_A = 2421
    val NORTH_LEVER_B = 2425
    val SOUTH_LEVER = 2423
    val LEVERS =
        intArrayOf(NORTH_LEVER_A, NORTH_LEVER_A + 1, NORTH_LEVER_B, NORTH_LEVER_B + 1, SOUTH_LEVER, SOUTH_LEVER + 1)

    val NORTH_DOOR = 2431
    val HELLHOUND_DOOR = 2430
    val SOUTHWEST_DOOR = 2427
    val SOUTHEAST_DOOR = 2429
    val DOORS = intArrayOf(NORTH_DOOR, HELLHOUND_DOOR, SOUTHWEST_DOOR, SOUTHEAST_DOOR)

    override fun defineListeners() {
        onUseWith(IntType.NPC, POISON, NPCs.JOHNATHON_668) { player, used, with ->
            val npc = with.asNpc()
            val antip = used.asItem()
            val stage = getQuestStage(player, Quests.FAMILY_CREST)

            val index = POISON.indexOf(used.id)
            val returnItem = if (index + 1 == POISON.size) Items.VIAL_229 else POISON[index + 1]

            if (stage == 17 && removeItem(player, antip)) {
                addItem(player, returnItem)
                setQuestStage(player, Quests.FAMILY_CREST, 18)
                openDialogue(player, NPCs.JOHNATHON_668, npc)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }

            return@onUseWith true
        }

        on(LEVERS, IntType.SCENERY, "pull") { player, node ->
            val baseId =
                if (node.id % 2 == 0) {
                    node.id - 1
                } else {
                    node.id
                }
            if (player.questRepository.getQuest(Quests.FAMILY_CREST).getStage(player) == 0) {
                sendMessage(player, "Nothing interesting happens.")
            }
            val old = player.getAttribute("family-crest:witchaven-lever:$baseId", false)
            setAttribute(player, "family-crest:witchaven-lever:$baseId", !old)
            val direction =
                if (old) {
                    "down"
                } else {
                    "up"
                }
            sendMessage(player, "You pull the lever $direction.")
            sendMessage(player, "You hear a clunk.")
            player.animate(
                if (old) {
                    DOWN_ANIMATION
                } else {
                    UP_ANIMATION
                },
            )
            val downLever = (node as Scenery).transform(baseId)
            val upLever = node.transform(baseId + 1)
            player.debug("lever: ${downLever.id} ${upLever.id}")
            val buffer =
                UpdateAreaPosition.getChunkUpdateBuffer(
                    player,
                    RegionManager.getRegionChunk(player.location).currentBase,
                )
            if (old) {
                ClearScenery.write(buffer, upLever)
                ConstructScenery.write(buffer, downLever)
            } else {
                ClearScenery.write(buffer, downLever)
                ConstructScenery.write(buffer, upLever)
            }
            player.session.write(buffer)
            return@on true
        }

        on(DOORS, IntType.SCENERY, "open") { player, node ->
            val northA = player.getAttribute("family-crest:witchaven-lever:$NORTH_LEVER_A", false)
            val northB = player.getAttribute("family-crest:witchaven-lever:$NORTH_LEVER_B", false)
            val south = player.getAttribute("family-crest:witchaven-lever:$SOUTH_LEVER", false)
            val questComplete = player.questRepository.getQuest(Quests.FAMILY_CREST).getStage(player) >= 100

            val canPass =
                when (node.id) {
                    NORTH_DOOR -> !northA && (south || northB)
                    HELLHOUND_DOOR -> questComplete || (northA && northB && !south)
                    SOUTHWEST_DOOR -> (northA && !south) || (northA && northB && !south)
                    SOUTHEAST_DOOR -> (northA && south) || (northA && northB && south)
                    else -> false
                }
            if (canPass) {
                sendMessage(player, "The door swings open. You go through the door.")
                doDoor(player, node as Scenery)
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
    }

    fun doDoor(
        player: Player,
        scenery: Scenery,
    ) {
        val d =
            if (scenery.rotation == 0 || scenery.rotation == 3) {
                -1
            } else {
                0
            }
        var direction = Direction.NORTH
        if (scenery.rotation % 2 == 0) {
            direction =
                if (player.location.x <= scenery.location.x + d) {
                    Direction.EAST
                } else {
                    Direction.WEST
                }
        } else {
            direction =
                if (player.location.y <= scenery.location.y + d) {
                    Direction.NORTH
                } else {
                    Direction.SOUTH
                }
        }
        ForceMovement.run(
            player,
            player.location,
            player.location.transform(direction.stepX, direction.stepY, 0),
            ForceMovement.WALK_ANIMATION,
            ForceMovement.WALK_ANIMATION,
            direction,
            8,
        )
    }
}
