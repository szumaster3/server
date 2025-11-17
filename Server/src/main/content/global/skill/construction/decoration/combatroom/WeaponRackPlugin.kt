package content.global.skill.construction.decoration.combatroom

import content.global.skill.construction.BuildHotspot
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Scenery

class WeaponRackPlugin : InteractionListener {
    override fun defineListeners() {
        on(WEAPON_RACK_FURNITURE_IDS, IntType.SCENERY, "search") { player, node ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space for that.")
                return@on true
            }

            lock(player, 3)
            openDialogue(player, WeaponRackDialogue(node))
            return@on true
        }

        onEquip(BOXING_GLOVES_ITEM_IDS) { player, _ ->
            val glovesSlot = getItemFromEquipment(player, EquipmentSlot.HANDS)
            if (glovesSlot != null) {
                sendMessage(player, "You cannot wear the boxing gloves over other gloves.")
                return@onEquip false
            }
            return@onEquip true
        }
    }

    private inner class WeaponRackDialogue(private val node: Node) : DialogueFile() {

        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> showTopics(
                    Topic("Red boxing gloves", 1, true),
                    Topic("Blue boxing gloves", 2, true),
                    IfTopic("Wooden sword", 3, node.id != Scenery.GLOVE_RACK_13381, true),
                    IfTopic("Wooden shield", 4, node.id != Scenery.GLOVE_RACK_13381, true),
                    IfTopic("Pugel", 5, node.id == Scenery.WEAPONS_RACK_13383, true),
                    title = "What do you want to take?"
                )
                1 -> takeItem(Items.BOXING_GLOVES_7671, "You take boxing gloves from the rack.")
                2 -> takeItem(Items.BOXING_GLOVES_7673, "You take boxing gloves from the rack.")
                3 -> takeItem(Items.WOODEN_SWORD_7675, "You take wooden sword from the rack.")
                4 -> takeItem(Items.WOODEN_SHIELD_7676, "You take wooden shield from the rack.")
                5 -> handleSpecialWeapon()
            }
        }

        private fun takeItem(itemId: Int, message: String) {
            end()
            addItem(player!!, itemId, 1)
            sendMessage(player!!, message)
        }

        private fun handleSpecialWeapon() {
            if (!isBalanceBeamBuilt()) {
                end()
                sendMessage(player!!, "You must build a balance beam first!")
                return
            }

            if (getItemFromEquipment(player!!, EquipmentSlot.WEAPON) != null &&
                getItemFromEquipment(player!!, EquipmentSlot.SHIELD) != null
            ) {
                end()
                sendMessage(player!!, "You cannot wield items in both hands.")
                return
            }

            player!!.equipment.replace(Item(Items.PUGEL_7679), 3)
        }

        private fun isBalanceBeamBuilt(): Boolean {
            val room = player!!.houseManager.getRoom(player!!.location) ?: return false
            return room.isBuilt(BuildHotspot.CR_RING4)
        }
    }

    companion object {
        private val WEAPON_RACK_FURNITURE_IDS = intArrayOf(
            Scenery.GLOVE_RACK_13381,
            Scenery.WEAPONS_RACK_13382,
            Scenery.WEAPONS_RACK_13383
        )

        private val BOXING_GLOVES_ITEM_IDS = intArrayOf(
            Items.BOXING_GLOVES_7671,
            Items.BOXING_GLOVES_7673
        )
    }
}