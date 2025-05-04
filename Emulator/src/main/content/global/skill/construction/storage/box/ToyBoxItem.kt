package content.global.skill.construction.storage.box

import org.rs.consts.Items

enum class ToyBoxItem(val displayId: Int, vararg val takeId: Int) {
    JackLanternMask(Items.JACK_LANTERN_MASK_10723, Items.JACK_LANTERN_MASK_9920),
    SkeletonBoots(Items.SKELETON_BOOTS_10724, Items.SKELETON_BOOTS_9921),
    SkeletonGloves(Items.SKELETON_GLOVES_10725, Items.SKELETON_GLOVES_9922),
    SkeletonLeggings(Items.SKELETON_LEGGINGS_10726, Items.SKELETON_LEGGINGS_9923),
    SkeletonShirt(Items.SKELETON_SHIRT_10727, Items.SKELETON_SHIRT_9924),
    SkeletonMask(Items.SKELETON_MASK_10728, Items.SKELETON_MASK_9925),
    EasterRing(Items.EASTER_RING_10729, Items.EASTER_RING_7927),
    ZombieHead(Items.ZOMBIE_HEAD_10731, Items.ZOMBIE_HEAD_6722),
    RubberChicken(Items.RUBBER_CHICKEN_10732, Items.RUBBER_CHICKEN_4566),
    YoYo(Items.YO_YO_10733, Items.YO_YO_4079),
    BunnyEars(Items.BUNNY_EARS_10734, Items.BUNNY_EARS_1037),
    Scythe(Items.SCYTHE_10735, Items.SCYTHE_1419),
    ReindeerHat(Items.REINDEER_HAT_10722, Items.REINDEER_HAT_10507),
    WintumberTree(Items.WINTUMBER_TREE_10508, Items.WINTUMBER_TREE_10508),
    BobbleHat(Items.BOBBLE_HAT_9815, Items.BOBBLE_HAT_6856),
    BobbleScarf(Items.BOBBLE_SCARF_9816, Items.BOBBLE_SCARF_6857),
    JesterHat(Items.JESTER_HAT_6858, Items.JESTER_HAT_6858),
    JesterScarf(Items.JESTER_SCARF_6859, Items.JESTER_SCARF_6859),
    TriJesterHat(Items.TRI_JESTER_HAT_6860, Items.TRI_JESTER_HAT_6860),
    TriJesterScarf(Items.TRI_JESTER_SCARF_6861, Items.TRI_JESTER_SCARF_6861),
    WoollyHat(Items.WOOLLY_HAT_6862, Items.WOOLLY_HAT_6862),
    WoollyScarf(Items.WOOLLY_SCARF_6863, Items.WOOLLY_SCARF_6863),
    ChickenFeet(Items.CHICKEN_FEET_11019, Items.CHICKEN_FEET_11019),
    ChickenWings(Items.CHICKEN_WINGS_11020, Items.CHICKEN_WINGS_11020),
    ChickenHead(Items.CHICKEN_HEAD_11021, Items.CHICKEN_HEAD_11021),
    ChickenLegs(Items.CHICKEN_LEGS_11022, Items.CHICKEN_LEGS_11022),
    GrimReaperHood(Items.GRIM_REAPER_HOOD_11789, Items.GRIM_REAPER_HOOD_11789),
    SnowGlobe(Items.SNOW_GLOBE_11949, Items.SNOW_GLOBE_11949),
    ChocaticeCape(Items.CHOCATRICE_CAPE_12634, Items.CHOCATRICE_CAPE_12645),
    WarlockTop(Items.WARLOCK_TOP_14076, Items.WARLOCK_TOP_14076),
    WarlockLegs(Items.WARLOCK_LEGS_14077, Items.WARLOCK_LEGS_14077),
    WarlockCloak(Items.WARLOCK_CLOAK_14081, Items.WARLOCK_CLOAK_14081),
    SantaCostumeTop(Items.SANTA_COSTUME_TOP_14595, Items.SANTA_COSTUME_TOP_14595),
    SantaCostumeGloves(Items.SANTA_COSTUME_GLOVES_14602, Items.SANTA_COSTUME_GLOVES_14602),
    SantaCostumeLegs(Items.SANTA_COSTUME_LEGS_14603, Items.SANTA_COSTUME_LEGS_14603),
    SantaCostumeBoots(Items.SANTA_COSTUME_BOOTS_14605, Items.SANTA_COSTUME_BOOTS_14605),
    IceAmulet(Items.ICE_AMULET_14596, Items.ICE_AMULET_14596),
    RedMarionette(Items.RED_MARIONETTE_6867, Items.RED_MARIONETTE_6867),
    GreenMarionette(Items.GREEN_MARIONETTE_6866, Items.GREEN_MARIONETTE_6866),
    BlueMarionette(Items.BLUE_MARIONETTE_6865, Items.BLUE_MARIONETTE_6865),
    More(Items.MORE_10165),
    Back(Items.BACK_10166);

    val labelId: Int get() = 56 + (ordinal * 2)
    val iconId: Int get() = 165 + (ordinal * 2)
}