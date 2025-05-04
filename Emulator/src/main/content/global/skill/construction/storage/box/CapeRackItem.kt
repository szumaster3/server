package content.global.skill.construction.storage.box

import org.rs.consts.Items

enum class CapeRackItem(val displayId: Int, vararg val takeId: Int) {
    LegendsCape(Items.CAPE_OF_LEGENDS_10635, Items.CAPE_OF_LEGENDS_1052),
    ObsidianCape(Items.OBSIDIAN_CAPE_10636, Items.OBSIDIAN_CAPE_6568),
    FireCape(Items.FIRE_CAPE_10637, Items.FIRE_CAPE_6570),
    TeamCape(Items.TEAM_1_CAPE_10638, Items.TEAM_10_CAPE_4333),
    AttackCape(Items.ATTACK_CAPE_10639, Items.ATTACK_CAPE_9747),
    StrengthCape(Items.STRENGTH_CAPE_10640, Items.STRENGTH_CAPE_9750),
    DefenceCape(Items.DEFENCE_CAPE_10641, Items.DEFENCE_CAPE_9753),
    RangingCape(Items.RANGING_CAPE_10642, Items.RANGING_CAPE_9756),
    PrayerCape(Items.PRAYER_CAPE_10643, Items.PRAYER_CAPE_9759),
    MagicCape(Items.MAGIC_CAPE_10644, Items.MAGIC_CAPE_9762),
    RunecraftCape(Items.RUNECRAFT_CAPE_10645, Items.RUNECRAFT_CAPE_9765),
    HunterCape(Items.HUNTER_CAPE_10646, Items.HUNTER_CAPE_9948),
    HitpointsCape(Items.HITPOINTS_CAPE_10647, Items.HITPOINTS_CAPE_9768),
    AgilityCape(Items.AGILITY_CAPE_10648, Items.AGILITY_CAPE_9771),
    HerbloreCape(Items.HERBLORE_CAPE_10649, Items.HERBLORE_CAPE_9774),
    ThievingCape(Items.THIEVING_CAPE_10650, Items.THIEVING_CAPE_9777),
    CraftingCape(Items.CRAFTING_CAPE_10651, Items.CRAFTING_CAPE_9780),
    FletchingCape(Items.FLETCHING_CAPE_10652, Items.FLETCHING_CAPE_9783),
    SlayerCape(Items.SLAYER_CAPE_10653, Items.SLAYER_CAPE_9786),
    ConstructionCape(Items.CONSTRUCT_CAPE_10654, Items.CONSTRUCT_CAPE_9789),
    MiningCape(Items.MINING_CAPE_10655, Items.MINING_CAPE_9792),
    SmithingCape(Items.SMITHING_CAPE_10656, Items.SMITHING_CAPE_9795),
    FishingCape(Items.FISHING_CAPE_10657, Items.FISHING_CAPE_9798),
    CookingCape(Items.COOKING_CAPE_10658, Items.COOKING_CAPE_9801),
    FiremakingCape(Items.FIREMAKING_CAPE_10659, Items.FIREMAKING_CAPE_9804),
    WoodcuttingCape(Items.WOODCUTTING_CAPE_10660, Items.WOODCUTTING_CAPE_9807),
    FarmingCape(Items.FARMING_CAPE_10661, Items.FARMING_CAPE_9810),
    QuestCape(Items.QUEST_POINT_CAPE_10662, Items.QUEST_POINT_CAPE_9813),
    SpottedCape(Items.SPOTTED_CAPE_10663, Items.SPOTTED_CAPE_10663),
    SpottierCape(Items.SPOTTIER_CAPE_10664, Items.SPOTTIER_CAPE_10664);

    val labelId: Int get() = 56 + (ordinal * 2)
    val iconId: Int get() = 165 + (ordinal * 2)
}