package core.game.bots

import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.Items
import java.util.*

/**
 * Assembles combat-oriented bots used for PvM and other activities.
 * Replaces the old `PVMBotBuilder` system.
 *
 * @author Ceikry, Sir Kermit
 */
class CombatBotAssembler {
    /**
     * Represents the combat style of a bot.
     */
    enum class Type { RANGE, MAGE, MELEE }

    /**
     * Represents the general bot strength tiers affecting stats and gear.
     */
    enum class Tier { LOW, MED, HIGH, PURE }

    /**
     * Produces a combat bot of the given type and tier at a specified location.
     *
     * @param type the combat style to use (MELEE, RANGE, or MAGE)
     * @param tier determines the range of skill levels and equipment
     * @param location spawn and respawn point of the bot
     * @return a configured [AIPlayer] instance
     */
    fun produce(type: Type, tier: Tier, location: Location): AIPlayer {
        return when (type) {
            Type.RANGE -> assembleRangedBot(tier, location)
            Type.MELEE -> assembleMeleeBot(tier, location)
            Type.MAGE -> assembleMeleeBot(tier, location)
        }
    }

    /**
     * Builds a ranged combat bot with level-appropriate gear and stats.
     *
     * @param tier defines stat and gear scaling
     * @param location bot spawn location
     * @param crossbow whether to use crossbows instead of bows (optional)
     */
    private fun assembleRangedBot(tier: Tier, location: Location, crossbow: Boolean? = null): CombatBot {
        val bot = CombatBot(location)

        generateStats(bot, tier, Skills.RANGE, Skills.DEFENCE)
        rangeBotGear(bot, (crossbow ?: (Random().nextInt() % 2)) == 0)
        return bot
    }

    /**
     * Builds a melee combat bot with level-appropriate stats and equipment.
     *
     * @param tier defines stat and gear scaling
     * @param location bot spawn location
     */
    private fun assembleMeleeBot(tier: Tier, location: Location): CombatBot {
        val bot = CombatBot(location)
        generateStats(bot, tier, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE)
        meleeBotGear(bot)
        return bot
    }

    /**
     * Builds a melee adventurer bot designed for dragon combat.
     *
     * @param tier defines stat and gear scaling
     * @param location bot spawn location
     */
    fun meleeAdventurer(tier: Tier, location: Location): CombatBot {
        val bot = CombatBot(location)
        var max = 0
        val level = RandomFunction.random(25, 69).also { max = 99 }
        generateStats(bot, tier, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE)
        bot.skills.setStaticLevel(Skills.HITPOINTS, level)
        bot.skills.setStaticLevel(Skills.ATTACK, level + 5)
        bot.skills.setStaticLevel(Skills.STRENGTH, level + 5)
        bot.skills.setLevel(Skills.HITPOINTS, level)
        bot.skills.setLevel(Skills.ATTACK, level + 5)
        bot.skills.setLevel(Skills.STRENGTH, level + 5)
        bot.skills.updateCombatLevel()
        equipHighest(bot, meleeHeadGear)
        equipHighest(bot, meleeTopGear)
        equipHighest(bot, meleeBottomGear)
        equipHighest(bot, meleeWeaponSlotGear)
        equipHighest(bot, meleeShieldSlotGear)
        equipHighest(bot, CAPE)
        equipHighest(bot, NNECK)
        equipHighest(bot, NGLOVES)
        equipHighest(bot, NBOOTS)
        bot.equipment.refresh()
        return bot
    }

