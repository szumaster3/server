package content.global.skill.summoning.familiar.dialogue

import core.api.amountInInventory
import core.api.inZone
import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Fruit Bat familiar dialogue.
 */
@Initializable
class FruitBatDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?) = FruitBatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC


        branch = when {
            amountInInventory(player, Items.PAPAYA_FRUIT_5972) >= 5 -> -1
            inZone(player, "karamja") -> -2
            else -> (0..3).random()
        }
        stage = 0

        when (branch) {
            -1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeeksqueekasqueeksquee?", "(Can I have a papaya?)")
            -2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeesqueak squeak!", "(I smell fruit!)")
            0 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeekasqueek squeek?", "(How much longer do you want me for?)")
                sendChat(npc, "Squeeeak!")
            }
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeakdqueesqueak.", "(This place is fun!)")
            2 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeeksqueekasqueek?", "(Where are we going?)")
                sendChat(npc, "Squeee!")
            }
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeeksqueekasqueek squee?", "(Can you smell lemons?)")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            -1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "No, I have a very specific plan for them."); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeek?", "(What?)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "I was just going to grate it over some other vegetables and eat it. Yum."); stage = END_DIALOGUE }
            }
            -2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Well, there is likely to be some up in those trees, if you go looking for it."); stage = END_DIALOGUE }
            }
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I don't really know at the moment, it all depends what I want to do today."); stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Glad you think so!"); stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Oh, we're likely to go to a lot of places today."); stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "No, why do you ask?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Squeaksqueak squeaksqueesqueak.", "(Must just be thinking about them.)"); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.FRUIT_BAT_6817)
}
