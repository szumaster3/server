package content.region.karamja.dialogue.shilovillage

import content.region.karamja.dialogue.KalebParamayaDiaryDialogue
import core.api.addItemOrDrop
import core.api.openDialogue
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class KalebParamayaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("Hello, Bwana, what can I do for you today?").also { stage++ }
            1 ->
                options(
                    "Can you tell me a bit about this place?",
                    "Have you anything for sale?",
                    "I have a question about my Achievement Diary.",
                    "I'm fine, thanks.",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Can you tell me a bit about this place?").also { stage++ }
                    2 -> player("Have you anything for sale?").also { stage = 8 }
                    3 -> player("I have a question about my Achievement Diary.").also { stage = 7 }
                    4 -> player("I'm fine, thanks.").also { stage = END_DIALOGUE }
                }
            3 -> npc("Of course Bwana, you look like a traveller!").also { stage++ }
            4 -> player("Yes I am actually!").also { stage++ }
            5 ->
                npc(
                    "Well, I am a traveller myself, and I have set up this hostel",
                    "for adventurers and travellers who are weary from their",
                    "journey.",
                ).also {
                    stage++
                }
            6 ->
                npc(
                    "There is a dormitory upstairs if you are tired, it costs",
                    "35 gold pieces which covers the cost of laundry and",
                    "cleaning.",
                ).also {
                    stage =
                        1
                }
            7 -> {
                end()
                openDialogue(player, KalebParamayaDiaryDialogue(), npc)
            }

            8 ->
                options(
                    "Buy some wine: 1 Gold.",
                    "Buy some Beer: 2 Gold.",
                    "Buy a nights rest: 35 Gold",
                    "Buy a pack of 5 Dorm tickets: 175 Gold",
                ).also { stage++ }

            9 ->
                when (buttonId) {
                    1 -> {
                        if (!removeItem(player, Item(Items.COINS_995, 1))) {
                            end()
                            npc("Sorry Bwana, you don't have enough money.")
                        } else {
                            end()
                            npc("Very good ${if (player.isMale) "sir" else "madam"}!")
                            sendMessage(player, "You purchase a jug of wine.")
                            addItemOrDrop(player!!, Items.JUG_OF_WINE_1993, 1)
                        }
                    }

                    2 -> {
                        if (!removeItem(player, Item(Items.COINS_995, 2))) {
                            end()
                            npc("Sorry Bwana, you don't have enough money.")
                        } else {
                            end()
                            npc("Very good ${if (player.isMale) "sir" else "madam"}!")
                            sendMessage(player, "You purchase a beer.")
                            addItemOrDrop(player!!, Items.BEER_1917, 1)
                        }
                    }

                    3 -> {
                        if (!removeItem(player, Item(Items.COINS_995, 35))) {
                            end()
                            npc("Sorry Bwana, you don't have enough money.")
                        } else {
                            end()
                            npc("Very good ${if (player.isMale) "sir" else "madam"}!")
                            sendMessage(player, "You purchase a ticket to access the dormitory.")
                            addItemOrDrop(player!!, Items.PARAMAYA_TICKET_619, 1)
                        }
                    }

                    4 -> {
                        if (!removeItem(player, Item(Items.COINS_995, 175))) {
                            end()
                            npc("Sorry Bwana, you don't have enough money.")
                        } else {
                            end()
                            npc("Very good ${if (player.isMale) "sir" else "madam"}!")
                            sendMessage(player, "You purchase 5 tickets to access the dormitory.")
                            addItemOrDrop(player!!, Items.PARAMAYA_TICKET_619, 5)
                        }
                    }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KALEB_PARAMAYA_512)
    }
}
