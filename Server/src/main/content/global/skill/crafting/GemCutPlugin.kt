package content.global.skill.crafting

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction.random
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

class GemCutPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cutting gems using chisel.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *UNCUT_Gem) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            val gem = CraftingDefinition.Gem.forId(if (used.id == Items.CHISEL_1755) with.id else used.id) ?: return@onUseWith true
            val invAmount = amountInInventory(player, gem.uncut)

            fun cutGem(amount: Int) {
                var remaining = if (amount <= 0) 1 else amount

                queueScript(player, 1, QueueStrength.WEAK) { stage ->
                    if (remaining <= 0 || !clockReady(player, Clocks.SKILLING) || !inInventory(player, gem.uncut))
                        return@queueScript stopExecuting(player)

                    if (getStatLevel(player, Skills.CRAFTING) < gem.level) {
                        sendMessage(player, "You need a Crafting level of ${gem.level} to craft this gem.")
                        return@queueScript stopExecuting(player)
                    }

                    if (!inInventory(player, gem.uncut)) {
                        sendMessage(player, "You do not have enough gems.")
                        return@queueScript stopExecuting(player)
                    }

                    when (stage) {
                        0 -> {
                            animate(player, gem.animation)
                            playAudio(player, Sounds.CHISEL_2586)
                            delayClock(player, Clocks.SKILLING, 1)
                            delayScript(player, 1)
                        }

                        else -> {
                            removeItem(player, gem.uncut)
                            val craftingLevel = getStatLevel(player, Skills.CRAFTING)
                            val crushedGem: Item? = when (gem.uncut) {
                                Items.UNCUT_OPAL_1625 -> if (random(100) < getGemCrushChance(7.42, 0.0, craftingLevel)) Item(Items.CRUSHED_GEM_1633) else null
                                Items.UNCUT_JADE_1627 -> if (random(100) < getGemCrushChance(9.66, 0.0, craftingLevel)) Item(Items.CRUSHED_GEM_1633) else null
                                Items.UNCUT_RED_TOPAZ_1629 -> if (random(100) < getGemCrushChance(9.2, 0.0, craftingLevel)) Item(Items.CRUSHED_GEM_1633) else null
                                else -> null
                            }

                            if (crushedGem != null) {
                                addItem(player, crushedGem.id)
                                rewardXP(
                                    player, Skills.CRAFTING, when (gem.uncut) {
                                        Items.UNCUT_OPAL_1625 -> 3.8
                                        Items.UNCUT_RED_TOPAZ_1629 -> 6.3
                                        else -> 5.0
                                    }
                                )
                                sendMessage(player, "You mis-hit the chisel and smash the ${getItemName(gem.cut)} to pieces!")
                            } else {
                                addItem(player, gem.cut)
                                rewardXP(player, Skills.CRAFTING, gem.xp)
                                sendMessage(player, "You cut the ${getItemName(gem.cut)}.")
                            }

                            remaining--
                            if (remaining > 0) {
                                setCurrentScriptState(player, 0)
                                delayScript(player, 1)
                            } else {
                                return@queueScript stopExecuting(player)
                            }
                        }
                    }
                }
            }

            if (invAmount == 1) {
                cutGem(1)
            } else {
                sendSkillDialogue(player) {
                    withItems(gem.cut)

                    create { _, amount ->
                        cutGem(amount)
                    }

                    calculateMaxAmount { amountInInventory(player, gem.uncut) }
                }
            }

            return@onUseWith true
        }


        /*
         * Handles crushing semi-precious gems using a hammer.
         * Patch: 27 January 2009
         */

        onUseWith(IntType.ITEM, Items.HAMMER_2347, *SEMIPRECIOUS_Gem) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val gemId = if (used.id == Items.HAMMER_2347) with.id else used.id
            val invAmount = amountInInventory(player, gemId)

            fun crushGem() {
                queueScript(player, 1, QueueStrength.WEAK) { stage ->
                    if (!clockReady(player, Clocks.SKILLING) || !inInventory(player, gemId))
                        return@queueScript stopExecuting(player)

                    when (stage) {
                        0 -> {
                            animate(player, Animations.USE_HAMMER_CHISEL_11041)
                            delayClock(player, Clocks.SKILLING, 1)
                            delayScript(player, 1)
                        }

                        else -> {
                            val removed = removeItem(player, gemId)
                            if (removed) {
                                addItem(player, Items.CRUSHED_GEM_1633)
                                sendMessage(player, "You deliberately crush the gem with the hammer.")
                            }
                            return@queueScript stopExecuting(player)
                        }
                    }
                }
            }

            if (invAmount == 1) {
                crushGem()
            } else {
                sendSkillDialogue(player) {
                    withItems(Items.CRUSHED_GEM_1633)
                    create { _, amount ->
                        repeat(if (amount <= 0) 1 else amount) {
                            crushGem()
                        }
                    }
                    calculateMaxAmount { amountInInventory(player, gemId) }
                }
            }

            return@onUseWith true
        }
    }

    companion object {
        /**
         * Represents the uncut gems.
         */
        private val UNCUT_Gem = intArrayOf(
            CraftingDefinition.Gem.OPAL.uncut,
            CraftingDefinition.Gem.JADE.uncut,
            CraftingDefinition.Gem.RED_TOPAZ.uncut,
            CraftingDefinition.Gem.SAPPHIRE.uncut,
            CraftingDefinition.Gem.EMERALD.uncut,
            CraftingDefinition.Gem.RUBY.uncut,
            CraftingDefinition.Gem.DIAMOND.uncut,
            CraftingDefinition.Gem.DRAGONSTONE.uncut,
            CraftingDefinition.Gem.ONYX.uncut,
        )

        /**
         * Represents the low tier gems.
         */
        private val SEMIPRECIOUS_Gem = intArrayOf(
            CraftingDefinition.Gem.OPAL.uncut,
            CraftingDefinition.Gem.JADE.uncut,
            CraftingDefinition.Gem.RED_TOPAZ.uncut
        )

        /**
         * Calculates the % chance of crushing a gem when cutting it.
         * @param low The base chance of crushing at level 1.
         * @param high The base chance of crushing at level 49.
         * @param level The player crafting level.
         * @return The chance `(0.0 to 100.0)` that the gem will be crushed.
         */
        fun getGemCrushChance(low: Double, high: Double, level: Int): Double {
            if (level >= 50) return 0.0
            val clamped = level.coerceIn(1, 49)
            val chance = low * ((50 - clamped) / 49.0) + high * ((clamped - 1) / 49.0)
            return chance.coerceIn(0.0, 100.0)
        }
    }
}