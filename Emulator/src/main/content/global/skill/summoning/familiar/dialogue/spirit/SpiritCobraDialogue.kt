package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.animate
import core.api.inEquipment
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.*

/**
 * The type Spirit cobra dialogue.
 */
@Initializable
class SpiritCobraDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return SpiritCobraDialogue(player)
    }

    /**
     * Instantiates a new Spirit cobra dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit cobra dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.RING_OF_CHAROSA_6465, 1) || inEquipment(player, Items.RING_OF_CHAROSA_6465, 1)) {
            npcl(FaceAnim.OLD_NORMAL, "You are under my power!")
            stage = 20
            return true
        }

        val randomIndex = Random().nextInt(5)
        when (randomIndex) {
            0 -> {
                npcl(FaceAnim.OLD_NORMAL, "Do we have to do thissss right now?")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.OLD_NORMAL, "You are feeling ssssleepy...")
                stage = 5
            }

            2 -> {
                npcl(FaceAnim.OLD_NORMAL, "I'm bored, do ssssomething to entertain me...")
                stage = 11
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "Your will is my command...")
                stage = 13
            }

            4 -> {
                npcl(FaceAnim.OLD_NORMAL, "I am king of the world!")
                stage = 15
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Yes, I'm afraid so.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.OLD_NORMAL, "You are under my sssspell...")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I will do as you ask...")
                stage++
            }

            3 -> {
                npcl(FaceAnim.OLD_NORMAL, "Do we have to do thissss right now?")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Not at all, I had just finished!")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "I am feeling sssso ssssleepy...")
                stage++
            }

            6 -> {
                npcl(FaceAnim.OLD_NORMAL, "You will bring me lotssss of sssstuff!")
                stage++
            }

            7 -> {
                playerl(FaceAnim.FRIENDLY, "What ssssort of sssstuff?")
                stage++
            }

            8 -> {
                npcl(FaceAnim.OLD_NORMAL, "What ssssort of sssstuff have you got?")
                stage++
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "All kindsss of sssstuff.")
                stage++
            }

            10 -> {
                npcl(FaceAnim.OLD_NORMAL, "Then just keep bringing sssstuff until I'm ssssatissssfied!")
                stage = END_DIALOGUE
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Errr, I'm not here to entertain you, you know.")
                stage++
            }

            12 -> {
                npcl(FaceAnim.OLD_NORMAL, "You will do as I assssk...")
                stage = END_DIALOGUE
            }

            13 -> {
                npcl(FaceAnim.OLD_NORMAL, "I'm bored, do ssssomething to entertain me...")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "I'll dance for you!")
                end()
                animate<Int>(npc, Animations.DANCE_866, true)
                stage = END_DIALOGUE
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "You know, I think there is a law against snakes being the king.")
                stage++
            }

            16 -> {
                npcl(FaceAnim.OLD_NORMAL, "My will is your command...")
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "I am yours to command...")
                stage++
            }

            18 -> {
                npcl(FaceAnim.OLD_NORMAL, "I am king of the world!")
                stage++
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "All hail King Serpentor!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_COBRA_6802, NPCs.SPIRIT_COBRA_6803)
    }
}
