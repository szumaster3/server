package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.Commands
import core.api.event.applyPoison
import core.api.event.curePoison
import core.api.event.isPoisoned
import core.api.playAudio
import core.api.sendMessage
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds

class CureMeSpell :
    SpellListener("lunar"),
    Commands {
    override fun defineListeners() {
        onCast(LunarSpells.CURE_ME, NONE) { player, _ ->
            if (!isPoisoned(player)) {
                sendMessage(player, "You are not poisoned.")
                return@onCast
            }
            requires(
                player,
                71,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 1), Item(Items.COSMIC_RUNE_564, 2)),
            )
            removeRunes(player, true)
            visualizeSpell(player, Animations.LUNAR_CURE_ME_4411, 742, 90, Sounds.LUNAR_CURE_2884)
            curePoison(player)
            addXP(player, 69.0)
            playAudio(player, Sounds.LUNAR_CURE_OTHER_INDIVIDUAL_2900)
            sendMessage(player, "You have been cured of poison.")
        }
    }

    override fun defineCommands() {
        define("poison", privilege = Privilege.ADMIN) { player, strings ->
            if (strings.size == 3) {
                var dmg = strings[2].toIntOrNull()
                val p = Repository.getPlayerByName(strings[1])
                if (p == null) {
                    sendMessage(player, "Player ${strings[1]} does not exist.")
                    return@define
                }
                if (dmg != null) {
                    p.let { applyPoison(it, it, dmg) }
                } else {
                    sendMessage(player, "Damage must be an integer. Format:")
                    sendMessage(player, "::poison username damage")
                }
            } else {
                sendMessage(player, "Invalid arguments provided. Format:")
                sendMessage(player, "::poison username damage")
            }
        }
    }
}
