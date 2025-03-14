package content.global.skill.cooking.handlers

import content.global.skill.cooking.data.DairyProduct
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items

class DairyChurnPulse(
    player: Player?,
    item: Item?,
    product: DairyProduct,
    amount: Int,
) : SkillPulse<Item?>(player, item) {
    private val dairyProduct: DairyProduct
    private var amount: Int

    init {
        super.setDelay(8)
        this.amount = amount
        this.dairyProduct = product
    }

    override fun checkRequirements(): Boolean {
        player.interfaceManager.closeChatbox()
        var hasAnyInput = false
        for (input in dairyProduct.inputs) {
            if (inInventory(player, input!!.id)) {
                hasAnyInput = true
                node = input
                break
            }
        }
        if (!hasAnyInput) {
            sendMessage(player, "You need a bucket of milk.")
            return false
        }
        if (getStatLevel(player, Skills.COOKING) < dairyProduct.level) {
            sendMessage(player, "You need a cooking level of ${dairyProduct.level} to cook this.")
            return false
        }
        if (amount > player.inventory.getAmount(node)) {
            amount = player.inventory.getAmount(node)
        }
        if (amount < 1) {
            return false
        }
        animate()
        return true
    }

    override fun animate() {
        animate(player, Animations.CHURN_BUTTER_2793)
    }

    override fun reward(): Boolean {
        amount--
        for (input in dairyProduct.inputs) {
            if (removeItem(player, input)) {
                addItem(player, dairyProduct.product.id)
                if (input?.id == Items.BUCKET_OF_MILK_1927) {
                    if (!addItem(player, Items.BUCKET_OF_MILK_1927, 1)) {
                        GroundItemManager.create(Item(Items.BUCKET_1925, 1), player)
                    }
                }
                player.dispatch(
                    ResourceProducedEvent(dairyProduct.product.id, amount, node!!, Items.BUCKET_OF_MILK_1927),
                )
                sendMessage(
                    player = player,
                    message = "You make ${if (StringUtils.isPlusN(
                            dairyProduct.product.name.lowercase(),
                        )
                    ) {
                        "an"
                    } else {
                        "a"
                    }} ${dairyProduct.product.name.lowercase()}.",
                )
                rewardXP(player, Skills.COOKING, dairyProduct.experience)
                break
            }
        }
        return amount < 1
    }
}
