package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction

/**
 * The type Geyser titan npc.
 */
@Initializable
class GeyserTitanNPC
/**
 * Instantiates a new Geyser titan npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Geyser titan npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = 7339) :
    Familiar(owner, id, 6900, 12786, 6, WeaponInterface.STYLE_RANGE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return GeyserTitanNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (!canCombatSpecial(special.target)) {
            return false
        }
        val target = special.target
        visualize(Animation(7883), Graphics(1375, 315))
        var maxHit = 30
        var defBonus = 0
        for (i in 5..10) {
            defBonus += target.properties.bonuses[i]
        }
        maxHit = defBonus / 40
        if (maxHit <= 1) {
            maxHit = RandomFunction.random(0, 3)
        }
        if (maxHit > 30) {
            maxHit = RandomFunction.random(20, 30)
        }
        Projectile.ranged(this, special.target, 1376, 300, 30, 0, 45).send()
        super.sendFamiliarHit(special.target, maxHit, Graphics.create(1377))
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(7339, 7340)
    }
}
