package core.game.worldevents.events.halloween

import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.type.ExperiencePlugin
import core.tools.RandomFunction
import core.tools.colorize
import org.rs.consts.Items

class CandyRewardPlugin : ExperiencePlugin() {
    override fun run(
        player: Player,
        skill: Int,
        amount: Double,
    ) {
        val awardCandy = RandomFunction.random(1, 200) == 55
        val candy = Item(Items.WRAPPED_CANDY_14084)

        if (awardCandy) {
            if (!player.inventory.add(candy)) {
                GroundItemManager.create(candy, player)
            }
            player.sendMessage(colorize("%OYou receive a candy while training ${Skills.SKILL_NAME[skill]}!"))
        }
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any {
        return Unit
    }
}
