package content.region.kandarin.ardougne.west.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class KoftikDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        sendMessage(player, "Koftik doesn't seem interested in talking.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.KOFTIK_972,
            NPCs.KOFTIK_973,
            NPCs.KOFTIK_974,
            NPCs.KOFTIK_975,
            NPCs.KOFTIK_976,
            NPCs.KOFTIK_1209
        )
}
