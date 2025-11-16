package content.global.skill.summoning.familiar.dialogue.titan

import core.api.amountInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Geyser Titan familiar dialogue.
 */
@Initializable
class GeyserTitanDialogue : Dialogue {

    private var branch: Int = -1

    override fun newInstance(player: Player?): Dialogue = GeyserTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as? NPC ?: return false
        if (amountInInventory(player, Items.SHARK_385) < 5) {
            npcl(FaceAnim.CHILD_NORMAL, "Hey mate, how are you?")
            stage = 0
            return true
        }

        if (branch == -1) {
            branch = (Math.random() * 7).toInt()
        }

        stage = when (branch) {
            0 -> 3
            1 -> 4
            2 -> 5
            3 -> 7
            4 -> 8
            5 -> 10
            6 -> 11
            else -> 0
        }

        when (branch) {
            0 -> npcl(FaceAnim.CHILD_NORMAL, "Over the course of their lifetime a shark may grow and use 20,000 teeth.")
            1 -> npcl(FaceAnim.CHILD_NORMAL, "Did you know a snail can sleep up to three years?")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Unlike most animals, both the shark's upper and lower jaws move when they bite.")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "Did you know that in one feeding a mosquito can absorb one-and-a-half times its own body weight in blood?")
            4 -> npcl(FaceAnim.CHILD_NORMAL, "Did you know that sharks have the most powerful jaws of any animal on the planet?")
            5 -> npcl(FaceAnim.CHILD_NORMAL, "Did you know that ${GameWorld.settings?.name} gets 100 tons heavier every day, due to dust falling from space?")
            6 -> npcl(FaceAnim.CHILD_NORMAL, "Did you know that sharks normally eat alone?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                3 -> { playerl(FaceAnim.HALF_ASKING, "Wow! That is a whole load of teeth. I wonder what the Tooth Fairy would give for those?"); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                4 -> { playerl(FaceAnim.FRIENDLY, "I wish I could do that. Ah...sleep."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                5 -> { playerl(FaceAnim.HALF_ASKING, "Really?"); stage++ }
                6 -> { npcl(FaceAnim.CHILD_NORMAL, "Yup. Chomp chomp."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                7 -> { playerl(FaceAnim.FRIENDLY, "Eugh."); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                8 -> { playerl(FaceAnim.FRIENDLY, "No, I didn't."); stage++ }
                9 -> { npcl(FaceAnim.CHILD_NORMAL, "Full of facts, me."); stage = END_DIALOGUE }
            }

            5 -> when (stage) {
                10 -> { playerl(FaceAnim.FRIENDLY, "What a fascinating fact."); stage = END_DIALOGUE }
            }

            6 -> when (stage) {
                11 -> { playerl(FaceAnim.FRIENDLY, "I see."); stage++ }
                12 -> { npcl(FaceAnim.CHILD_NORMAL, "Sometimes one feeding shark attracts others and they all try and get a piece of the prey."); stage++ }
                13 -> { npcl(FaceAnim.CHILD_NORMAL, "They take a bite at anything in their way and may even bite each other!"); stage++ }
                14 -> { playerl(FaceAnim.FRIENDLY, "Ouch!"); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GEYSER_TITAN_7339, NPCs.GEYSER_TITAN_7340)
}