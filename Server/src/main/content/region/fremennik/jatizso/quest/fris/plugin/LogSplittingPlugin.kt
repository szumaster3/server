package content.region.fremennik.jatizso.quest.fris.plugin

import content.data.skill.SkillingTool
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class LogSplittingPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cut option on the woodcutting stump.
         */

        on(Scenery.WOODCUTTING_STUMP_21305, IntType.SCENERY, "cut-wood") { player, _ ->
            logCutting(player)
            return@on true
        }

        /*
         * Handles using arctic pine log on the woodcutting stump.
         */

        onUseWith(IntType.SCENERY, ARCTIC_PINE_LOG, Scenery.WOODCUTTING_STUMP_21305) { player, _, _ ->
            logCutting(player)
            return@onUseWith true
        }
    }

    /**
     * Log cutting & shield crafting pulse.
     */
    private fun logCutting(player: Player) {
        if (getStatLevel(player, Skills.WOODCUTTING) < 54) {
            sendMessage(player, "You need a woodcutting level of 54 in order to do this.")
            return
        }

        if (!inInventory(player, ARCTIC_PINE_LOG)) {
            sendMessage(player, "You don't have the required items in your inventory.")
            return
        }

        sendSkillDialogue(player) {
            withItems(FREMENNIK_SHIELD, SPLIT_LOG)
            create { id, amount ->
                submitIndividualPulse(
                    player,
                    when (id) {
                        FREMENNIK_SHIELD -> FremennikShieldPulse(player, Item(ARCTIC_PINE_LOG), amount)
                        else -> LogCuttingPulse(player, Item(ARCTIC_PINE_LOG), amount)
                    }
                )
            }

            calculateMaxAmount {
                amountInInventory(player, ARCTIC_PINE_LOG)
            }
        }
    }

    companion object {
        const val ARCTIC_PINE_LOG = Items.ARCTIC_PINE_LOGS_10810
        const val SPLIT_LOG = Items.SPLIT_LOG_10812
        const val FREMENNIK_SHIELD = Items.FREMENNIK_ROUND_SHIELD_10826
    }
}

/**
 * Handles split pine pulse.
 */
private class LogCuttingPulse(
    player: Player?,
    node: Item?,
    var amount: Int,
) : SkillPulse<Item?>(player, null) {

    private val arcticPineLog = Items.ARCTIC_PINE_LOGS_10810
    private val splitLog: Item = Item(Items.SPLIT_LOG_10812)
    private val splittingAnimation = Animation(Animations.HUMAN_SPLIT_LOGS_5755)

    var ticks = 0

    override fun checkRequirements(): Boolean {
        val tool = SkillingTool.getToolForSkill(player, Skills.WOODCUTTING)
        if (tool == null) {
            sendMessage(player, "You do not have a axe to use.")
            return false
        }
        if (amountInInventory(player, Items.ARCTIC_PINE_LOGS_10810) < 1) {
            sendMessage(player, "You have run out of an Arctic pine log.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            animate(player, splittingAnimation)
        }
    }

    override fun reward(): Boolean {
        if (++ticks % 5 != 0) {
            return false
        }
        amount = arcticPineLog
        if (removeItem(player, arcticPineLog)) {
            addItem(player, splitLog.id, 1)
            rewardXP(player, Skills.WOODCUTTING, 42.5)
            sendMessage(player, "You make a split log of Arctic pine.")
        }
        amount--
        return amount < 1
    }
}

/**
 * Handles creating round shield pulse.
 */
private class FremennikShieldPulse(
    player: Player?,
    node: Item,
    var amount: Int,
) : SkillPulse<Item>(player, null) {
    val splitAnimation = Animations.HUMAN_SPLIT_LOGS_5755
    var ticks = 0

    override fun checkRequirements(): Boolean {
        if (!anyInInventory(
                player,
                Items.HAMMER_2347,
                Items.ARCTIC_PINE_LOGS_10810,
                Items.ROPE_954,
                Items.BRONZE_NAILS_4819,
            )
        ) {
            sendMessage(player, "You don't have required items in your inventory.")
            return false
        }
        if (amountInInventory(player, Items.ARCTIC_PINE_LOGS_10810) < 2) {
            sendMessage(player, "You need at least 2 arctic pine logs to do this.")
            return false
        }
        if (!inInventory(player, Items.ROPE_954)) {
            sendMessage(player, "You will need a rope in order to do this.")
            return false
        }
        if (!inInventory(player, Items.HAMMER_2347) && inInventory(player, Items.BRONZE_NAILS_4819)) {
            sendMessage(player, "You need a hammer to force the nails in with.")
            return false
        }
        if (!inInventory(player, Items.BRONZE_NAILS_4819)) {
            sendMessage(player, "You need bronze nails for this.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, splitAnimation)
    }

    override fun reward(): Boolean {
        if (ticks == 1) {
            delay = 3
            return false
        }

        if (player.inventory.remove(
                Item(Items.ARCTIC_PINE_LOGS_10810, 2),
                Item(Items.ROPE_954, 1),
                Item(Items.BRONZE_NAILS_4819, 1),
            )
        ) {
            rewardXP(player, Skills.CRAFTING, 34.0)
            addItem(player, Items.FREMENNIK_ROUND_SHIELD_10826, 1)
            sendMessage(player, "You make a Fremennik round shield.")
            amount--
            return amount == 0
        }

        return true
    }

    override fun message(type: Int) {}
}
