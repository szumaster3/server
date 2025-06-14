package content.minigame.barrows.plugin

import core.api.announceIfRare
import core.api.utils.BossKillCounter
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.component.Component
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items

object RewardChest {
    private val REGULAR_DROPS =
        WeightBasedTable.create(
            WeightedItem(Items.COINS_995, 1, 777, 380.0),
            WeightedItem(Items.MIND_RUNE_558, 250, 350, 125.0),
            WeightedItem(Items.CHAOS_RUNE_562, 115, 135, 125.0),
            WeightedItem(Items.DEATH_RUNE_560, 70, 85, 125.0),
            WeightedItem(Items.BLOOD_RUNE_565, 35, 45, 125.0),
            WeightedItem(Items.BOLT_RACK_4740, 35, 40, 125.0),
            WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 3.0),
            WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 3.0),
            WeightedItem(Items.DRAGON_MED_HELM_1149, 1, 1, 1.0),
        )
    private val AHRIM = arrayOf(4708, 4710, 4712, 4714)
    private val DHAROK = arrayOf(4716, 4718, 4720, 4722)
    private val GUTHAN = arrayOf(4724, 4726, 4728, 4730)
    private val KARIL = arrayOf(4732, 4734, 4736, 4738)
    private val TORAG = arrayOf(4745, 4747, 4749, 4751)
    private val VERAC = arrayOf(4753, 4755, 4757, 4759)
    private val BARROWS_DROP_IDS = arrayOf(AHRIM, DHAROK, GUTHAN, KARIL, TORAG, VERAC)

    @JvmStatic
    fun reward(player: Player) {
        var barrowsRewardsIDs: MutableList<Int> = ArrayList()
        for (i in 0..5) {
            if (player.savedData.activityData.barrowBrothers[i]) {
                barrowsRewardsIDs.addAll(BARROWS_DROP_IDS[i])
            }
        }
        barrowsRewardsIDs.shuffle()

        var rewards: MutableList<Item> = ArrayList()
        val nKilled = barrowsRewardsIDs.size / 4
        roll@ for (i in 0 until nKilled + 1) {
            if (barrowsRewardsIDs.size > 0 && RandomFunction.roll(450 - 58 * nKilled)) {
                val reward = barrowsRewardsIDs[0]
                rewards.add(Item(reward, 1))
                barrowsRewardsIDs.removeAt(0)
            } else {
                val drop = REGULAR_DROPS.roll(null, 1)[0]
                for (i in 0 until rewards.size) {
                    if (rewards[i].id == drop.id) {
                        rewards[i].amount += drop.amount
                        continue@roll
                    }
                }
                rewards.add(drop)
            }
        }

        InterfaceContainer.generateItems(player, rewards.toTypedArray(), arrayOf("Examine"), 364, 4, 3, 4)
        player.interfaceManager.open(Component(Components.TRAIL_REWARD_364))
        BossKillCounter.addToBarrowsCount(player)
        for (item in rewards) {
            announceIfRare(player, item)
            if (!player.inventory.add(item)) {
                GroundItemManager.create(item, player)
            }
        }
    }
}
