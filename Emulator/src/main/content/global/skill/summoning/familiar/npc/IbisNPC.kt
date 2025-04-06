package content.global.skill.summoning.familiar.npc

import content.global.skill.gathering.fishing.Fish
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Ibis npc.
 */
@Initializable
class IbisNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.IBIS_6991) :
    Forager(owner, id, 3800, Items.IBIS_POUCH_12531, 12, Item(Items.TUNA_361), Item(Items.SWORDFISH_373)) {
    /**
     * Instantiates a new Ibis npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Ibis npc.
     */
    init {
        boosts.add(SkillBonus(Skills.FISHING, 3.0))
    }

    override fun handlePassiveAction() {
        if (RandomFunction.random(15) < 4) {
            produceItem()
        }
    }

    override fun produceItem(item: Item): Boolean {
        if (super.produceItem(item)) {
            if (item.id == 373) {
                owner.getSkills().addExperience(Skills.FISHING, 10.0)
            }
            return true
        }
        return false
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return IbisNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        animate(Animation.create(8201))
        Pulser.submit(object : Pulse(3, owner, this) {
            override fun pulse(): Boolean {
                var loc: Location? = null
                for (i in 0..1) {
                    loc = owner.location.transform(RandomFunction.random(2), RandomFunction.random(2), 0)
                    if (getObject(loc) != null) {
                        continue
                    }
                    GroundItemManager.create(FISH[RandomFunction.random(FISH.size)].getItem(), loc, owner)
                }
                return true
            }
        })
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IBIS_6991)
    }

    companion object {
        private val FISH = arrayOf(Fish.SHRIMP, Fish.BASS, Fish.COD, Fish.MACKEREL)
    }
}