    /**
     * Builds a ranged adventurer bot designed for dragon combat.
     *
     * @param tier defines stat and gear scaling
     * @param location bot spawn location
     */
    fun rangeAdventurer(tier: Tier, location: Location): CombatBot {
        val bot = CombatBot(location)
        var max = 0
        val level = RandomFunction.random(35, 69).also { max = 75 }
        generateStats(bot, tier, Skills.ATTACK, Skills.STRENGTH)
        bot.skills.setStaticLevel(Skills.HITPOINTS, level)
        bot.skills.setStaticLevel(Skills.DEFENCE, level)
        bot.skills.setStaticLevel(Skills.RANGE, level + 10)
        bot.skills.setLevel(Skills.HITPOINTS, level)
        bot.skills.setLevel(Skills.DEFENCE, level)
        bot.skills.setLevel(Skills.RANGE, level + 10)
        bot.skills.updateCombatLevel()
        equipHighest(bot, rangeHeadGear, 65)
        equipHighest(bot, rangeTopGear, 65)
        equipHighest(bot, rangeBottomGear, 65)
        equipHighest(bot, crossbows, 50)
        equipHighest(bot, CAPE)
        equipHighest(bot, NRANGENECK)
        equipHighest(bot, NRANGESHIELD)
        equipHighest(bot, pestControlRangeCapeSlot)
        equipHighest(bot, NGLOVES)
        equipHighest(bot, NRBOOTS)
        bot.equipment.add(Item(Items.BRONZE_BOLTS_877, 100000), 13, false, false)
        bot.equipment.refresh()
        return bot
    }

    /**
     * Builds a melee dragon-fighter bot with high-tier equipment.
     *
     * @param tier defines stat and gear scaling
     * @param location bot spawn location
     */
    fun assembleMeleeDragonBot(tier: Tier, location: Location): CombatBot {
        val bot = CombatBot(location)
        generateStats(bot, tier, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE)
        equipHighest(bot, meleeHeadGear, 60)
        equipHighest(bot, meleeTopGear, 60)
        equipHighest(bot, meleeBottomGear, 60)
        equipHighest(bot, meleeWeaponSlotGear, 70)
        equipHighest(bot, CAPE)
        equipHighest(bot, NGLOVES)
        equipHighest(bot, NBOOTS)
        bot.equipment.refresh()
        return bot
    }

    /**
     * Builds a ranged dragon-fighter bot with high-tier equipment.
     *
     * @param tier defines stat and gear scaling
     * @param location bot spawn location
     */
    fun assembleRangeDragonBot(tier: Tier, location: Location): CombatBot {
        val bot = CombatBot(location)
        bot.fullRestore()
        generateStats(bot, tier, Skills.RANGE, Skills.DEFENCE)
        equipHighest(bot, rangeHeadGear, 50)
        equipHighest(bot, rangeTopGear, 50)
        equipHighest(bot, rangeBottomGear, 50)
        equipHighest(bot, crossbows, 50)
        equipHighest(bot, CAPE)
        equipHighest(bot, NGLOVES)
        equipHighest(bot, NRBOOTS)
        bot.equipment.add(Item(Items.BRONZE_BOLTS_877, 100000), 13, false, false)
        bot.equipment.refresh()
        return bot
    }

    /**
     * Equips a ranged bot with the best available gear for its stats.
     *
     * @param bot bot instance to equip
     * @param crossbow whether to prefer crossbows to bows
     */
    private fun rangeBotGear(bot: AIPlayer, crossbow: Boolean? = false) {
        equipHighest(bot, rangeHeadGear)
        equipHighest(bot, rangeTopGear)
        equipHighest(bot, rangeBottomGear)
        equipHighest(bot, CAPE)
        equipHighest(bot, NGLOVES)
        equipHighest(bot, NRBOOTS)
        if (crossbow == true) {
            equipHighest(bot, crossbows); equipHighest(
                bot, meleeShieldSlotGear
            ); bot.equipment.add(Item(Items.BRONZE_BOLTS_877, Integer.MAX_VALUE), 13, false, false)
        } else {
            equipHighest(bot, bows); bot.equipment.add(
                Item(Items.BRONZE_ARROW_882, Integer.MAX_VALUE), 13, false, false
            )
        }
        bot.equipment.refresh()
    }

    /**
     * Equips a melee bot with the best available gear for its stats.
     *
     * @param bot bot instance to equip
     */
    private fun meleeBotGear(bot: AIPlayer) {
        equipHighest(bot, meleeHeadGear)
        equipHighest(bot, meleeBottomGear)
        equipHighest(bot, meleeShieldSlotGear)
        equipHighest(bot, meleeTopGear)
        equipHighest(bot, CAPE)
        equipHighest(bot, NGLOVES)
        equipHighest(bot, NBOOTS)
        equipHighest(bot, meleeWeaponSlotGear)
        bot.equipment.refresh()
    }

