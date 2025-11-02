package content.global.skill.construction.decoration.costumeroom

/**
 * Repository for retrieving relevant
 * storable items by storage types.
 */
object StorableRepository {

    /**
     * Represents the tiers of storable types.
     */
    private val tiers = mapOf(
        StorableType.LOW_LEVEL_TRAILS
                to listOf(StorableType.LOW_LEVEL_TRAILS),
        StorableType.MED_LEVEL_TRAILS
                to listOf(StorableType.LOW_LEVEL_TRAILS, StorableType.MED_LEVEL_TRAILS),
        StorableType.HIGH_LEVEL_TRAILS
                to listOf(StorableType.LOW_LEVEL_TRAILS, StorableType.MED_LEVEL_TRAILS, StorableType.HIGH_LEVEL_TRAILS),
        StorableType.ONE_SET_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR),
        StorableType.TWO_SETS_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR),
        StorableType.THREE_SETS_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR, StorableType.THREE_SETS_OF_ARMOUR),
        StorableType.FOUR_SETS_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR, StorableType.THREE_SETS_OF_ARMOUR, StorableType.FOUR_SETS_OF_ARMOUR),
        StorableType.FIVE_SETS_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR, StorableType.THREE_SETS_OF_ARMOUR, StorableType.FOUR_SETS_OF_ARMOUR, StorableType.FIVE_SETS_OF_ARMOUR),
        StorableType.SIX_SETS_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR, StorableType.THREE_SETS_OF_ARMOUR, StorableType.FOUR_SETS_OF_ARMOUR, StorableType.FIVE_SETS_OF_ARMOUR, StorableType.SIX_SETS_OF_ARMOUR),
        StorableType.ALL_SETS_OF_ARMOUR
                to listOf(StorableType.ONE_SET_OF_ARMOUR, StorableType.TWO_SETS_OF_ARMOUR, StorableType.THREE_SETS_OF_ARMOUR, StorableType.FOUR_SETS_OF_ARMOUR, StorableType.FIVE_SETS_OF_ARMOUR, StorableType.SIX_SETS_OF_ARMOUR),
        StorableType.TWO_SETS_ARMOUR_CASE
                to listOf(StorableType.TWO_SETS_ARMOUR_CASE),
        StorableType.FOUR_SETS_ARMOUR_CASE
                to listOf(StorableType.TWO_SETS_ARMOUR_CASE, StorableType.FOUR_SETS_ARMOUR_CASE),
        StorableType.ALL_SETS_ARMOUR_CASE
                to listOf(StorableType.TWO_SETS_ARMOUR_CASE, StorableType.FOUR_SETS_ARMOUR_CASE, StorableType.ALL_SETS_ARMOUR_CASE)
    )

    /**
     * Precomputed mapping from [StorableType] to the list of relevant [Storable]s.
     *
     * Uses [tiers] to include all appropriate lower-tier types automatically.
     */
    private val relevantMap: Map<StorableType, List<Storable>> = StorableType.values().associateWith { type ->
        val includedTypes = tiers[type] ?: listOf(type)
        Storable.values().filter { it.type in includedTypes }
    }

    /**
     * Gets all [Storable] items that are relevant for the given [StorableType].
     *
     * @param type The type of storable.
     * @return A list of relevant [Storable] items. Empty if no matching items found.
     */
    fun getRelevantItems(type: StorableType): List<Storable> = relevantMap[type] ?: emptyList()
}