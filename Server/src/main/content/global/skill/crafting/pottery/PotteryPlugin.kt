package content.global.skill.crafting.pottery

import core.api.amountInInventory
import core.api.isQuestComplete
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery
import java.util.*

@Initializable
class PotteryPlugin : UseWithHandler(Items.SOFT_CLAY_1761) {

    private val SOFT_CLAY = Items.SOFT_CLAY_1761
    private val OVENS = intArrayOf(
        Scenery.POTTERY_OVEN_2643, Scenery.POTTERY_OVEN_4308, Scenery.POTTERY_OVEN_11601, Scenery.POTTERY_OVEN_34802
    )

    override fun newInstance(arg: Any?): Plugin<Any> {
        FireOvenPlugin().newInstance(arg)
        addHandler(Scenery.POTTER_S_WHEEL_2642, OBJECT_TYPE, this)
        addHandler(Scenery.POTTERY_OVEN_2643, OBJECT_TYPE, this)
        addHandler(Scenery.POTTERY_OVEN_4308, OBJECT_TYPE, this)
        addHandler(Scenery.POTTER_S_WHEEL_4310, OBJECT_TYPE, this)
        addHandler(Scenery.POTTER_S_WHEEL_20375, OBJECT_TYPE, this)
        addHandler(Scenery.POTTER_S_WHEEL_34801, OBJECT_TYPE, this)
        addHandler(Scenery.POTTERY_OVEN_34802, OBJECT_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        object : SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.FIVE_OPTION, *getPottery(false)) {
            override fun create(amount: Int, index: Int) {
                player.pulseManager.run(PotteryCraftingPulse(player, event.usedItem, amount, Pottery.values()[index]),)
            }

            override fun getAll(index: Int): Int = amountInInventory(player, SOFT_CLAY)
        }.open()
        return true
    }

    private fun getPottery(finished: Boolean): Array<Item> {
        val items = Array(Pottery.values().size) { Item(0) }
        for (i in items.indices) {
            items[i] = if (finished) Pottery.values()[i].product else Pottery.values()[i].unfinished
        }
        return items
    }

    inner class FireOvenPlugin : OptionHandler() {
        override fun newInstance(arg: Any?): Plugin<Any> {
            for (id in OVENS) {
                SceneryDefinition.forId(id).handlers["option:fire"] = this
            }
            FireUseHandler().newInstance(arg)
            return this
        }

        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean {
            getSkillHandler(player).open()
            return true
        }

        inner class FireUseHandler : UseWithHandler(
            Items.UNFIRED_POT_1787,
            Items.UNFIRED_PIE_DISH_1789,
            Items.UNFIRED_BOWL_1791,
            Items.UNFIRED_PLANT_POT_5352,
            Items.UNFIRED_POT_LID_4438
        ) {
            override fun newInstance(arg: Any?): Plugin<Any> {
                addHandler(Scenery.POTTERY_OVEN_2643, OBJECT_TYPE, this)
                addHandler(Scenery.POTTERY_OVEN_4308, OBJECT_TYPE, this)
                addHandler(Scenery.POTTERY_OVEN_11601, OBJECT_TYPE, this)
                addHandler(Scenery.POTTERY_OVEN_34802, OBJECT_TYPE, this)
                return this
            }

            override fun handle(event: NodeUsageEvent): Boolean {
                val player = event.player
                val scenery = event.usedWith

                if (scenery.id == Scenery.POTTERY_OVEN_4308 && !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                    sendMessage(player, "Only Fremenniks may use this ${scenery.name.lowercase(Locale.getDefault())}.")
                    return false
                }

                getSkillHandler(player).open()
                return true
            }
        }

        fun getSkillHandler(player: Player): SkillDialogueHandler =
            object : SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.FIVE_OPTION, *getPottery(true)) {
                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(
                        FirePotteryPulse(
                            player, Pottery.values()[index].unfinished, Pottery.values()[index], amount
                        )
                    )
                }

                override fun getAll(index: Int): Int = player.inventory.getAmount(Pottery.values()[index].unfinished)
            }
    }
}