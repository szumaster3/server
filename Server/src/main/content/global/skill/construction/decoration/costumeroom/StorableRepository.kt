package content.global.skill.construction.decoration.costumeroom

object StorableRepository {
    private val byType = Storable.values().groupBy { it.type }
    fun getItems(type: StorableType): List<Storable> = byType[type].orEmpty()
    fun getItems(type: StorableType, tier: Int): List<Storable> = byType[type].orEmpty().filter { it.tier <= tier }
}
