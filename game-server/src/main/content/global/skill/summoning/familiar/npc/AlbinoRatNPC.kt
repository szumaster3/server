package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AlbinoRatNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.ALBINO_RAT_6847) :
    Forager(owner, id, 2200, Items.ALBINO_RAT_POUCH_12067, 6, WeaponInterface.STYLE_ACCURATE, CHEESE) {

    override fun construct(owner: Player, id: Int): Familiar {
        return AlbinoRatNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (produceItem(CHEESE)) {
            owner.lock(7)
            visualize(Animation.create(4934), Graphics.create(1384))
            return true
        }
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALBINO_RAT_6847, NPCs.ALBINO_RAT_6848)
    }

    companion object {
        private val CHEESE = Item(Items.CHEESE_1985, 4)
    }
}
