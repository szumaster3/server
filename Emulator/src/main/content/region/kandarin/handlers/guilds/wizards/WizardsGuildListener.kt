package content.region.kandarin.handlers.guilds.wizards

import content.global.travel.EssenceTeleport.teleport
import core.api.*
import core.api.quest.isQuestComplete
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WizardsGuildListener : InteractionListener {
    override fun defineListeners() {
        on(MAGIC_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getDynLevel(player, Skills.MAGIC) < 66) {
                sendPlayerDialogue(player, "You need a Magic level of at least 66 to enter.")
                return@on true
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles basement of the Wizards' Guild interaction with gates.
         */

        on(GATE, IntType.SCENERY, "open") { player, _ ->
            sendNPCDialogue(
                player,
                NPCs.WIZARD_FRUMSCONE_460,
                "You can't attack the Zombies in the room, my Zombies are for magic target practice only and should be attacked from the other side of the fence.",
            )
            return@on true
        }

        /*
         * Handles staircase.
         */

        on(Scenery.STAIRCASE_1722, IntType.SCENERY, "climb-up") { player, node ->
            if (node.location == Location(2590, 3089, 0)) {
                ClimbActionHandler.climb(player, null, Location.create(2591, 3092, 1))
            } else {
                ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-up")
            }
            return@on true
        }

        /*
         * Handling the essence teleport inside guild.
         */

        on(NPCs.WIZARD_DISTENTOR_462, IntType.NPC, "teleport") { player, node ->
            if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                sendMessage(player, "You need to have completed the Rune Mysteries Quest to use this feature.")
                return@on false
            }

            teleport((node as NPC), player)
            return@on true
        }
    }

    companion object {
        val MAGIC_DOOR = intArrayOf(Scenery.MAGIC_GUILD_DOOR_1600, Scenery.MAGIC_GUILD_DOOR_1601)
        val GATE = intArrayOf(Scenery.GATE_2154, Scenery.GATE_2155)
    }
}
