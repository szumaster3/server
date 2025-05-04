package content.global.skill.construction.storage.box

import org.rs.consts.Items

enum class FancyDressBoxItem(val displayId: Int, vararg val takeId: Int) {
    MimeCostume(Items.MIME_MASK_10629, Items.MIME_MASK_3057, Items.MIME_TOP_3058, Items.MIME_LEGS_3059, Items.MIME_GLOVES_3060, Items.MIME_BOOTS_3061),
    RoyalFrogCostume(Items.PRINCESS_BLOUSE_10630, Items.PRINCE_TUNIC_6184, Items.PRINCE_LEGGINGS_6185, Items.PRINCESS_BLOUSE_6186, Items.PRINCESS_SKIRT_6187),
    ZombieOutfit(Items.ZOMBIE_SHIRT_10631, Items.ZOMBIE_MASK_7594, Items.ZOMBIE_SHIRT_7592, Items.ZOMBIE_TROUSERS_7593, Items.ZOMBIE_GLOVES_7595, Items.ZOMBIE_BOOTS_7596),
    CamoOutfit(Items.CAMO_TOP_10632, Items.CAMO_HELMET_6656, Items.CAMO_TOP_6654, Items.CAMO_BOTTOMS_6655),
    LederhosenOutfit(Items.LEDERHOSEN_TOP_10633, Items.LEDERHOSEN_TOP_6180, Items.LEDERHOSEN_SHORTS_6181, Items.LEDERHOSEN_HAT_6182),
    ShadeRobes(Items.SHADE_ROBE_10634, Items.SHADE_ROBE_546, Items.SHADE_ROBE_548);

    val labelId: Int get() = 56 + (ordinal * 2)
    val iconId: Int get() = 165 + (ordinal * 2)
}