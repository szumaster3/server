package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * The type Spirit dagannoth npc.
 */
@Initializable
class SpiritDagannothNPC
/**
 * Instantiates a new Spirit dagannoth npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Spirit dagannoth npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = 6804) :
    Familiar(owner, id, 5700, 12017, 6, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritDagannothNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(6804, 6805)
    }
}
