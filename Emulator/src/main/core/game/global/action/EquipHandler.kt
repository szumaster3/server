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

class EquipHandler : InteractionListener {
    override fun defineListeners() {
        on(IntType.ITEM, "equip", "wield", "wear", "hold") { player, node ->
            handleEquip(player, node)
            return@on true
        }
    }

    companion object {
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
            if (lock != null && lock.isLocked) {
                if (lock.message != null) {
                    sendMessage(player, lock.message)
                }
                return
            }
            if (slot == EquipmentContainer.SLOT_WEAPON) {
                player.packetDispatch.sendString("", 92, 0)
            }
            val maximumAdd = player.inventory.getMaximumAdd(item)
            if (maximumAdd < item.amount) {
                sendMessage(player, "Not enough free space in your inventory.")
                return
            }
            val plugin = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
            if (plugin != null) {
                if (!(plugin.fireEvent("unequip", player, item) as Boolean)) {
                    return
                }
            }
            if (!InteractionListeners.run(itemId, player, item, false)) {
                return
            }
            if (player.equipment.remove(item)) {
                playAudio(player, Sounds.EQUIP_FUN_2238)
                player.dialogueInterpreter.close()
                player.inventory.add(item)

                player.dispatch(ItemUnequipEvent(itemId, slot))
            }

            if (SlayerEquipmentFlags.isSlayerEq(item.id)) {
                SlayerEquipmentFlags.updateFlags(player)
            }
        }

        @JvmStatic
        fun handleEquip(
            player: Player,
            node: Node,
        ) {
            val item = node.asItem()

            if (item == null || player.inventory[item.slot] != item || item.name.lowercase().contains("goblin mail")) {
                return
            }

            val equipStateListener = item.definition.getConfiguration<Plugin<Any>>("equipment", null)
            if (equipStateListener != null) {
                val bool = equipStateListener.fireEvent("equip", player, item)
                if (bool != true) {
                    return
                }
            }
            if (!InteractionListeners.run(node.id, player, node, true)) {
                return
            }

            val lock = player.locks.equipmentLock
            if (lock != null && lock.isLocked) {
                if (lock.message != null) {
                    sendMessage(player, lock.message)
                }
                return
            }

            if (player.equipment.add(item, item.slot, true, true)) {
                if (item.id in 13845..13857) {
                    player.debug("Registering gloves... ID: " + item.id)
                    BrawlingGlovesManager.getInstance(player).registerGlove(item.id)
                }

                player.dialogueInterpreter.close()

                playAudio(player, item.definition.getConfiguration(ItemConfigParser.EQUIP_AUDIO, 2244))

                if (player.properties.autocastSpell != null) {
                    val itemEquipmentSlot = item.definition.getConfiguration<Int>(ItemConfigParser.EQUIP_SLOT, -1)

                    if (itemEquipmentSlot == EquipmentContainer.SLOT_WEAPON) {
                        player.properties.autocastSpell = null
                        val wif = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                        wif.selectAutoSpell(-1, true)
                        wif.openAutocastSelect()
                    }
                }

                if (SlayerEquipmentFlags.isSlayerEq(item.id)) {
                    SlayerEquipmentFlags.updateFlags(player)
                }

                player.dispatch(ItemEquipEvent(item.id, item.slot))
            }
        }
    }
}
