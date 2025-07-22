package content.global.skill.magic.spells.modern

import content.global.skill.magic.spells.ModernSpells
import core.api.registerTimer
import core.api.removeTimer
import core.api.spawnTimer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.audio.Audio
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Represents the Charge spell.
 */
@Initializable
class ChargeSpell : MagicSpell(
    SpellBook.MODERN,
    80,
    180.0,
    Animation(Animations.NS_CHARGE_SPELL_811),
    Graphics(6, 96),
    Audio(Sounds.CHARGE_1651),
    arrayOf(Runes.FIRE_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3))
) {

    @Throws(Throwable::class)
    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(ModernSpells.CHARGE, this)
        return this
    }

    override fun cast(entity: Entity, target: Node): Boolean {
        val p = entity as Player
        if (p.locks.isLocked("charge_cast")) {
            p.packetDispatch.sendMessage("You need to wait for the spell to recharge.")
            return false
        }
        if (!meetsRequirements(entity, true, true)) {
            return false
        }
        p.locks.lock("charge_cast", 100)
        visualize(entity, target)

        removeTimer(p, "magic:spellcharge")
        registerTimer(p, spawnTimer("magic:spellcharge"))
        p.packetDispatch.sendMessage("You feel charged with magic power.")
        return true
    }
}
