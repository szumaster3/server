package content.region.asgarnia.handlers

import core.api.*
import core.api.interaction.openBankAccount
import core.api.interaction.openGrandExchangeCollectionBox
import core.api.interaction.openNpcShop
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BurthopeListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles entering through the Thieving Guild passage.
         */

        on(THIEVING_GUILD_PASSAGE, IntType.SCENERY, "enter") { player, node ->
            if (node.id == Scenery.TRAPDOOR_7257) {
                teleport(player, Location(3061, 4985, 1))
            } else {
                teleport(player, Location(2906, 3537, 0))
            }
            return@on true
        }

        /*
         * Handles climbing up/down set of stairs.
         */

        on(STAIRS_1, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(2205, 4934, 1))
            return@on true
        }
        on(STAIRS_2, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(2899, 3565, 0))
            return@on true
        }

        /*
         * Handles banking interaction with Benedict.
         */

        on(BENEDICT, IntType.NPC, "bank") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        /*
         * Handles collecting items from the Grand Exchange collection box via Benedict NPC.
         */

        on(BENEDICT, IntType.NPC, "collect") { player, _ ->
            openGrandExchangeCollectionBox(player)
            return@on true
        }

        /*
         * Handles trading with Martin Thwait.
         */

        on(MARTIN, IntType.NPC, "trade") { player, _ ->
            if (getStatLevel(player, Skills.THIEVING) < 50 || getStatLevel(player, Skills.AGILITY) < 50) {
                sendNPCDialogue(
                    player,
                    NPCs.MARTIN_THWAIT_2270,
                    "Sorry, mate. Train up your ${if (getStatLevel(
                            player,
                            Skills.THIEVING,
                        ) < 50 &&
                        getStatLevel(player, Skills.AGILITY) < 50
                    ) {
                        "Thieving and Agility"
                    } else if (getStatLevel(player, Skills.THIEVING) < 50) {
                        "Thieving"
                    } else {
                        "Agility"
                    }} skill to at least 50 and I might be able to help you out.",
                    FaceAnim.HALF_GUILTY,
                )
            } else {
                openNpcShop(player, NPCs.MARTIN_THWAIT_2270)
            }
            return@on true
        }

        /*
         * Handles dialogue when talking to Sergeants.
         */

        on(NPCs.SERGEANT_1061, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The Sergeant is busy training the soldiers.")
            return@on true
        }
        on(NPCs.SERGEANT_1062, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The Sergeant is busy training the soldiers.")
            return@on true
        }

        /*
         * Handles dialogue when talking to soldiers.
         */

        on(NPCs.SOLDIER_1063, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The soldier is busy training.")
            return@on true
        }
        on(NPCs.SOLDIER_1064, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The soldier is busy training.")
            return@on true
        }
        on(NPCs.SOLDIER_1066, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The soldier is busy eating.")
            return@on true
        }
        on(NPCs.SOLDIER_1067, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The soldier is busy eating.")
            return@on true
        }
        on(NPCs.SOLDIER_1068, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The soldier is busy eating.")
            return@on true
        }

        /*
         * Handles dialogue when talking to archers.
         */

        on(NPCs.ARCHER_1073, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The archer won't talk whilst on duty.")
            return@on true
        }
        on(NPCs.ARCHER_1074, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The archer won't talk whilst on duty.")
            return@on true
        }

        /*
         * Handles dialogue when talking to guards.
         */

        on(NPCs.GUARD_1076, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The guard won't talk whilst on duty.")
            return@on true
        }
        on(NPCs.GUARD_1077, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The guard won't talk whilst on duty.")
            return@on true
        }
    }

    companion object {
        private const val BENEDICT = NPCs.EMERALD_BENEDICT_2271
        private const val MARTIN = NPCs.MARTIN_THWAIT_2270
        private const val STAIRS_1 = Scenery.STAIRS_4624
        private const val STAIRS_2 = Scenery.STAIRS_4627
        private val THIEVING_GUILD_PASSAGE = intArrayOf(Scenery.TRAPDOOR_7257, Scenery.PASSAGEWAY_7258)
    }
}
