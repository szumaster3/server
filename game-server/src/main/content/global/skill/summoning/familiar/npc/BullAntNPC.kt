package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BullAntNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.BULL_ANT_6867) :
    BurdenBeast(owner, id, 3000, Items.BULL_ANT_POUCH_12087, 12, 9, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(owner: Player, id: Int): Familiar {
        return BullAntNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (owner.settings.runEnergy >= 100) {
            owner.packetDispatch.sendMessage("You already have full run energy.")
            return false
        }
        val amount = owner.getSkills().getStaticLevel(Skills.AGILITY) / 2
        visualize(Animation.create(7896), Graphics.create(1382))
        owner.settings.updateRunEnergy(-amount.toDouble())
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(Animation(7660), Graphics(1296))
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BULL_ANT_6867, NPCs.BULL_ANT_6868)
    }
}
