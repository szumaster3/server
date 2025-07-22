package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class TalonBeastNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.TALON_BEAST_7347) :
    Familiar(owner, id, 4900, Items.TALON_BEAST_POUCH_12794, 6, WeaponInterface.STYLE_AGGRESSIVE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return TalonBeastNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TALON_BEAST_7347, NPCs.TALON_BEAST_7348)
    }
}
