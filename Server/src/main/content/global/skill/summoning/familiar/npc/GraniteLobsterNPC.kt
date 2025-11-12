package content.global.skill.summoning.familiar.npc

import content.global.skill.gather.fishing.Fish.Companion.forItem
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class GraniteLobsterNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.GRANITE_LOBSTER_6849) :
    Forager(owner, id, 4700, Items.GRANITE_LOBSTER_POUCH_12069, 6) {

    init {
        boosts.add(SkillBonus(Skills.FISHING, 4.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return GraniteLobsterNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.target
        if (!canCombatSpecial(target)) {
            return false
        }
        animate(Animation(8118))
        graphics(Graphics.create(1351))
        Projectile.ranged(this, target, 1352, 60, 40, 1, 45).send()
        sendFamiliarHit(target, 14)
        return true
    }

    override fun handlePassiveAction() {
        if (RandomFunction.random(40) == 1) {
            val item = FISH[RandomFunction.random(FISH.size)]
            animate(Animation.create(8107))
            val fish = forItem(item)
            owner.getSkills().addExperience(Skills.FISHING, fish!!.experience * 0.10)
            produceItem(item)
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRANITE_LOBSTER_6849, NPCs.GRANITE_LOBSTER_6850)
    }

    companion object {
        private val FISH = arrayOf(Item(Items.RAW_SHARK_383), Item(Items.RAW_SWORDFISH_371))
    }
}
