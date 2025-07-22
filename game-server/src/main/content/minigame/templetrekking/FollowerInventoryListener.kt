package content.minigame.templetrekking

import org.rs.consts.Items
import org.rs.consts.NPCs

class FollowerInventoryListener {
    companion object {
        val easyGroupNPC =
            intArrayOf(
                NPCs.FYIONA_FRAY_3634,
                NPCs.DALCIAN_FANG_3632,
                NPCs.MAGE_1513,
                NPCs.ADVENTURER_1512,
            )

        val allowedFood =
            intArrayOf(
                Items.THIN_SNAIL_MEAT_3369,
                Items.LEAN_SNAIL_MEAT_3371,
                Items.FAT_SNAIL_MEAT_3373,
                Items.SALMON_329,
                Items.STEW_2003,
                Items.COOKED_SLIMY_EEL_3381,
                Items.COOKED_MEAT_2142,
                Items.TUNA_361,
                Items.COOKED_FISHCAKE_7530,
                Items.LOBSTER_379,
                Items.BASS_365,
                Items.REDBERRY_PIE_2325,
                Items.APPLE_PIE_2323,
                Items.GARDEN_PIE_7178,
                Items.FISH_PIE_7188,
                Items.CAKE_1891,
                Items.CHOCOLATE_CAKE_1897,
                Items.COOKED_SWEETCORN_5988,
                Items.POTATO_WITH_BUTTER_6703,
            )
    }
}
