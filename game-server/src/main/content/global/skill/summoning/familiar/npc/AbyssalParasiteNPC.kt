package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.sendMessage
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AbyssalParasiteNPC @JvmOverloads constructor(owner: Player? = null, id: Int = NPCs.ABYSSAL_PARASITE_6818) :
    BurdenBeast(owner, id, 3000, Items.ABYSSAL_PARASITE_POUCH_12035, 1, 7) {

    @Suppress("unused")
    private val specialMove = false

    override fun construct(owner: Player, id: Int): Familiar {
        return AbyssalParasiteNPC(owner, id)
    }

    override fun isAllowed(owner: Player, item: Item): Boolean {
        if (item.id != Items.RUNE_ESSENCE_1436 && item.id != Items.PURE_ESSENCE_7936) {
            sendMessage(owner, "Your familiar can only hold unnoted essence.")
            return false
        }
        return super.isAllowed(owner, item)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val target = special.target
        if (!canCombatSpecial(target)) {
            return false
        }
        faceTemporary(target, 2)
        sendFamiliarHit(target, 7)
        visualize(Animation.create(7672), Graphics.create(1422))
        Projectile.magic(this, target, 1423, 40, 36, 51, 10).send()
        target.getSkills().decrementPrayerPoints(RandomFunction.random(1, 3).toDouble())
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ABYSSAL_PARASITE_6818, NPCs.ABYSSAL_PARASITE_6819)
    }
}
