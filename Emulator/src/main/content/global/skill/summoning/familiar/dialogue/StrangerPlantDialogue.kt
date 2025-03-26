package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Stranger plant dialogue.
 */
@Initializable
class StrangerPlantDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return StrangerPlantDialogue(player)
    }

    /**
     * Instantiates a new Stranger plant dialogue.
     */
    constructor()

    /**
     * Instantiates a new Stranger plant dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val randomChoice = (Math.random() * 4).toInt()
        when (randomChoice) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'M STRANGER PLANT!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "WILL WE HAVE TO BE HERE LONG?")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "DIIIIVE!")
                stage = 16
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I THINK I'M WILTING!")
                stage = 21
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "I know you are.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I KNOW! I'M JUST SAYING!")
                stage++
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "Do you have to shout like that all of the time?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "WHO'S SHOUTING?")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "If this is you speaking normally, I'd hate to hear you shouting.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "OH, SNAP!")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "We'll be here until I am finished.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "BUT THERE'S NO DRAMA HERE!")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Well, how about you pretend to be an undercover agent.")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "WONDERFUL! WHAT'S MY MOTIVATION?")
                stage++
            }

            10 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "You're trying to remain stealthy and secretive, while looking out for clues."
                )
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'LL JUST GET INTO CHARACTER! AHEM!")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "PAPER! PAPER! VARROCK HERALD FOR SALE!")
                stage++
            }

            13 -> {
                playerl(FaceAnim.HALF_ASKING, "What kind of spy yells loudly like that?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "ONE WHOSE COVER IDENTITY IS A PAPER-SELLER, OF COURSE!")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Ask a silly question...")
                stage = END_DIALOGUE
            }

            16 -> {
                playerl(FaceAnim.HALF_ASKING, "What? Help! Why dive?")
                stage++
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "OH, DON'T WORRY! I JUST LIKE TO YELL THAT FROM TIME TO TIME!")
                stage++
            }

            18 -> {
                playerl(FaceAnim.HALF_ASKING, "Well, can you give me a little warning next time?")
                stage++
            }

            19 -> {
                npcl(FaceAnim.CHILD_NORMAL, "WHAT, AND TAKE ALL THE FUN OUT OF LIFE?")
                stage++
            }

            20 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "If by 'fun' you mean 'sudden heart attacks', then yes, please take them out of my life!"
                )
                stage = END_DIALOGUE
            }

            21 -> {
                playerl(FaceAnim.HALF_ASKING, "Do you need some water?")
                stage++
            }

            22 -> {
                npcl(FaceAnim.CHILD_NORMAL, "DON'T BE SILLY! I CAN PULL THAT OUT OF THE GROUND!")
                stage++
            }

            23 -> {
                playerl(FaceAnim.HALF_ASKING, "Then why are you wilting?")
                stage++
            }

            24 -> {
                npcl(FaceAnim.CHILD_NORMAL, "IT'S SIMPLE: THERE'S A DISTINCT LACK OF DRAMA!")
                stage++
            }

            25 -> {
                playerl(FaceAnim.HALF_ASKING, "Drama?")
                stage++
            }

            26 -> {
                npcl(FaceAnim.CHILD_NORMAL, "YES, DRAMA!")
                stage++
            }

            27 -> {
                playerl(FaceAnim.FRIENDLY, "Okay...")
                stage++
            }

            28 -> {
                playerl(FaceAnim.FRIENDLY, "Let's see if we can find some for you.")
                stage++
            }

            29 -> {
                npcl(FaceAnim.CHILD_NORMAL, "LEAD ON!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STRANGER_PLANT_6827, NPCs.STRANGER_PLANT_6828)
    }
}
