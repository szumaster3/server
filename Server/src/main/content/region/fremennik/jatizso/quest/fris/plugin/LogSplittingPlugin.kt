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
            sendMessage(player, "You need a Woodcutting level of 54 in order to do this.")
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
    var amount: Int
) : SkillPulse<Item?>(player, null) {

    private val arcticPineLog = Items.ARCTIC_PINE_LOGS_10810
    private val splitLog = Item(Items.SPLIT_LOG_10812)
    private val splittingAnimation = Animation(Animations.HUMAN_SPLIT_LOGS_5755)

    private var ticks = 0

    override fun checkRequirements(): Boolean {
        val tool = SkillingTool.getToolForSkill(player, Skills.WOODCUTTING)
        if (tool == null) {
            sendMessage(player, "You do not have an axe to use.")
            return false
        }
        if (amountInInventory(player, arcticPineLog) < 1) {
            sendMessage(player, "You have run out of Arctic pine logs.")
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
        ticks++
        if (ticks % 5 != 0) return false

        if (!removeItem(player, arcticPineLog)) {
            sendMessage(player, "You have run out of Arctic pine logs.")
            return true
        }

        addItem(player, splitLog.id)
        rewardXP(player, Skills.WOODCUTTING, 42.5)
        sendMessage(player, "You make a split log of Arctic pine.")

        amount--
        return amount <= 0
    }
}

/**
 * Handles creating round shield pulse.
 */
private class FremennikShieldPulse(
    player: Player?,
    node: Item,
    var amount: Int
) : SkillPulse<Item>(player, null) {

    private val splitAnimation = Animations.HUMAN_SPLIT_LOGS_5755
    private var ticks = 0

    override fun checkRequirements(): Boolean {
        val hasHammer = inInventory(player, Items.HAMMER_2347)
        val hasNails = inInventory(player, Items.BRONZE_NAILS_4819)
        val hasRope = inInventory(player, Items.ROPE_954)
        val logCount = amountInInventory(player, Items.ARCTIC_PINE_LOGS_10810)

        if (!hasHammer) {
            sendMessage(player, "You need a hammer to force the nails in with.")
            return false
        }
        if (!hasNails) {
            sendMessage(player, "You need bronze nails for this.")
            return false
        }
        if (!hasRope) {
            sendMessage(player, "You will need a rope in order to do this.")
            return false
        }
        if (logCount < 2) {
            sendMessage(player, "You need at least 2 arctic pine logs to do this.")
            return false
        }

        return true
    }

    override fun animate() {
        animate(player, splitAnimation)
    }

    override fun reward(): Boolean {
        ticks++
        if (ticks == 1) {
            delay = 3
            return false
        }

        if (player.inventory.remove(
                Item(Items.ARCTIC_PINE_LOGS_10810, 2),
                Item(Items.ROPE_954, 1),
                Item(Items.BRONZE_NAILS_4819, 1)
            )
        ) {
            rewardXP(player, Skills.CRAFTING, 34.0)
            addItem(player, Items.FREMENNIK_ROUND_SHIELD_10826, 1)
            sendMessage(player, "You make a Fremennik round shield.")
            amount--
        } else {
            sendMessage(player, "You don't have the required items to make this.")
            return true
        }

        return amount <= 0
    }

    override fun message(type: Int) {

    }
}
