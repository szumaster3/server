package content.minigame.sorceressgarden.dialogue

import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class DelMontyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hey kitty!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                npc(FaceAnim.CHILD_NORMAL, "Meow.")
                stage = 1
            }

            1 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DEL_MONTY_5563)
    }

    companion object {
        fun hasCatAmulet(player: Player): Boolean {
            val item = player.equipment[EquipmentContainer.SLOT_AMULET] ?: return false
            return item.id == Items.CATSPEAK_AMULET_4677 || item.id == Items.CATSPEAK_AMULETE_6544
        }
    }
}
