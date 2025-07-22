package content.global.bots

import core.api.produceGroundItem
import core.game.bots.Script
import core.game.bots.SkillingBotAssembler
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class BarbarianSalmon : Script() {
    private var state = State.FISHING

    override fun tick() {
        when (state) {
            State.FISHING -> {
                val fishingSpot = scriptAPI.getNearestNode(NPCs.FISHING_SPOT_309, false)
                if (fishingSpot != null && !bot.inventory.isFull) {
                    InteractionListeners.run(fishingSpot.id, IntType.NPC, "lure", bot, fishingSpot)
                } else if (bot.inventory.isFull) {
                    state = State.DROP_FISH
                }
            }

            State.DROP_FISH -> {
                var droppedAny = false

                val salmonCount = bot.inventory.getAmount(Items.RAW_SALMON_331)
                if (salmonCount > 0) {
                    val dropAmount = RandomFunction.random(1, salmonCount)
                    produceGroundItem(bot, Items.RAW_SALMON_331, dropAmount, bot.location)
                    bot.inventory.remove(Item(Items.RAW_SALMON_331, dropAmount))
                    droppedAny = true
                }

                val troutCount = bot.inventory.getAmount(Items.RAW_TROUT_335)
                if (troutCount > 0) {
                    val dropAmount = RandomFunction.random(1, troutCount)
                    produceGroundItem(bot, Items.RAW_TROUT_335, dropAmount, bot.location)
                    bot.inventory.remove(Item(Items.RAW_TROUT_335, dropAmount))
                    droppedAny = true
                }

                if (!droppedAny || !bot.inventory.isFull) {
                    state = State.REFILL_BAIT
                }
            }

            State.REFILL_BAIT -> {
                val hasRod = bot.inventory.containsAtLeastOneItem(Items.FLY_FISHING_ROD_309)
                val hasFeathers = bot.inventory.containsAtLeastOneItem(Items.FEATHER_314)
                if (!hasRod || !hasFeathers) {
                    bot.inventory.clear()
                    bot.inventory.add(Item(Items.FLY_FISHING_ROD_309))
                    bot.inventory.add(Item(Items.FEATHER_314, 10000))
                }
                state = State.FISHING
            }
        }
    }

    override fun newInstance(): Script {
        val script = BarbarianSalmon()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.values().random(), bot.startLocation)
        return script
    }

    init {
        skills[Skills.FISHING] = 55
        inventory.add(Item(Items.FLY_FISHING_ROD_309))
        inventory.add(Item(Items.FEATHER_314, 10000))
    }

    enum class State {
        FISHING,
        DROP_FISH,
        REFILL_BAIT,
    }
}