package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Albino rat dialogue.
 */
@Initializable
class AlbinoRatDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = AlbinoRatDialogue(player)

    /**
     * Instantiates a new Albino rat dialogue.
     */
    constructor()

    /**
     * Instantiates a new Albino rat dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val random = Random()
        val randomIndex = random.nextInt(4)

        when (randomIndex) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey boss, we going to do anything wicked today?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey boss, can we go and loot something now?")
                stage = 4
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So what we up to today, boss?")
                stage = 9
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "You know, boss, I don't think you're totally into this whole 'evil' thing.",
                )
                stage = 13
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
                playerl(FaceAnim.FRIENDLY, "Well, I don't know why we would: I tend not to go around being wicked.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Not even a little?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Well there was that one time... I'm sorry, no wickedness today.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Awwwwww...")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "Well, what did you have in mind?")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I dunno - where are we headed?")
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "I hadn't decided yet.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "When we get there, let's loot something nearby!")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Sounds like a plan, certainly.")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "Oh I'm sure we'll find something to occupy our time.")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Let's go robbin' graves again!")
                stage++
            }

            11 -> {
                playerl(FaceAnim.ASKING, "What do you mean 'again'?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Nuffin'...")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.HALF_ASKING, "I wonder what gave you that impression?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well, I worked with a lot of evil people; some of the best.")
                stage++
            }

            15 -> {
                playerl(FaceAnim.HALF_ASKING, "Such as?")
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm not telling! I've got my principles to uphold.")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "There is honour amongst thieves, it would seem.")
                stage = END_DIALOGUE
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ALBINO_RAT_6847, NPCs.ALBINO_RAT_6848)
}
