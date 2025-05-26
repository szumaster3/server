package core.game.global.action

import content.global.handlers.item.equipment.gloves.BrawlingGlovesManager
import content.global.skill.slayer.SlayerEquipmentFlags
import core.api.playAudio
import core.api.sendMessage
import core.game.container.impl.EquipmentContainer
import core.game.event.ItemEquipEvent
import core.game.event.ItemUnequipEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InteractionListeners
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.system.config.ItemConfigParser
import core.plugin.Plugin
import org.rs.consts.Sounds

/**
 * `EquipHandler` manages the logic for equipping and unequipping items for players.
 * It handles equipping items to the player's equipment slots, playing appropriate sound effects,
 * and dispatching related events. The class also manages item-specific logic, such as brawling gloves
 * and Slayer equipment flags.
 */
class EquipHandler : InteractionListener {
    /**
     * Defines listeners for item-related interactions like equipping, wielding, wearing, or holding an item.
     * These listeners trigger the respective methods to handle item equipping.
     */
    override fun defineListeners() {
        on(IntType.ITEM, "equip", "wield", "wear", "hold") { player, node ->
            handleEquip(player, node)
            return@on true
        }
    }

    companion object {
        /**
         * Unequips an item from the player's equipment. It first checks for valid conditions such as
         * ensuring the item is in the player's equipment, the player is not locked from unequipping, and
         * there is enough space in the inventory. If successful, the item is removed from the equipment
         * and added to the player's inventory.
         *
         * @param player The player who is unequipping the item.
         * @param slot The equipment slot where the item is equipped.
         * @param itemId The ID of the item to unequip.
         */
        @JvmStatic
        fun unequip(
            player: Player,
            slot: Int,
            itemId: Int,
        ) {
            if (slot < 0 || slot > 13) {
                return
            }
            val item = player.equipment[slot] ?: return
            val lock = player.locks.equipmentLock
            if (lock != null && lock.isLocked()) {
                if (lock.message != null) {
                    sendMessage(player, lock.message!!)
                }
                return
            }

            // If unequipping the weapon, clear the weapon display in the interface
            if (slot == EquipmentContainer.SLOT_WEAPON) {
                player.packetDispatch.sendString("", 92, 0)
            }

            val maximumAdd = player.inventory.getMaximumAdd(item)
            if (maximumAdd < item.amount) {
                sendMessage(player, "Not enough free space in your inventory.")
                return
            }

            // Fire plugin event if defined
            val plugin = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
            if (plugin != null) {
                if (!(plugin.fireEvent("unequip", player, item) as Boolean)) {
                    return
                }
            }

            if (!InteractionListeners.run(itemId, player, item, false)) {
                return
            }

            // Remove the item from the equipment and add it to inventory
            if (player.equipment.remove(item)) {
                playAudio(player, Sounds.EQUIP_FUN_2238)
                player.dialogueInterpreter.close()
                player.inventory.add(item)

                // Dispatch unequip event
                player.dispatch(ItemUnequipEvent(itemId, slot))
            }

            // Update Slayer equipment flags if necessary
            if (SlayerEquipmentFlags.isSlayerEq(item.id)) {
                SlayerEquipmentFlags.updateFlags(player)
            }
        }

        /**
         * Handles equipping an item to the player. It performs various checks, including ensuring the
         * player has the item in their inventory, that they are allowed to equip it (through plugin logic),
         * and that there are no locks on equipping. If successful, the item is added to the player's equipment.
         * It also handles special item logic, such as brawling gloves and Slayer equipment flags.
         *
         * @param player The player who is equipping the item.
         * @param node The item node representing the item to equip.
         */
        @JvmStatic
        fun handleEquip(
            player: Player,
            node: Node,
        ) {
            val item = node.asItem()

            // If the item is not valid or the player doesn't own it, return
            if (item == null || player.inventory[item.slot] != item || item.name.lowercase().contains("goblin mail")) {
                return
            }

            // Fire the equipment plugin event if defined
            val equipStateListener = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
            if (equipStateListener != null) {
                val bool = equipStateListener.fireEvent("equip", player, item)
                if (bool != true) {
                    return
                }
            }

            // Run the interaction listeners for equipping the item
            if (!InteractionListeners.run(node.id, player, node, true)) {
                return
            }

            val lock = player.locks.equipmentLock
            if (lock != null && lock.isLocked()) {
                if (lock.message != null) {
                    sendMessage(player, lock.message!!)
                }
                return
            }

            // Add the item to the equipment if there is space
            if (player.equipment.add(item, item.slot, true, true)) {
                // Handle special cases like Brawling Gloves
                if (item.id in 13845..13857) {
                    player.debug("Registering gloves... ID: " + item.id)
                    BrawlingGlovesManager.getInstance(player).registerGlove(item.id)
                }

                // Close any open dialogues and play the equip sound
                player.dialogueInterpreter.close()
                playAudio(player, item.definition.getConfiguration(ItemConfigParser.EQUIP_AUDIO, 2244))

                // Clear any autocast spells if necessary
                if (player.properties.autocastSpell != null) {
                    val itemEquipmentSlot = item.definition.getConfiguration<Int>(ItemConfigParser.EQUIP_SLOT, -1)
                    if (itemEquipmentSlot == EquipmentContainer.SLOT_WEAPON) {
                        player.properties.autocastSpell = null
                        val wif = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                        wif.selectAutoSpell(-1, true)
                        wif.openAutocastSelect()
                    }
                }

                // Update Slayer equipment flags if necessary
                if (SlayerEquipmentFlags.isSlayerEq(item.id)) {
                    SlayerEquipmentFlags.updateFlags(player)
                }

                // Dispatch equip event
                player.dispatch(ItemEquipEvent(item.id, item.slot))
            }
        }
    }
}
