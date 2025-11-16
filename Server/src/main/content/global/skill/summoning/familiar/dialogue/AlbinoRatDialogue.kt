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
 * Represents the Albino Rat familiar dialogue.
 */
@Initializable
class AlbinoRatDialogue : Dialogue {

    private var branch: Int = 0

    override fun newInstance(player: Player?) = AlbinoRatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = Random.nextInt(4)
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
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
                }
            }

            1 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.HALF_ASKING, "Well, what did you have in mind?")
                        stage++
                    }
                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "I dunno - where are we headed?")
                        stage++
                    }
                    2 -> {
                        playerl(FaceAnim.FRIENDLY, "I hadn't decided yet.")
                        stage++
                    }
                    3 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "When we get there, let's loot something nearby!")
                        stage++
                    }
                    4 -> {
                        playerl(FaceAnim.FRIENDLY, "Sounds like a plan, certainly.")
                        stage = END_DIALOGUE
                    }
                }
            }

            2 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.FRIENDLY, "Oh I'm sure we'll find something to occupy our time.")
                        stage++
                    }
                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Let's go robbin' graves again!")
                        stage++
                    }
                    2 -> {
                        playerl(FaceAnim.ASKING, "What do you mean 'again'?")
                        stage++
                    }
                    3 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Nuffin'...")
                        stage = END_DIALOGUE
                    }
                }
            }

            3 -> {
                when (stage) {
                    0 -> {
                        playerl(FaceAnim.HALF_ASKING, "I wonder what gave you that impression?")
                        stage++
                    }
                    1 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "Well, I worked with a lot of evil people; some of the best.")
                        stage++
                    }
                    2 -> {
                        playerl(FaceAnim.HALF_ASKING, "Such as?")
                        stage++
                    }
                    3 -> {
                        npcl(FaceAnim.CHILD_NORMAL, "I'm not telling! I've got my principles to uphold.")
                        stage++
                    }
                    4 -> {
                        playerl(FaceAnim.FRIENDLY, "There is honour amongst thieves, it would seem.")
                        stage = END_DIALOGUE
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ALBINO_RAT_6847, NPCs.ALBINO_RAT_6848)
}
