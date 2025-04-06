package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager.getLocalEntitys
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Spirit tz kih npc.
 */
@Initializable
class SpiritTzKihNPC
/**
 * Instantiates a new Spirit tz kih npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Spirit tz kih npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_TZ_KIH_7361) :
    Familiar(owner, id, 1800, Items.SPIRIT_TZ_KIH_POUCH_12808, 6, WeaponInterface.STYLE_CAST) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritTzKihNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val entitys = getLocalEntitys(owner, 8)
        if (entitys.size == 0) {
            return false
        }
        var success = false
        var target: Entity? = null
        for (i in 0..1) {
            if (entitys.size >= i) {
                target = entitys[i]
                if (target == null || target === this || target === owner) {
                    continue
                }
                if (!canCombatSpecial(target)) {
                    continue
                }
                success = true
                sendFamiliarHit(target, 7, Graphics.create(1329))
            }
        }
        if (success) {
            animate(Animation.create(8257))
            return true
        }
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_TZ_KIH_7361, NPCs.SPIRIT_TZ_KIH_7362)
    }
}
