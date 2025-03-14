package content.region.misthalin.dialogue.digsite

import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class EdWoodDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val forceChat =
        arrayOf(
            "Can't stop. Too busy.",
            "Wonder when I'll get paid.",
            "Is it lunch break yet?",
            "Hey I'm working here. I'm working here.",
            "This work isn't going to do itself.",
            "Ouch! That was my finger!",
        )

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        sendChat(npc, forceChat.random())
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return EdWoodDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ED_WOOD_5964)
    }
}
