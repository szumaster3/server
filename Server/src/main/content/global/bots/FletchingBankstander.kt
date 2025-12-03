package content.global.bots

import content.global.skill.fletching.FletchingDefinition
import content.global.skill.fletching.FletchingListener
import core.game.bots.Script
import core.game.bots.SkillingBotAssembler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class FletchingBankstander : Script() {
    private var state = State.FLETCHING

    override fun tick() {
        val bank = scriptAPI.getNearestNode("Bank booth")
        bot.faceLocation(bank?.location)

        when (state) {
            State.FLETCHING -> {
                bot.inventory.add(Item(Items.KNIFE_946))
                bot.inventory.add(Item(Items.LOGS_1511, 27))
                FletchingListener.handleFletching(bot, Item(Items.KNIFE_946), Item(Items.LOGS_1511))
                state = State.BANKING
            }

            State.BANKING -> {
                bot.inventory.clear()
                state = State.FLETCHING
            }
        }
    }

    override fun newInstance(): Script {
        val script = FletchingBankstander()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.AVERAGE, bot.startLocation)
        return script
    }

    init {
        skills[Skills.FLETCHING] = 99
    }

    enum class State {
        FLETCHING,
        BANKING,
    }
}
