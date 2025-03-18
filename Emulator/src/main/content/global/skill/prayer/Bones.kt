package content.global.skill.prayer

import org.rs.consts.Items

enum class Bones(
    val itemId: Int,
    val experience: Double,
    val bonemealId: Int? = null,
) {
    BONES(Items.BONES_2530, 4.5, Items.BONEMEAL_4255),
    BONES_2(Items.BONES_526, 4.5, Items.BONEMEAL_4256),
    WOLF_BONES(Items.WOLF_BONES_2859, 4.5, Items.BONEMEAL_4257),
    BURNT_BONES(Items.BURNT_BONES_528, 4.5, Items.BONEMEAL_4258),
    MONKEY_BONES(Items.MONKEY_BONES_3183, 5.0, Items.BONEMEAL_4260),
    MONKEY_BONES2(Items.MONKEY_BONES_3179, 5.0, Items.BONEMEAL_4260),
    BAT_BONES(Items.BAT_BONES_530, 5.3, Items.BONEMEAL_4261),
    BIG_BONES(Items.BIG_BONES_532, 15.0, Items.BONEMEAL_4262),
    JOGRE_BONES(Items.JOGRE_BONES_3125, 15.0, Items.BONEMEAL_4263),
    ZOGRE_BONES(Items.ZOGRE_BONES_4812, 12.5, Items.BONEMEAL_4264),
    SHAIKAHAN_BONES(Items.SHAIKAHAN_BONES_3123, 25.0, Items.BONEMEAL_4265),
    BABY_DRAGON_BONES(Items.BABYDRAGON_BONES_534, 30.0, Items.BONEMEAL_4266),
    WYVERN_BONES(Items.WYVERN_BONES_6812, 50.0, Items.BONEMEAL_6810),
    DRAGON_BONES(Items.DRAGON_BONES_536, 72.0, Items.BONEMEAL_4268),
    FAYRG(Items.FAYRG_BONES_4830, 84.0, Items.BONEMEAL_4852),
    RAURG_BONES(Items.RAURG_BONES_4832, 96.0, Items.BONEMEAL_4853),
    DAGANNOTH(Items.DAGANNOTH_BONES_6729, 125.0, Items.BONEMEAL_6728),
    OURG_BONES(Items.OURG_BONES_4834, 140.0, Items.BONEMEAL_4854),
    BURNT_JOGRE_BONES(Items.BURNT_JOGRE_BONES_3127, 16.0, Items.BONEMEAL_4259),
    BURNT_RAW_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3128, 17.0),
    BURNT_COOKED_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3129, 17.0),
    MARINATED_JOGRE_BONES(Items.MARINATED_J_BONES_3130, 18.0),
    RAW_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3131, 17.0),
    COOKED_PASTY_JOGRE_BONES(Items.PASTY_JOGRE_BONES_3132, 17.0),
    MARINATED_JOGRE_BONES_BAD(Items.MARINATED_J_BONES_3133, 18.0),
    ;

    companion object {
        private val bones: MutableMap<Int, Bones> = mutableMapOf()
        private val bonemealIndex: MutableMap<Int, Bones> = mutableMapOf()

        @JvmStatic
        fun forBoneMeal(itemId: Int): Bones? = bonemealIndex[itemId]

        val array: IntArray
            get() = bones.keys.toIntArray()

        @JvmStatic
        fun forId(itemId: Int): Bones? = bones[itemId]

        init {
            for (bone in values()) {
                bones[bone.itemId] = bone
                bone.bonemealId?.let {
                    bonemealIndex[it] = bone
                }
            }
        }
    }
}
