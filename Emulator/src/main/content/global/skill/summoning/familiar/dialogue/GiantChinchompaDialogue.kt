package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Giant chinchompa dialogue.
 */
@Initializable
class GiantChinchompaDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return GiantChinchompaDialogue(player)
    }

    /**
     * Instantiates a new Giant chinchompa dialogue.
     */
    constructor()

    /**
     * Instantiates a new Giant chinchompa dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 5).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Half a pound of tuppenny rice, half a pound of treacle...")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "What's small, brown and blows up?")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I spy, with my little eye, something beginning with 'B'.")
                stage = 10
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I seem to have found a paper bag.")
                stage = 15
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Woah, woah, woah - hold up there.")
                stage = 19
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "I hate it when you sing that song.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "...that's the way the money goes...")
                stage++
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "Couldn't you sing 'Kumbaya' or something?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "...BANG, goes the chinchompa!")
                stage++
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "Sheesh.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "A brown balloon?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "A chinchompa! Pull my finger.")
                stage++
            }

            7 -> {
                playerl(FaceAnim.HALF_ASKING, "I'm not pulling your finger.")
                stage++
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Nothing will happen. Truuuuust meeeeee.")
                stage++
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, go away.")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.HALF_ASKING, "Bomb? Bang? Boom? Blowing-up-little-chipmunk?")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "No. Body odour. You should wash a bit more.")
                stage++
            }

            12 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Well, that was pleasant. You don't smell all that great either, you know."
                )
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Stop talking, stop talking! Your breath stinks!")
                stage++
            }

            14 -> {
                playerl(FaceAnim.HALF_ASKING, "We're never going to get on, are we?")
                stage = END_DIALOGUE
            }

            15 -> {
                playerl(FaceAnim.HALF_ASKING, "Well done. Anything in it?")
                stage++
            }

            16 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Hmmm. Let me see. It seems to be full of some highly sought after, very expensive...chinchompa breath!"
                )
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "No, don't pop it!")
                stage++
            }

            18 -> {
                playerl(FaceAnim.HALF_ASKING, "You just cannot help yourself, can you?")
                stage = END_DIALOGUE
            }

            19 -> {
                playerl(FaceAnim.HALF_ASKING, "What is it, ratty?")
                stage++
            }

            20 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You got something in your backpack that you'd like to tell me about?")
                stage++
            }

            21 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "I was wondering when you were going to bring up the chinchompa. I'm sure they like it in my inventory."
                )
                stage++
            }

            22 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Did they not teach you anything in school? Chinchompas die in hot bags. You know what happens when chinchompas die. Are you attached to your back?"
                )
                stage++
            }

            23 -> {
                playerl(FaceAnim.HALF_ASKING, "Medically, yes. And I kind of like it too. I get the point.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GIANT_CHINCHOMPA_7353, NPCs.GIANT_CHINCHOMPA_7354)
    }
}
