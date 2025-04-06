package content.global.skill.summoning.familiar.npc

import content.global.skill.crafting.casting.Gem
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Magpie npc.
 */
@Initializable
class MagpieNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.MAGPIE_6824) :
    Forager(owner, id, 3400, Items.MAGPIE_POUCH_12041, 3, *ITEMS) {
    /**
     * Instantiates a new Magpie npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Magpie npc.
     */
    init {
        boosts.add(SkillBonus(Skills.THIEVING, 3.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return MagpieNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        visualize(Animation.create(8020), Graphics.create(1336))
        return true
    }

    override fun visualizeSpecialMove() {
        owner.getSkills().updateLevel(Skills.THIEVING, 2)
        owner.visualize(Animation(7660), Graphics(1296))
    }

    override fun getRandom(): Int {
        return 14
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAGPIE_6824)
    }

    companion object {
        private val ITEMS = arrayOf(Gem.SAPPHIRE.uncut, Gem.EMERALD.uncut, Gem.RUBY.uncut, Gem.DIAMOND.uncut)
    }
}
