package content.global.skill.crafting.pottery

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class FireOptionPlugin : InteractionListener {

    private val sceneryID = intArrayOf(Scenery.POTTERY_OVEN_2643, Scenery.POTTERY_OVEN_4308, Scenery.POTTERY_OVEN_11601, Scenery.POTTERY_OVEN_34802)
    private val rangeID = intArrayOf(Scenery.COOKING_RANGE_114, Scenery.RANGE_2728, Scenery.RANGE_2729, Scenery.RANGE_2730, Scenery.RANGE_2731, Scenery.COOKING_RANGE_2859, Scenery.RANGE_3039, Scenery.COOKING_RANGE_4172, Scenery.COOKING_RANGE_5275, Scenery.COOKING_RANGE_8750, Scenery.RANGE_9682, Scenery.RANGE_12102, Scenery.RANGE_14919, Scenery.COOKING_RANGE_16893, Scenery.RANGE_21792, Scenery.COOKING_RANGE_22154, Scenery.RANGE_22713, Scenery.RANGE_22714, Scenery.RANGE_24283, Scenery.RANGE_24284, Scenery.RANGE_25730, Scenery.RANGE_33500, Scenery.COOKING_RANGE_34410, Scenery.RANGE_34495, Scenery.RANGE_34546, Scenery.COOKING_RANGE_34565, Scenery.RANGE_36973, Scenery.RANGE_37629)

    override fun defineListeners() {

        /*
         * Handles interaction with fire ovens.
         */

        on(sceneryID, IntType.SCENERY, "fire") { player, _ ->
            sendSkillDialogue(player) {
                val potteryMap = mapOf(
                    Items.UNFIRED_POT_1787 to Pottery.POT,
                    Items.UNFIRED_PIE_DISH_1789 to Pottery.DISH,
                    Items.UNFIRED_BOWL_1791 to Pottery.BOWL,
                    Items.UNFIRED_PLANT_POT_5352 to Pottery.PLANT,
                    Items.UNFIRED_POT_LID_4438 to Pottery.LID
                )

                withItems(*potteryMap.keys.toIntArray())

                create { selectedItemId, amount ->
                    val pottery = potteryMap[selectedItemId]
                    if (pottery != null) {
                        player.pulseManager.run(
                            FirePotteryPulse(
                                player = player,
                                node = pottery.unfinished,
                                pottery = pottery,
                                amount = amount,
                            )
                        )
                    }
                }

                calculateMaxAmount { amount ->
                    amountInInventory(player, amount)
                }
            }
            return@on true
        }

        /*
         * Handles interacting with the cooking range.
         */

        on(rangeID, IntType.SCENERY, "fire") { player, node ->
            if (inInventory(player, Items.UNCOOKED_STEW_2001, 1)) {
                faceLocation(player, node.location)
                openDialogue(player, 43989, Items.UNCOOKED_STEW_2001, "stew")
            }
            return@on true
        }
    }
}