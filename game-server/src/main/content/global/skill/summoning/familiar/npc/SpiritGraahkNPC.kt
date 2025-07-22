package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SpiritGraahkNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_GRAAHK_7363) :
    Familiar(owner, id, 4900, Items.SPIRIT_GRAAHK_POUCH_12810, 3, WeaponInterface.STYLE_AGGRESSIVE) {

    init {
        boosts.add(SkillBonus(Skills.HUNTER, 5.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritGraahkNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (!super.isOwnerAttackable()) {
            return false
        }
        call()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_GRAAHK_7363, NPCs.SPIRIT_GRAAHK_7364)
    }
}
