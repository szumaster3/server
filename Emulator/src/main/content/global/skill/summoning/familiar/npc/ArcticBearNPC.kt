package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * The type Arctic bear npc.
 */
@Initializable
class ArcticBearNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.ARCTIC_BEAR_6839) :
    Familiar(owner, id, 2800, 12057, 6, WeaponInterface.STYLE_CONTROLLED) {
    /**
     * Instantiates a new Arctic bear npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Arctic bear npc.
     */
    init {
        boosts.add(SkillBonus(Skills.HUNTER, 7.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return ArcticBearNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.target
        if (!canCombatSpecial(target)) {
            return false
        }
        animate(Animation.create(4926))
        graphics(Graphics.create(1405))
        val p = Projectile.magic(this, target, 1406, 40, 40, 1, 10)
        p.speed = 25
        p.send()
        sendFamiliarHit(target, 15, Graphics.create(1407))
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARCTIC_BEAR_6839, NPCs.ARCTIC_BEAR_6840)
    }
}
