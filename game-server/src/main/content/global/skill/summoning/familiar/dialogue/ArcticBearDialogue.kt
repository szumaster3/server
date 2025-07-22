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
 * The type Arctic bear dialogue.
 */
@Initializable
class ArcticBearDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = ArcticBearDialogue(player)

    /**
     * Instantiates a new Arctic bear dialogue.
     */
    constructor()

    /**
     * Instantiates a new Arctic bear dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val rand = Random()
        when (rand.nextInt(5)) {
            0 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Crikey! We're tracking ourselves a real live one here. I call 'em 'Brighteyes'.",
                )
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Crikey! Something seems to have startled Brighteyes, here.")
                stage = 5
            }

            2 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "We're tracking Brighteyes here as goes about " +
                        (if (player.isMale) "his" else "her") + " daily routine.",
                )
                stage = 8
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "These little guys get riled up real easy.")
                stage = 11
            }

            4 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "I'm going to use this snow to blend in and get closer to this little feller.",
                )
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
                playerl(FaceAnim.HALF_ASKING, "Will you stop stalking me like that?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Lookit that! Something's riled this one up good and proper.")
                stage++
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "Who are you talking to anyway?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Looks like I've been spotted.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.HALF_ASKING, "Did you think you didn't stand out here or something?")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "What? What's happening?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Maybe " + (if (player.isMale) "he" else "she") + " scented a rival.")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "I smell something, but it's not a rival.")
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "My name is Player, not Brighteyes!")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Looks like the little critter's upset about something.")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "I wonder if he'd be quiet if I just did really boring stuff.")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Who wouldn't be upset with a huge bear tracking along behind them, commenting on everything they do?",
                )
                stage = END_DIALOGUE
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "I'm looking right at you. I can still see you, you know.")
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I don't think they can see me...")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "*Siiiigh*")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "So, I'm gonna get a little closer and see if I can rile 'em up.")
                stage++
            }

            16 -> {
                sendDialogue("The bear nudges you in the stomach.")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Hey!")
                stage++
            }

            18 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Willya lookit that! Lookit them teeth; I'd be a goner if it got hold of me!",
                )
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "You have no idea how true that is.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ARCTIC_BEAR_6839, NPCs.ARCTIC_BEAR_6840)
}
