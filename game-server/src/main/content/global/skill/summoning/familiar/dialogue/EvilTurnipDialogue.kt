package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Evil turnip dialogue.
 */
@Initializable
class EvilTurnipDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = EvilTurnipDialogue(player)

    /**
     * Instantiates a new Evil turnip dialogue.
     */
    constructor()

    /**
     * Instantiates a new Evil turnip dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "So, how are you feeling?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.OLD_NORMAL, "Hur hur hur...")
                stage = 3
            }

            2 -> {
                npcl(FaceAnim.OLD_NORMAL, "When we gonna fighting things, boss?")
                stage = 4
            }

            3 -> {
                npcl(FaceAnim.OLD_NORMAL, "I are turnip hear me roar! I too deadly to ignore.")
                stage = 6
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
                npcl(FaceAnim.OLD_NORMAL, "My roots feel hurty. I thinking it be someone I eated.")
                stage++
            }

            1 -> {
                playerl(FaceAnim.ASKING, "You mean some THING you ate?")
                stage++
            }

            2 -> {
                npcl(FaceAnim.OLD_NORMAL, "Hur hur hur. Yah, sure, why not.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, as sinister as it's chuckling is, at least it's happy. That's a good thing, right?",
                )
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Soon enough.")
                stage++
            }

            5 -> {
                npcl(FaceAnim.OLD_NORMAL, "Hur hur hur. I gets the fighting.")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "I'm glad it's on my side... and not behind me.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EVIL_TURNIP_6833, NPCs.EVIL_TURNIP_6834)
}