    /**
     * Equips and configures a **novice ranged bot** for the Pest Control minigame.
     *
     * @param bot the [AIPlayer] instance to configure
     * @param crossbow if true, equips a crossbow setup instead of a bow
     * @param skills optional skill list to modify (unused, for compatibility)
     */
    fun rangeBotNovice(bot: AIPlayer, crossbow: Boolean? = false, vararg skills: Int) {
        var max = 0
        val level = RandomFunction.random(30, 70).also { max = 75 }
        bot.fullRestore()

        bot.skills.setStaticLevel(Skills.RANGE, 50)
        bot.skills.setStaticLevel(Skills.DEFENCE, 50)
        bot.skills.setStaticLevel(Skills.ATTACK, level)
        bot.skills.setStaticLevel(Skills.STRENGTH, level)
        bot.skills.setStaticLevel(Skills.HITPOINTS, level)
        bot.skills.setLevel(Skills.RANGE, 50)
        bot.skills.setLevel(Skills.DEFENCE, 50)
        bot.skills.setLevel(Skills.ATTACK, level)
        bot.skills.setLevel(Skills.STRENGTH, level)
        bot.skills.setLevel(Skills.HITPOINTS, level)
        bot.skills.updateCombatLevel()
        equipHighest(bot, pestControlRangeHeadGear)
        equipHighest(bot, pestControlRangeTopGear)
        equipHighest(bot, pestControlRangeCapeSlot)
        equipHighest(bot, pestControlRangeBottomGear)
        bot.equipment.refresh()
        equipHighest(bot, NECK)
        equipHighest(bot, GLOVES)
        equipHighest(bot, BOOTS)
        equipHighest(bot, RING_ARCH)
        bot.equipment.refresh()
        if (crossbow == true) {
            equipHighest(bot, crossbows); equipHighest(
                bot, meleeShieldSlotGear
            ); bot.equipment.add(Item(Items.BRONZE_BOLTS_877, Integer.MAX_VALUE), 13, false, false)
        } else {
            equipHighest(bot, bows); bot.equipment.add(
                Item(Items.BRONZE_ARROW_882, Integer.MAX_VALUE), 13, false, false
            )
        }
        bot.skills.setStaticLevel(Skills.RANGE, 99)
        bot.skills.setLevel(Skills.RANGE, 99)
        bot.equipment.refresh()
    }

    /**
     * Equips and configures a **novice melee bot** for the Pest Control minigame.
     *
     * @param bot the [AIPlayer] instance to configure
     * @param skills optional skill list to modify (unused, for compatibility)
     */
    fun meleeBotNovice(bot: AIPlayer, vararg skills: Int) {
        var max = 0
        val initial = RandomFunction.random(30, 75).also { max = 75 }
        var level = initial
        bot.fullRestore()

        bot.skills.setStaticLevel(Skills.STRENGTH, level)
        bot.skills.setStaticLevel(Skills.DEFENCE, level)
        bot.skills.setStaticLevel(Skills.ATTACK, level)
        bot.skills.setStaticLevel(Skills.HITPOINTS, level)
        bot.skills.setStaticLevel(Skills.PRAYER, 70)
        bot.skills.setStaticLevel(Skills.RANGE, 10)
        bot.skills.setStaticLevel(Skills.MAGIC, 10)
        bot.skills.setLevel(Skills.STRENGTH, level)
        bot.skills.setLevel(Skills.DEFENCE, level)
        bot.skills.setLevel(Skills.ATTACK, level)
        bot.skills.setLevel(Skills.HITPOINTS, level)
        bot.skills.setLevel(Skills.PRAYER, 70)
        bot.skills.setLevel(Skills.RANGE, 10)
        bot.skills.setLevel(Skills.MAGIC, 10)
        bot.skills.updateCombatLevel()
        equipHighest(bot, PCMELEE_HELMS)
        equipHighest(bot, PCMELEE_LEG)
        equipHighest(bot, PCMELEE_SHIELD)
        equipHighest(bot, PCMELEE_TOP)
        equipHighest(bot, PCMELEE_WEP)
        bot.equipment.refresh()
        equipHighest(bot, CAPE)
        equipHighest(bot, NECK)
        equipHighest(bot, GLOVES)
        equipHighest(bot, BOOTS)
        equipHighest(bot, RING_BERS)
        bot.equipment.refresh()
        bot.skills.setStaticLevel(Skills.DEFENCE, 70)
        bot.skills.setStaticLevel(Skills.ATTACK, 99)
        bot.skills.setStaticLevel(Skills.STRENGTH, 99)
        bot.skills.setStaticLevel(Skills.HITPOINTS, 80)
        bot.skills.setLevel(Skills.DEFENCE, 70)
        bot.skills.setLevel(Skills.ATTACK, 99)
        bot.skills.setLevel(Skills.STRENGTH, 99)
        bot.skills.setLevel(Skills.HITPOINTS, 80)
        bot.fullRestore()
    }

