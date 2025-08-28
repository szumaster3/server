package content.global.bots

import core.api.produceGroundItem
import core.game.bots.Script
import core.game.bots.SkillingBotAssembler
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs

class BarbarianSalmon : Script() {
    private var state = State.FISHING
    private var cooldownTicks = 0

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
                var isDropped = false

                if (bot.inventory.containsAtLeastOneItem(Items.RAW_SALMON_331)) {
                    produceGroundItem(bot, Items.RAW_SALMON_331, 1, bot.location)
                    bot.inventory.remove(Item(Items.RAW_SALMON_331, 1))
                    isDropped = true
                } else if (bot.inventory.containsAtLeastOneItem(Items.RAW_TROUT_335)) {
                    produceGroundItem(bot, Items.RAW_TROUT_335, 1, bot.location)
                    bot.inventory.remove(Item(Items.RAW_TROUT_335, 1))
                    isDropped = true
                }

                if (!isDropped || !bot.inventory.isFull) {
                    cooldownTicks = (10..20).random()
                    state = State.COOLDOWN
                }
            }

            State.COOLDOWN -> {
                if (cooldownTicks > 0) {
                    cooldownTicks--
                } else {
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
        script.bot = SkillingBotAssembler().produce(
            SkillingBotAssembler.Wealth.values().random(),
            bot.startLocation
        )
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
        COOLDOWN,
        REFILL_BAIT,
    }
}
