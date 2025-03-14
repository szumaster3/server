package core.game.bots

import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Items

class SkillingBotAssembler {
    fun produce(
        type: Wealth,
        loc: Location,
    ): AIPlayer {
        return assembleBot(AIPlayer(loc), type)
    }

    fun assembleBot(
        bot: AIPlayer,
        type: Wealth,
    ): AIPlayer {
        return when (type) {
            Wealth.TUTORIAL -> equipSet(bot, TUTORIALSETS.random())
            Wealth.POOR -> equipSet(bot, POORSETS.random())
            Wealth.AVERAGE -> equipSet(bot, AVGSETS.random())
            Wealth.RICH -> equipSet(bot, RICHSETS.random())
        }
    }

    fun equipSet(
        bot: AIPlayer,
        set: List<Int>,
    ): AIPlayer {
        for (i in set) {
            val item = Item(i)
            val configs = item.definition.handlers
            val slot = configs["equipment_slot"] ?: continue
            if (bot.inventory.get(slot as Int) == null) {
                bot.equipment.add(item, slot as Int, false, false)
            }
            val reqs = configs["requirements"]
            if (reqs != null) {
                for (req in configs["requirements"] as HashMap<Int, Int>) {
                    bot.skills.setStaticLevel(req.key, req.value)
                }
            }
        }
        bot.skills.updateCombatLevel()
        return bot
    }

    enum class Wealth {
        TUTORIAL,
        POOR,
        AVERAGE,
        RICH,
    }

    val TUTORIALSETS =
        arrayOf(
            listOf(-1),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
        )

    val POORSETS =
        arrayOf(
            listOf(Items.MONKS_ROBE_542, Items.MONKS_ROBE_544),
            listOf(Items.BLACK_ROBE_581),
            listOf(Items.CAMO_TOP_6654, Items.CAMO_BOTTOMS_6655, Items.CAMO_HELMET_6656),
            listOf(Items.CAMO_TOP_6654, Items.CAMO_HELMET_6656),
            listOf(Items.ROBE_TOP_636, Items.ROBE_BOTTOMS_646),
            listOf(Items.ROBE_TOP_638, Items.ROBE_BOTTOMS_648),
            listOf(),
            listOf(),
            listOf(),
        )

    val AVGSETS =
        arrayOf(
            listOf(Items.BROWN_HEADBAND_2649, Items.DHIDE_BODY_G_7374, Items.DHIDE_CHAPS_G_7378),
            listOf(Items.WIZARD_HAT_G_7394, Items.MONKS_ROBE_542, Items.MONKS_ROBE_544),
            listOf(Items.CAMO_TOP_6654, Items.CAMO_BOTTOMS_6655, Items.CAMO_HELMET_6656),
            listOf(Items.LEDERHOSEN_HAT_6182, Items.LEDERHOSEN_TOP_6180, Items.LEDERHOSEN_SHORTS_6181),
            listOf(Items.SKELETON_LEGGINGS_9923, Items.SKELETON_SHIRT_9924, Items.SKELETON_MASK_9925),
            listOf(Items.BLACK_ELE_SHIRT_10400, Items.BLACK_ELE_LEGS_10402, Items.BROWN_HEADBAND_2649),
            listOf(Items.RED_ELE_SHIRT_10404, Items.RED_ELE_LEGS_10406),
            listOf(Items.COMBAT_ROBE_TOP_100_12971, Items.COMBAT_ROBE_BOTTOM_100_12978),
        )

    val RICHSETS =
        arrayOf(
            listOf(Items.THIRD_AGE_RANGE_TOP_10330, Items.THIRD_AGE_RANGE_LEGS_10332, Items.KARILS_COIF_4732),
            listOf(Items.BATTLE_ROBE_TOP_100_12873, Items.BATTLE_ROBE_BOTTOM_100_12880, Items.PURPLE_PARTYHAT_1046),
            listOf(Items.ZURIELS_ROBE_TOP_13858, Items.ZURIELS_ROBE_BOTTOM_13861, Items.ZURIELS_HOOD_13864),
            listOf(Items.STATIUS_PLATEBODY_13884, Items.STATIUS_PLATELEGS_13890, Items.STATIUS_FULL_HELM_13896),
            listOf(Items.GILDED_PLATEBODY_3481, Items.GILDED_PLATELEGS_3483, Items.WIZARD_HAT_G_7394),
            listOf(Items.ZAMORAK_PLATEBODY_2653, Items.ZAMORAK_PLATELEGS_2655),
            listOf(Items.SARADOMIN_PLATEBODY_2661, Items.SARADOMIN_PLATELEGS_2663),
            listOf(Items.BLACK_PLATEBODY_G_2591, Items.BLACK_PLATELEGS_G_2593, Items.GILDED_FULL_HELM_3486),
            listOf(Items.ELITE_BLACK_PLATELEGS_14490, Items.ELITE_BLACK_PLATEBODY_14492),
        )
}
