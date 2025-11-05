package content.region.kandarin.ardougne.west.dialogue

import core.api.amountInInventory
import core.api.hasRequirement
import core.api.removeItem
import core.api.replaceSlot
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class DarkMageDialogue(player: Player? = null) : Dialogue(player) {

    private var hasIbanStaff = false

    override fun open(vararg args: Any?): Boolean {
        val npc = args[0] as NPC
        this.npc = npc
        hasIbanStaff = player!!.inventory.contains(Items.IBANS_STAFF_1410, 1)
        if (!hasRequirement(player, Quests.UNDERGROUND_PASS)) {
            npcl(FaceAnim.NEUTRAL, "Why do you interrupt me, traveller?")
        } else {
            npcl(FaceAnim.NEUTRAL, "Oh, it's the liberator of Ardougne. Why do you interrupt me?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.NEUTRAL, "I just wondered what you're doing?")
                stage++
            }
            1 -> {
                npcl(FaceAnim.NEUTRAL, "I experiment with dark magic. It's a dangerous craft.")
                stage = if (hasIbanStaff) 3 else 6
            }
            2 -> {
                playerl(FaceAnim.NEUTRAL, "Could you fix this staff?")
                stage++
            }
            3 -> {
                npcl(FaceAnim.FRIENDLY, "Almighty Zamorak! The Staff of Iban!")
                stage++
            }
            4 -> {
                npcl(FaceAnim.NEUTRAL, "This truly is dangerous magic, traveller. I can fix it, but it will cost you. The process could kill me.")
                stage++
            }

            5 -> {
                playerl(FaceAnim.NEUTRAL, "How much?")
                stage++
            }
            6 -> {
                npcl(FaceAnim.NEUTRAL, "200,000 gold pieces. Not a penny less.")
                stage++
            }
            7 -> {
                options("Okay then.", "No chance. that's ridiculous!")
                stage++
            }
            8 -> {
                when(buttonId) {
                    1 -> player("Okay then.").also { stage++ }
                    2 -> player(FaceAnim.HALF_THINKING, "No chance. that's ridiculous!").also { stage = 11 }
                }
            }
            9 -> {
                val coins = amountInInventory(player,Items.COINS_995)
                if (coins >= 200_000) {
                    removeItem(player, Item(Items.COINS_995, 200_000))
                    replaceSlot(player, player.inventory.getAsId(Items.IBANS_STAFF_1410), Item(Items.IBANS_STAFF_1409))
                    playerl(FaceAnim.HAPPY, "Thanks. I appreciate that.")
                    stage++
                } else {
                    playerl(FaceAnim.SAD, "Oops! I'm a bit short.")
                    stage = 11
                }
            }
            10 -> {
                npcl(FaceAnim.NEUTRAL, "You be careful with that thing!")
                stage = END_DIALOGUE
            }
            11 -> {
                npcl(FaceAnim.ANGRY, "...Fine by me.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.DARK_MAGE_1001)
}
