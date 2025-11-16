package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import kotlin.random.Random

/**
 * Represents the Arctic Bear familiar dialogue.
 */
@Initializable
class ArcticBearDialogue : Dialogue {

    private var branch: Int = 0

    override fun newInstance(player: Player?) = ArcticBearDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = Random.nextInt(5)
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
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
                }
            }

            1 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.HALF_ASKING, "What? What's happening?")
                        stage++
                    }
                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Maybe " + if (player.isMale) "he" else "she" + " scented a rival.")
                        stage++
                    }
                    2 -> {
                        playerl(FaceAnim.FRIENDLY, "I smell something, but it's not a rival.")
                        stage = END_DIALOGUE
                    }
                }
            }

            2 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.FRIENDLY, "My name is Player, not Brighteyes!")
                        stage++
                    }
                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Looks like the little critter's upset about something.")
                        stage++
                    }
                    2 -> {
                        playerl(FaceAnim.FRIENDLY, "I wonder if he'd be quiet if I just did really boring stuff.")
                        stage = END_DIALOGUE
                    }
                }
            }

            3 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.HALF_ASKING, "Who wouldn't be upset with a huge bear tracking along behind them, commenting on everything they do?")
                        stage = END_DIALOGUE
                    }
                }
            }

            4 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.FRIENDLY, "I'm looking right at you. I can still see you, you know.")
                        stage++
                    }
                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "I don't think they can see me...")
                        stage++
                    }
                    2 -> {
                        playerl(FaceAnim.FRIENDLY, "*Siiiigh*")
                        stage++
                    }
                    3 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "So, I'm gonna get a little closer and see if I can rile 'em up.")
                        stage++
                    }
                    4 -> {
                        sendDialogue("The bear nudges you in the stomach.")
                        stage++
                    }
                    5 -> {
                        playerl(FaceAnim.FRIENDLY, "Hey!")
                        stage++
                    }
                    6 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Willya lookit that! Lookit them teeth; I'd be a goner if it got hold of me!")
                        stage++
                    }
                    7 -> {
                        playerl(FaceAnim.FRIENDLY, "You have no idea how true that is.")
                        stage = END_DIALOGUE
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ARCTIC_BEAR_6839, NPCs.ARCTIC_BEAR_6840)
}