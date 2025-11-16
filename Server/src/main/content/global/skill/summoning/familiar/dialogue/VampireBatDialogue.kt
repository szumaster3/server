package content.global.skill.summoning.familiar.dialogue

import core.api.inBorders
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Vampire Bat familiar dialogue.
 */
@Initializable
class VampireBatDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = VampireBatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = if (inBorders(player, 3139, 9535, 3306, 9657)) 0 else (1..3).random()

        stage = 0

        when (branch) {
            0 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Ze creatures ov ze dark; vat vonderful music zey make.")
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "You're vasting all that blood, can I have some?")
            2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Ven are you going to feed me?")
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Ven can I eat somethink?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> {
                    playerl(FaceAnim.FRIENDLY, "Riiight.")
                    stage++
                }
                1 -> {
                    npc(FaceAnim.FAMILIAR_NEUTRAL, "I like it down here. Let's stay and eat moths!")
                    stage++
                }
                2 -> {
                    playerl(FaceAnim.FRIENDLY, "I think I'll pass, thanks.")
                    stage = END_DIALOGUE
                }
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "No!")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Well for a start, I'm not giving you any of my blood.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Just as soon as I find something to attack.")
                stage = END_DIALOGUE
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VAMPIRE_BAT_6835, NPCs.VAMPIRE_BAT_6836)
}