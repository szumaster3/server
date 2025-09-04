package content.global.skill.crafting.items.weapon

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import shared.consts.Items
import shared.consts.Sounds
import kotlin.math.min

class BattlestaveMakePlugin : InteractionListener {
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
                    playAudio(player, Sounds.ATTACH_ORB_2585)
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
                            playAudio(player, Sounds.ATTACH_ORB_2585)
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

/**
 * Represents the different types of battlestaffs that can be crafted.
 */
private enum class Battlestaff(val required: Int, val productId: Int, val amount: Int = 1, val requiredLevel: Int, val experience: Double) {
    WATER_BATTLESTAFF(Items.WATER_ORB_571, Items.WATER_BATTLESTAFF_1395, requiredLevel = 54, experience = 100.0),
    EARTH_BATTLESTAFF(Items.EARTH_ORB_575, Items.EARTH_BATTLESTAFF_1399, requiredLevel = 58, experience = 112.5),
    FIRE_BATTLESTAFF(Items.FIRE_ORB_569, Items.FIRE_BATTLESTAFF_1393, requiredLevel = 62, experience = 125.0),
    AIR_BATTLESTAFF(Items.AIR_ORB_573, Items.AIR_BATTLESTAFF_1397, requiredLevel = 66, experience = 137.5), ;

    companion object {
        private val requiredMap: Map<Int, Battlestaff> = values().associateBy { it.required }

        /**
         * Finds battlestaff by orb item id.
         */
        @JvmStatic
        fun forId(itemId: Int): Battlestaff? = requiredMap[itemId]
    }
}