    /**
     * Equips and configures an **intermediate ranged bot** for the Pest Control minigame.
     *
     * @param bot the [AIPlayer] instance to configure
     * @param crossbow if true, equips a crossbow setup instead of a bow
     * @param skills optional skill list to modify (unused, for compatibility)
     */
    fun rangeBotIntermediate(bot: AIPlayer, crossbow: Boolean? = false, vararg skills: Int) {
        var max = 0
        val level = RandomFunction.random(50, 80).also { max = 99 }
        bot.fullRestore()

        bot.skills.setStaticLevel(Skills.RANGE, level)
        bot.skills.setStaticLevel(Skills.DEFENCE, 80)
        bot.skills.setStaticLevel(Skills.ATTACK, level)
        bot.skills.setStaticLevel(Skills.STRENGTH, level)
        bot.skills.setStaticLevel(Skills.HITPOINTS, 70)
        bot.skills.setStaticLevel(Skills.SUMMONING, 99)
        bot.skills.setLevel(Skills.RANGE, level)
        bot.skills.setLevel(Skills.DEFENCE, 80)
        bot.skills.setLevel(Skills.ATTACK, level)
        bot.skills.setLevel(Skills.STRENGTH, level)
        bot.skills.setLevel(Skills.HITPOINTS, 70)
        bot.skills.setLevel(Skills.SUMMONING, 99)
        bot.skills.updateCombatLevel()
        equipHighest(bot, pestControlRangeHeadGear)
        equipHighest(bot, pestControlRangeTopGear)
        equipHighest(bot, pestControlRangeCapeSlot)
        equipHighest(bot, pestControlRangeBottomGear)
        bot.equipment.refresh()
        equipHighest(bot, GLOVES)
        equipHighest(bot, NECK)
        equipHighest(bot, BOOTS)
        equipHighest(bot, RING_ARCH)
        bot.equipment.refresh()
        if (crossbow == true) {
            equipHighest(bot, crossbows); equipHighest(
                bot, meleeShieldSlotGear
            ); bot.equipment.add(Item(Items.BRONZE_BOLTS_877, Integer.MAX_VALUE), 13, false, false)
        } else {
            equipHighest(bot, bows); bot.equipment.add(
                Item(Items.BRONZE_ARROW_882, Integer.MAX_VALUE), 13, false, false
            )
        }
        bot.skills.setStaticLevel(Skills.RANGE, 99)
        bot.skills.setLevel(Skills.RANGE, 99)
        bot.equipment.refresh()
    }

