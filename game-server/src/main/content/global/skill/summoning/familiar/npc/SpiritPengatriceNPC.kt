package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.player.Player
import org.rs.consts.Items
import org.rs.consts.NPCs

class SpiritPengatriceNPC
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_PENGATRICE_6883) :
    Familiar(owner, id, 3600, Items.SP_PENGATRICE_POUCH_12103, 3) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritPengatriceNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_PENGATRICE_6883, NPCs.SPIRIT_PENGATRICE_6884)
    }
}