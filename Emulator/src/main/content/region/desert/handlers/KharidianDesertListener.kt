package content.region.desert.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.RegionManager.getLocalPlayers
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.Scenery

class KharidianDesertListener : InteractionListener {
    override fun defineListeners() {
        on(CACTUS, IntType.SCENERY, "cut") { player, node ->
            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to cut this Cactus...")
                return@on true
            }

            var failed = false
            if (RandomFunction.random(3) == 1) {
                failed = true
                sendMessage(player, "You fail to cut the cactus correctly and it gives you no water this time.")
            }
            if (!failed) {
                val remove: Item = getWaterSkin(player)!!
                if (!removeItem(player, remove)) {
                    addItem(player, remove.id - 2)
                    sendMessage(player, "You top up your skin with water from the cactus.")
                } else {
                    sendMessage(player, "You have no empty waterskins to put the water in.")
                }
            }

            lock(player, 3)
            animate(player, ANIMATION)
            if (!failed) {
                rewardXP(player, Skills.WOODCUTTING, 10.0)
            }
            replaceScenery(
                node.asScenery(),
                DRY_CACTUS,
                SPAWN_DELAY + RandomFunction.random(getLocalPlayers(player).size / 2),
            )
            return@on true
        }
    }

    fun getWaterSkin(player: Player): Item? {
        for (item in WATER_SKINS) {
            if (player.inventory.containsItem(item)) {
                return item
            }
        }
        return null
    }

    companion object {
        val WATER_SKINS: Array<Item> = arrayOf(Item(1825), Item(1827), Item(1829), Item(1831))
        val ANIMATION: Animation = Animation(911)
        const val DRY_CACTUS: Int = 2671
        const val SPAWN_DELAY: Int = 45
        private const val CACTUS = Scenery.KHARIDIAN_CACTUS_HEALTHY_2670
        private const val KNIFE = Items.KNIFE_946
    }
}
