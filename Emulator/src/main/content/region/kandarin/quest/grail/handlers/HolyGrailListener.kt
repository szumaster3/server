package content.region.kandarin.quest.grail.handlers

import content.region.kandarin.quest.grail.HolyGrail
import content.region.kandarin.quest.grail.dialogue.*
import core.api.*
import core.api.quest.getQuestStage
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.secondsToTicks
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class HolyGrailListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.BLACK_KNIGHT_TITAN_221, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, BlackKnightTitanDialogue(false), NPCs.BLACK_KNIGHT_TITAN_221)
            return@on true
        }

        on(NPCs.HIGH_PRIEST_216, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, HighPriestDialogue(), NPCs.FISHERMAN_219)
            return@on true
        }

        on(NPCs.FISHERMAN_219, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, FishermanDialogue(), NPCs.FISHERMAN_219)
            return@on true
        }

        on(NPCs.PEASANT_214, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, DiseasedPeasantDialogue(), NPCs.PEASANT_214)
            return@on true
        }

        on(NPCs.PEASANT_215, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, HealthyPeasantDialogue(), NPCs.PEASANT_215)
            return@on true
        }

        on(NPCs.GRAIL_MAIDEN_210, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, MaidenDialogue(false), NPCs.GRAIL_MAIDEN_210)
            return@on true
        }

        on(NPCs.THE_FISHER_KING_220, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, FisherKingDialogue(), NPCs.THE_FISHER_KING_220)
            return@on true
        }

        on(Items.GRAIL_BELL_17, IntType.ITEM, "ring") { player, _ ->
            openDialogue(player, MaidenDialogue(true), NPCs.GRAIL_MAIDEN_210)
            return@on true
        }

        on(Items.MAGIC_GOLD_FEATHER_18, IntType.ITEM, "blow-on") { player, _ ->
            if (getQuestStage(player, Quests.HOLY_GRAIL) != 40) {
                sendMessage(player, "Nothing interesting happens.")
                return@on true
            }

            var dest = Location.create(2962, 3505, 0)
            var direction =
                "to the " +
                    Direction
                        .getDirection(player.location, dest)
                        .toString()
                        .lowercase()
                        .replace("_", " ")

            var zoneInBuilding = ZoneBorders(Location.create(2959, 3504, 0), Location.create(2962, 3507, 0))

            if (zoneInBuilding.insideBorder(player)) {
                direction = "at the sacks"
            } else if (dest.getDistance(player.location) <= 10) {
                direction = "to somewhere nearby"
            }

            sendMessage(player, "The feather points $direction.")
            return@on true
        }

        on(Items.MAGIC_WHISTLE_16, IntType.ITEM, "blow") { player, _ ->
            var zoneCanTeleport = ZoneBorders(Location.create(2740, 3234, 0), Location.create(2743, 3237, 0))
            var zoneIsDiseased = ZoneBorders(Location.create(2741, 4650, 0), Location.create(2811, 4742, 0))
            var zoneIsHealthy = ZoneBorders(Location.create(2614, 4661, 0), Location.create(2698, 4746, 0))

            if (!zoneCanTeleport.insideBorder(player) &&
                !zoneIsDiseased.insideBorder(player) &&
                !zoneIsHealthy.insideBorder(
                    player,
                )
            ) {
                sendDialogueLines(player, "The whistle makes no noise. It will not work in this location.")
                return@on true
            }

            var destLoc = Location.create(2806, 4715, 0)

            if (zoneCanTeleport.insideBorder(player)) {
                if (getQuestStage(player, Quests.HOLY_GRAIL) >= 50) {
                    destLoc = Location.create(2678, 4715, 0)
                }
            } else if (zoneIsDiseased.insideBorder(player) || zoneIsHealthy.insideBorder(player)) {
                destLoc = Location.create(2741, 3235, 0)
            }

            teleport(player, destLoc)
            return@on true
        }

        on(Items.HOLY_GRAIL_19, IntType.ITEM, "take") { player, grail ->
            player.faceLocation(grail.location)

            if (grail.location.getDistance(player.location) >= 2) {
                return@on false
            }

            if (player.inventory.contains(Items.HOLY_GRAIL_19, 1)) {
                sendDialogue(player, "You feel that taking more than one Holy Grail might be greedy...")
                return@on true
            }

            if (getQuestStage(player, Quests.HOLY_GRAIL) <= 40) {
                sendDialogueLines(
                    player,
                    "You feel that the Grail shouldn't be moved.",
                    "You must complete some task here before you are worthy.",
                )
            } else {
                addItem(player, Items.HOLY_GRAIL_19, 1)
            }
            return@on true
        }

        on(NPCs.SIR_PERCIVAL_211, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, SirPercivalDialogue("talk-to"))
            return@on true
        }

        on(org.rs.consts.Scenery.SACKS_23, IntType.SCENERY, "prod") { player, _ ->
            openDialogue(player, SirPercivalDialogue("prod"))
            return@on true
        }

        on(org.rs.consts.Scenery.SACKS_23, IntType.SCENERY, "open") { player, _ ->
            openDialogue(player, SirPercivalDialogue("open"))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRCASE_1730, IntType.SCENERY, "walk-up") { player, stairs ->
            ClimbActionHandler.climbLadder(player, stairs.asScenery(), "walk-up")
            spawnGrail(player)
            return@on true
        }

        on(org.rs.consts.Scenery.LADDER_1750, IntType.SCENERY, "climb-up") { player, stairs ->
            ClimbActionHandler.climbLadder(player, stairs.asScenery(), "climb-up")
            spawnGrail(player)
            return@on true
        }

        on(HolyGrail.MERLIN_DOOR_ID, IntType.SCENERY, "open") { player, door ->
            if (!door.location.equals(HolyGrail.MERLIN_DOOR_LOCATION_OPEN) &&
                !door.location.equals(HolyGrail.MERLIN_DOOR_LOCATION_CLOSED)
            ) {
                DoorActionHandler.handleDoor(player, door.asScenery())
                return@on false
            }

            if (getQuestStage(player, Quests.HOLY_GRAIL) == 0) {
                sendMessage(player, "The door won't open.")
                return@on false
            }

            val moveToX =
                if (player.location.x <=
                    HolyGrail.MERLIN_DOOR_LOCATION_CLOSED.x
                ) {
                    HolyGrail.MERLIN_DOOR_LOCATION_OPEN.x
                } else {
                    HolyGrail.MERLIN_DOOR_LOCATION_CLOSED.x
                }
            DoorActionHandler.handleAutowalkDoor(
                player,
                door as Scenery,
                Location.create(moveToX, HolyGrail.MERLIN_DOOR_LOCATION_OPEN.y, HolyGrail.MERLIN_DOOR_LOCATION_OPEN.z),
            )
            return@on true
        }

        on(org.rs.consts.Scenery.DOOR_22, IntType.SCENERY, "open") { player, door ->
            if (!door.location.equals(HolyGrail.DOOR_MAGIC_WHISTLE_LOCATION)) {
                DoorActionHandler.handleDoor(player, door.asScenery())
                return@on true
            }

            val moveToY = if (player.location.y <= 3361) 3362 else 3361
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery, Location.create(3106, moveToY, 2))

            if (getQuestStage(player, Quests.HOLY_GRAIL) == 30 && player.hasItem(Item(Items.HOLY_TABLE_NAPKIN_15, 1))) {
                GroundItemManager.create(
                    GroundItem(
                        Item(Items.MAGIC_WHISTLE_16, 1),
                        Location.create(3107, 3359, 2),
                        secondsToTicks(60),
                        player,
                    ),
                )
                GroundItemManager.create(
                    GroundItem(
                        Item(Items.MAGIC_WHISTLE_16, 1),
                        Location.create(3107, 3359, 2),
                        secondsToTicks(60),
                        player,
                    ),
                )
            }

            return@on true
        }
    }

    private fun spawnGrail(player: Player) {
        val loc = Location.create(2777, 4684, 2)

        queueScript(player, 2, QueueStrength.SOFT) { _ ->
            if (GroundItemManager.get(Items.HOLY_GRAIL_19, loc, player) == null) {
                GroundItemManager.create(GroundItem(Item(Items.HOLY_GRAIL_19, 1), loc, secondsToTicks(60 * 60), player))
            }
            return@queueScript stopExecuting(player)
        }
    }
}
