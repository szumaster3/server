package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.RegionManager.getLocalEntitys
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction

/**
 * The type Giant chinchompa npc.
 */
@Initializable
class GiantChinchompaNPC
/**
 * Instantiates a new Giant chinchompa npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Giant chinchompa npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = GIANT_CHINCHOMPA_7353) :
    Familiar(owner, id, 3100, GIANT_CHINCHOMPA_POUCH_12800, 3, WeaponInterface.STYLE_RANGE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return GiantChinchompaNPC(owner, id)
    }

    override fun onAttack(entity: Entity) {
        super.onAttack(entity)
        if (RandomFunction.random(20) == 10) {
            executeSpecialMove(FamiliarSpecial(null))
        }
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (!isOwnerAttackable) {
            return false
        }
        val entitys = getLocalEntitys(owner, 6)
        entitys.remove(owner)
        entitys.remove(this)
        sendChat("Squeak!")
        animate(Animation.create(7758))
        graphics(Graphics.create(1364))
        Pulser.submit(object : Pulse(3, owner, this) {
            override fun pulse(): Boolean {
                for (entity in entitys) {
                    if (canCombatSpecial(entity, false)) {
                        entity.impactHandler.manualHit(
                            this@GiantChinchompaNPC,
                            RandomFunction.random(13),
                            HitsplatType.NORMAL
                        )
                    }
                }
                dismiss()
                return true
            }
        })
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(GIANT_CHINCHOMPA_7353, GIANT_CHINCHOMPA_7354)
    }
}
