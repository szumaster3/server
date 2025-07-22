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
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class WarTortoiseNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.WAR_TORTOISE_6815) :
    BurdenBeast(owner, id, 4300, Items.WAR_TORTOISE_POUCH_12031, 20, 18, WeaponInterface.STYLE_DEFENSIVE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return WarTortoiseNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        owner.getSkills().updateLevel(Skills.DEFENCE, 9, owner.getSkills().getStaticLevel(Skills.DEFENCE) + 9)
        visualize(Animation.create(8288), Graphics.create(org.rs.consts.Graphics.TURTLE_SHELL_SPIN_1414))
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(
            Animation.create(Animations.CAST_FAMILIAR_SCROLL_7660),
            Graphics.create(org.rs.consts.Graphics.YELLOW_FAMILIAR_GRAPHIC_1310)
        )
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WAR_TORTOISE_6815, NPCs.WAR_TORTOISE_6816)
    }
}
