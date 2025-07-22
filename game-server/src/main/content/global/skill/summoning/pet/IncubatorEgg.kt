package content.global.skill.summoning.pet

import core.game.node.item.Item
import org.rs.consts.Items

/**
 * Represents the types of incubator eggs.
 */
enum class IncubatorEgg(
    val egg: Item,
    val level: Int,
    val incubationTime: Int,
    val product: Item,
) {
    /**
     * Penguin egg that hatches into a baby penguin.
     */
    PENGUIN(
        egg = Item(Items.PENGUIN_EGG_12483),
        level = 30,
        incubationTime = 30,
        product = Item(Items.BABY_PENGUIN_12481),
    ),

    /**
     * Raven egg that hatches into a raven chick.
     */
    RAVEN(
        egg = Item(Items.RAVEN_EGG_11964),
        level = 50,
        incubationTime = 30,
        product = Item(Items.RAVEN_CHICK_12484),
    ),

    /**
     * Saradomin owl egg that hatches into a saradomin chick.
     */
    SARADOMIN_OWL(
        egg = Item(Items.BIRDS_EGG_5077),
        level = 70,
        incubationTime = 60,
        product = Item(Items.SARADOMIN_CHICK_12503),
    ),

    /**
     * Zamorak hawk egg that hatches into a zamorak chick.
     */
    ZAMORAK_HAWK(
        egg = Item(Items.BIRDS_EGG_5076),
        level = 70,
        incubationTime = 60,
        product = Item(Items.ZAMORAK_CHICK_12506),
    ),

    /**
     * Guthix raptor egg that hatches into a guthix chick.
     */
    GUTHIX_RAPTOR(
        egg = Item(Items.BIRDS_EGG_5078),
        level = 70,
        incubationTime = 60,
        product = Item(Items.GUTHIX_CHICK_12509),
    ),

    /**
     * Vulture egg that hatches into a vulture chick.
     */
    VULTURE(
        egg = Item(Items.VULTURE_EGG_11965),
        level = 85,
        incubationTime = 60,
        product = Item(Items.VULTURE_CHICK_12498),
    ),

    /**
     * Chameleon egg that hatches into a baby chameleon.
     */
    CHAMELEON(
        egg = Item(Items.CHAMELEON_EGG_12494),
        level = 90,
        incubationTime = 60,
        product = Item(Items.BABY_CHAMELEON_12492),
    ),

    /**
     * Red dragon egg that hatches into a hatchling dragon.
     */
    RED_DRAGON(
        egg = Item(Items.RED_DRAGON_EGG_12477),
        level = 99,
        incubationTime = 60,
        product = Item(Items.HATCHLING_DRAGON_12469),
    ),

    /**
     * Black dragon egg that hatches into a hatchling dragon.
     */
    BLACK_DRAGON(
        egg = Item(Items.BLACK_DRAGON_EGG_12480),
        level = 99,
        incubationTime = 60,
        product = Item(Items.HATCHLING_DRAGON_12475),
    ),

    /**
     * Blue dragon egg that hatches into a hatchling dragon.
     */
    BLUE_DRAGON(
        egg = Item(Items.BLUE_DRAGON_EGG_12478),
        level = 99,
        incubationTime = 60,
        product = Item(Items.HATCHLING_DRAGON_12471),
    ),

    /**
     * Green dragon egg that hatches into a hatchling dragon.
     */
    GREEN_DRAGON(
        egg = Item(Items.GREEN_DRAGON_EGG_12479),
        level = 99,
        incubationTime = 60,
        product = Item(Items.HATCHLING_DRAGON_12473)
    ), ;

    companion object {
        /**
         * Returns the [IncubatorEgg] for a given item.
         */
        fun forItem(item: Item): IncubatorEgg? {
            for (e in values()) {
                if (e.egg.id == item.id) {
                    return e
                }
            }
            return null
        }
    }
}
