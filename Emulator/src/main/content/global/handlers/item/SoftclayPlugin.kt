package content.global.handlers.item

import core.api.sendMessages
import core.api.submitIndividualPulse
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class SoftclayPlugin : UseWithHandler(Items.CLAY_434) {
    private val clayId = Item(Items.CLAY_434)
    private val softClay = Item(Items.SOFT_CLAY_1761)
    private val bowlOfWater = Item(Items.BOWL_OF_WATER_1921)
    private val bowlId = Item(Items.BOWL_1923)
    private val bucketId = Item(Items.BUCKET_1925)
    private val bucketOfWater = Item(Items.BUCKET_OF_WATER_1929)
    private val jugId = Item(Items.JUG_1935)
    private val jugOfWater = Item(Items.JUG_OF_WATER_1937)

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(bowlOfWater.id, ITEM_TYPE, this)
        addHandler(bucketOfWater.id, ITEM_TYPE, this)
        addHandler(jugOfWater.id, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val handler =
            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, softClay) {
                override fun create(
                    amount: Int,
                    index: Int,
                ) {
                    submitIndividualPulse(
                        player,
                        object : Pulse(2, player) {
                            var count = 0

                            override fun pulse(): Boolean {
                                if (!this@SoftclayPlugin.create(player, event)) {
                                    return true
                                }
                                return ++count >= amount
                            }
                        },
                    )
                }

                override fun getAll(index: Int): Int {
                    return player.inventory.getAmount(clayId)
                }
            }
        if (player.inventory.getAmount(clayId) == 1) {
            create(player, event)
        } else {
            handler.open()
        }
        return true
    }

    private fun create(
        player: Player,
        event: NodeUsageEvent,
    ): Boolean {
        var removeItem: Item? = null
        var returnItem: Item? = null
        if (event.usedItem.id == Items.BUCKET_OF_WATER_1929 || event.baseItem.id == Items.BUCKET_OF_WATER_1929) {
            removeItem = bucketOfWater
            returnItem = bucketId
        }
        if (event.usedItem.id == Items.BOWL_OF_WATER_1921 || event.baseItem.id == Items.BOWL_OF_WATER_1921) {
            removeItem = bowlOfWater
            returnItem = bowlId
        }
        if (event.usedItem.id == Items.JUG_OF_WATER_1937 || event.baseItem.id == Items.JUG_OF_WATER_1937) {
            removeItem = jugOfWater
            returnItem = jugId
        }

        if (player.inventory.containsItem(clayId) && player.inventory.containsItem(removeItem)) {
            player.inventory.remove(removeItem)
            player.inventory.remove(clayId)
            sendMessages(player, "You mix the clay and water.", "You now have some soft, workable clay.")
            player.inventory.add(softClay)
            player.inventory.add(returnItem)
            return true
        } else {
            return false
        }
    }
}
