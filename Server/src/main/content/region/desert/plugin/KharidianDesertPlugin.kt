package content.region.desert.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class KharidianDesertPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cutting cactuses to gather water.
         */

        on(CACTUS, IntType.SCENERY, "cut") { player, node ->
            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to cut this Cactus...")
                return@on true
            }

            val failed = RandomFunction.random(3) == 1

            if (failed) {
                sendMessage(player, "You fail to cut the cactus correctly and it gives you no water this time.")
            } else {
                val waterskin = getWaterSkin(player)
                if (waterskin == null || !removeItem(player, waterskin)) {
                    sendMessage(player, "You have no empty waterskins to put the water in.")
                    return@on true
                } else {
                    addItem(player, waterskin.id - 2)
                    sendMessage(player, "You top up your skin with water from the cactus.")

                    lock(player, 3)
                    animate(player, ANIMATION)
                    rewardXP(player, Skills.WOODCUTTING, 10.0)
                }
            }

            replaceScenery(
                node.asScenery(),
                DRY_CACTUS,
                SPAWN_DELAY + RandomFunction.random(getLocalPlayers(player).size / 2),
            )

            return@on true
        }
    }

    private fun getWaterSkin(player: Player): Item? {
        for (item in WATER_SKINS) {
            if (player.inventory.containsItem(item)) {
                return item
            }
        }
        return null
    }

    companion object {
        val WATER_SKINS: Array<Item> = arrayOf(Item(Items.WATERSKIN3_1825), Item(Items.WATERSKIN2_1827), Item(Items.WATERSKIN1_1829), Item(Items.WATERSKIN0_1831))
        val ANIMATION: Animation = Animation(Animations.CUT_SPIDER_WEB_911)
        const val DRY_CACTUS: Int = 2671
        const val SPAWN_DELAY: Int = 45
        private const val CACTUS = Scenery.KHARIDIAN_CACTUS_HEALTHY_2670
        private const val KNIFE = Items.KNIFE_946
    }
}
