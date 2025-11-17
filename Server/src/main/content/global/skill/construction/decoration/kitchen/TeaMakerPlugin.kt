package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

class TeaMakerPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles add leaves to teapot.
         */

        onUseWith(IntType.ITEM, Items.TEA_LEAVES_7738, *TEAPOT_IDS.toIntArray()) { player, used, with ->
            val teapot = with.asItem()
            val teaLeaves = used.asItem()
            if (teaLeaves.id != Items.TEA_LEAVES_7738) return@onUseWith false
            if (teapot.id !in TEAPOT_IDS) return@onUseWith false
            if (!removeItem(player, teaLeaves)) return@onUseWith false

            replaceSlot(player, teapot.slot, Item(teapot.id - 2))
            sendMessage(player, "You add the leaves to the teapot.")
            return@onUseWith true
        }

        /*
         * Handles pour water into teapot.
         */

        onUseWith(IntType.ITEM, Items.HOT_KETTLE_7691, *TEAPOT_LEAVES_IDS.toIntArray()) { player, used, with ->
            val kettle = used.asItem()
            val teapot = with.asItem()

            if (kettle.id != Items.HOT_KETTLE_7691) return@onUseWith false
            val newTea = TEAPOT_TO_POT_MAP[teapot.id] ?: return@onUseWith false

            replaceSlot(player, kettle.slot, Item(Items.KETTLE_7688))
            replaceSlot(player, teapot.slot, Item(newTea))
            sendMessage(player, "You pour the water into the teapot.")
            return@onUseWith true
        }

        /*
         * Handles pour tea into cup.
         */

        onUseWith(IntType.ITEM, TEAPOT_PROGRESS_MAP.keys.toIntArray(), *EMPTY_CUP_IDS.toIntArray()) { player, used, with ->
            val teapot = used.asItem()
            val cup = with.asItem()

            if (cup.id == Items.TEA_FLASK_10859) {
                sendMessage(player, "You cannot do that.")
                return@onUseWith false
            }

            if (getStatLevel(player, Skills.COOKING) < 20) {
                sendDialogue(player, "You need a Cooking level of 20 to do that.")
                return@onUseWith false
            }

            val nextState = TEAPOT_PROGRESS_MAP[teapot.id]
            if (nextState == null) {
                sendMessage(player, "The teapot is empty.")
                return@onUseWith true
            }

            replaceSlot(player, teapot.slot, Item(nextState))
            replaceSlot(player, cup.slot, Item(nextCupId(cup.id)))
            sendMessage(player, "You pour some tea.")
            rewardXP(player, Skills.COOKING, 52.0)
            return@onUseWith true
        }


        /*
         * Handles add milk to cup of tea.
         */

        onUseWith(IntType.ITEM, Items.BUCKET_OF_MILK_1927, *CUP_OF_TEA_IDS.toIntArray()) { player, used, with ->
            val bucket = used.asItem()
            val cup = with.asItem()

            val milkTea = TEA_WITH_MILK_MAP[cup.id] ?: return@onUseWith false
            if (!inInventory(player, Items.BUCKET_OF_MILK_1927)) return@onUseWith false
            replaceSlot(player, bucket.slot, Item(Items.BUCKET_1925))
            replaceSlot(player, cup.slot, Item(milkTea))
            return@onUseWith true
        }

        /*
         * Handles fill kettle from sink.
         */

        onUseWith(IntType.SCENERY, KETTLE_IDS.toIntArray(), *SINK_IDS.toIntArray()) { player, used, with ->
            if (!player.houseManager.isBuildingMode) {
                sendMessage(player, "You cannot do this in building mode.")
                return@onUseWith false
            }
            if (used.id != Items.KETTLE_7688) {
                sendMessage(player, "You need an empty kettle to fill it.")
                return@onUseWith false
            }
            val scenery = with.asScenery()
            lock(player, 7)
            animate(player, Animations.GRAB_AND_HOLDING_ONTO_SOMETHING_BIG_3622)
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        1 -> {
                            animate(player, Animations.KETTLE_3625)
                            replaceScenery(scenery, with.id + 1, 4)
                            animateScenery(scenery, 3720)
                        }
                        6 -> {
                            sendMessage(player, "You fill the kettle from the sink.")
                            replaceSlot(player, used.asItem().slot, Item(Items.FULL_KETTLE_7690))
                            animate(player, Animations.LET_GO_OF_SOMETHING_BIG_3623)
                            return true
                        }
                    }
                    return false
                }
            })
            return@onUseWith true
        }
    }

    companion object {
        private val KETTLE_IDS = setOf(Items.KETTLE_7688, Items.FULL_KETTLE_7690, Items.HOT_KETTLE_7691)
        private val SINK_IDS = setOf(Scenery.PUMP_AND_DRAIN_13559, Scenery.PUMP_AND_TUB_13561, Scenery.SINK_13563)
        private val TEAPOT_IDS = setOf(Items.TEAPOT_7702, Items.TEAPOT_7714, Items.TEAPOT_7726)
        private val TEAPOT_LEAVES_IDS = setOf(Items.TEAPOT_WITH_LEAVES_7700, Items.TEAPOT_WITH_LEAVES_7712, Items.TEAPOT_WITH_LEAVES_7724)
        private val EMPTY_CUP_IDS = setOf(Items.EMPTY_CUP_7728, Items.PORCELAIN_CUP_7732, Items.PORCELAIN_CUP_7735)
        private val CUP_OF_TEA_IDS = setOf(Items.CUP_OF_TEA_7730, Items.CUP_OF_TEA_7733, Items.CUP_OF_TEA_7736)

        private val TEAPOT_TO_POT_MAP = mapOf(
            Items.TEAPOT_WITH_LEAVES_7700 to Items.POT_OF_TEA_4_7692,
            Items.TEAPOT_WITH_LEAVES_7712 to Items.POT_OF_TEA_4_7704,
            Items.TEAPOT_WITH_LEAVES_7724 to Items.POT_OF_TEA_4_7716
        )

        private val TEAPOT_PROGRESS_MAP = mapOf(
            Items.POT_OF_TEA_4_7692 to Items.POT_OF_TEA_3_7694,
            Items.POT_OF_TEA_3_7694 to Items.POT_OF_TEA_2_7696,
            Items.POT_OF_TEA_2_7696 to Items.POT_OF_TEA_1_7698,
            Items.POT_OF_TEA_4_7704 to Items.POT_OF_TEA_3_7706,
            Items.POT_OF_TEA_3_7706 to Items.POT_OF_TEA_2_7708,
            Items.POT_OF_TEA_2_7708 to Items.POT_OF_TEA_1_7710,
            Items.POT_OF_TEA_4_7716 to Items.POT_OF_TEA_3_7718,
            Items.POT_OF_TEA_3_7718 to Items.POT_OF_TEA_2_7720,
            Items.POT_OF_TEA_2_7720 to Items.POT_OF_TEA_1_7722
        )

        private val TEA_WITH_MILK_MAP = mapOf(
            Items.CUP_OF_TEA_7730 to Items.CUP_OF_TEA_7731,
            Items.CUP_OF_TEA_7733 to Items.CUP_OF_TEA_7734,
            Items.CUP_OF_TEA_7736 to Items.CUP_OF_TEA_7737
        )

        private fun nextCupId(cupId: Int): Int = when(cupId) {
            Items.EMPTY_CUP_7728 -> cupId + 2
            Items.PORCELAIN_CUP_7732, Items.PORCELAIN_CUP_7735 -> cupId + 1
            else -> cupId
        }
    }
}
