package content.global.skill.summoning.familiar.dialogue.titan

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Fire Giant familiar dialogue.
 */
@Initializable
class FireTitanDialogue : Dialogue {
    private var branch: Int = -1

    override fun newInstance(player: Player?) = FireTitanDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = if (inInventory(player, Items.TINDERBOX_590, 1)) {
            0
        } else {
            (1..5).random()
        }

        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Relight my fire.")
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Pick flax.")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "You're fanning my flame with your wind spells.")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'm burning up.")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "It's raining flame!")
            5 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Let's go fireside.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "A tinderbox is my only desire."); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "What are you singing?"); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Just a song I heard a while ago."); stage++ }
                3 -> { playerl(FaceAnim.HALF_ASKING, "It's not very good."); stage++ }
                4 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You're just jealous of my singing voice."); stage++ }
                5 -> { playerl(FaceAnim.HALF_ASKING, "Where did you hear this again?"); stage++ }
                6 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, you know, just with some other fire titans. Out for a night on the pyres."); stage++ }
                7 -> { playerl(FaceAnim.FRIENDLY, "Hmm. Come on then. We have stuff to do."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Jump to it."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "If you want to get to fletching level 99."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "That song...is terrible."); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Sorry."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I'm singeing the curtains with my heat."); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "Oooh, very mellow."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I want the world to know."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I got to let it show."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Catchy."); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Huzzah!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "You have a...powerful voice."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Thanks."); stage = END_DIALOGUE }
            }

            5 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I think I've roasted the sofa."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I think I've burnt down the hall."); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "Can't you sing quietly?"); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Sorry."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FIRE_TITAN_7355, NPCs.FIRE_TITAN_7356)
}