    /**
     * Equips and configures an **intermediate melee bot** for the Pest Control minigame.
     *
     * @param bot the [AIPlayer] instance to configure
     * @param skills optional skill list to modify (unused, for compatibility)
     */
    fun meleeBotIntermediate(bot: AIPlayer, vararg skills: Int) {
        var max = 0
        val initial = RandomFunction.random(55, 95).also { max = 95 }
        var level = initial
        bot.fullRestore()

        bot.skills.setStaticLevel(Skills.STRENGTH, level)
        bot.skills.setStaticLevel(Skills.DEFENCE, level)
        bot.skills.setStaticLevel(Skills.ATTACK, level)
        bot.skills.setStaticLevel(Skills.HITPOINTS, level)
        bot.skills.setStaticLevel(Skills.PRAYER, 99)
        bot.skills.setStaticLevel(Skills.RANGE, level)
        bot.skills.setStaticLevel(Skills.MAGIC, level)
        bot.skills.setStaticLevel(Skills.SUMMONING, 99)
        bot.skills.setLevel(Skills.STRENGTH, level)
        bot.skills.setLevel(Skills.DEFENCE, level)
        bot.skills.setLevel(Skills.ATTACK, level)
        bot.skills.setLevel(Skills.HITPOINTS, level)
        bot.skills.setLevel(Skills.PRAYER, 99)
        bot.skills.setLevel(Skills.RANGE, level)
        bot.skills.setLevel(Skills.MAGIC, level)
        bot.skills.setLevel(Skills.SUMMONING, 99)
        bot.skills.updateCombatLevel()
        equipHighest(bot, PCMELEE_HELMS)
        equipHighest(bot, PCMELEE_LEG)
        equipHighest(bot, PCMELEE_SHIELD)
        equipHighest(bot, PCMELEE_TOP)
        equipHighest(bot, PCMELEE_WEP)
        bot.equipment.refresh()
        equipHighest(bot, CAPE)
        equipHighest(bot, NECK)
        equipHighest(bot, GLOVES)
        equipHighest(bot, BOOTS)
        equipHighest(bot, RING_BERS)
        bot.equipment.refresh()
        bot.skills.setStaticLevel(Skills.DEFENCE, 99)
        bot.skills.setStaticLevel(Skills.ATTACK, 99)
        bot.skills.setStaticLevel(Skills.STRENGTH, 99)
        bot.skills.setStaticLevel(Skills.HITPOINTS, 99)
        bot.skills.setLevel(Skills.DEFENCE, 99)
        bot.skills.setLevel(Skills.ATTACK, 99)
        bot.skills.setLevel(Skills.STRENGTH, 99)
        bot.skills.setLevel(Skills.HITPOINTS, 99)
        bot.fullRestore()
    }

    /**
     * Generates skill levels for a bot based on the specified tier and skills.
     *
     * @param bot target bot
     * @param tier defines average skill strength
     * @param skills list of skills to randomize and assign
     */
    private fun generateStats(bot: AIPlayer, tier: Tier, vararg skills: Int) {
        var totalXPAdd = 0.0
        var skillAmt = 0.0
        val variance = 0.5
        var max = 0
        val initial = when (tier) {
            Tier.LOW -> RandomFunction.random(33).also { max = 33 }
            Tier.MED -> RandomFunction.random(33, 66).also { max = 66 }
            Tier.HIGH -> RandomFunction.random(66, 99).also { max = 99 }
            Tier.PURE -> RandomFunction.random(90, 99).also { max = 99 }
        }
        for (skill in skills.indices) {
            val perc = RandomFunction.random(-variance, variance)
            var level = initial + (perc * 33).toInt()
            if (level < 1) level = 1
            if (level > max) level = max
            bot.skills.setLevel(skills[skill], level)
            bot.skills.setStaticLevel(skills[skill], level)
            totalXPAdd += bot.skills.getExperience(skills[skill])
            skillAmt++
        }
        when (tier) {
            Tier.PURE -> {
                bot.skills.setStaticLevel(Skills.DEFENCE, 10)
                bot.skills.setStaticLevel(Skills.STRENGTH, 99)
                bot.skills.setStaticLevel(Skills.ATTACK, 90)
                bot.skills.setStaticLevel(Skills.PRAYER, 43)
                bot.skills.setStaticLevel(Skills.RANGE, 1)
                bot.skills.setStaticLevel(Skills.MAGIC, 1)
            }

            else -> {}
        }

        bot.skills.addExperience(Skills.HITPOINTS, (totalXPAdd / skillAmt) * 0.2)
        val new_hp = bot.skills.levelFromXP((totalXPAdd / skillAmt) * 0.2)
        bot.skills.setStaticLevel(Skills.HITPOINTS, 10 + new_hp)
        bot.skills.updateCombatLevel()
        bot.fullRestore()
    }

