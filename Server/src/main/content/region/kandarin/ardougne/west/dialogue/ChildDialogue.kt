package content.region.kandarin.ardougne.west.dialogue

import content.global.skill.construction.decoration.costumeroom.Storable
import content.global.skill.construction.decoration.costumeroom.Storable.Companion.hasStorableEquipped
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Child dialogue.
 */
@Initializable
class ChildDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> if(hasStorableEquipped(player, Storable.MournerGear)) {
                npcl(FaceAnim.CHILD_NEUTRAL, "I'm not allowed to speak with strangers.").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.CHILD_NEUTRAL, "I'm not allowed to speak with Mourners.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.CHILD_6339,
        NPCs.CHILD_6345,
        NPCs.CHILD_355,
        NPCs.CHILD_356,
        NPCs.CHILD_6334,
        NPCs.CHILD_6335,
        NPCs.CHILD_6336,
        NPCs.CHILD_6337,
        NPCs.CHILD_6338,
        NPCs.CHILD_6339,
        NPCs.CHILD_6340,
        NPCs.CHILD_6341,
        NPCs.CHILD_6342,
        NPCs.CHILD_6343
    )
}
