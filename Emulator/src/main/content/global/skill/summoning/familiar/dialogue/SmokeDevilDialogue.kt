package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Smoke devil dialogue.
 */
@Initializable
class SmokeDevilDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return SmokeDevilDialogue(player)
    }

    /**
     * Instantiates a new Smoke devil dialogue.
     */
    constructor()

    /**
     * Instantiates a new Smoke devil dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val randomChoice = (Math.random() * 4).toInt()
        when (randomChoice) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When are you going to be done with that?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey!")
                stage = 6
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ah, this is the life!")
                stage = 16
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why is it always so cold here?")
                stage = 22
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Soon, I hope.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Good, because this place is too breezy.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "What do you mean?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I mean, it's tricky to keep hovering in this draft.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Ok, we'll move around a little if you like.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes please!")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.HALF_ASKING, "Yes?")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Where are we going again?")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I have a lot of things to do today, so we might go a lot of places.")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Are we there yet?")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "No, not yet.")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "How about now?")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "No.")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Are we still not there?")
                stage++
            }

            14 -> {
                playerl(FaceAnim.ANNOYED, "NO!")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Okay, just checking.")
                stage = END_DIALOGUE
            }

            16 -> {
                playerl(FaceAnim.HALF_ASKING, "Having a good time up there?")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yeah! It's great to feel the wind in your tentacles.")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "Sadly, I don't know what that feels like.")
                stage++
            }

            19 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why not?")
                stage++
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "No tentacles for a start.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well, nobody's perfect.")
                stage = END_DIALOGUE
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "I don't think it's that cold.")
                stage++
            }

            23 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It is compared to back home.")
                stage++
            }

            24 -> {
                playerl(FaceAnim.FRIENDLY, "How hot is it where you are from?")
                stage++
            }

            25 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I can never remember. What is the vaporisation point of steel again?")
                stage++
            }

            26 -> {
                playerl(FaceAnim.FRIENDLY, "Pretty high.")
                stage++
            }

            27 -> {
                playerl(FaceAnim.FRIENDLY, "No wonder you feel cold here...")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SMOKE_DEVIL_6865, NPCs.SMOKE_DEVIL_6866)
    }
}
