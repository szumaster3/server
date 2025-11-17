package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

class SuperglassMakeSpell : SpellListener("lunar") {
    companion object {
        private val GLASS_WEEDS = hashSetOf(Items.SODA_ASH_1781, Items.SEAWEED_401, Items.SWAMP_WEED_10978)
        private val SAND_SOURCES = intArrayOf(Items.BUCKET_OF_SAND_1783, Items.SANDBAG_9943)
    }

    override fun defineListeners() {
        onCast(LunarSpells.SUPERGLASS_MAKE, NONE) { player, _ ->
            requires(player, 77, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.FIRE_RUNE_554, 6), Item(Items.AIR_RUNE_556, 10)))

            val inv = player.inventory.toArray()
            var playerWeed = amountInInventory(player, Items.SODA_ASH_1781) + amountInInventory(player, Items.SEAWEED_401) + amountInInventory(player, Items.SWAMP_WEED_10978)
            var playerSand = 0
            for (sandId in SAND_SOURCES) {
                playerSand += amountInInventory(player, sandId)
            }

            var index = 0

            fun addMolten(): Boolean {
                return if (RandomFunction.randomDouble(1.0) < 0.3) {
                    addItem(player, Items.MOLTEN_GLASS_1775, 2)
                } else {
                    addItem(player, Items.MOLTEN_GLASS_1775)
                }
            }

            val size = minOf(playerSand, playerWeed)

            if (index != size && size != 0) {
                for (item in inv) {
                    if (item == null) continue
                    if (index == size) break
                    if (GLASS_WEEDS.contains(item.id)) {
                        for (sandId in SAND_SOURCES) {
                            if (amountInInventory(player, sandId) > 0) {
                                if (removeItem(player, item) && removeItem(player, sandId) && addMolten()) {
                                    index++
                                    break
                                } else {
                                    break
                                }
                            }
                        }
                    }
                }
            } else if (playerWeed == 0 || playerSand == 0 || size == 0) {
                sendMessage(player, "You lack the required ingredients.")
            }

            if (index == size && size != 0) {
                removeRunes(player, true)
                visualizeSpell(player, Animations.FERTILE_SPELL_4413, 729, 120, Sounds.LUNAR_HEATGLASS_2896)
                rewardXP(player, Skills.CRAFTING, 10.0)
                addXP(player, 78.0)
            }
        }
    }
}
