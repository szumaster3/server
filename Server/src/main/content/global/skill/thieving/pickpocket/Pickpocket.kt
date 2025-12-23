package content.global.skill.thieving.pickpocket

import content.global.skill.thieving.pickpocket.loot.*
import core.api.utils.WeightBasedTable

/**
 * Represents pickpocket data.
 */
enum class Pickpocket(
    val ids: IntArray,
    val requiredLevel: Int,
    val low: Double,
    val high: Double,
    val xp: Double,
    val stunDamageMin: Int,
    val stunDamageMax: Int,
    val stunTime: Int,
    val message: String?,
    val loot: WeightBasedTable
) {
    HUMAN(HumanLootTable.NPC_ID, 1, 180.0, 240.0, 8.0, 1, 1, 5, "What do you think you're doing?", HumanLootTable.LOOT),
    CURATOR_HAIG_HELEN(CuratorHaigHelenLootTable.NPC_ID, 1, 180.0, 240.0, 8.0, 1, 1, 5, null, CuratorHaigHelenLootTable.LOOT),
    FARMER(FarmerLootTable.NPC_ID, 10, 180.0, 240.0, 14.5, 1, 1, 5, "What do you think you're doing?", FarmerLootTable.LOOT),
    MALE_HAM_MEMBER(HamMemberLootTable.NPC_ID_MALE, 20, 117.0, 240.0, 22.2, 1, 3, 4, "What do you think you're doing?", HamMemberLootTable.LOOT),
    FEMALE_HAM_MEMBER(HamMemberLootTable.NPC_ID_FEMALE, 15, 135.0, 240.0, 18.5, 1, 3, 4, "Stop! @name is a thief!", HamMemberLootTable.LOOT),
    WARRIOR(WarriorLootTable.NPC_ID, 25, 84.0, 240.0, 26.0, 2, 2, 5, "What do you think you're doing?", WarriorLootTable.LOOT),
    VILLAGER(VillagerLootTable.NPC_ID, 30, 74.0, 240.0, 8.0, 2, 2, 5, "Thief! Thief! Get away from me.", VillagerLootTable.LOOT),
    ROGUE(RogueLootTable.NPC_ID, 32, 74.0, 240.0, 35.5, 2, 2, 5, "What do you think you're doing?", RogueLootTable.LOOT),
    CAVE_GOBLIN(CaveGoblinLootTable.NPC_ID, 36, 72.0, 240.0, 40.0, 1, 1, 5, null, CaveGoblinLootTable.LOOT),
    MASTER_FARMER(MasterFarmerLootTable.NPC_ID, 38, 90.0, 240.0, 43.0, 3, 3, 5, "Cor blimey, mate! What are ye doing in me pockets?", MasterFarmerLootTable.LOOT),
    GUARD(GuardLootTable.NPC_ID, 40, 50.0, 240.0, 46.8, 2, 2, 5, "What do you think you're doing?", GuardLootTable.LOOT),
    FREMENNIK_CITIZEN(FremennikCitizenLootTable.NPC_ID, 45, 65.0, 240.0, 65.0, 2, 2, 5, "You stay away from me Outlander!", FremennikCitizenLootTable.LOOT),
    BEARDED_BANDIT(BeardedBanditLootTable.NPC_ID, 45, 50.0, 240.0, 65.0, 2, 2, 5, "What do you think you're doing?", BeardedBanditLootTable.LOOT),
    DESERT_BANDIT(DesertBanditLootTable.NPC_ID, 53, 50.0, 240.0, 79.4, 3, 3, 5, "I'll kill you for that!", DesertBanditLootTable.LOOT),
    POLLNIVNIAN_BANDIT(PollnivnianBanditLootTable.NPC_ID, 55, 50.0, 240.0, 84.3, 5, 5, 5, "I'll kill you for that!", PollnivnianBanditLootTable.LOOT),
    KNIGHT_OF_ADROUGNE(KnightLootTable.NPC_ID, 55, 50.0, 240.0, 84.3, 3, 3, 6, null, KnightLootTable.LOOT),
    YANILLE_WATCHMAN(WatchmanLootTable.NPC_ID, 65, 50.0, 240.0, 137.5, 3, 3, 5, "What do you think you're doing?", WatchmanLootTable.LOOT),
    MENAPHITE_THUG(MenaphiteThugLootTable.NPC_ID, 65, 50.0, 240.0, 137.5, 5, 5, 5, "I'll kill you for that!", MenaphiteThugLootTable.LOOT),
    PALADIN(PaladinLootTable.NPC_ID, 70, 50.0, 150.0, 151.8, 3, 3, 5, "Hey! Get your hands off there!", PaladinLootTable.LOOT),
    GNOME(GnomeLootTable.NPC_ID, 75, 8.0, 120.0, 198.3, 1, 1, 5, "What do you think you're doing?", GnomeLootTable.LOOT),
    HERO(HeroLootTable.NPC_ID, 80, 6.0, 100.0, 273.3, 6, 6, 6, "What do you think you're doing?", HeroLootTable.LOOT),
    ELF(ElfLootTable.NPC_ID, 85, 4.0, 80.0, 353.3, 5, 5, 6, "What do you think you're doing?", ElfLootTable.LOOT);

    companion object {
        /**
         * Map linking NPC ids to their [Pickpocket] enum.
         */
        val idMap: Map<Int, Pickpocket> = Pickpocket.values()
            .flatMap { pickpocket -> pickpocket.ids.map { it to pickpocket } }
            .toMap()

        /**
         * Returns the [Pickpocket] for a given npc id.
         */
        @JvmStatic
        fun forID(id: Int): Pickpocket? = idMap[id]
    }
}