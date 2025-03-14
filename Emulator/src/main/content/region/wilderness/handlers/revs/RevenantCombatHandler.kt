package content.region.wilderness.handlers.revs

import core.api.*
import core.api.event.applyPoison
import core.api.event.isPoisoned
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.world.map.zone.impl.WildernessZone
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Sounds

class RevenantCombatHandler(
    meleeAnimation: Animation?,
    magicAnimation: Animation?,
    rangeAnimation: Animation?,
) : MultiSwingHandler(
        true,
        SwitchAttack(CombatStyle.MELEE.swingHandler, meleeAnimation),
        SwitchAttack(CombatStyle.RANGE.swingHandler, rangeAnimation, createProjectile(RANGE_Graphics)),
        SwitchAttack(CombatStyle.MAGIC.swingHandler, magicAnimation, createProjectile(MAGIC_Graphics)),
    ) {
    override fun visualizeImpact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ) {
        if (victim is Player) {
            val attack = current
            if (attack.style == CombatStyle.RANGE) {
                playGlobalAudio(victim.getLocation(), 4061, 0, 1, 10)
            }
        }
        super.visualizeImpact(entity, victim, state)
    }

    override fun impact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ) {
        if (victim is Player) {
            val attack = current
            if (attack.style == CombatStyle.RANGE &&
                !hasTimerActive(victim, "frozen") &&
                !hasTimerActive(
                    victim,
                    "frozen:immunity",
                )
            ) {
                registerTimer(victim, spawnTimer("frozen", 16, true))
                sendMessage((victim as Player?)!!, "The icy darts freeze your muscles!")
                playGlobalAudio(victim.getLocation(), 4059, 0, 1, 10)
            } else if (attack.style == CombatStyle.MAGIC) {
                var ticks = 500
                if (victim.asPlayer().prayer[PrayerType.PROTECT_FROM_MAGIC]) {
                    ticks /= 2
                }
                if (hasTimerActive(victim, "teleblock")) {
                    playGlobalAudio(victim.getLocation(), 4064, 0, 1, 10)
                } else {
                    registerTimer(victim, spawnTimer("teleblock", ticks))
                }
            }
        }
        if (!isPoisoned(victim!!) && (WildernessZone.getWilderness(entity!!) >= 50 || entity.id == 6998)) {
            applyPoison(victim, entity, 6)
        }
        super.impact(entity, victim, state)
    }

    override fun visualize(
        entity: Entity,
        victim: Entity?,
        state: BattleState?,
    ) {
        super.visualize(entity, victim, state)
        if (victim!!.isPlayer) {
            val attack = current
            if (attack.style == CombatStyle.MAGIC) {
                playGlobalAudio(entity.location, Sounds.TPBLOCK_CAST_202, 0, 1, 10)
            } else if (attack.style == CombatStyle.RANGE) {
                playGlobalAudio(entity.location, 4062, 0, 1, 10)
            }
        }
    }

    companion object {
        private val MAGIC_Graphics: Graphics = Graphics.create(org.rs.consts.Graphics.PUFF_OF_GREY_1276)
        private val RANGE_Graphics: Graphics = Graphics.create(org.rs.consts.Graphics.BIT_OF_WATER_1278)

        fun createProjectile(graphics: Graphics): Projectile {
            return Projectile.create(null, null, graphics.id, 48, 36, 34, 20)
        }
    }
}
