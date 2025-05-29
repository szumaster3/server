package content.global.skill.magic.spells.modern

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * The Crumble undead spell.
 */
@Initializable
class CrumbleUndeadSpell :
/**
    * Instantiates a new Crumble undead spell.
    */
    CombatSpell(
        SpellType.CRUMBLE_UNDEAD,
        SpellBook.MODERN,
        39,
        24.5,
        Sounds.CRUMBLE_CAST_AND_FIRE_122,
        Sounds.CRUMBLE_HIT_124,
        Animation(Animations.CAST_SPELL_PUSH_724, Priority.HIGH),
        Graphics(org.rs.consts.Graphics.CRUMBLE_UNDEAD_CAST_145, 96),
        Projectile.create(
            null as Entity?,
            null,
            org.rs.consts.Graphics.CRUMBLE_UNDEAD_PROJECTILE_146,
            40,
            36,
            52,
            75,
            15,
            11,
        ),
        Graphics(org.rs.consts.Graphics.CRUMBLE_UNDEAD_IMPACT_147, 96),
        Runes.EARTH_RUNE.getItem(2),
        Runes.AIR_RUNE.getItem(2),
        Runes.CHAOS_RUNE.getItem(1),
    ) {
    override fun cast(
        entity: Entity,
        target: Node,
    ): Boolean {
        val npc = if (target is NPC) target else null
        if (npc == null || npc.task == null || !npc.task.undead) {
            (entity as Player).packetDispatch.sendMessage("This spell only affects the undead.")
            return false
        }
        return super.cast(entity, target)
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(22, this)
        return this
    }

    override fun getMaximumImpact(
        entity: Entity,
        victim: Entity,
        state: BattleState,
    ): Int = type.getImpactAmount(entity, victim, 0)
}
