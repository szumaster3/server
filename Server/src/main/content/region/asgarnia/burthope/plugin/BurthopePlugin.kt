package content.region.asgarnia.burthope.plugin

import content.region.asgarnia.burthope.dialogue.*
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class BurthopePlugin : InteractionListener {

    companion object {
        private const val BENEDICT = NPCs.EMERALD_BENEDICT_2271
        private const val MARTIN = NPCs.MARTIN_THWAIT_2270
        private const val STAIRS_1 = Scenery.STAIRS_4624
        private const val STAIRS_2 = Scenery.STAIRS_4627
        private val THIEVING_GUILD_PASSAGE = intArrayOf(Scenery.TRAPDOOR_7257, Scenery.PASSAGEWAY_7258)
        private val BURTHOPE_CITIZEN_NPC_IDS = intArrayOf(NPCs.BREOCA_1084, NPCs.BERNALD_2580, NPCs.CEOLBURG_1089, NPCs.OCGA_1085, NPCs.HILD_1090, NPCs.HYGD_1088)
    }

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
         * Handles dunstan drawer interaction (Treasure trail).
         */

        on(Scenery.DRAWERS_350, IntType.SCENERY, "open") { player, node ->
            val inRoom = inBorders(player, 2920, 3576, 2922, 3578)
            val hasClue = inInventory(player, Items.CLUE_SCROLL_10236)
            val hasKey = inInventory(player, Items.KEY_2836)

            if (inRoom && hasClue && hasKey) {
                if (removeItem(player, Items.KEY_2836)) {
                    replaceScenery(node.asScenery(), Scenery.DRAWERS_351, 80, node.location)
                } else {
                    sendMessage(player, "The chest is locked.")
                }
            } else {
                sendMessage(player, "The chest is locked.")
            }

            return@on true
        }

        on(Scenery.DRAWERS_351, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), Scenery.DRAWERS_350, -1)
            return@on true
        }

        /*
         * Handles trading with Martin Thwait.
         */

        on(MARTIN, IntType.NPC, "trade") { player, _ ->
            val thievingLevel = getStatLevel(player, Skills.THIEVING)
            val agilityLevel = getStatLevel(player, Skills.AGILITY)

            if (thievingLevel < 50 || agilityLevel < 50) {
                val skillMessage = when {
                    thievingLevel < 50 && agilityLevel < 50 -> "Thieving and Agility"
                    thievingLevel < 50 -> "Thieving"
                    else -> "Agility"
                }

                sendNPCDialogue(
                    player,
                    NPCs.MARTIN_THWAIT_2270,
                    "Sorry, mate. Train up your $skillMessage skill to at least 50 and I might be able to help you out.",
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
         * Handles talking to guards.
         */

        on(NPCs.GUARD_1076, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The guard won't talk whilst on duty.")
            return@on true
        }
        on(NPCs.GUARD_1077, IntType.NPC, "talk-to") { player, _ ->
            sendDialogue(player, "The guard won't talk whilst on duty.")
            return@on true
        }

        /*
         * Handles talking to citizens.
         */

        on(BURTHOPE_CITIZEN_NPC_IDS, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, BurthopeNPCDialogue(), node)
            return@on true
        }

        on(NPCs.SOLDIER_1065, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, BurthorpeSoldierDialogue(), node)
            return@on true
        }

        on(NPCs.EADBURG_1072, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, EadburgDialogue(), node)
            return@on true
        }

        on(NPCs.HELEMOS_797, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, HelemosDialogue(), node)
            return@on true
        }

        on(NPCs.PENDA_1087, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, PendaDialogue(), node)
            return@on true
        }

        on(intArrayOf(NPCs.SAM_1357, NPCs.RACHAEL_1358), IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, RachaelDialogue(), node)
            return@on true
        }

        on(NPCs.SERVANT_1081, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, ServantDialogue(), node)
            return@on true
        }

        on(NPCs.TOSTIG_1079, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, TostigDialogue(), node)
            return@on true
        }

        on(NPCs.WISTAN_1083, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, WistanDialogue(), node)
            return@on true
        }

        on(NPCs.UNFERTH_2655, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, UnferthDialogue(), node)
            return@on true
        }

    }

}