    /**
     * Equips the best-fitting item from a given set based on the bot’s stats.
     *
     * @param bot target bot
     * @param set array of item IDs to choose from
     * @param levelCap optional maximum level threshold for equipment
     */
    private fun equipHighest(bot: AIPlayer, set: Array<Int>, levelCap: Int? = null) {
        val highestItems = ArrayList<Item>()
        var highest: Item? = null
        for (i in set.indices) {
            val item = Item(set[i])
            var canEquip = true
            (item.definition.handlers.getOrDefault("requirements", null) as HashMap<Int, Int>?)?.let { map ->
                levelCap?.let { levelcap ->
                    map.map {
                        if (bot.skills.getLevel(it.key) < it.value || it.value > levelcap) canEquip = false
                    }
                } ?: map.map {
                    if (bot.skills.getLevel(it.key) < it.value) canEquip = false
                }
            }
            if (canEquip) {
                if (highest == null) {
                    highest = item
                    highestItems.add(item)
                    continue
                }
                if (item.averageLevel() > highest.averageLevel()) {
                    highest = item
                    highestItems.clear()
                    highestItems.add(item)
                } else if (item.averageLevel() == highest.averageLevel()) {
                    highestItems.add(item)
                }
            }
        }
        bot.equipment.add(highestItems.random(), highest!!.definition!!.handlers["equipment_slot"] as Int, false, false)
    }

    /**
     * Extension that calculates the average required level of an [Item].
     */
    private fun Item.averageLevel(): Int {
        var total = 1
        var count = 1
        (definition.handlers.getOrDefault("requirements", null) as HashMap<Int, Int>?)?.let { map ->
            map.map {
                total += it.value
                count++
            }
        }
        return total / count
    }

    private val rangeHeadGear = arrayOf(Items.ROBIN_HOOD_HAT_2581, Items.LEATHER_COWL_1167, Items.COIF_1169, Items.GREEN_DHIDE_COIF_100_12936, Items.BLUE_DHIDE_COIF_100_12943, Items.RED_DHIDE_COIF_100_12950, Items.BLACK_DHIDE_COIF_100_12957, Items.KARILS_COIF_4732, Items.ARCHER_HELM_3749)
    private val rangeTopGear = arrayOf(Items.LEATHER_BODY_1129, Items.HARDLEATHER_BODY_1131, Items.STUDDED_BODY_1133, Items.GREEN_DHIDE_BODY_1135, Items.BLUE_DHIDE_BODY_2499, Items.RED_DHIDE_BODY_2501, Items.BLACK_DHIDE_BODY_2503, Items.KARILS_TOP_100_4940)
    private val rangeBottomGear = arrayOf(Items.LEATHER_CHAPS_1095, Items.STUDDED_CHAPS_1097, Items.GREEN_DHIDE_CHAPS_1099, Items.BLUE_DHIDE_CHAPS_2493, Items.RED_DHIDE_CHAPS_2495, Items.BLACK_DHIDE_CHAPS_2497)
    private val bows = arrayOf(Items.SHORTBOW_841, Items.OAK_SHORTBOW_843, Items.WILLOW_LONGBOW_847, Items.MAPLE_SHORTBOW_853, Items.MAGIC_SHORTBOW_861)
    private val crossbows = arrayOf(Items.BRONZE_CROSSBOW_9174, Items.IRON_CROSSBOW_9177, Items.BLURITE_CROSSBOW_9176, Items.STEEL_CROSSBOW_9179, Items.MITH_CROSSBOW_9181, Items.ADAMANT_CROSSBOW_9183, Items.RUNE_CROSSBOW_9185)

