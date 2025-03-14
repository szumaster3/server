package content.global.skill.crafting.items.weapon

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import kotlin.math.min

class BattlestaffListener : InteractionListener {
    private val BATTLESTAFF_ID = Items.BATTLESTAFF_1391
    private val ORB_ID = Battlestaff.values().map { it.required }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, ORB_ID, BATTLESTAFF_ID) { player, used, with ->
            val product = Battlestaff.forId(used.id) ?: return@onUseWith true

            if (!hasLevelDyn(player, Skills.CRAFTING, product.requiredLevel)) {
                sendMessage(player, "You need a crafting level of ${product.requiredLevel} to make this.")
                return@onUseWith true
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, product.required) && removeItem(player, BATTLESTAFF_ID)) {
                    addItem(player, product.productId, product.amount)
                    rewardXP(player, Skills.CRAFTING, product.experience)
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(product.productId)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask

                        if (removeItem(player, product.required) && removeItem(player, BATTLESTAFF_ID)) {
                            addItem(player, product.productId)
                            rewardXP(player, Skills.CRAFTING, product.experience)
                        }

                        if (product.productId == Items.AIR_BATTLESTAFF_1397) {
                            finishDiaryTask(player, DiaryType.VARROCK, 2, 6)
                        } else {
                            return@runTask
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }
    }
}
