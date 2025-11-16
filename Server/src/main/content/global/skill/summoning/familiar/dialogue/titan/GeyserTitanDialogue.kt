package content.global.skill.summoning.familiar.dialogue.titan

import core.api.amountInInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import core.game.world.GameWorld
import kotlin.random.Random

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

        if (amountInInventory(player, Items.SHARK_385) >= 5) {
            npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hey mate, how are you?")
            stage = END_DIALOGUE
            return true
        }

        if (branch == -1) branch = Random.nextInt(7)
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Over the course of their lifetime a shark may grow and use 20,000 teeth.")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Did you know a snail can sleep up to three years?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Unlike most animals, both the shark's upper and lower jaws move when they bite.")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Did you know that in one feeding a mosquito can absorb one-and-a-half times its own body weight in blood?")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Did you know that sharks have the most powerful jaws of any animal on the planet?")
            5 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Did you know that ${GameWorld.settings?.name} gets 100 tons heavier every day, due to dust falling from space?")
            6 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Did you know that sharks normally eat alone?")
        }

        stage++
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                1 -> { playerl(FaceAnim.HALF_ASKING, "Wow! That is a whole load of teeth. I wonder what the Tooth Fairy would give for those?"); stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                1 -> { playerl(FaceAnim.FRIENDLY, "I wish I could do that. Ah...sleep."); stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                1 -> { playerl(FaceAnim.HALF_ASKING, "Really?"); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yup. Chomp chomp."); stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                1 -> { playerl(FaceAnim.FRIENDLY, "Eugh."); stage = END_DIALOGUE }
            }
            4 -> when (stage) {
                1 -> { playerl(FaceAnim.FRIENDLY, "No, I didn't."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Full of facts, me."); stage = END_DIALOGUE }
            }
            5 -> when (stage) {
                1 -> { playerl(FaceAnim.FRIENDLY, "What a fascinating fact."); stage = END_DIALOGUE }
            }
            6 -> when (stage) {
                1 -> { playerl(FaceAnim.FRIENDLY, "I see."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Sometimes one feeding shark attracts others and they all try and get a piece of the prey."); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "They take a bite at anything in their way and may even bite each other!"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Ouch!"); stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GEYSER_TITAN_7339, NPCs.GEYSER_TITAN_7340)
}