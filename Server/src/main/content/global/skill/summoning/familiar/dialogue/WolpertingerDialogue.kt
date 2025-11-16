package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Wolpertinger familiar dialogue.
 */
@Initializable
class WolpertingerDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = WolpertingerDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.CHILD_NORMAL, "Raaar! Mewble, whurf whurf.")
        stage = END_DIALOGUE
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun getIds(): IntArray = intArrayOf(NPCs.WOLPERTINGER_6869, NPCs.WOLPERTINGER_6870)
}
