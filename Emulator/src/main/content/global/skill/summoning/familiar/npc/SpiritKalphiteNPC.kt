package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.RegionManager.getLocalEntitys
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Spirit kalphite npc.
 */
@Initializable
class SpiritKalphiteNPC
/**
 * Instantiates a new Spirit kalphite npc.
 *
 * @param owner the owner
 * @param id    the id
 */
/**
 * Instantiates a new Spirit kalphite npc.
 */
@JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.SPIRIT_KALPHITE_6994) :
    BurdenBeast(owner, id, 2200, Items.SPIRIT_KALPHITE_POUCH_12063, 6, 6, WeaponInterface.STYLE_DEFENSIVE) {
    override fun construct(owner: Player, id: Int): Familiar {
        return SpiritKalphiteNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        if (!isOwnerAttackable) {
            return false
        }
        val entitys = getLocalEntitys(owner, 6)
        visualize(Animation.create(8517), Graphics.create(1350))
        Pulser.submit(object : Pulse(1, owner) {
            override fun pulse(): Boolean {
                var count = 0
                for (entity in entitys) {
                    if (count > 5) {
                        return true
                    }
                    if (!canCombatSpecial(entity)) {
                        continue
                    }
                    Projectile.magic(this@SpiritKalphiteNPC, entity, 1349, 40, 36, 50, 5).send()
                    sendFamiliarHit(entity, 20)
                    count++
                }
                return true
            }
        })
        return false
    }

    public override fun getText(): String {
        return "Hsssss!"
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_KALPHITE_6994, NPCs.SPIRIT_KALPHITE_6995)
    }
}
