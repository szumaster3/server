package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.event.curePoison
import core.api.event.isPoisoned
import core.api.isPlayer
import core.api.sendMessage
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class CureOtherSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.CURE_OTHER, PLAYER) { player, node ->
            node?.let {
                if (!isPlayer(node)) {
                    sendMessage(player, "You can only cast this spell on other players.")
                    return@onCast
                }
                val p = node.asPlayer()
                if (!p.isActive || p.locks.isInteractionLocked) {
                    sendMessage(player, "This player is busy.")
                    return@onCast
                }
                if (!p.settings.isAcceptAid) {
                    sendMessage(player, "This player is not accepting any aid.")
                    return@onCast
                }
                if (!isPoisoned(p)) {
                    sendMessage(player, "This player is not poisoned.")
                    return@onCast
                }
                requires(
                    player,
                    68,
                    arrayOf(Item(Items.ASTRAL_RUNE_9075, 1), Item(Items.LAW_RUNE_563), Item(Items.EARTH_RUNE_557, 10)),
                )
                player.face(p)
                visualizeSpell(player, Animations.LUNAR_CURE_OTHER_4411, 736, 130, Sounds.LUNAR_CURE_OTHER_2886)
                visualizeSpell(p, -1, 736, 130, Sounds.LUNAR_CURE_OTHER_INDIVIDUAL_2889)
                removeRunes(player, true)
                curePoison(p)
                sendMessage(p, "You have been cured of poison.")
                addXP(player, 65.0)
            }
        }
    }
}
