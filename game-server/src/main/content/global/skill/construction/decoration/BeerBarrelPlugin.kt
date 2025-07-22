package content.global.skill.construction.decoration

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery as Obj

/**
 * The type Beer barrel plugin.
 */
@Initializable
class BeerBarrelPlugin : UseWithHandler(Items.BEER_GLASS_1919) {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (id in OBJECTS) {
            addHandler(id, OBJECT_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val scenery = event.usedWith as Scenery

        if (player.inventory.remove(Item(Items.BEER_GLASS_1919))) {
            player.animate(Animation.create(Animations.HUMAN_WITHDRAW_833))
            player.sendMessage("You fill up your glass.")
            player.inventory.add(Item(getReward(scenery.id), 1))
        }
        return true
    }

    /**
     * Gets reward.
     *
     * @param barrelId the barrel id
     * @return the reward
     */
    fun getReward(barrelId: Int): Int {
        return when (barrelId) {
            Obj.BEER_BARREL_13568 -> Items.BEER_1917
            Obj.CIDER_BARREL_13569 -> Items.CIDER_5763
            Obj.ASGARNIAN_ALE_13570 -> Items.ASGARNIAN_ALE_1905
            Obj.GREENMAN_S_ALE_13571 -> Items.GREENMANS_ALE_1909
            Obj.DRAGON_BITTER_13572 -> Items.DRAGON_BITTER_1911
            Obj.CHEF_S_DELIGHT_13573 -> Items.CHEFS_DELIGHT_5755
            else -> Items.BEER_1917
        }
    }

    companion object {
        private val OBJECTS = intArrayOf(
            Obj.BEER_BARREL_13568,
            Obj.CIDER_BARREL_13569,
            Obj.ASGARNIAN_ALE_13570,
            Obj.GREENMAN_S_ALE_13571,
            Obj.DRAGON_BITTER_13572,
            Obj.CHEF_S_DELIGHT_13573
        )
    }
}
