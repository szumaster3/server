package content.global.skill.summoning.familiar.dialogue.spirit

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Spirit dagannoth dialogue.
 */
@Initializable
class SpiritDagannothDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = SpiritDagannothDialogue(player)

    /**
     * Instantiates a new Spirit dagannoth dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit dagannoth dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val randomIndex = (Math.random() * 4).toInt()
        when (randomIndex) {
            0 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Grooooooowl graaaaawl raaaawl?",
                    "(Are you ready to surrender to the power of",
                    "the Deep Waters?)",
                )
                stage = 0
            }

            1 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Groooooowl. Hsssssssssssssss!",
                    "(The Deeps will swallow the lands. None will",
                    "stand before us!)",
                )
                stage = 5
            }

            2 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Hssssss graaaawl grooooowl, growwwwwwwwwl!",
                    "(Oh how the bleak gulfs hunger for the",
                    "Day of Rising.)",
                )
                stage = 8
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Raaaawl!", "(Submit!)")
                stage = 11
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
                playerl(FaceAnim.FRIENDLY, "Err, not really.")
                stage++
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Rooooowl?", "(How about now?)")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "No, sorry.")
                stage++
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Rooooowl?", "(How about now?)")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "No, sorry. You might want to try again a little later.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "What if we build boats?")
                stage++
            }

            6 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Hsssssssss groooooowl?",
                    "Hssssshsss grrooooooowl?",
                    "(What are boats? The tasty wooden containers full of meat?)",
                )
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "I suppose they could be described as such, yes.")
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "My brain hurts when I listen to you talk...")
                stage++
            }

            9 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Raaaaawl groooowl grrrrawl!",
                    "(That's the truth biting into your clouded mind!)",
                )
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Could you try using a little less truth please?")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Submit to what?")
                stage++
            }

            12 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Hssssssssss rawwwwwl graaaawl!",
                    "(To the inevitable defeat of all life on the Surface!)",
                )
                stage++
            }

            13 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "I think I'll wait a little longer before I just keep over and submit, thanks.",
                )
                stage++
            }

            14 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Hsssss, grooooowl, raaaaawl.",
                    "(Well, it's your choice, but those that submit first will be eaten first.)",
                )
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "I'll pass on that one, thanks.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_DAGANNOTH_6804, NPCs.SPIRIT_DAGANNOTH_6805)
}
