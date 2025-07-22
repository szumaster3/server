package content.region.misthalin.varrock.quest.crest.plugin

import core.api.*
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.Scenery
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.net.packet.out.ClearScenery
import core.net.packet.out.ConstructScenery
import core.net.packet.out.UpdateAreaPosition
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class FamilyCrestListener : InteractionListener {
    private val poisonId =
        intArrayOf(Items.ANTIPOISON4_2446, Items.ANTIPOISON3_175, Items.ANTIPOISON2_177, Items.ANTIPOISON1_179)
    private val pullDownAnimation = Animation(Animations.PULL_DOWN_LEVER_2140)
    private val pullUpAnimation = Animation(Animations.PULL_UP_LEVER_2139)
    private val northLeverA = org.rs.consts.Scenery.LEVER_2421
    private val northLeverB = org.rs.consts.Scenery.LEVER_2425
    private val southLever = org.rs.consts.Scenery.LEVER_2423
    private val leverId =
        intArrayOf(northLeverA, northLeverA + 1, northLeverB, northLeverB + 1, southLever, southLever + 1)
    private val northDoorId = org.rs.consts.Scenery.DOOR_2431
    private val hellhoundDoorId = org.rs.consts.Scenery.DOOR_2430
    private val southwestDoorId = org.rs.consts.Scenery.DOOR_2427
    private val southEastDoorId = org.rs.consts.Scenery.DOOR_2429
    private val doorsIDs = intArrayOf(northDoorId, hellhoundDoorId, southwestDoorId, southEastDoorId)

    override fun defineListeners() {
        onUseWith(IntType.NPC, poisonId, NPCs.JOHNATHON_668) { player, used, with ->
            val npc = with.asNpc()
            val antip = used.asItem()
            val stage = getQuestStage(player, Quests.FAMILY_CREST)

            val index = poisonId.indexOf(used.id)
            val returnItem = if (index + 1 == poisonId.size) Items.VIAL_229 else poisonId[index + 1]

            if (stage == 17 && removeItem(player, antip)) {
                addItem(player, returnItem)
                setQuestStage(player, Quests.FAMILY_CREST, 18)
                openDialogue(player, NPCs.JOHNATHON_668, npc)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }

            return@onUseWith true
        }

        on(leverId, IntType.SCENERY, "pull") { player, node ->
            val baseId = if (node.id % 2 == 0) {
                node.id - 1
            } else {
                node.id
            }
            if (getQuestStage(player, Quests.FAMILY_CREST) == 0) {
                sendMessage(player, "Nothing interesting happens.")
            }
            val old = player.getAttribute("family-crest:witchaven-lever:$baseId", false)
            setAttribute(player, "family-crest:witchaven-lever:$baseId", !old)
            val direction = if (old) "down" else "up"
            sendMessage(player, "You pull the lever $direction.")
            sendMessage(player, "You hear a clunk.")
            animate(player,
                if (old) {
                    pullDownAnimation
                } else {
                    pullUpAnimation
                },
            )
            val downLever = (node as Scenery).transform(baseId)
            val upLever = node.transform(baseId + 1)
            val buffer = UpdateAreaPosition.getChunkUpdateBuffer(
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

        on(doorsIDs, IntType.SCENERY, "open") { player, node ->
            val northA = player.getAttribute("family-crest:witchaven-lever:$northLeverA", false)
            val northB = player.getAttribute("family-crest:witchaven-lever:$northLeverB", false)
            val south = player.getAttribute("family-crest:witchaven-lever:$southLever", false)
            val questComplete = getQuestStage(player, Quests.FAMILY_CREST) >= 100

            val canPass = when (node.id) {
                northDoorId -> !northA && (south || northB)
                hellhoundDoorId -> questComplete || (northA && northB && !south)
                southwestDoorId -> (northA && !south) || (northA && northB && !south)
                southEastDoorId -> (northA && south) || (northA && northB && south)
                else -> false
            }
            if (canPass) {
                sendMessage(player, "The door swings open. You go through the door.")
                DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
    }
}
