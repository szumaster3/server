package content.global.skill.summoning.familiar.dialogue

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Fire giant dialogue.
 */
@Initializable
class FireGiantDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = FireGiantDialogue(player)

    /**
     * Instantiates a new Fire giant dialogue.
     */
    constructor()

    /**
     * Instantiates a new Fire giant dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.TINDERBOX_590, 1)) {
            npcl(FaceAnim.CHILD_NORMAL, "Relight my fire.")
            stage = 0
            return true
        }
        when ((Math.random() * 5).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Pick flax.")
                stage = 8
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You're fanning my flame with your wind spells.")
                stage = 12
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm burning up.")
                stage = 14
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "It's raining flame!")
                stage = 17
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Let's go fireside.")
                stage = 20
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
                npcl(FaceAnim.CHILD_NORMAL, "A tinderbox is my only desire.")
                stage++
            }

            1 -> {
                playerl(FaceAnim.HALF_ASKING, "What are you singing?")
                stage++
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Just a song I heard a while ago.")
                stage++
            }

            3 -> {
                playerl(FaceAnim.HALF_ASKING, "It's not very good.")
                stage++
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "You're just jealous of my singing voice.")
                stage++
            }

            5 -> {
                playerl(FaceAnim.HALF_ASKING, "Where did you hear this again?")
                stage++
            }

            6 -> {
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "Oh, you know, just with some other fire titans. Out for a night on the pyres.",
                )
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "Hmm. Come on then. We have stuff to do.")
                stage = END_DIALOGUE
            }

            8 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Jump to it.")
                stage++
            }

            9 -> {
                npcl(FaceAnim.CHILD_NORMAL, "If you want to get to fletching level 99.")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "That song...is terrible.")
                stage++
            }

            11 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sorry.")
                stage = END_DIALOGUE
            }

            12 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I'm singeing the curtains with my heat.")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "Oooh, very mellow.")
                stage = END_DIALOGUE
            }

            14 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I want the world to know.")
                stage++
            }

            15 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I got to let it show.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "Catchy.")
                stage = END_DIALOGUE
            }

            17 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Huzzah!")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "You have a...powerful voice.")
                stage++
            }

            19 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Thanks.")
                stage = END_DIALOGUE
            }

            20 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I think I've roasted the sofa.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.CHILD_NORMAL, "I think I've burnt down the hall.")
                stage++
            }

            22 -> {
                playerl(FaceAnim.HALF_ASKING, "Can't you sing quietly?")
                stage++
            }

            23 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Sorry.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FIRE_GIANT_7003, NPCs.FIRE_GIANT_7004)
}
