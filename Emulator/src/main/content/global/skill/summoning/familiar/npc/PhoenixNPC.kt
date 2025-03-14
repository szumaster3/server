package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class PhoenixNPC(
    owner: Player? = null,
    id: Int = NPCs.PHOENIX_8575,
) : Familiar(owner, id, 3200, Items.PHOENIX_POUCH_14623, 12, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(
        owner: Player,
        id: Int,
    ): Familiar {
        return PhoenixNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        owner.getSkills().updateLevel(Skills.FIREMAKING, 6, 6)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PHOENIX_8575, NPCs.PHOENIX_8576)
    }
}
