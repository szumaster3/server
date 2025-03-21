package core.game.global

import core.api.freeSlots
import core.api.removeItem
import core.api.sendDialogue
import core.api.ui.closeDialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

/**
 * The Skillcape object handles operations related to the purchasing, trimming, and checking of skillcapes for players.
 * This includes offering a player the ability to buy skillcapes, trim their skillcapes, and determine if they have achieved
 * mastery in a skill (level 99).
 */
object Skillcape {

    /**
     * The list of skillcape IDs for each skill.
     * These IDs are used for referencing specific skillcapes by their associated skill.
     */
    @JvmStatic
    val ids =
        intArrayOf(
        Items.ATTACK_CAPE_9747,
        Items.DEFENCE_CAPE_9753,
        Items.STRENGTH_CAPE_9750,
        Items.HITPOINTS_CAPE_9768,
        Items.RANGING_CAPE_9756,
        Items.PRAYER_CAPE_9759,
        Items.MAGIC_CAPE_9762,
        Items.COOKING_CAPE_9801,
        Items.WOODCUTTING_CAPE_9807,
        Items.FLETCHING_CAPE_9783,
        Items.FISHING_CAPE_9798,
        Items.FIREMAKING_CAPE_9804,
        Items.CRAFTING_CAPE_9780,
        Items.SMITHING_CAPE_9795,
        Items.MINING_CAPE_9792,
        Items.HERBLORE_CAPE_9774,
        Items.AGILITY_CAPE_9771,
        Items.THIEVING_CAPE_9777,
        Items.SLAYER_CAPE_9786,
        Items.FARMING_CAPE_9810,
        Items.RUNECRAFT_CAPE_9765,
        Items.HUNTER_CAPE_9948,
        Items.CONSTRUCT_CAPE_9789,
        Items.SUMMONING_CAPE_12169,
    )

    /**
     * Purchases a skillcape for the player if they meet the requirements (skill mastery and available inventory space).
     * Deducts coins from the player's inventory and adds the appropriate skillcape items.
     *
     * @param player The player who is purchasing the skillcape.
     * @param skill The skill index for which the player is purchasing the skillcape.
     * @return `true` if the purchase was successful, otherwise `false`.
     */
    @JvmStatic
    fun purchase(
        player: Player,
        skill: Int,
    ): Boolean {
        // Check if the player has mastered the skill (level 99)
        if (!isMaster(player, skill)) {
            return false
        }

        // Check if the player has enough free slots in their inventory
        if (freeSlots(player) < 2) {
            closeDialogue(player)
            sendDialogue(player, "You don't have enough space in your inventory.")
            return false
        }

        // Check if the player has enough coins to purchase the skillcape
        if (!removeItem(player, Item(Items.COINS_995, 99000))) {
            closeDialogue(player)
            sendDialogue(player, "You don't have enough coins.")
            return false
        } else {
            // Add the skillcape and the trimmed version to the player's inventory
            return player.inventory.add(*getItems(player, skill))
        }
    }

    /**
     * Trims the player's existing skillcapes (upgrades them to trimmed versions) if they have any in their inventory,
     * equipment, or bank. Only skillcapes that are at least level 99 are eligible for trimming.
     *
     * @param player The player whose skillcapes will be trimmed.
     */
    @JvmStatic
    fun trim(player: Player) {
        val containers = arrayOf(player.inventory, player.equipment, player.bank)
        var skill = -1
        // Iterate through the player's inventory, equipment, and bank to find skillcapes
        for (container in containers) {
            for (item in container.toArray()) {
                if (item == null || item.id < 9700) {
                    continue
                }

                // Get the skill index for the current item
                skill = getCapeIndex(item)
                if (skill != -1) {
                    // Replace the skillcape with the trimmed version
                    container.replace(Item(getTrimmed(skill).id, item.amount), item.slot)
                    skill = -1
                }
            }
        }
    }

    /**
     * Checks if the player has reached mastery in a particular skill (level 99).
     *
     * @param player The player whose skill level is being checked.
     * @param skill The skill index to check for mastery.
     * @return `true` if the player has achieved level 99 in the specified skill, otherwise `false`.
     */
    @JvmStatic
    fun isMaster(
        player: Player,
        skill: Int,
    ): Boolean = player.getSkills().getStaticLevel(skill) == 99

    /**
     * Retrieves the skillcape items for the specified skill.
     * The items returned are the basic skillcape and its corresponding trimmed version.
     *
     * @param player The player purchasing the skillcape.
     * @param skill The skill index for which the items are being retrieved.
     * @return An array of two items: the skillcape and its trimmed version.
     */
    @JvmStatic
    fun getItems(
        player: Player,
        skill: Int,
    ): Array<Item> = arrayOf(
        Item(ids[skill] + if (player.getSkills().getMasteredSkills() > 1) 1 else 0),
        Item(ids[skill] + 2),
    )

    /**
     * Retrieves the trimmed version of the skillcape for the given skill.
     *
     * @param skill The skill index for which the trimmed skillcape is needed.
     * @return The trimmed version of the skillcape item.
     */
    @JvmStatic
    private fun getTrimmed(skill: Int): Item = Item(ids[skill] + 1)

    /**
     * Gets the index of the skill that corresponds to the given skillcape item.
     * This is used to identify which skill the skillcape belongs to based on its item ID.
     *
     * @param item The skillcape item for which the index is being determined.
     * @return The index of the skill associated with the skillcape item, or `-1` if no match is found.
     */
    @JvmStatic
    private fun getCapeIndex(item: Item): Int {
        for (i in ids.indices) {
            if (ids[i] == item.id) {
                return i
            }
        }
        return -1
    }
}
