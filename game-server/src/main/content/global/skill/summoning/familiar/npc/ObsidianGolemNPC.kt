package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ObsidianGolemNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.OBSIDIAN_GOLEM_7345) :
    Familiar(owner, id, 5500, Items.OBSIDIAN_GOLEM_POUCH_12792, 12, WeaponInterface.STYLE_AGGRESSIVE) {

    init {
        boosts.add(SkillBonus(Skills.MINING, 7.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return ObsidianGolemNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        graphics(Graphics.create(1465))
        owner.getSkills().updateLevel(Skills.STRENGTH, 9)
        return true
    }

    public override fun getText(): String {
        return "Onwards, to Glory!"
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OBSIDIAN_GOLEM_7345, NPCs.OBSIDIAN_GOLEM_7346)
    }
}
