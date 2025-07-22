package content.global.skill.construction.decoration.kitchen

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class TeaMakerPlugin : InteractionListener {

    companion object {
        private val KETTLE_IDS = intArrayOf(Items.KETTLE_7688, Items.FULL_KETTLE_7690, Items.HOT_KETTLE_7691)
        private val SINK_IDS = intArrayOf(Scenery.PUMP_AND_DRAIN_13559, Scenery.PUMP_AND_TUB_13561, Scenery.SINK_13563)
        private val TEAPOT_IDS = intArrayOf(Items.TEAPOT_7702, Items.TEAPOT_7714, Items.TEAPOT_7726)
        private val TEAPOT_LEAVES_IDS = intArrayOf(Items.TEAPOT_WITH_LEAVES_7700, Items.TEAPOT_WITH_LEAVES_7712, Items.TEAPOT_WITH_LEAVES_7724)
        private val EMPTY_CUP_IDS = intArrayOf(Items.EMPTY_CUP_7728, Items.PORCELAIN_CUP_7732, Items.PORCELAIN_CUP_7735)
        private val CUP_OF_TEA_IDS = intArrayOf(Items.CUP_OF_TEA_7730, Items.CUP_OF_TEA_7733, Items.CUP_OF_TEA_7736)

        // Teapot with leaves to Pot of tea (4)
        private val TEAPOT_TO_POT_MAP = mapOf(
            Items.TEAPOT_WITH_LEAVES_7700 to Items.POT_OF_TEA_4_7692,
            Items.TEAPOT_WITH_LEAVES_7712 to Items.POT_OF_TEA_4_7704,
            Items.TEAPOT_WITH_LEAVES_7724 to Items.POT_OF_TEA_4_7716
        )

        // Pot of tea to next tea.
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

        // Cup of tea with milk.
        private val TEA_WITH_MILK_MAP = mapOf(
            Items.CUP_OF_TEA_7730 to Items.CUP_OF_TEA_7731,
            Items.CUP_OF_TEA_7733 to Items.CUP_OF_TEA_7734,
            Items.CUP_OF_TEA_7736 to Items.CUP_OF_TEA_7737
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.TEA_LEAVES_7738, *TEAPOT_IDS) { player, used, with ->
            val usedWith = with.asItem()
            if (inInventory(player, Items.TEA_LEAVES_7738) && anyInInventory(player, *TEAPOT_IDS)) {
                replaceSlot(player, usedWith.slot, Item(usedWith.id - 2))
                sendMessage(player, "You add the leaves to the teapot.")
                removeItem(player, Items.TEA_LEAVES_7738)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.HOT_KETTLE_7691, *TEAPOT_LEAVES_IDS) { player, used, with ->
            val usedWith = with.asItem()
            val newTeaPot = TEAPOT_TO_POT_MAP[usedWith.id]
            if (newTeaPot != null) {
                replaceSlot(player, used.asItem().slot, Item(Items.KETTLE_7688))
                replaceSlot(player, usedWith.slot, Item(newTeaPot))
                sendMessage(player, "You pour the water into the teapot.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, TEAPOT_PROGRESS_MAP.keys.toIntArray(), *EMPTY_CUP_IDS) { player, used, with ->
            val usedWith = with.asItem()
            if (usedWith.id == Items.TEA_FLASK_10859) {
                sendMessage(player, "You cannot do that.")
                return@onUseWith false
            }
            if (getStatLevel(player, Skills.COOKING) < 20) {
                sendDialogue(player, "You need a Cooking level of 20 to do that.")
                return@onUseWith false
            }
            val nextState = TEAPOT_PROGRESS_MAP[used.id]
            if (nextState != null) {
                replaceSlot(player, used.asItem().slot, Item(nextState))
                val cup = if (with.id == Items.EMPTY_CUP_7728) with.id + 2 else with.id + 1
                replaceSlot(player, usedWith.slot, Item(cup))
                sendMessage(player, "You pour some tea.")
                rewardXP(player, Skills.COOKING, 52.0)
            } else sendMessage(player, "The teapot is empty.")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.BUCKET_OF_MILK_1927, *CUP_OF_TEA_IDS) { player, used, with ->
            val usedWith = with.asItem()
            val milkTea = TEA_WITH_MILK_MAP[usedWith.id] ?: return@onUseWith false
            if (!inInventory(player, Items.BUCKET_OF_MILK_1927)) return@onUseWith false

            replaceSlot(player, used.asItem().slot, Item(Items.BUCKET_1925))
            replaceSlot(player, usedWith.slot, Item(milkTea))
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, KETTLE_IDS, *SINK_IDS) { player, used, with ->
            if (!player.houseManager.isBuildingMode) {
                sendMessage(player, "You cannot do this in building mode.")
                return@onUseWith false
            }
            if (used.id != Items.KETTLE_7688) {
                sendMessage(player, "You need an empty kettle to fill it.")
                return@onUseWith false
            }
            val scenery = with.asScenery()
            lock(player, 6)
            animate(player, Animations.GRAB_AND_HOLDING_ONTO_SOMETHING_BIG_3622)
            submitIndividualPulse(player, object : Pulse(1) {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            animate(player, Animations.KETTLE_3625)
                            replaceScenery(scenery, with.id + 1, 4)
                            animateScenery(scenery, 3720)
                        }
                        5 -> {
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
}
