package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class KaramthulhuOverlordNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.KARAMTHULHU_OVERLORD_6809) :
    Familiar(owner, id, 4400, Items.KARAM_OVERLORD_POUCH_12023, 3, WeaponInterface.STYLE_RANGE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return KaramthulhuOverlordNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        //Special: 4193,
        //Special Impact:4248
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KARAMTHULHU_OVERLORD_6809, NPCs.KARAMTHULHU_OVERLORD_6810)
    }
}
