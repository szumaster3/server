package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.impact
import core.api.isPlayer
import core.api.sendMessage
import core.api.visualize
import core.game.node.entity.combat.ImpactHandler
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds
import kotlin.math.floor

class EnergyTransferSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.ENERGY_TRANSFER, PLAYER) { player, node ->
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
                if (10 >= player.skills.lifepoints) {
                    sendMessage(player, "You need more hitpoints to cast this spell.")
                    return@onCast
                }
                requires(
                    player,
                    91,
                    arrayOf(
                        Item(Items.ASTRAL_RUNE_9075, 3),
                        Item(Items.LAW_RUNE_563, 2),
                        Item(Items.NATURE_RUNE_561, 1),
                    ),
                )
                player.face(p)
                visualizeSpell(
                    player,
                    Animations.LUNAR_ENERGY_TRANSFER_4411,
                    738,
                    90,
                    Sounds.LUNAR_ENERGY_TRANSFER_2885,
                )
                visualize(p, -1, 738)
                val hp = floor(player.skills.lifepoints * 0.10)
                var r = hp
                if (r > (100 - p.settings.runEnergy)) {
                    r = (100 - p.settings.runEnergy)
                }
                if (r < 0) {
                    r = 0.0
                }
                p.settings.runEnergy += r
                player.settings.runEnergy -= r
                impact(player, hp.toInt(), ImpactHandler.HitsplatType.NORMAL)
                var e = 100
                e -= p.settings.specialEnergy
                if (e < 0) {
                    e = 0
                }
                if (e > player.settings.specialEnergy) {
                    e = player.settings.specialEnergy
                }
                p.settings.specialEnergy += e
                player.settings.specialEnergy -= e
                removeRunes(player, true)
                addXP(player, 100.0)
            }
        }
    }
}
