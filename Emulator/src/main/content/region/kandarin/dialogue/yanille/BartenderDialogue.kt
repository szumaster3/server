package content.region.kandarin.dialogue.yanille

import core.api.addItemOrDrop
import core.api.amountInInventory
import core.api.sendDialogue
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BartenderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_ASKING, "What can I get you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("What's on the menu?").also { stage++ }
            1 -> npc("Dragon Bitter and Greenman's Ale, oh and some cheap beer.").also { stage++ }
            2 ->
                options(
                    "I'll give it a miss I think.",
                    "I'll try the Dragon Bitter.",
                    "Can I have some Greenman's Ale?",
                    "One cheap beer please!",
                ).also {
                    stage++
                }

            3 ->
                when (buttonId) {
                    1 -> player("I'll give it a miss I think.").also { stage++ }
                    2 -> player("I'll try the Dragon Bitter.").also { stage = 5 }
                    3 -> player("Can I have some Greenman's Ale?").also { stage = 7 }
                    4 -> player("One cheap beer please!").also { stage = 9 }
                }

            4 -> npc("Come back when you're a little thirstier.").also { stage = END_DIALOGUE }
            5 -> npc("Ok, that'll be two coins.").also { stage++ }
            6 -> {
                end()
                if (amountInInventory(player, Items.COINS_995) >= 2) {
                    player.inventory.remove(Item(Items.COINS_995, 2))
                    sendMessage(player, "You buy a pint of Dragon Bitter.")
                    addItemOrDrop(player, Items.DRAGON_BITTER_1911)
                } else {
                    sendMessage(player, "You don't have enough coins.")
                }
            }

            7 -> npc("Ok, that'll be ten coins.").also { stage++ }
            8 -> {
                end()
                if (amountInInventory(player, Items.COINS_995) >= 10) {
                    player.inventory.remove(Item(Items.COINS_995, 10))
                    sendMessage(player, "You buy a pint of Greenman's Ale.")
                    addItemOrDrop(player, Items.GREENMANS_ALE_1909)
                } else {
                    sendMessage(player, "You don't have enough coins.")
                }
            }

            9 -> npc("That'll be 2 gold coins please!").also { stage++ }
            10 -> {
                if (amountInInventory(player, Items.COINS_995) >= 2) {
                    player.inventory.remove(Item(Items.COINS_995, 2))
                    sendDialogue(player, "You buy a pint of cheap beer.")
                    addItemOrDrop(player, Items.BEER_1917)
                    stage = 11
                } else {
                    end()
                    sendMessage(player, "You don't have enough coins.")
                }
            }

            11 -> npc(FaceAnim.HAPPY, "Have a super day!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BartenderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARTENDER_739)
    }
}
