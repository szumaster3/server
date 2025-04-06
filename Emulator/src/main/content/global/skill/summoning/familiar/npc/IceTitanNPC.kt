package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Ice titan npc.
 */
@Initializable
class IceTitanNPC
/**
 * Instantiates a new Ice titan npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Ice titan npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.ICE_TITAN_7359) :
    ElementalTitanNPC(owner, id, 6400, Items.ICE_TITAN_POUCH_12806, 20, WeaponInterface.STYLE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return IceTitanNPC(owner, id)
    }


    override fun visualizeSpecialMove() {
        owner.visualize(Animation(7660), Graphics(1306))
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ICE_TITAN_7359, NPCs.ICE_TITAN_7360)
    }
}
