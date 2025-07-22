package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Hydra dialogue.
 */
@Initializable
class HydraDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = HydraDialogue(player)

    /**
     * Instantiates a new Hydra dialogue.
     */
    constructor()

    /**
     * Instantiates a new Hydra dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Raaaspraaasp? (Isn't it hard to get things done with just one head?)")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Raaaasp raaaasp! (Man, I feel good!)")
                stage = 4
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Rassssp rasssssp! (You know, two heads are better than one!)")
                stage = 10
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Raaaaaaasp. (Siiiigh.)")
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
                playerl(FaceAnim.FRIENDLY, "Not really!")
                stage++
            }

            1 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Raaasp raaaaap raaaasp?",
                    "(Well I suppose you work with what you got, right?)",
                )
                stage++
            }

            2 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Raaaaaasp raaaasp raaaasp.",
                    "(At least he doesn't have someone whittering in their ear all the time.)",
                )
                stage++
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Raaaaaaasp!", "(Quiet, you!)")
                stage = END_DIALOGUE
            }

            4 -> {
                npc(FaceAnim.CHILD_NORMAL, "Raaasp ssssss raaaasp.", "(That's easy for you to say.)")
                stage++
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "What's up?")
                stage++
            }

            6 -> {
                npc(FaceAnim.CHILD_NORMAL, "Raaa....", "(well...)")
                stage++
            }

            7 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Raaaaasp sss rassssp.",
                    "(Don't pay any attention, they are just feeling whiny.)",
                )
                stage++
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "But they're you, aren't they?")
                stage++
            }

            9 -> {
                npc(FaceAnim.CHILD_NORMAL, "Raaaasp raasp rasssp!", "(Don't remind me!)")
                stage = END_DIALOGUE
            }

            10 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Raaaasp rassssp sssssp....",
                    "(Unless you're the one doing all the heavy thinking....)",
                )
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "I think I'll stick to one for now, thanks.")
                stage = END_DIALOGUE
            }

            12 -> {
                npc(FaceAnim.CHILD_NORMAL, "Raasp raasp raaaaasp?", "(What's up this time?)")
                stage++
            }

            13 -> {
                playerl(FaceAnim.HALF_ASKING, "Can I help?")
                stage++
            }

            14 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Rasssp ssssssp? raaaaasp raaaasp.",
                    "(Do you mind? This is a private conversation.)",
                )
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Well, excu-u-use me.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HYDRA_6811, NPCs.HYDRA_6812)
}
