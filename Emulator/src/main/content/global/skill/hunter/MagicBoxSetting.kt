package content.global.skill.hunter

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation

class MagicBoxSetting :
    TrapSetting(
        10025,
        intArrayOf(19223),
        intArrayOf(1470, 1472, 1476, 1474),
        "activate",
        19224,
        Animation.create(5208),
        Animation.create(9726),
        27,
    ) {
    override fun handleCatch(
        counter: Int,
        wrapper: TrapWrapper,
        node: TrapNode,
        npc: NPC,
        success: Boolean,
    ) {
        when (counter) {
            2 ->
                if (success) {
                    wrapper.player.packetDispatch.sendPositionedGraphic(932, 0, 0, npc.location)
                }

            3 -> npc.moveStep()
        }
    }

    override fun addTool(
        player: Player,
        wrapper: TrapWrapper,
        type: Int,
    ) {
        if (!wrapper.isCaught) {
            super.addTool(player, wrapper, type)
        }
    }
}
