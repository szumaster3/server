package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillBonus
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The Wolpertinger familiar.
 */
@Initializable
class WolpertingerNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.WOLPERTINGER_6869) :
    Familiar(owner, id, 6200, Items.WOLPERTINGER_POUCH_12089, 1, WeaponInterface.STYLE_CAST) {
    /**
     * Instantiates a new Wolpertinger npc.
     *
     * @param owner the owner
     * @param id    the id
     */
    /**
     * Instantiates a new Wolpertinger npc.
     */
    init {
        boosts.add(SkillBonus(Skills.HUNTER, 5.0))
    }

    override fun construct(owner: Player, id: Int): Familiar {
        return WolpertingerNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        owner.getSkills().updateLevel(Skills.MAGIC, 7, (owner.getSkills().getStaticLevel(Skills.MAGIC) + 7))
        visualize(Animation.create(8267), Graphics.create(1464))
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(Animation.create(Animations.CAST_FAMILIAR_SCROLL_7660), Graphics.create(org.rs.consts.Graphics.WHITE_FAMILIAR_GRAPHIC_1306))
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WOLPERTINGER_6869, NPCs.WOLPERTINGER_6870)
    }
}
