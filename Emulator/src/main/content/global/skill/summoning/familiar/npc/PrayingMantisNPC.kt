package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Praying mantis npc.
 */
@Initializable
class PrayingMantisNPC
/**
 * Instantiates a new Praying mantis npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Praying mantis npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.PRAYING_MANTIS_6798) :
    Familiar(owner, id, 6900, Items.PRAYING_MANTIS_POUCH_12011, 6, WeaponInterface.STYLE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return PrayingMantisNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRAYING_MANTIS_6798, NPCs.PRAYING_MANTIS_6799)
    }
}
