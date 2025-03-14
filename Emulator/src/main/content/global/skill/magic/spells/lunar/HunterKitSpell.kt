package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.addItem
import core.api.freeSlots
import core.api.sendMessage
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Items
import org.rs.consts.Sounds

class HunterKitSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.HUNTER_KIT, NONE) { player, _ ->
            if (freeSlots(player) == 0) {
                sendMessage(
                    player,
                    "You need at least one free inventory space to cast this spell.",
                ).also { return@onCast }
            }
            requires(player, 71, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.EARTH_RUNE_557, 2)))
            removeRunes(player, true)
            if (addItem(player, Items.HUNTER_KIT_11159)) {
                visualizeSpell(
                    player,
                    Animations.LUNAR_HUNTER_KIT_6303,
                    Graphics.MAKE_HUNTER_KIT_1074,
                    soundID = Sounds.LUNAR_HUNTER_KIT_3615,
                )
                addXP(player, 70.0)
                setDelay(player, 2)
            }
        }
    }
}
