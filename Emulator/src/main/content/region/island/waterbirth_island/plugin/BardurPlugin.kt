package content.region.island.waterbirth_island.plugin

import content.region.fremennik.quest.viking.FremennikTrials
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class BardurPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with Bardur NPCs.
         */

        onUseWith(IntType.NPC, exchangeItemIDs, NPCs.BARDUR_2879) { player, node, _ ->
            val npc = node.asNpc()
            if (npc.inCombat()) {
                sendMessage(player, "He's busy right now.")
                return@onUseWith false
            }

            if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                sendNPCDialogue(player, NPCs.BARDUR_2879, "I do not trust you outerlander, I will not accept your gifts, no matter what your intention...")
            } else {
                npc.impactHandler.disabledTicks = 8
                openDialogue(player, BardurExchangeDialogue())
            }
            return@onUseWith true
        }

    }

    companion object {
        val exchangeItemIDs =
            intArrayOf(Items.FREMENNIK_HELM_3748, Items.FREMENNIK_BLADE_3757, Items.FREMENNIK_SHIELD_3758)
    }
}

private class BardurExchangeDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.BARDUR_2879)
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Ah, just what I was looking for! You wish to trade me that for a cooked shark?").also { stage++ }
            1 -> sendDialogueOptions(player!!, "Trade with Bardur?", "YES", "NO").also { stage++ }
            2 ->
                when (buttonID) {
                    1 -> if ((inInventory(player!!, Items.FREMENNIK_HELM_3748) && removeItem(
                            player!!,
                            Items.FREMENNIK_HELM_3748,
                        )) || (inInventory(player!!, Items.FREMENNIK_BLADE_3757) && removeItem(
                            player!!,
                            Items.FREMENNIK_BLADE_3757,
                        )) || (inInventory(player!!, Items.FREMENNIK_SHIELD_3758) && removeItem(
                            player!!,
                            Items.FREMENNIK_SHIELD_3758,
                        ))
                    ) {
                        end()
                        addItemOrDrop(player!!, Items.SHARK_385)
                        npcl(
                            FaceAnim.FRIENDLY, "Ah, this will serve me well as a backup! My thanks to you ${
                            FremennikTrials.getFremennikName(player!!)
                        }, I trust we will one day sing songs together of glorious battles in the Rellekka longhall!",)
                    }

                    2 -> {
                        end()
                        npcl(
                            FaceAnim.FRIENDLY, "As you wish, ${
                            FremennikTrials.getFremennikName(player!!)
                        }. My weapons have lasted me this long, I will keep my trust in them yet.",)
                    }
                }
        }
    }
}
