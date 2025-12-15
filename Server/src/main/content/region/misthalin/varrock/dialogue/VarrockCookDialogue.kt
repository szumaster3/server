package content.region.misthalin.varrock.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Cook (Varrock) dialogue.
 */
@Initializable
class VarrockCookDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "What do you want? I'm busy!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                setTitle(player, 3)
                sendOptions(player, "What would you like to say?", "Can you sell me any food?", "Can you give me any free food?", "I don't want anything from this horrible kitchen.").also { stage++ }
            }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "Can you sell me any food?").also { stage = 10 }
                2 -> player(FaceAnim.HALF_GUILTY, "Can you give me any free food?").also { stage = 20 }
                3 -> player(FaceAnim.HALF_GUILTY, "I don't want anything from this horrible kitchen.").also { stage = 30 }

            }
            10 -> npc(FaceAnim.HALF_GUILTY, "I suppose I could sell you some cabbage, if you're willing to", "pay for it. Cabbage is good for you.").also { stage++ }
            11 -> {
                setTitle(player, 2)
                sendOptions(player, "What would you like to say?", "Alright, I'll buy a cabbage.", "No thanks, I don't like cabbage.").also { stage++ }
            }
            12 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "Alright, I'll buy a cabbage.").also { stage = 33 }
                2 -> player(FaceAnim.HALF_GUILTY, "No thanks, I don't like cabbage.").also { stage = 34 }
            }
            20 -> npc(FaceAnim.HALF_GUILTY, "Can you give me any free money?").also { stage++ }
            21 -> player(FaceAnim.HALF_GUILTY, "Why should I give you free money?").also { stage++ }
            22 -> npc(FaceAnim.HALF_GUILTY, "Why should I give you free food?").also { stage++ }
            23 -> player(FaceAnim.HALF_GUILTY, "Oh, forget it.").also { stage = END_DIALOGUE }
            30 -> npc(FaceAnim.HALF_GUILTY, "How dare you? I put a lot of effort into cleaning this", "kitchen. My daily sweat and elbow-grease keep this kitchen", "clean!").also { stage++ }
            31 -> player(FaceAnim.DISGUSTED, "Ewww!").also { stage++ }
            32 -> npc(FaceAnim.HALF_GUILTY, "Oh, just leave me alone.").also { stage = END_DIALOGUE }
            33 -> {
                end()
                if (!removeItem(player, Item(Items.COINS_995, 1))) {
                    sendMessage(player, "You need one coin to buy a cabbage.")
                    stage = END_DIALOGUE
                } else {
                    addItem(player, Items.CABBAGE_1965, 1)
                    npc(FaceAnim.HALF_GUILTY, "It's a deal. Now, make sure you eat it all up. Cabbage is", "good for you.")
                    stage = END_DIALOGUE
                }
            }
            34 -> npc(FaceAnim.HALF_GUILTY, "Bah! People these days only appreciate junk food.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = VarrockCookDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.COOK_5910)
}
