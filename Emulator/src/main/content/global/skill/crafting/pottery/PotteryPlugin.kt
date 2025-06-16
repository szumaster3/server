package content.global.skill.crafting.pottery

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.OptionHandler
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

@Initializable
class PotteryPlugin : UseWithHandler(Items.SOFT_CLAY_1761) {

    private val SOFT_CLAY = Items.SOFT_CLAY_1761
    private val OVENS = intArrayOf(Scenery.POTTERY_OVEN_2643, Scenery.POTTERY_OVEN_4308, Scenery.POTTERY_OVEN_11601, Scenery.POTTERY_OVEN_34802)

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
            override fun create(
                amount: Int,
                index: Int,
            ) {
                player.pulseManager.run(PotteryCraftingPulse(player, event.usedItem, amount, Pottery.values()[index]))
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

        inner class FireUseHandler :
            UseWithHandler(
                Items.UNFIRED_POT_1787,
                Items.UNFIRED_PIE_DISH_1789,
                Items.UNFIRED_BOWL_1791,
                Items.UNFIRED_PLANT_POT_5352,
                Items.UNFIRED_POT_LID_4438,
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
                getSkillHandler(player).open()
                return true
            }
        }

        fun getSkillHandler(player: Player): SkillDialogueHandler =
            object :
                SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.FIVE_OPTION, *getPottery(true)) {
                override fun create(
                    amount: Int,
                    index: Int,
                ) {
                    player.pulseManager.run(
                        FirePotteryPulse(
                            player = player,
                            node = Pottery.values()[index].unfinished,
                            pottery = Pottery.values()[index],
                            amount = amount,
                        ),
                    )
                }

                override fun getAll(index: Int): Int = player.inventory.getAmount(Pottery.values()[index].unfinished)
            }
    }
}

/**
 * Handles crafting pottery pulse.
 */
private class PotteryCraftingPulse(player: Player?, node: Item?, var amount: Int, val pottery: Pottery, ) : SkillPulse<Item?>(player, node) {
    var ticks = 0

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < pottery.level) {
            sendMessage(player, "You need a crafting level of " + pottery.level + " to make this.")
            return false
        }
        if (!inInventory(player, Items.SOFT_CLAY_1761, 1)) {
            sendMessage(player, "You have run out of clay.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, ANIMATION)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        if (removeItem(player, SOFT_CLAY)) {
            if (pottery == Pottery.BOWL && withinDistance(player, Location(3086, 3410, 0))) {
                setAttribute(player, "/save:diary:varrock:spun-bowl", true)
            }

            val item = pottery.unfinished
            player.inventory.add(item)
            rewardXP(player, Skills.CRAFTING, pottery.exp)

            sendMessage(player, "You make the clay into " + (if (StringUtils.isPlusN(item.name)) "an" else "a") + " " + item.name.lowercase() + ".")
            if (pottery == Pottery.POT && withinDistance(player, Location(3086, 3410, 0))) {
                finishDiaryTask(player, DiaryType.LUMBRIDGE, 0, 7)
            }
        }
        amount--
        return amount < 1
    }

    companion object {
        private const val ANIMATION = Animations.SPIN_POTTER_WHEEL_883
        private const val SOFT_CLAY = Items.SOFT_CLAY_1761
    }
}