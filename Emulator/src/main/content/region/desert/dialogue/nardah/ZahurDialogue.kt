package content.region.desert.dialogue.nardah

import core.api.interaction.decantContainer
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ZahurDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "I can combine your potion vials to try and make",
            "the potions fit into fewer vials. This service is free.",
            "Would you like to do this?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes", "No").also { stage++ }
            1 ->
                if (buttonId == 1) {
                    val decantResult = decantContainer(player.inventory)
                    val toRemove = decantResult.first
                    val toAdd = decantResult.second
                    for (item in toRemove) {
                        player.inventory.remove(item)
                    }
                    for (item in toAdd) {
                        player.inventory.add(item)
                    }
                    npc("There, all done.")
                } else {
                    end()
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ZahurDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAHUR_3037)
    }
}
