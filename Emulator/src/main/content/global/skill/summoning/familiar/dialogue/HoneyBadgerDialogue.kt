package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Honey badger dialogue.
 */
@Initializable
class HoneyBadgerDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return HoneyBadgerDialogue(player)
    }

    /**
     * Instantiates a new Honey badger dialogue.
     */
    constructor()

    /**
     * Instantiates a new Honey badger dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 5).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "An outpouring of sanity-straining abuse*")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "An outpouring of spittal-flecked insults.*")
                stage = 0
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "A lambasting of visibly illustrated obscenities.*")
                stage = 0
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "A tirade of biologically questionable threats*")
                stage = 0
            }

            4 -> {
                npcl(FaceAnim.CHILD_NORMAL, "A stream of eye-watering crudities*")
                stage = 0
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (stage == 0) {
            playerl(FaceAnim.FRIENDLY, "Why do I talk to you again?")
            stage = END_DIALOGUE
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HONEY_BADGER_6845, NPCs.HONEY_BADGER_6846)
    }
}
