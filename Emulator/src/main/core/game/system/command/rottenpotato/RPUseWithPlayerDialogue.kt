package core.game.system.command.rottenpotato

import core.game.dialogue.Dialogue
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
class RPUseWithPlayerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var other: Player? = null
    val ID = 38575796

    override fun newInstance(player: Player?): Dialogue {
        return RPUseWithPlayerDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        other = args[0] as Player

        options("Kill", "View Bank", "Copy Inventory")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (buttonId) {
            1 -> other!!.impactHandler.manualHit(player, other!!.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
            2 -> other!!.bank!!.open(player)
            3 -> {
                player.inventory.clear()
                player.inventory.addAll(other!!.inventory)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(ID)
    }
}
