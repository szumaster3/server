package content.region.fremennik.island.neitiznot.dialogue

import core.api.inInventory
import core.api.removeItem
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ThakkradYakDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        options("Cure my yak-hide, please.", "Nothing, thanks.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> when (buttonId) {
                1 -> player("Cure my yak-hide, please.").also { stage = 3 }
                2 -> player("Nothing, thanks.").also { stage++ }
            }
            1 -> sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "See you later.").also { stage++ }
            2 -> sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "You won't find anyone else who can cure yak-hide.").also { stage = END_DIALOGUE }
            3 -> sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "I will cure yak-hide for a fee of 5 gp per hide.").also { stage++ }
            4 -> {
                val hasYakHide = player.inventory.contains(Items.YAK_HIDE_10818, 1)
                val hasCoins = player.inventory.contains(Items.COINS_995, 5)

                when {
                    !hasYakHide -> {
                        sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "You have no yak-hide to cure.")
                        stage = 7
                        return true
                    }
                    !hasCoins -> {
                        sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "You don't have enough gold to pay me!")
                        stage = 7
                        return true
                    }
                    else -> {
                        options(
                            "Cure all my hides.",
                            "Cure one hide.",
                            "Cure no hide.",
                            "Can you cure any other type of leather?"
                        )
                        stage++
                    }
                }
            }
            5 -> when (buttonId) {
                1, 2 -> {
                    end()
                    cure(player, if (buttonId == 2) 1 else player.inventory.getAmount(Items.YAK_HIDE_10818))
                }
                3 -> sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "Bye!").also { stage = 7 }
                4 -> sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "Other types of leather? Why would you need any other type of leather?").also { stage = 6 }
            }
            6 -> player("I'll take that as a no then.").also { stage = 7 }
            7 -> end()
        }
        return true
    }

    private fun cure(player: Player, amount: Int): Boolean {
        if (!inInventory(player, Items.COINS_995, 5 * amount)) {
            sendNPCDialogue(player, NPCs.THAKKRAD_SIGMUNDSON_5506, "You don't have enough gold to pay me!")
            return false
        }
        if (removeItem(player, Item(Items.COINS_995, 5 * amount))) {
            for (i in 0 until amount) {
                if (player.inventory.remove(Item(Items.YAK_HIDE_10818))) {
                    player.inventory.add(Item(Items.CURED_YAK_HIDE_10820))
                    npc("There you go!")
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ThakkradYakDialogue(player)

    override fun getIds(): IntArray = intArrayOf(DialogueInterpreter.getDialogueKey("thakkrad-yak"))
}
