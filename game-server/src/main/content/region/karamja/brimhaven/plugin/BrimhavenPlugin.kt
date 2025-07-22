package content.region.karamja.brimhaven.plugin

import content.region.karamja.brimhaven.dialogue.CapnIzzyDialogue
import content.region.karamja.brimhaven.dialogue.PirateJackieDialogue
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import kotlin.math.ceil

class BrimhavenPlugin : InteractionListener {

    override fun defineListeners() {
        on(AGILITY_ARENA_EXIT_LADDER, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, AGILITY_ARENA_HUT)
            return@on true
        }

        on(AGILITY_ARENA_ENTRANCE_LADDER, IntType.SCENERY, "climb-down") { player, _ ->
            if (!getAttribute(player, "capn_izzy", false)) {
                openDialogue(player, CapnIzzyDialogue(1))
                return@on true
            }

            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, AGILITY_ARENA)
            removeAttribute(player, "capn_izzy")
            return@on true
        }

        on(NPCs.CAPN_IZZY_NO_BEARD_437, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, CapnIzzyDialogue(0), node)
            return@on true
        }

        on(NPCs.CAPN_IZZY_NO_BEARD_437, IntType.NPC, "pay") { player, node ->
            openDialogue(player, CapnIzzyDialogue(2), node)
            return@on true
        }

        on(NPCs.PIRATE_JACKIE_THE_FRUIT_1055, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, PirateJackieDialogue(), node)
            return@on true
        }

        on(NPCs.PIRATE_JACKIE_THE_FRUIT_1055, IntType.NPC, "trade") { player, _ ->
            openInterface(player, TICKET_EXCHANGE)
            return@on true
        }

        on(RESTAURANT_REAR_DOOR, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You try and open the door...")
            sendMessage(player, "The door is locked tight, I can't open it.")
            return@on true
        }

        on(KARAMBWAN_FISHING_SPOT, IntType.NPC, "fish") { player, node ->
            sendNPCDialogue(player, node.id, "Keep off my fishing spot, whippersnapper!", FaceAnim.FURIOUS)
            return@on true
        }

        on(SANDY, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Nice day for sand isn't it?", FaceAnim.FURIOUS)
            return@on true
        }

        on(PIRATE, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello!", FaceAnim.HALF_GUILTY)
            addDialogueAction(player) { _,_ ->
                sendNPCDialogue(player, node.id, "Man overboard!", FaceAnim.HALF_GUILTY)
            }
            return@on true
        }
    }

    companion object {
        private val AGILITY_ARENA = location(2805, 9589, 3)
        private val AGILITY_ARENA_HUT = location(2809, 3193, 0)
        private const val AGILITY_ARENA_EXIT_LADDER = Scenery.LADDER_3618
        private const val AGILITY_ARENA_ENTRANCE_LADDER = Scenery.LADDER_3617
        private const val TICKET_EXCHANGE = Components.AGILITYARENA_TRADE_6
        private const val RESTAURANT_REAR_DOOR = Scenery.DOOR_1591
        private const val KARAMBWAN_FISHING_SPOT = NPCs.FISHING_SPOT_1178
        private val SANDY = intArrayOf(3110, NPCs.SANDY_3112, NPCs.SANDY_3113)
        private val PIRATE = intArrayOf(NPCs.PIRATE_183, NPCs.PIRATE_6349, NPCs.PIRATE_6350, NPCs.PIRATE_6346, NPCs.PIRATE_6347, NPCs.PIRATE_6348, NPCs.PIRATE_GUARD_799)

        @JvmStatic
        fun success(player: Player, skill: Int): Boolean {
            val level = player.getSkills().getLevel(skill).toDouble()
            val req = 40.0
            val successChance = ceil((level * 50 - req) / req / 3 * 4)
            val roll = RandomFunction.random(99)
            return successChance >= roll
        }
    }
}
