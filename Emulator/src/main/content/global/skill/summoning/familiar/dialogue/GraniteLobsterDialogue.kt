package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Granite lobster dialogue.
 */
@Initializable
class GraniteLobsterDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = GraniteLobsterDialogue(player)

    /**
     * Instantiates a new Granite lobster dialogue.
     */
    constructor()

    /**
     * Instantiates a new Granite lobster dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 5).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We shall heap the helmets of the fallen into a mountain!")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "We march to war, Fremennik Player Name. Glory and plunder for all!")
                stage = 2
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Fremennik Player Name, what is best in life?")
                stage = 3
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Ho, my Fremennik brother, shall we go raiding?")
                stage = 5
            }

            4 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Clonkclonk clonk grind clonk. (Keep walking, Outlander. We have nothing to discuss.)",
                )
                stage = 7
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
                playerl(FaceAnim.FRIENDLY, "The outlanders have insulted our heritage for the last time!")
                stage++
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "The longhall will resound with our celebration!")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Yes! We shall pile gold before the longhall of our tribesmen!")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Crush your enemies, see them driven before you, and hear the lamentation of their women!",
                )
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I would have settled for raw sharks, but that's good too!")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Well, I suppose we could when I'm done with this.")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yes! To the looting and the plunder!")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Fair enough.")
                stage++
            }

            8 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Clonkclonkclonk grind clonk grind? (It's nothing personal, you're just an Outlander, you know?)",
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GRANITE_LOBSTER_6849, NPCs.GRANITE_LOBSTER_6850)
}
