package content.global.plugin.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class WaterSourcePlugin : InteractionListener {

    companion object {
        private val waterSources = intArrayOf(Scenery.FOUNTAIN_153, Scenery.WELL_8927, Scenery.WELL_884, Scenery.WELL_878, Scenery.WELL_8747, Scenery.WELL_6549, Scenery.WELL_6097, Scenery.WELL_4005, Scenery.WELL_4004, Scenery.WELL_35671, Scenery.WELL_34576, Scenery.WELL_3359, Scenery.WELL_3305, Scenery.WELL_32024, Scenery.WELL_32023, Scenery.WELL_31359, Scenery.WELL_26945, Scenery.WELL_24166, Scenery.WELL_12897, Scenery.WELL_12201, Scenery.WELL_11793, Scenery.WATER_PUMP_15938, Scenery.WATER_PUMP_15937, Scenery.WATER_PUMP_15936, Scenery.WATERPUMP_6827, Scenery.WATERPUMP_34577, Scenery.WATERPUMP_24160, Scenery.WASHBASIN_14918, Scenery.SINK_9684, Scenery.SINK_9143, Scenery.SINK_874, Scenery.SINK_873, Scenery.SINK_8699, Scenery.SINK_6151, Scenery.SINK_4063, Scenery.SINK_40063, Scenery.SINK_37155, Scenery.SINK_37154, Scenery.SINK_36971, Scenery.SINK_35762, Scenery.SINK_34566, Scenery.SINK_34547, Scenery.SINK_34496, Scenery.SINK_34411, Scenery.SINK_34082, Scenery.SINK_33458, Scenery.SINK_29105, Scenery.SINK_26966, Scenery.SINK_25929, Scenery.SINK_25729, Scenery.SINK_24314, Scenery.SINK_24112, Scenery.SINK_22715, Scenery.SINK_20358, Scenery.SINK_16705, Scenery.SINK_16704, Scenery.SINK_15678, Scenery.SINK_14917, Scenery.SINK_14868, Scenery.SINK_13564, Scenery.SINK_13563, Scenery.SINK_12974, Scenery.SINK_12279, Scenery.SINK_10175, Scenery.PUMP_AND_TUB_13561, Scenery.PUMP_AND_DRAIN_23920, Scenery.PUMP_AND_DRAIN_13559, Scenery.LARGE_GEYSER_21355, Scenery.FOUNTAIN_880, Scenery.FOUNTAIN_879, Scenery.FOUNTAIN_6232, Scenery.FOUNTAIN_36781, Scenery.FOUNTAIN_34579, Scenery.FOUNTAIN_30820, Scenery.FOUNTAIN_30223, Scenery.FOUNTAIN_28662, Scenery.FOUNTAIN_2864, Scenery.FOUNTAIN_24265, Scenery.FOUNTAIN_24214, Scenery.FOUNTAIN_24161, Scenery.FOUNTAIN_22973, Scenery.FOUNTAIN_21764, Scenery.FOUNTAIN_11759, Scenery.FOUNTAIN_11007, Scenery.FOUNTAIN_10827, Scenery.FOUNTAIN_10437, Scenery.FOUNTAIN_10436, Scenery.FAIRY_FOUNTAIN_12089, Scenery.CARVED_FOUNTAIN_35469, 6249, 3264, 16302, 11661)
    }

    override fun defineListeners() {

        /*
         * Handles water sources.
         */

        onUseWith(IntType.SCENERY, WaterVessel.getInputs(), *waterSources) { player, used, with ->
            val vessel = WaterVessel.forId(used.id) ?: return@onUseWith false
            if (with.name.contains("well", ignoreCase = true) && !vessel.wellable) {
                sendMessage(
                    player,
                    formatMsgText(used.name, "If I drop my @ down there, I don't think I'm likely to get it back."),
                )
                return@onUseWith true
            }
            if (vessel == WaterVessel.BUCKET && with.id == 11661) {
                if (!player.achievementDiaryManager
                        .getDiary(DiaryType.FALADOR)!!
                        .isComplete(0, 7)
                ) {
                    player.achievementDiaryManager.getDiary(DiaryType.FALADOR)!!.updateTask(player, 0, 7, true)
                }
            }
            submitIndividualPulse(
                player,
                object : Pulse(1) {
                    override fun pulse(): Boolean {
                        if (removeItem(player, used.id)) {
                            animate(player, Animations.MULTI_TAKE_832)
                            sendMessage(player, formatMsgText(used.name, vessel.fillMsg))
                            addItemOrDrop(player, vessel.output)
                        }
                        return !vessel.autofill || amountInInventory(player, used.id) == 0
                    }
                },
            )
            return@onUseWith true
        }
    }

    private fun formatMsgText(name: String, template: String, ): String {
        val sb = StringBuilder()
        val templateChars = template.toCharArray()
        val nameChars = name.toCharArray()
        for (tc in templateChars) {
            if (tc == '@') {
                for (nc in nameChars) {
                    if (nc == '(') {
                        break
                    } else {
                        sb.append(nc.lowercaseChar())
                    }
                }
            } else {
                sb.append(tc)
            }
        }
        return sb.toString()
    }

    internal enum class WaterVessel(val inputs: IntArray, val output: Int, val wellable: Boolean = false, val autofill: Boolean = true, val fillMsg: String = "You fill the @.", ) {
        BUCKET(intArrayOf(Items.BUCKET_1925), Items.BUCKET_OF_WATER_1929, true),
        VIAL(intArrayOf(Items.VIAL_229), Items.VIAL_OF_WATER_227),
        JUG(intArrayOf(Items.JUG_1935), Items.JUG_OF_WATER_1937),
        BOWL(intArrayOf(Items.BOWL_1923), Items.BOWL_OF_WATER_1921),
        WATERING_CAN(intArrayOf(Items.WATERING_CAN_5331, Items.WATERING_CAN1_5333, Items.WATERING_CAN2_5334, Items.WATERING_CAN3_5335, Items.WATERING_CAN4_5336, Items.WATERING_CAN5_5337, Items.WATERING_CAN6_5338, Items.WATERING_CAN7_5339), Items.WATERING_CAN8_5340,),
        WATER_SKIN(intArrayOf(Items.WATERSKIN0_1831, Items.WATERSKIN1_1829, Items.WATERSKIN2_1827, Items.WATERSKIN3_1825), Items.WATERSKIN4_1823),
        FISHBOWL(intArrayOf(Items.FISHBOWL_6667), Items.FISHBOWL_6668),
        ;

        companion object {
            private val itemMap = HashMap<Int, WaterVessel>()

            init {
                for (entry in values()) {
                    entry.inputs.forEach { itemMap[it] = entry }
                }
            }

            fun getInputs(): IntArray = itemMap.keys.toIntArray()

            fun forId(id: Int): WaterVessel? = itemMap[id]
        }
    }
}
