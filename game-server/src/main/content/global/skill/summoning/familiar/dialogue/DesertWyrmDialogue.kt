package content.global.skill.summoning.familiar.dialogue

import core.api.anyInEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Desert wyrm dialogue.
 */
@Initializable
class DesertWyrmDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = DesertWyrmDialogue(player)

    /**
     * Instantiates a new Desert wyrm dialogue.
     */
    constructor()

    /**
     * Instantiates a new Desert wyrm dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (anyInEquipment(player, *PICKAXE)) {
            npcl(FaceAnim.CHILD_NORMAL, "If you have that pick, why make me dig?")
            stage = 0
            return true
        }

        when (Random().nextInt(4)) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "This is so unsafe...I should have a hard hat for this work...")
                stage = 6
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You can't touch me, I'm part of the union!")
                stage = 8
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You know, you might want to register with the union.")
                stage = 10
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why are you ignoring that good ore seam, " + player.username + "?")
                stage = 12
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
                playerl(FaceAnim.FRIENDLY, "Because it's a little quicker and easier on my arms.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I should take industrial action over this...")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "You mean you won't work for me any more?")
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "No. It means me and the lads feed you legs-first into some industrial machinery, maybe the Blast Furnace.",
                )
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "I'll just be over here, digging.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That's the spirit, lad!")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I could get you a rune helm if you like - those are pretty hard.")
                stage++
            }

            7 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Keep that up and you'll have the union on your back, " + player.username + ".",
                )
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "Is that some official no touching policy or something?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You really don't get it, do you " + player.username + "?")
                stage = END_DIALOGUE
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I stop bugging you to join the union.")
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Ask that again later; I'll have to consider that generous proposal.")
                stage = END_DIALOGUE
            }

            12 -> {
                playerl(FaceAnim.HALF_ASKING, "Which ore seam?")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "There's a good ore seam right underneath us at this very moment.")
                stage++
            }

            14 -> {
                playerl(FaceAnim.HALF_ASKING, "Great! How long will it take for you to get to it?")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Five years, give or take.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Five years!")
                stage++
            }

            17 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "That's if we go opencast, mind. I could probably reach it in three if I just dug.",
                )
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "Right. I see. I think I'll skip it thanks.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DESERT_WYRM_6831, NPCs.DESERT_WYRM_6832)

    companion object {
        /**
         * The constant pickaxes.
         */
        val PICKAXE: IntArray =
            intArrayOf(
                Items.BRONZE_PICKAXE_1265,
                Items.IRON_PICKAXE_1267,
                Items.STEEL_PICKAXE_1269,
                Items.MITHRIL_PICKAXE_1273,
                Items.ADAMANT_PICKAXE_1271,
                Items.RUNE_PICKAXE_1275,
                Items.INFERNO_ADZE_13661,
            )
    }
}
