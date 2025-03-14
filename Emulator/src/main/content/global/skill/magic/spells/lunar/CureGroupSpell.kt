package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.event.curePoison
import core.api.sendMessage
import core.game.node.item.Item
import core.game.world.map.RegionManager
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class CureGroupSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.CURE_GROUP, NONE) { player, _ ->
            requires(
                player,
                74,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 2), Item(Items.COSMIC_RUNE_564, 2)),
            )
            removeRunes(player, true)
            visualizeSpell(player, Animations.LUNAR_CURE_GROUP_4409, 744, 130, Sounds.LUNAR_CURE_GROUP_2882)
            curePoison(player)
            for (acct in RegionManager.getLocalPlayers(player, 1)) {
                if (!acct.isActive || acct.locks.isInteractionLocked) {
                    continue
                }
                if (!acct.settings.isAcceptAid) {
                    continue
                }
                curePoison(acct)
                sendMessage(acct, "You have been cured of poison.")
                visualizeSpell(acct, -1, 744, 130, Sounds.LUNAR_CURE_OTHER_INDIVIDUAL_2889)
            }
            addXP(player, 74.0)
        }
    }
}
