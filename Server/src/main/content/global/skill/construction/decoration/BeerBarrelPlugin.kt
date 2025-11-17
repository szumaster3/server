package content.global.skill.construction.decoration

import core.api.addItem
import core.api.playAudio
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds
import shared.consts.Scenery as Obj

/**
 * Handles filling a glass from various beer barrels.
 */
@Initializable
class BeerBarrelPlugin : UseWithHandler(Items.BEER_GLASS_1919) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        REWARDS.keys.forEach { addHandler(it, OBJECT_TYPE, this) }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val scenery = event.usedWith as Scenery

        val glass = Item(Items.BEER_GLASS_1919)
        if (!removeItem(player, glass.id)) {
            sendMessage(player, "You don't have a glass to fill.")
            return true
        }

        playAudio(player, Sounds.FILL_GLASS_2395)
        player.animate(Animation.create(Animations.HUMAN_WITHDRAW_833))
        sendMessage(player, "You fill up your glass.")
        addItem(player, getReward(scenery.id))
        return true
    }

    private fun getReward(barrelId: Int): Int = REWARDS.getOrDefault(barrelId, Items.BEER_1917)

    companion object {
        private val REWARDS = mapOf(
            Obj.BEER_BARREL_13568    to Items.BEER_1917,
            Obj.CIDER_BARREL_13569   to Items.CIDER_5763,
            Obj.ASGARNIAN_ALE_13570  to Items.ASGARNIAN_ALE_1905,
            Obj.GREENMAN_S_ALE_13571 to Items.GREENMANS_ALE_1909,
            Obj.DRAGON_BITTER_13572  to Items.DRAGON_BITTER_1911,
            Obj.CHEF_S_DELIGHT_13573 to Items.CHEFS_DELIGHT_5755
        )
    }
}