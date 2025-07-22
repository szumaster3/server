package content.global.skill.summoning.familiar.dialogue.pc

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Void torcher dialogue.
 */
@Initializable
class VoidTorcherDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = VoidTorcherDialogue(player)

    /**
     * Instantiates a new Void torcher dialogue.
     */
    constructor()

    /**
     * Instantiates a new Void torcher dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val randomIndex = (Math.random() * 4).toInt()
        when (randomIndex) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "You okay there, spinner?")
                stage = 0
            }

            1 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "'T' is for torcher, that's good enough for me... 'T' is for torcher, I'm happy you can see.",
                )
                stage = 7
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Burn, baby, burn! Torcher inferno!")
                stage = 8
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So hungry... must devour...")
                stage = 9
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
                npcl(FaceAnim.CHILD_NORMAL, "I not spinner!")
                stage++
            }

            1 -> {
                playerl(FaceAnim.HALF_ASKING, "Sorry, splatter?")
                stage++
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I not splatter either!")
                stage++
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "No, wait, I meant defiler.")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I torcher!")
                stage++
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Hehe, I know. I was just messing with you.")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Grr. Don't be such a pest.")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(FaceAnim.HALF_ASKING, "You're just a bit weird, aren't you?")
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "*Wibble*")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "*Gulp* Er, yeah, I'll find you something to eat in a minute.")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Is flesh-bag scared of torcher?")
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "No, no. I, er, always look like this... honest.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VOID_TORCHER_7351, NPCs.VOID_TORCHER_7352)
}
