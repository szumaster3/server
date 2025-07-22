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
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Fruit bat dialogue.
 */
@Initializable
class FruitBatDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = FruitBatDialogue(player)

    /**
     * Instantiates a new Fruit bat dialogue.
     */
    constructor()

    /**
     * Instantiates a new Fruit bat dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (amountInInventory(player, Items.PAPAYA_FRUIT_5972) >= 5) {
            npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueeksquee?", "(Can I have a papaya?)")
            stage = 0
            return true
        }
        if (inZone(player, "karamja")) {
            npc(FaceAnim.CHILD_NORMAL, "Squeesqueak squeak!", "(I smell fruit!)")
            stage = 9
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeekasqueek squeek?", "(How much longer do you want me for?)")
                sendChat(npc, "Squeeeak!", 0)
                stage = 3
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeakdqueesqueak.", "(This place is fun!)")
                stage = 4
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueek?", "(Where are we going?)")
                sendChat(npc, "Squeee!", 0)
                stage = 5
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueek squee?", "(Can you smell lemons?)")
                stage = 6
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "No, I have a very specific plan for them.")
                stage++
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeek?", "(What?)")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I was just going to grate it over some other vegetables and eat it. Yum.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "I don't really know at the moment, it all depends what I want to do today.")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Glad you think so!")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, we're likely to go to a lot of places today.")
                stage = END_DIALOGUE
            }

            6 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueek squee?", "(Can you smell lemons?)")
                stage++
            }

            7 -> {
                playerl(FaceAnim.HALF_ASKING, "No, why do you ask?")
                stage++
            }

            8 -> {
                npc(FaceAnim.CHILD_NORMAL, "Squeaksqueak squeaksqueesqueak.", "(Must just be thinking about them.)")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, there is likely to be some up in those trees, if you go looking for it.",
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FRUIT_BAT_6817)
}
