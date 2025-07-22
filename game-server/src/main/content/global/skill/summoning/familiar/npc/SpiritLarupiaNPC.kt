package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SpiritLarupiaNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_LARUPIA_7337) :
    Familiar(owner, id, 4900, Items.SPIRIT_LARUPIA_POUCH_12784, 6, WeaponInterface.STYLE_CONTROLLED) {

    init {
        boosts.add(SkillBonus(Skills.HUNTER, 5.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritLarupiaNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.target
        if (!canCombatSpecial(target)) {
            return false
        }
        target.getSkills().updateLevel(Skills.STRENGTH, -1, target.getSkills().getStaticLevel(Skills.STRENGTH) - 1)
        faceTemporary(target, 2)
        projectile(target, 1371)
        sendFamiliarHit(target, 10)
        visualize(Animation.create(5229), Graphics.create(1370))
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_LARUPIA_7337, NPCs.SPIRIT_LARUPIA_7338)
    }
}
