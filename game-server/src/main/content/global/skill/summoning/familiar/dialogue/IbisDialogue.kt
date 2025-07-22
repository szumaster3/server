package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Ibis dialogue.
 */
@Initializable
class IbisDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = IbisDialogue(player)

    /**
     * Instantiates a new Ibis dialogue.
     */
    constructor()

    /**
     * Instantiates a new Ibis dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 5).toInt()) {
            0 -> {
                npcl(FaceAnim.OLD_DEFAULT, "I'm the best fisherman ever!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.OLD_DEFAULT, "I like to fish!")
                stage = 3
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I want to go fiiiish.")
                stage = 4
            }

            3 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Hey, where are we?")
                stage = 5
            }

            4 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Can I look after those sharks for you?")
                stage = 8
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
                playerl(FaceAnim.HALF_ASKING, "Where is your skillcape to prove it, then?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.OLD_DEFAULT, "At home...")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I'll bet it is.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.HAPPY, "I know!")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "We can't be fishing all the time you know.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "What do you mean?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.OLD_DEFAULT, "I just noticed we weren't fishing.")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Well, we can't fish all the time.")
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "I don't know. Would you eat them?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.OLD_DEFAULT, "Yes! Ooops...")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "I think I'll hang onto them myself for now.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.IBIS_6991)
}
