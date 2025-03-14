package content.region.kandarin.dialogue.ardougne

import content.global.activity.enchkey.EnchKeyTreasure
import content.region.kandarin.quest.makinghistory.dialogue.SilverMerchantDialogueExtension
import content.region.kandarin.quest.makinghistory.handlers.MakingHistoryUtils
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SilverMerchantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Silver! Silver! Best prices for buying and selling in all", "Kandarin!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val hasKey = hasAnItem(player, Items.ENCHANTED_KEY_6754).container != null
        val hasJournal = hasAnItem(player, Items.JOURNAL_6755).container == player.inventory
        val questStage = getQuestStage(player, Quests.MAKING_HISTORY)
        val erinProgress = getVarbit(player, MakingHistoryUtils.ERIN_PROGRESS)

        when (stage) {
            0 -> {
                if (questStage >= 1) {
                    options("Yes please.", "No, thank you.", "Ask about the outpost.").also { stage = 2 }
                } else if (isQuestComplete(player, Quests.MAKING_HISTORY)) {
                    npc("Hello, I hope Jorral was pleased with that Journal.").also { stage = 4 }
                } else {
                    options("Yes please.", "No, thank you.").also { stage++ }
                }
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.SILVER_MERCHANT_569)
                    }
                    2 -> player(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
                }

            2 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.SILVER_MERCHANT_569)
                    }
                    2 -> player(FaceAnim.NEUTRAL, "No, thank you.").also { stage = END_DIALOGUE }
                    3 -> {
                        end()
                        when {
                            questStage < 2 -> openDialogue(player, SilverMerchantDialogueExtension(0))
                            erinProgress == 1 || !hasKey -> openDialogue(player, SilverMerchantDialogueExtension(13))
                            inInventory(
                                player,
                                Items.CHEST_6759,
                            ) -> openDialogue(player, SilverMerchantDialogueExtension(19))
                            erinProgress in 1..3 && !hasJournal ->
                                player(
                                    "I found a chest, but I lost it and any contents",
                                    "it had.",
                                ).also {
                                    stage++
                                }
                            inInventory(
                                player,
                                Items.JOURNAL_6755,
                            ) -> openDialogue(player, SilverMerchantDialogueExtension(21))
                            else -> npc("Hello, I hope Jorral was pleased with that Journal.").also { stage = 4 }
                        }
                    }
                }

            3 -> npc("Well I suggest you go back to where you found it.").also { stage = END_DIALOGUE }
            4 -> npc("I'm sure it's been a valuable find.").also { stage = END_DIALOGUE }
            5 -> npc("I'm sure it's been a valuable find.").also { stage++ }

            6 -> {
                if (hasAnItem(player!!, Items.ENCHANTED_KEY_6754).exists()) {
                    end()
                    npc(
                        "You know you can use that enchanted key you have on",
                        "your keyring all over Gielinor. Who knows what you might find?",
                    )
                } else if (getAttribute(player!!, EnchKeyTreasure.ENCHANTED_KEY_ATTR, 0) == 11) {
                    player("Oh, You know that key you gave me?").also { stage = 11 }
                } else {
                    player("What I came to ask was: I lost that key you gave me.").also { stage++ }
                }
            }

            7 ->
                npc(
                    "Oh dear, luckily I found it, but it'll cost you 500gp",
                    "as I know it's valuable.",
                ).also { stage++ }
            8 -> options("Yes please.", "No thanks.").also { stage++ }

            9 ->
                when (buttonId) {
                    1 -> player("Yes please.").also { stage++ }
                    2 -> player("No thanks.").also { stage = END_DIALOGUE }
                }

            10 -> {
                end()
                if (freeSlots(player!!) < 0) {
                    npc("You don't have the space to carry it! Empty some space", "in your inventory.")
                    return true
                }
                if (!removeItem(player!!, Item(Items.COINS_995, 500))) {
                    npc("You don't have enough money, sorry.")
                } else {
                    npc("Thank you, enjoy!")
                    setAttribute(player!!, EnchKeyTreasure.ENCHANTED_KEY_ATTR, 0)
                    addItemOrDrop(player!!, Items.ENCHANTED_KEY_6754, 1)
                }
            }
            11 -> npc("Yes?").also { stage++ }
            12 -> player("It dissolved!").also { stage++ }
            13 -> npc("Really? I suppose it served its purpose.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SilverMerchantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SILVER_MERCHANT_569)
    }
}
