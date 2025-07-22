package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SpiritJellyNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_JELLY_6992) :
    Familiar(owner, id, 4300, Items.SPIRIT_JELLY_POUCH_12027, 6, WeaponInterface.STYLE_AGGRESSIVE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritJellyNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.node as Entity
        if (!canCombatSpecial(target)) {
            return false
        }
        faceTemporary(target, 2)
        sendFamiliarHit(target, 13)
        animate(Animation.create(8575))
        Projectile.magic(this, target, 1360, 40, 36, 51, 10).send()
        target.getSkills().updateLevel(Skills.ATTACK, -3, target.getSkills().getStaticLevel(Skills.ATTACK) - 3)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_JELLY_6992, NPCs.SPIRIT_JELLY_6993)
    }
}
