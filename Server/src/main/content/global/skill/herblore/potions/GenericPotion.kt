package content.global.skill.herblore.potions

/**
 * Represents a generic potion.
 */
class GenericPotion(
    val base: Int, val ingredient: Int, val level: Int, val experience: Double, val product: Int?
) {

    companion object {

        /**
         * Creates a [GenericPotion] from an unfinished potion.
         *
         * @param potion The unfinished potion.
         * @return The generic potion.
         */
        fun transform(potion: UnfinishedPotion): GenericPotion = GenericPotion(
            base = potion.base,
            ingredient = potion.ingredient,
            level = potion.level,
            experience = 0.0,
            product = potion.potion,
        )

        /**
         * Creates a [GenericPotion] from a finished potion.
         *
         * @param potion The finished potion.
         * @return The generic potion.
         */
        fun transform(potion: FinishedPotion): GenericPotion = GenericPotion(
            base = potion.unfinished.potion,
            ingredient = potion.ingredient,
            level = potion.level,
            experience = potion.experience,
            product = potion.potion,
        )
    }
}