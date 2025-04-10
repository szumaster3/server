package content.global.skill.slayer

import core.api.EquipmentSlot
import core.api.getItemFromEquipment
import core.game.node.entity.player.Player
import org.rs.consts.Items

/**
 * Contains helper functions and constants for identifying and handling Slayer-related equipment.
 * Used to determine player bonuses and status effects while on Slayer tasks.
 */
object SlayerEquipmentFlags {

    /**
     * Array of all black mask item ids (from +10 charges to base variants).
     */
    private val blackMasks = (Items.BLACK_MASK_10_8901..Items.BLACK_MASK_8921).map { it }.toIntArray()

    /**
     * Array of all recognized Slayer equipment item ids.
     */
    private val slayerItems = intArrayOf(
        Items.NOSE_PEG_4168,
        Items.EARMUFFS_4166,
        Items.FACE_MASK_4164,
        *blackMasks,
        Items.SPINY_HELMET_4551,
        Items.SLAYER_CAPET_9787,
        Items.SLAYER_CAPE_9786,
        Items.SLAYER_HELMET_13263,
        Items.WITCHWOOD_ICON_8923,
        Items.MIRROR_SHIELD_4156,
    )

    /**
     * Updates the equipment flags for a given player based on currently equipped Slayer-related gear.
     *
     * @param player The player whose equipment is being evaluated.
     */
    @JvmStatic
    fun updateFlags(player: Player) {
        var flags = 0
        if (hasItem(player, Items.SLAYER_HELMET_13263)) {
            flags = 0x1F
        } else if (hasItem(player, Items.NOSE_PEG_4168)) {
            flags = 1
        } else if (hasItem(player, Items.EARMUFFS_4166)) {
            flags = flags or (1 shl 1)
        } else if (hasItem(player, Items.FACE_MASK_4164)) {
            flags = flags or (1 shl 2)
        } else if ((getItemFromEquipment(player, EquipmentSlot.HEAD)?.id ?: 0) in blackMasks) {
            flags = flags or (1 shl 3)
        } else if (hasItem(player, Items.SPINY_HELMET_4551)) {
            flags = flags or (1 shl 4)
        }

        if ((getItemFromEquipment(player, EquipmentSlot.NECK)?.id ?: 0) == Items.WITCHWOOD_ICON_8923) {
            flags = flags or (1 shl 7)
        }
        if ((getItemFromEquipment(player, EquipmentSlot.SHIELD)?.id ?: 0) == Items.MIRROR_SHIELD_4156) {
            flags = flags or (1 shl 8)
        }

        SlayerManager.getInstance(player).flags.equipmentFlags = flags
    }

    /**
     * @return True if the player is wearing a Nose Peg.
     */
    @JvmStatic
    fun hasNosePeg(player: Player): Boolean = SlayerManager.getInstance(player).flags.equipmentFlags and 1 == 1

    /**
     * @return True if the player is wearing Earmuffs.
     */
    @JvmStatic
    fun hasEarmuffs(player: Player): Boolean = (SlayerManager.getInstance(player).flags.equipmentFlags shr 1) and 1 == 1

    /**
     * @return True if the player is wearing a Face Mask.
     */
    @JvmStatic
    fun hasFaceMask(player: Player): Boolean = (SlayerManager.getInstance(player).flags.equipmentFlags shr 2) and 1 == 1

    /**
     * @return True if the player is wearing a Black Mask variant.
     */
    @JvmStatic
    fun hasBlackMask(player: Player): Boolean =
        (SlayerManager.getInstance(player).flags.equipmentFlags shr 3) and 1 == 1

    /**
     * @return True if the player is wearing a Spiny Helmet.
     */
    @JvmStatic
    fun hasSpinyHelmet(player: Player): Boolean =
        (SlayerManager.getInstance(player).flags.equipmentFlags shr 4) and 1 == 1

    /**
     * @return True if the player is wearing a Witchwood Icon.
     */
    @JvmStatic
    fun hasWitchwoodIcon(player: Player): Boolean =
        (SlayerManager.getInstance(player).flags.equipmentFlags shr 7) and 1 == 1

    /**
     * @return True if the player is using a Mirror Shield.
     */
    @JvmStatic
    fun hasMirrorShield(player: Player): Boolean =
        (SlayerManager.getInstance(player).flags.equipmentFlags shr 8) and 1 == 1

    /**
     * Calculates the player's Slayer damage and accuracy bonus based on their equipment.
     *
     * @param player The player to check.
     * @return The damage/accuracy multiplier (1.15 for black mask, 1.075 for Slayer cape set, or 1.0 base).
     */
    @JvmStatic
    fun getDamAccBonus(player: Player): Double {
        val isCape = SlayerManager.getInstance(player).flags.equipmentFlags == 0x3F
        val hasMask = hasBlackMask(player)
        return if (hasMask) {
            1.15
        } else if (isCape) {
            1.075
        } else {
            1.0
        }
    }

    /**
     * Checks if the player has a specific item equipped in the HEAD slot.
     *
     * @param player The player to check.
     * @param id The item id to compare.
     * @return True if the item is equipped in the HEAD slot.
     */
    private fun hasItem(
        player: Player,
        id: Int,
    ): Boolean = (getItemFromEquipment(player, EquipmentSlot.HEAD)?.id ?: 0) == id

    /**
     * Checks if the given item id belongs to any Slayer-specific gear.
     *
     * @param item The item id to check.
     * @return True if the item is recognized as Slayer equipment.
     */
    fun isSlayerEq(item: Int): Boolean = item in slayerItems
}
