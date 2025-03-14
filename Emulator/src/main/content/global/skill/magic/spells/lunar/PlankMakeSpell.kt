package content.global.skill.magic.spells.lunar

import content.global.skill.construction.item.Planks
import content.global.skill.construction.item.Planks.Companion.spellPrice
import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.*
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Items
import org.rs.consts.Sounds

class PlankMakeSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.PLANK_MAKE, ITEM) { player, node ->
            requires(
                player,
                86,
                arrayOf(
                    Item(Items.ASTRAL_RUNE_9075, 2),
                    Item(Items.NATURE_RUNE_561, 1),
                    Item(Items.EARTH_RUNE_557, 15),
                ),
            )

            val planks = Planks.getForLog(node!!.id)
            if (planks == null) {
                sendMessage(player, "You need to use this spell on logs.")
                return@onCast
            }
            if (amountInInventory(player, Items.COINS_995) < planks.spellPrice() ||
                !removeItem(
                    player,
                    Item(Items.COINS_995, planks.spellPrice()),
                )
            ) {
                sendMessage(player, "You need ${planks.spellPrice()} coins to convert that log into a plank.")
                return@onCast
            }

            lock(player, 3)
            setDelay(player, false)
            visualizeSpell(
                player,
                Animations.LUNAR_PLANK_MAKE_6298,
                Graphics.PLANK_MAKE_GFX_1063,
                100,
                Sounds.LUNAR_MAKE_PLANK_3617,
            )
            removeRunes(player)
            replaceSlot(player, node.asItem().slot, Item(planks.plank))
            addXP(player, 90.0)
            showMagicTab(player)
        }
    }
}
