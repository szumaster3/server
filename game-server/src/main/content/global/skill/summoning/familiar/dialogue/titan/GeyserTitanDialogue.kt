package content.global.skill.summoning.familiar.dialogue.titan

import core.api.amountInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Geyser titan dialogue.
 */
@Initializable
class GeyserTitanDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = GeyserTitanDialogue(player)

    /**
     * Instantiates a new Geyser titan dialogue.
     */
    constructor()

    /**
     * Instantiates a new Geyser titan dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (amountInInventory(player, Items.SHARK_385) < 5) {
            npcl(FaceAnim.CHILD_NORMAL, "Hey mate, how are you?")
            stage = 0
            return true
        }
        when ((Math.random() * 7).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Over the course of their lifetime a shark may grow and use 20,000 teeth.")
                stage = 3
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Did you know a snail can sleep up to three years?")
                stage = 4
            }

            2 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Unlike most animals, both the shark's upper and lower jaws move when they bite.",
                )
                stage = 5
            }

            3 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Did you know that in one feeding a mosquito can absorb one-and-a-half times its own body weight in blood?",
                )
                stage = 7
            }

            4 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Did you know that sharks have the most powerful jaws of any animal on the planet?",
                )
                stage = 8
            }

            5 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Did you know that " + settings!!.name +
                        " gets 100 tons heavier every day, due to dust falling from space?",
                )
                stage = 10
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Did you know that sharks normally eat alone?")
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
                playerl(FaceAnim.FRIENDLY, "Not so bad.")
                stage++
            }

            1 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Did you know that during the average human life-span the heart will beat approximately 2.5 billion times?",
                )
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Wow, that is a lot of non-stop work!")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Wow! That is a whole load of teeth. I wonder what the Tooth Fairy would give for those?",
                )
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "I wish I could do that. Ah...sleep.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "Really?")
                stage++
            }

            6 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Yup. Chomp chomp.")
                stage = END_DIALOGUE
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Eugh.")
                stage = END_DIALOGUE
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "No, I didn't.")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Full of facts, me.")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "What a fascinating fact.")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "I see.")
                stage++
            }

            12 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Sometimes one feeding shark attracts others and they all try and get a piece of the prey.",
                )
                stage++
            }

            13 -> {
                npcl(FaceAnim.CHILD_NORMAL, "They take a bite at anything in their way and may even bite each other!")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "Ouch!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GEYSER_TITAN_7339, NPCs.GEYSER_TITAN_7340)
}
