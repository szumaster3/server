package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Unicorn stallion dialogue.
 */
@Initializable
class UnicornStallionDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = UnicornStallionDialogue(player)

    /**
     * Instantiates a new Unicorn stallion dialogue.
     */
    constructor()

    /**
     * Instantiates a new Unicorn stallion dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val randomChoice = (Math.random() * 5).toInt()
        when (randomChoice) {
            0 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Neigh neigh neighneigh snort?",
                    "(Isn't everything so awesomely wonderful?)",
                )
                stage = 0
            }

            1 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Whicker whicker. Neigh, neigh, whinny.",
                    "(I feel so, like, enlightened. Let's meditate and enhance our auras.)",
                )
                stage = 5
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whinny whinny whinny.", "(I think I'm astrally projecting.)")
                stage = 7
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whinny, neigh!", "(Oh, happy day!)")
                stage = 9
            }

            4 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Whicker snort! Whinny whinny whinny.",
                    "(You're hurt! Let me try to heal you.)",
                )
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
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Neigh neigh neighneigh snort?",
                    "(Isn't everything so awesomely wonderful?)",
                )
                stage++
            }

            1 -> {
                playerl(FaceAnim.ASKING, "Err...yes?")
                stage++
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whicker whicker snuffle.", "(I can see you're not tuning in, Player name.)")
                stage++
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "No, no, I'm completely at one with...you know...everything.")
                stage++
            }

            4 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whicker!", "(Cosmic.)")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "I can't do that! I barely even know you.")
                stage++
            }

            6 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whicker...", "(Bipeds...)")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Okay... Hang on. Seeing as I summoned you here, wouldn't that mean you are physically projecting instead?",
                )
                stage++
            }

            8 -> {
                npc(FaceAnim.CHILD_NORMAL, "Whicker whicker whicker.", "(You're, like, no fun at all, man.)")
                stage = END_DIALOGUE
            }

            9 -> {
                playerl(FaceAnim.HALF_ASKING, "Happy day? Is that some sort of holiday or something?")
                stage++
            }

            10 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Snuggle whicker",
                    "(Man, you're totally, like, uncosmic, " + player.username + ".)",
                )
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Yes, please do!")
                stage++
            }

            12 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Snuffle whicker whicker neigh neigh...",
                    "(Okay, we'll begin with acupuncture and some reiki, then I'll get my crystals...)",
                )
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "Or you could use some sort of magic...like the other unicorns...")
                stage++
            }

            14 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Whicker whinny whinny neigh.",
                    "(Yes, but I believe in alternative medicine.)",
                )
                stage++
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "Riiight. Don't worry about it, then; I'll be fine.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.UNICORN_STALLION_6822, NPCs.UNICORN_STALLION_6823)
}