    private val pestControlRangeHeadGear = arrayOf(Items.LEATHER_COWL_1167, Items.COIF_1169, Items.BLACK_DHIDE_COIF_100_12957, Items.ARCHER_HELM_3749, Items.KARILS_COIF_4732, Items.ARMADYL_HELMET_11718, Items.SLAYER_HELMET_13263)
    private val pestControlRangeTopGear = arrayOf(Items.LEATHER_BODY_1129, Items.HARDLEATHER_BODY_1131, Items.GREEN_DHIDE_BODY_1135, Items.BLACK_DHIDE_BODY_2503, Items.KARILS_TOP_100_4940, Items.ARMADYL_CHESTPLATE_11720)
    private val pestControlRangeBottomGear = arrayOf(Items.LEATHER_CHAPS_1095, Items.STUDDED_CHAPS_1097, Items.GREEN_DHIDE_CHAPS_1099, Items.BLACK_DHIDE_CHAPS_2497, Items.KARILS_LEATHERSKIRT_4738, Items.ARMADYL_PLATESKIRT_11722)
    private val pestControlRangeCapeSlot = arrayOf(Items.BLACK_CAPE_1019, Items.OBSIDIAN_CAPE_6568, Items.AVAS_ATTRACTOR_10498, Items.AVAS_ACCUMULATOR_10499, Items.FIRE_CAPE_6570)

    private val meleeHeadGear = arrayOf(Items.IRON_MED_HELM_1137, Items.BRONZE_MED_HELM_1139, Items.STEEL_MED_HELM_1141, Items.WHITE_MED_HELM_6621, Items.MITHRIL_MED_HELM_1143, Items.ADAMANT_MED_HELM_1145, Items.RUNE_MED_HELM_1147, Items.DRAGON_MED_HELM_1149, Items.BLACK_MED_HELM_1151, Items.IRON_FULL_HELM_1153, Items.WHITE_FULL_HELM_6623, Items.MITHRIL_FULL_HELM_1159, Items.RUNE_FULL_HELM_1163, Items.BLACK_FULL_HELM_1165, Items.FREMENNIK_HELM_3748, Items.BERSERKER_HELM_3751, Items.WARRIOR_HELM_3753, Items.DHAROKS_HELM_4716, Items.GUTHANS_HELM_4724, Items.TORAGS_HELM_4745, Items.VERACS_HELM_4753)
    private val meleeTopGear = arrayOf(Items.IRON_CHAINBODY_1101, Items.BRONZE_CHAINBODY_1103, Items.STEEL_CHAINBODY_1105, Items.BLACK_CHAINBODY_1107, Items.MITHRIL_CHAINBODY_1109, Items.ADAMANT_CHAINBODY_1111, Items.RUNE_CHAINBODY_1113, Items.DRAGON_CHAINBODY_2513, Items.IRON_PLATEBODY_1115, Items.BRONZE_PLATEBODY_1117, Items.STEEL_PLATEBODY_1119, Items.MITHRIL_PLATEBODY_1121, Items.ADAMANT_PLATEBODY_1123, Items.BLACK_PLATEBODY_1125, Items.RUNE_PLATEBODY_1127, Items.DHAROKS_PLATEBODY_4720, Items.GUTHANS_PLATEBODY_4728, Items.KARILS_LEATHERTOP_4736, Items.VERACS_BRASSARD_4757, Items.TORAGS_PLATEBODY_4749)
    private val meleeBottomGear = arrayOf(1081, 1083, 1085, 1087, 1089, 1091, 1093, 4759, 1067, 1069, 1071, 1073, 1075, 1077, 1079, 4722, 4751, 4722, 4751)
    private val meleeShieldSlotGear = arrayOf(1171, 1173, 1175, 1177, 1179, 1181, 1183, 1185, 1187, 1189, 1191, 1193, 1195, 1197, 1199, 1201)
    private val meleeWeaponSlotGear = arrayOf(Items.BRONZE_SWORD_1277, Items.IRON_SWORD_1279, Items.STEEL_SWORD_1281, Items.BLACK_SWORD_1283, Items.MITHRIL_SWORD_1285, Items.ADAMANT_SWORD_1287, Items.RUNE_SWORD_1289, Items.BRONZE_LONGSWORD_1291, Items.IRON_LONGSWORD_1293, Items.STEEL_LONGSWORD_1295, Items.BLACK_LONGSWORD_1297, Items.MITHRIL_LONGSWORD_1299, Items.ADAMANT_LONGSWORD_1301, Items.RUNE_LONGSWORD_1303, Items.DRAGON_LONGSWORD_1305, Items.BRONZE_SCIMITAR_1321, Items.IRON_SCIMITAR_1323, Items.STEEL_SCIMITAR_1325, Items.BLACK_SCIMITAR_1327, Items.MITHRIL_SCIMITAR_1329, Items.ADAMANT_SCIMITAR_1331, Items.RUNE_SCIMITAR_1333, Items.DRAGON_SCIMITAR_4587, Items.ABYSSAL_WHIP_4151, Items.BRONZE_BATTLEAXE_1375, Items.IRON_BATTLEAXE_1363, Items.STEEL_BATTLEAXE_1365, Items.BLACK_BATTLEAXE_1367, Items.MITHRIL_BATTLEAXE_1369, Items.ADAMANT_BATTLEAXE_1371, Items.RUNE_BATTLEAXE_1373, Items.DRAGON_BATTLEAXE_1377)

