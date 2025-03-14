package core.game.global

import core.api.freeSlots
import core.api.removeItem
import core.api.sendDialogue
import core.api.ui.closeDialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

object Skillcape {
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

    @JvmStatic
    fun purchase(
        player: Player,
        skill: Int,
    ): Boolean {
        if (!isMaster(player, skill)) {
            return false
        }

        if (freeSlots(player) < 2) {
            closeDialogue(player)
            sendDialogue(player, "You don't have enough space in your inventory.")
            return false
        }

        if (!removeItem(player, Item(Items.COINS_995, 99000))) {
            closeDialogue(player)
            sendDialogue(player, "You don't have enough coins.")
            return false
        } else {
            return player.inventory.add(*getItems(player, skill))
        }
    }

    @JvmStatic
    fun trim(player: Player) {
        val containers = arrayOf(player.inventory, player.equipment, player.bank)
        var skill = -1
        for (container in containers) {
            for (item in container.toArray()) {
                if (item == null || item.id < 9700) {
                    continue
                }

                skill = getCapeIndex(item)
                if (skill != -1) {
                    container.replace(Item(getTrimmed(skill).id, item.amount), item.slot)
                    skill = -1
                }
            }
        }
    }

    @JvmStatic
    fun isMaster(
        player: Player,
        skill: Int,
    ): Boolean {
        return player.getSkills().getStaticLevel(skill) == 99
    }

    @JvmStatic
    fun getItems(
        player: Player,
        skill: Int,
    ): Array<Item> {
        return arrayOf(
            Item(ids[skill] + if (player.getSkills().getMasteredSkills() > 1) 1 else 0),
            Item(ids[skill] + 2),
        )
    }

    @JvmStatic
    private fun getTrimmed(skill: Int): Item {
        return Item(ids[skill] + 1)
    }

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
