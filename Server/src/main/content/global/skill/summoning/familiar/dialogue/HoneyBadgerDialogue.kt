package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Honey Badger familiar dialogues.
 */
@Initializable
class HoneyBadgerDialogue : Dialogue {
    private var branch: Int = 0

    override fun newInstance(player: Player?) = HoneyBadgerDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..4).random()
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (stage == 0) {
            when (branch) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "*An outpouring of sanity-straining abuse*")
                1 -> npcl(FaceAnim.CHILD_NORMAL, "*An outpouring of spittal-flecked insults.*")
                2 -> npcl(FaceAnim.CHILD_NORMAL, "*A lambasting of visibly illustrated obscenities.*")
                3 -> npcl(FaceAnim.CHILD_NORMAL, "*A tirade of biologically questionable threats*")
                4 -> npcl(FaceAnim.CHILD_NORMAL, "*A stream of eye-watering crudities*")
            }
            stage++
        } else if (stage == 1) {
            playerl(FaceAnim.FRIENDLY, "Why do I talk to you again?")
            stage = END_DIALOGUE
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HONEY_BADGER_6845, NPCs.HONEY_BADGER_6846)
}