    private val NGLOVES = arrayOf(1059, 2922, 2912, 2902, 2932, 2942, 3799)
    private val NBOOTS = arrayOf(4121, 4123, 4125, 4127, 4129, 4131, 1061, 1837, 2579, 9005)
    private val NRBOOTS = arrayOf(9006, 626, 628, 630, 632, 634)
    private val NNECK = arrayOf(1704, 1725, 1729, 1731)
    private val NRANGENECK = arrayOf(1478, 1704)
    private val NRANGESHIELD = arrayOf(1191, 1193, 1195, 1197, 1199, 1201)

    private val PCMELEE_HELMS = arrayOf(1137, 1139, 1141, 6621, 1143, 1145, 1147, 1149, 1151, 1153, 6623, 1159, 1163, 1165, 3748, 3751, 10828, 11335, 3753, 4716, 4724, 4745, 4753, 3751)
    private val PCMELEE_TOP = arrayOf(1101, 1103, 1105, 1107, 1109, 1111, 1113, 2513, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 4720, 4728, 4749, 4749, 11724, 14479, 2513)
    private val PCMELEE_LEG = arrayOf(1081, 1083, 1085, 1087, 1089, 1091, 1093, 4759, 1067, 1069, 1071, 1073, 1075, 1077, 1079, 4722, 4751, 4722, 4751, 11726, 4087)
    private val PCMELEE_SHIELD = arrayOf(1171, 1173, 1175, 1177, 1179, 1181, 1183, 1185, 1187, 1189, 1191, 1193, 1195, 1197, 1199, 1201, 6524, 13742, 13740, 13738, 13736, 13734)
    private val PCMELEE_WEP = arrayOf(1277, 1279, 1281, 1283, 1285, 1287, 1289, 1291, 1293, 1295, 1297, 1299, 1301, 1303, 1305, 1321, 1323, 1325, 1327, 1329, 1331, 1333, 4587, 4151, 1363, 1365, 1367, 1369, 1371, 1373, 1375, 1377, 1434, 5698)

    private val NECK = arrayOf(1704, 6585)
    private val CAPE = arrayOf(1019, 1021, 1023, 6568, 4315, 4317, 4319, 4321, 4323, 4325, 4327, 4329, 4331, 4333, 4335, 4337, 4339, 4341, 4343, 4345, 4347, 4349, 4351)
    private val GLOVES = arrayOf(1059, 7456, 7457, 7458, 7459, 7460, 7461, 7462)
    private val BOOTS = arrayOf(1061, 4131, 11732, 11728, 4131)
    private val RING_BERS = arrayOf(6737)
    private val RING_ARCH = arrayOf(6733)
}
