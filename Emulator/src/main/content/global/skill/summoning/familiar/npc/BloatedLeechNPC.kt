package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.event.cureDisease
import core.api.event.curePoison
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.math.ceil

/**
 * The type Bloated leech npc.
 */
@Initializable
class BloatedLeechNPC
/**
 * Instantiates a new Bloated leech npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Bloated leech npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.BLOATED_LEECH_6843) :
    Familiar(owner, id, 3400, Items.BLOATED_LEECH_POUCH_12061, 6, WeaponInterface.STYLE_ACCURATE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return BloatedLeechNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        curePoison(owner)
        cureDisease(owner)
        for (i in Skills.SKILL_NAME.indices) {
            if (owner.getSkills().getLevel(i) < owner.getSkills().getStaticLevel(i)) {
                owner.getSkills().updateLevel(
                    i,
                    ceil(owner.getSkills().getStaticLevel(i) * 0.2).toInt(),
                    owner.getSkills().getStaticLevel(i)
                )
            }
        }
        owner.impactHandler.manualHit(owner, RandomFunction.random(1, 5), HitsplatType.NORMAL)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLOATED_LEECH_6843, NPCs.BLOATED_LEECH_6844)
    }
}
