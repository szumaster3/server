package content.region.fremennik.island.jatizso.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class HringHringDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Oh, hello again. Want some ore?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0-> showTopics(
                Topic("I'll have a look.", openNpcShop(player, NPCs.HRING_HRING_5483)),
                Topic("Not right now.", end())
            )
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = HringHringDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.HRING_HRING_5483)
}
