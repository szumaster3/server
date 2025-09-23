package content.region.wilderness.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Cape Merchant dialogue.
 */
@Initializable
class CapeMerchantDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Hello there, are you interested in buying one of my", "special capes?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("What's so special about your capes?", 1, false),
                Topic("Yes please!", 4, false),
                Topic("No thanks.", 5, false)
            )
            1 -> npc("Ahh well they make it less likely that you'll accidently", "attack anyone wearing the same cape as you and easier", "to attack everyone else. They also make it easier to", "distinguish people who're wearing the same cape as you").also { stage++ }
            2 -> npc("from everyone else. They're very useful when out in", "the wilderness with friends or anyone else you don't", "want to harm.").also { stage++ }
            3 -> npc("So would you like to buy one?").also { stage++ }
            4 -> {
                end()
                openNpcShop(player, npc.id)
            }
            5 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CapeMerchantDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.WILLIAM_1778,
        NPCs.IAN_1779,
        NPCs.LARRY_1780,
        NPCs.DARREN_1781,
        NPCs.EDWARD_1782,
        NPCs.RICHARD_1783,
        NPCs.NEIL_1784,
        NPCs.EDMOND_1785,
        NPCs.SIMON_1786,
        NPCs.SAM_1787,
    )
}
