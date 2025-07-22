package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit scorpion dialogue.
 */
@Initializable
class SpiritScorpionDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = SpiritScorpionDialogue(player)

    /**
     * Instantiates a new Spirit scorpion dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit scorpion dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey, boss, how about we go to the bank?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Say hello to my little friend!")
                stage = 9
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Hey, boss, I've been thinking.")
                stage = 13
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Why do we never go to crossroads and rob travelers?")
                stage = 20
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
                playerl(FaceAnim.FRIENDLY, "And do what?")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Well, we could open by shouting, 'Stand and deliver!'")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Why does everything with you end with something getting held up?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That isn't true! Give me one example.")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "How about the post office?")
                stage++
            }

            5 -> {
                npcl(FaceAnim.CHILD_NORMAL, "How about another?")
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Those junior White Knights? The ones selling the gnome crunchies?")
                stage++
            }

            7 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That was self defence.")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "No! No more hold-ups, stick-ups, thefts, or heists, you got that?")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.ASKING, "What?")
                stage++
            }

            10 -> {
                npcl(FaceAnim.CHILD_NORMAL, "My little friend: you ignored him last time you met him.")
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "So, who is your friend?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "If I tell you, what is the point?")
                stage = END_DIALOGUE
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "That's never a good sign.")
                stage++
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "See, I heard about this railway...")
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "We are not robbing it!")
                stage++
            }

            16 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I might not have wanted to suggest that, boss...")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Then what were you going to suggest?")
                stage++
            }

            18 -> {
                npcl(FaceAnim.CHILD_NORMAL, "That isn't important right now.")
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "I thought as much.")
                stage = END_DIALOGUE
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "There are already highwaymen at the good spots.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Maybe we need to think bigger.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_SCORPION_6837, NPCs.SPIRIT_SCORPION_6838)
}
