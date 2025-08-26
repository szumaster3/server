package content.global.skill.magic.spells.modern

import content.data.GameAttributes
import content.global.skill.magic.spells.ModernSpells
import core.api.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Sounds

@Initializable
class TeleblockSpell : CombatSpell {

    companion object {
        private val TELEBLOCK_ORB = Projectile.create(null as Entity?, null, 1842, 40, 36, 52, 75, 15, 11)
        private val TELEBLOCK_SUCCESS = Graphics(1843, 0)
        private val TELEBLOCK_START = Graphics(1841, 0)
    }

    constructor() : super()

    constructor(
        type: SpellType,
        level: Int,
        baseExperience: Double,
        impactSound: Int,
        anim: Animation,
        start: Graphics,
        projectile: Projectile,
        end: Graphics,
        vararg runes: Item
    ) : super(
        type,
        SpellBook.MODERN,
        level,
        baseExperience,
        Sounds.TPBLOCK_CAST_202,
        Sounds.TPBLOCK_IMPACT_203,
        anim,
        TELEBLOCK_START,
        TELEBLOCK_ORB,
        TELEBLOCK_SUCCESS,
        *runes
    )

    @Throws(Throwable::class)
    override fun newInstance(arg: SpellType?): Plugin<SpellType?> {
        SpellBook.MODERN.register(
            ModernSpells.TELEBLOCK, TeleblockSpell(
                SpellType.TELEBLOCK,
                85,
                80.0,
                Sounds.TPBLOCK_IMPACT_203,
                Animation(Animations.TELEBLOCK_CAST_10503, Priority.HIGH),
                TELEBLOCK_START,
                TELEBLOCK_ORB,
                TELEBLOCK_SUCCESS,
                Runes.LAW_RUNE.getItem(1),
                Runes.DEATH_RUNE.getItem(1),
                Runes.CHAOS_RUNE.getItem(1)
            )
        )
        return this
    }

    override fun visualize(entity: Entity, target: Node) {
        entity.graphics(graphics)
        projectile?.transform(entity, target as Entity, false, 58, 10)?.send()
        entity.animate(animation)
        audio?.let { playGlobalAudio(entity.location, it.id, 1, 20) }
    }

    override fun cast(entity: Entity, target: Node): Boolean {
        if (target !is Player) {
            entity.asPlayer().sendMessage("You can only cast this spell on another player.")
            return false
        }
        if (!entity.zoneMonitor.isInZone("Wilderness") || !target.zoneMonitor.isInZone("Wilderness")) {
            entity.asPlayer()
                .sendMessage("You and your opponent must both be in the wilderness for you to use this spell.")
            return false
        }
        if (hasTimerActive(target.asPlayer(), GameAttributes.TELEBLOCK_TIMER)) {
            entity.asPlayer().sendMessage("That player is already affected by this spell.")
            return false
        }
        if (!meetsRequirements(entity, true, false)) {
            return false
        }
        return super.cast(entity, target)
    }

    override fun visualizeImpact(entity: Entity, target: Entity, state: BattleState) {
        super.visualizeImpact(entity, target, state)
    }

    override fun getMaximumImpact(entity: Entity, victim: Entity, state: BattleState): Int {
        return 0
    }

    override fun fireEffect(entity: Entity, victim: Entity, state: BattleState) {
        if (!hasTimerActive(victim, GameAttributes.TELEBLOCK_TIMER)
            && victim is Player
            && state.style.swingHandler.isAccurateImpact(entity, victim)
        ) {
            var ticks = 500
            if (victim.prayer[PrayerType.PROTECT_FROM_MAGIC]) {
                ticks /= 2
            }
            registerTimer(victim, spawnTimer(GameAttributes.TELEBLOCK_TIMER, ticks))
        } else if (hasTimerActive(victim, GameAttributes.TELEBLOCK_TIMER)) {
            entity.asPlayer().sendMessage("Your target is already blocked from teleporting.")
        }
    }
}
