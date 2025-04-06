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

/**
 * The type Spirit terrorbird npc.
 */
@Initializable
class SpiritTerrorbirdNPC
/**
 * Instantiates a new Spirit terrorbird npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Spirit terrorbird npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_TERRORBIRD_6794) :
    BurdenBeast(owner, id, 3600, Items.SPIRIT_TERRORBIRD_POUCH_12007, 8, 12, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritTerrorbirdNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        visualize(Animation.create(1009), Graphics.create(org.rs.consts.Graphics.WHITE_SEAGALS_1521))
        owner.getSkills().updateLevel(Skills.AGILITY, 2)
        owner.settings.updateRunEnergy(-owner.getSkills().getStaticLevel(Skills.AGILITY) / 2.0)
        return true
    }

    override fun visualizeSpecialMove() {
        owner.visualize(
            Animation(Animations.CAST_FAMILIAR_SCROLL_7660),
            Graphics(org.rs.consts.Graphics.WHITE_FAMILIAR_GRAPHIC_1295)
        )
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_TERRORBIRD_6794, NPCs.SPIRIT_TERRORBIRD_6795)
    }
}
