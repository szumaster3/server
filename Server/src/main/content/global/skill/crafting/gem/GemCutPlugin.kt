package content.global.skill.crafting.gem

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction.random
import shared.consts.Items
import shared.consts.Sounds

class GemCutPlugin : InteractionListener {

    private val UNCUT_GEMS = intArrayOf(
        Gems.OPAL.uncut.id,
        Gems.JADE.uncut.id,
        Gems.RED_TOPAZ.uncut.id,
        Gems.SAPPHIRE.uncut.id,
        Gems.EMERALD.uncut.id,
        Gems.RUBY.uncut.id,
        Gems.DIAMOND.uncut.id,
        Gems.DRAGONSTONE.uncut.id,
        Gems.ONYX.uncut.id,
    )

    private val SEMIPRECIOUS_GEMS = intArrayOf(
        Gems.OPAL.uncut.id,
        Gems.JADE.uncut.id,
        Gems.RED_TOPAZ.uncut.id,
        Gems.OPAL.gem.id,
        Gems.JADE.gem.id,
        Gems.RED_TOPAZ.gem.id
    )

    override fun defineListeners() {

        /*
         * Handles cutting gems.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *UNCUT_GEMS) { player, used, with ->
            val gem = Gems.forId(if (used.id == Items.CHISEL_1755) with.asItem() else used.asItem())
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, gem!!.gem) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(GemCutPulse(player, gem!!.uncut, amount, gem))
                    }

                    override fun getAll(index: Int): Int = amountInInventory(player, gem!!.uncut.id)
                }
            if (amountInInventory(player, gem.uncut.id) == 1) {
                handler.create(0, 1)
            } else {
                handler.open()
            }
            return@onUseWith true
        }

        /*
         * Handles crushing gems by using a hammer.
         * Patch: 27 January 2009
         */

        onUseWith(IntType.ITEM, Items.HAMMER_2347, *SEMIPRECIOUS_GEMS) { player, used, with ->
            val gemId = if (used.id == Items.HAMMER_2347) with.id else used.id
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(gemId)) {
                    override fun create(amount: Int, index: Int) {
                        player.pulseManager.run(object : SkillPulse<Item?>(player, Item(gemId)) {
                            private var remaining = amount

                            override fun checkRequirements(): Boolean {
                                return inInventory(player, gemId)
                            }

                            override fun animate() {
                            }

                            override fun reward(): Boolean {
                                if (player.inventory.remove(Item(gemId))) {
                                    player.inventory.add(Item(Items.CRUSHED_GEM_1633))
                                    sendMessage(player, "You deliberately crush the gem with the hammer.")
                                }
                                remaining--
                                return remaining <= 0
                            }
                        })
                    }

                    override fun getAll(index: Int): Int = amountInInventory(player, gemId)
                }

            if (amountInInventory(player, gemId) == 1) {
                handler.create(0, 1)
            } else {
                handler.open()
            }

            return@onUseWith true
        }
    }

}

/**
 * Represents the pulse used to cut a gem.
 */
private class GemCutPulse(player: Player?, item: Item?, var amount: Int, val gem: Gems) : SkillPulse<Item?>(player, item) {
    private var ticks = 0

    init {
        resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.CRAFTING) < gem.level) {
            sendMessage(player, "You need a crafting level of ${gem.level} to craft this gem.")
            return false
        }
        if (!inInventory(player, CHISEL)) {
            return false
        }
        if (!inInventory(player, gem.uncut.id)) {
            return false
        }
        if (!hasSpaceFor(player, Item(gem.uncut.id))) {
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }
        return true
    }

    override fun animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            playAudio(player, Sounds.CHISEL_2586)
            animate(player, gem.animation)
        }
    }

    override fun reward(): Boolean {
        if (player.inventory.remove(gem.uncut)) {
            val craftingLevel = getStatLevel(player, Skills.CRAFTING)
            val crushedGem: Item? = when (gem.uncut.id) {
                Items.UNCUT_OPAL_1625 -> if (random(100) < getGemCrushChance(7.42, 0.0, craftingLevel)) Item(Items.CRUSHED_GEM_1633) else null
                Items.UNCUT_JADE_1627 -> if (random(100) < getGemCrushChance(9.66, 0.0, craftingLevel)) Item(Items.CRUSHED_GEM_1633) else null
                Items.UNCUT_RED_TOPAZ_1629 -> if (random(100) < getGemCrushChance(9.2, 0.0, craftingLevel)) Item(Items.CRUSHED_GEM_1633) else null
                else -> null
            }
            val gemName = getItemName(gem.gem.id)
            if (crushedGem != null) {
                player.inventory.add(crushedGem)
                rewardXP(player, Skills.CRAFTING, when (gem.uncut.id) {
                    Items.UNCUT_OPAL_1625 -> 3.8
                    Items.UNCUT_RED_TOPAZ_1629 -> 6.3
                    else -> 5.0
                })
                sendMessage(player, "You mis-hit the chisel and smash the $gemName to pieces!")
            } else {
                player.inventory.add(gem.gem)
                rewardXP(player, Skills.CRAFTING, gem.exp)
                sendMessage(player, "You cut the $gemName.")
            }
        }

        amount--
        return amount < 1
    }

    companion object {
        private const val CHISEL = Items.CHISEL_1755
    }

    /**
     * Calculates the chance to accidentally crush a gem when cutting it.
     */
    fun getGemCrushChance(low: Double, high: Double, level: Int): Double {
        if (level >= 50) return 0.0
        val clampedLevel = level.coerceIn(1, 49)
        val chance = low * ((50 - clampedLevel) / 49.0) + high * ((clampedLevel - 1) / 49.0)
        return chance.coerceIn(0.0, 100.0)
    }
}