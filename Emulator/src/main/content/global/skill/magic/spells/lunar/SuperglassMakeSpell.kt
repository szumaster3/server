package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class SuperglassMakeSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.SUPERGLASS_MAKE, NONE) { player, _ ->
            requires(
                player,
                77,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.FIRE_RUNE_554, 6), Item(Items.AIR_RUNE_556, 10)),
            )
            val GLASS_WEEDS = hashSetOf(Items.SODA_ASH_1781, Items.SEAWEED_401, Items.SWAMP_WEED_10978)
            val inv = player.inventory.toArray()
            var playerWeed: Int =
                amountInInventory(player, Items.SODA_ASH_1781) +
                    amountInInventory(
                        player,
                        Items.SEAWEED_401,
                    ) + amountInInventory(player, Items.SWAMP_WEED_10978)
            var playerSand: Int = amountInInventory(player, Items.BUCKET_OF_SAND_1783)
            var index = 0

            fun addMolten(): Boolean {
                if (RandomFunction.randomDouble(1.0) < 0.3) {
                    if (addItem(player, Items.MOLTEN_GLASS_1775, 2)) return true
                } else {
                    if (addItem(player, Items.MOLTEN_GLASS_1775)) return true
                }
                return false
            }

            val size = minOf(playerSand, playerWeed)

            if (index != size && size != 0) {
                for (item in inv) {
                    if (item == null) continue
                    if (index == size) break
                    if (GLASS_WEEDS.contains(item.id)) {
                        if (removeItem(player, item) && removeItem(player, Items.BUCKET_OF_SAND_1783) && addMolten()) {
                            index++
                        } else {
                            break
                        }
                    }
                }
            } else if (playerWeed == 0 || playerSand == 0 || size == 0) {
                sendMessage(player, "You lack the required ingredients.")
            }

            if (index == size && size != 0) {
                removeRunes(player, true)
                visualizeSpell(player, Animations.LUNAR_SUPERGLASS_MAKE_4413, 729, 120, Sounds.LUNAR_HEATGLASS_2896)
                rewardXP(player, Skills.CRAFTING, 10.0)
                addXP(player, 78.0)
            }
        }
    }
}
