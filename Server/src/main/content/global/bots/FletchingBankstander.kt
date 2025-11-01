package content.global.bots

import content.global.skill.fletching.Fletching
import content.global.skill.fletching.FletchingPulse
import core.game.bots.Script
import core.game.bots.SkillingBotAssembler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class FletchingBankstander : Script() {
    var state = State.FLETCHING

    override fun tick() {
        val bank = scriptAPI.getNearestNode("Bank booth")
        val fletchData = Fletching.getEntries(Items.LOGS_1511)?.firstOrNull() ?: return
        bot.faceLocation(bank?.location)
        state =
            when (state) {
                State.FLETCHING -> {
                    bot.inventory.add(Item(Items.KNIFE_946))
                    bot.inventory.add(Item(Items.LOGS_1511, 27))
                    bot.pulseManager.run(
                        FletchingPulse(
                            bot,
                            Item(Items.LOGS_1511),
                            27,
                            fletchData,
                        ),
                    )
                    State.BANKING
                }

                State.BANKING -> {
                    bot.inventory.clear()
                    State.FLETCHING
                }
            }
    }

    init {
        skills[Skills.FLETCHING] = 99
    }

    override fun newInstance(): Script {
        val script = FletchingBankstander()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.AVERAGE, bot.startLocation)
        return script
    }

    enum class State {
        FLETCHING,
        BANKING,
    }
}
