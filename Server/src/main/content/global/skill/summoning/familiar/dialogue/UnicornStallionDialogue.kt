package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Unicorn stallion familiar dialogue.
 */
@Initializable
class UnicornStallionDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = UnicornStallionDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..4).random()
        stage = 0

        when (branch) {
            0 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Neigh neigh neighneigh snort?", "(Isn't everything so awesomely wonderful?)")
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Whicker whicker. Neigh, neigh, whinny.", "(I feel so, like, enlightened. Let's meditate and enhance our auras.)")
            2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Whinny whinny whinny.", "(I think I'm astrally projecting.)")
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Whinny, neigh!", "(Oh, happy day!)")
            4 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Whicker snort! Whinny whinny whinny.", "(You're hurt! Let me try to heal you.)")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { stage++; playerl(FaceAnim.ASKING, "Err...yes?") }
                1 -> { stage++; npc(FaceAnim.FAMILIAR_NEUTRAL, "Whicker whicker snuffle.", "(I can see you're not tuning in, ${player?.username}.)") }
                2 -> { stage++; playerl(FaceAnim.FRIENDLY, "No, no, I'm completely at one with...you know...everything.") }
                3 -> stage = END_DIALOGUE
            }

            1 -> when (stage) {
                0 -> { stage++; playerl(FaceAnim.FRIENDLY, "I can't do that! I barely even know you.") }
                1 -> stage = END_DIALOGUE.also { npc(FaceAnim.FAMILIAR_NEUTRAL, "Whicker...", "(Bipeds...)") }
            }

            2 -> when (stage) {
                0 -> { stage++; playerl(FaceAnim.HALF_ASKING, "Okay... Hang on. Seeing as I summoned you here, wouldn't that mean you are physically projecting instead?") }
                1 -> stage = END_DIALOGUE.also { npc(FaceAnim.FAMILIAR_NEUTRAL, "Whicker whicker whicker.", "(You're, like, no fun at all, man.)") }
            }

            3 -> when (stage) {
                0 -> { stage++; playerl(FaceAnim.HALF_ASKING, "Happy day? Is that some sort of holiday or something?") }
                1 -> stage = END_DIALOGUE.also { npc(FaceAnim.FAMILIAR_NEUTRAL, "Snuggle whicker", "(Man, you're totally, like, uncosmic, ${player?.username}.)") }
            }

            4 -> when (stage) {
                0 -> { stage++; playerl(FaceAnim.FRIENDLY, "Yes, please do!") }
                1 -> { stage++; npc(FaceAnim.FAMILIAR_NEUTRAL, "Snuffle whicker whicker neigh neigh...", "(Okay, we'll begin with acupuncture and some reiki, then I'll get my crystals...)") }
                2 -> { stage++; playerl(FaceAnim.FRIENDLY, "Or you could use some sort of magic...like the other unicorns...") }
                3 -> { stage++; npc(FaceAnim.FAMILIAR_NEUTRAL, "Whicker whinny whinny neigh.", "(Yes, but I believe in alternative medicine.)") }
                4 -> stage = END_DIALOGUE.also { playerl(FaceAnim.FRIENDLY, "Riiight. Don't worry about it, then; I'll be fine.") }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.UNICORN_STALLION_6822, NPCs.UNICORN_STALLION_6823)
}