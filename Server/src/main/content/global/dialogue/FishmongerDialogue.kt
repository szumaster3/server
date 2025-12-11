package content.global.dialogue

import content.global.skill.thieving.ThievingDefinition
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Fishmongers dialogue.
 */
@Initializable
class FishmongerDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when (npc.id) {
                NPCs.FISHMONGER_1393 -> {
                    if (player.getSavedData().globalData.getStallSteal("FISH_STALL")  > System.currentTimeMillis()) {
                        npc.sendChat("Get away from me!")
                        npc.sendChat("Guards, guards!", 2)
                        return true
                    }
                    val dialogue = if (!hasRequirement(player, Quests.THRONE_OF_MISCELLANIA, false)) {
                        "Greetings, Sir. Get your fresh fish here!"
                    } else {
                        "Greetings, Your Highness. Have some fresh fish!"
                    }
                    npcl(FaceAnim.FRIENDLY, dialogue)
                    stage = 1
                }
                NPCs.FISH_MONGER_1315 -> {
                    if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                        npc(FaceAnim.ANNOYED, "I don't sell to outlanders.").also { stage = END_DIALOGUE }
                    } else {
                        val canTrade = ThievingDefinition.Stall.handleStallCooldown(
                            player = player,
                            stallName = "FISH_STALL",
                            shopNpc = npc,
                            guardNpcIds = listOf(NPCs.MARKET_GUARD_1317, NPCs.WARRIOR_1318)
                        )
                        if (!canTrade) return false
                        npcl(FaceAnim.HALF_ASKING, "Hello there, ${FremennikTrials.getFremennikName(player)}. Looking for fresh fish?")
                        stage = 3
                    }
                }
                NPCs.FISHMONGER_1369 -> {
                    npcl(FaceAnim.FRIENDLY, "Welcome, ${FremennikTrials.getFremennikName(player)}. My fish is fresher than any in Miscellania.")
                    stage = 4
                }

            }

            1 -> {
                npcl(FaceAnim.HAPPY, "I've heard that the Etceterian fish is stored in a cow shed.")
                stage = 2
            }

            2 -> { end(); openNpcShop(player, NPCs.FISHMONGER_1393) }
            3 -> { end(); openNpcShop(player, NPCs.FISH_MONGER_1315) }
            4 -> { end(); openNpcShop(player, NPCs.FISHMONGER_1369) }
        }

        return true
    }

    override fun getIds() = intArrayOf(
        NPCs.FISHMONGER_1369,
        NPCs.FISHMONGER_1393,
        NPCs.FISH_MONGER_1315
    )
}