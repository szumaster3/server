package content.global.skill.summoning.familiar.npc

import content.global.skill.farming.FarmingPatch
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Hydra npc.
 */
@Initializable
class HydraNPC
/**
 * Instantiates a new Hydra npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Hydra npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.HYDRA_6811) :
    Familiar(owner, id, 4900, Items.HYDRA_POUCH_12025, 6, WeaponInterface.STYLE_RANGE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return HydraNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val node = special.node
        if (node is Scenery) {
            val farmingPatch = FarmingPatch.forObject(node)
            if (farmingPatch != null) {
                val patch = farmingPatch.getPatchFor(owner, true)
                patch.regrowIfTreeStump()
                return true
            }
        }

        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HYDRA_6811, NPCs.HYDRA_6812)
    }
}
