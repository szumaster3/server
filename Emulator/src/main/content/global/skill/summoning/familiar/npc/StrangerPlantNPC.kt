package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.event.applyPoison
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Stranger plant npc.
 */
@Initializable
class StrangerPlantNPC
/**
 * Instantiates a new Stranger plant npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Stranger plant npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.STRANGER_PLANT_6827) :
    Forager(owner, id, 4900, Items.STRANGER_PLANT_POUCH_12045, 6, Item(Items.STRANGE_FRUIT_464)) {
    override fun construct(owner: Player, id: Int): Familiar {
        return StrangerPlantNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (!canCombatSpecial(special.target)) {
            return false
        }
        val target = special.target
        if (RandomFunction.random(2) == 1) {
            applyPoison(target, owner, 20)
        }
        animate(Animation.create(8211))
        Projectile.ranged(this, target, 1508, 50, 40, 1, 45).send()
        target.graphics(Graphics.create(1511))
        sendFamiliarHit(target, 2)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STRANGER_PLANT_6827, NPCs.STRANGER_PLANT_6828)
    }
}
