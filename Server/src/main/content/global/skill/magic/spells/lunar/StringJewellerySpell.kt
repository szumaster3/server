package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

class StringJewellerySpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.STRING_JEWELLERY, NONE)
        on@{ player, _ ->
            requires(player, 80, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.EARTH_RUNE_557, 10), Item(Items.WATER_RUNE_555, 5)))
            val playerJewellery = ArrayDeque<Item>()
            for (item in player.inventory.toArray()) {
                if (item == null) continue
                if (!StringJewelleryItems.unstrungContains(item.id)) continue
                playerJewellery.add(item)
            }
            if (playerJewellery.isEmpty()) return@on
            player.pulseManager.run(object : Pulse() {
                init {
                    delay = animationDuration(Animation(4412)) + 1
                }

                override fun pulse(): Boolean {
                    removeAttribute(player, "spell:runes")

                    if (playerJewellery.isEmpty()) return true

                    val item = playerJewellery.removeFirst()
                    val strung = StringJewelleryItems.forId(item.id)

                    if (addItem(player, strung) && removeItem(player, item)) {
                        removeRunes(player, false)
                        visualizeSpell(
                            player, Animations.LUNAR_STRING_JEWELLERY_4412, 730, 100, Sounds.LUNAR_STRING_AMULET_2903
                        )
                        rewardXP(player, Skills.CRAFTING, 4.0)
                        addXP(player, 83.0)
                        addItem(player, strung)
                    }

                    if (playerJewellery.isEmpty()) removeRunes(player, true)
                    return playerJewellery.isEmpty()
                }
            })
        }
    }

    private enum class StringJewelleryItems(val unstrung: Int, val strung: Int) {
        GOLD(       unstrung = Items.GOLD_AMULET_1673,      strung = Items.GOLD_AMULET_1692),
        SAPPHIRE(   unstrung = Items.SAPPHIRE_AMULET_1675,  strung = Items.SAPPHIRE_AMULET_1694),
        EMERALD(    unstrung = Items.EMERALD_AMULET_1677,   strung = Items.EMERALD_AMULET_1696),
        RUBY(       unstrung = Items.RUBY_AMULET_1679,      strung = Items.RUBY_AMULET_1698),
        DIAMOND(    unstrung = Items.DIAMOND_AMULET_1681,   strung = Items.DIAMOND_AMULET_1700),
        DRAGONSTONE(unstrung = Items.DRAGONSTONE_AMMY_1683, strung = Items.DRAGONSTONE_AMMY_1702),
        ONYX(       unstrung = Items.ONYX_AMULET_6579,      strung = Items.ONYX_AMULET_6581),
        SALVE(      unstrung = Items.SALVE_SHARD_4082,      strung = Items.SALVE_AMULET_4081),
        HOLY(       unstrung = Items.UNSTRUNG_SYMBOL_1714,  strung = Items.UNBLESSED_SYMBOL_1716),
        UNHOLY(     unstrung = Items.UNSTRUNG_EMBLEM_1720,  strung = Items.UNPOWERED_SYMBOL_1722), ;

        companion object {
            private val productOfString = values().associate { it.unstrung to it.strung }
            fun forId(id: Int): Int = productOfString[id]!!
            fun unstrungContains(id: Int): Boolean = productOfString.contains(id)
        }
    }
}
