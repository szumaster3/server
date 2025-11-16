package content.global.skill.summoning.familiar.dialogue

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
class FireGiantDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = FireGiantDialogue(player)

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
            0 -> npcl(FaceAnim.CHILD_NORMAL, "Relight my fire.")
            1 -> npcl(FaceAnim.CHILD_NORMAL, "Pick flax.")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "You're fanning my flame with your wind spells.")
            3 -> npcl(FaceAnim.CHILD_NORMAL, "I'm burning up.")
            4 -> npcl(FaceAnim.CHILD_NORMAL, "It's raining flame!")
            5 -> npcl(FaceAnim.CHILD_NORMAL, "Let's go fireside.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "A tinderbox is my only desire."); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "What are you singing?"); stage++ }
                2 -> { npcl(FaceAnim.CHILD_NORMAL, "Just a song I heard a while ago."); stage++ }
                3 -> { playerl(FaceAnim.HALF_ASKING, "It's not very good."); stage++ }
                4 -> { npcl(FaceAnim.CHILD_NORMAL, "You're just jealous of my singing voice."); stage++ }
                5 -> { playerl(FaceAnim.HALF_ASKING, "Where did you hear this again?"); stage++ }
                6 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh, you know, just with some other fire titans. Out for a night on the pyres."); stage++ }
                7 -> { playerl(FaceAnim.FRIENDLY, "Hmm. Come on then. We have stuff to do."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "Jump to it."); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "If you want to get to fletching level 99."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "That song...is terrible."); stage++ }
                3 -> { npcl(FaceAnim.CHILD_NORMAL, "Sorry."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "I'm singeing the curtains with my heat."); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "Oooh, very mellow."); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "I want the world to know."); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "I got to let it show."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Catchy."); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "Huzzah!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "You have a...powerful voice."); stage++ }
                2 -> { npcl(FaceAnim.CHILD_NORMAL, "Thanks."); stage = END_DIALOGUE }
            }

            5 -> when (stage) {
                0 -> { npcl(FaceAnim.CHILD_NORMAL, "I think I've roasted the sofa."); stage++ }
                1 -> { npcl(FaceAnim.CHILD_NORMAL, "I think I've burnt down the hall."); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "Can't you sing quietly?"); stage++ }
                3 -> { npcl(FaceAnim.CHILD_NORMAL, "Sorry."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FIRE_GIANT_7003, NPCs.FIRE_GIANT_7004)
}