package content.global.skill.construction.decoration.combatroom

import content.global.skill.construction.BuildHotspot
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class StorageSpace : InteractionListener {
    private val sceneryIDs =
        intArrayOf(Scenery.GLOVE_RACK_13381, Scenery.WEAPONS_RACK_13382, Scenery.WEAPONS_RACK_13383)
    private val boxingGlovesIDs = intArrayOf(Items.BOXING_GLOVES_7671, Items.BOXING_GLOVES_7673)

    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "search") { player, r ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space for that.")
                return@on true
            }

            lock(player, 1)
            openDialogue(player, WeaponRackDialogue(player, r))
            return@on true
        }

        onEquip(boxingGlovesIDs) { player, _ ->
            if (isWearingOtherGloves(player)) {
                sendMessage(player, "You cannot wear the boxing gloves over other gloves.")
                return@onEquip false
            }
            return@onEquip true
        }
    }

    private fun isWearingOtherGloves(player: Player): Boolean {
        return getItemFromEquipment(player, EquipmentSlot.HANDS) != null
    }

    private inner class WeaponRackDialogue(
        player: Player,
        private val r: Node,
    ) : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            when (stage) {
                0 ->
                    sendDialogueOptions(
                        player!!,
                        "What do you want to take?" +
                            showTopics(
                                Topic("Red boxing gloves", 1, true),
                                Topic("Blue boxing gloves", 2, true),
                                IfTopic("Wooden sword", 3, r.id != 13381, true),
                                IfTopic("Wooden shield", 4, r.id != 13381, true),
                                IfTopic("Pugel", 5, r.id == 13383, true),
                            ),
                    )

                1 -> takeItem(player!!, Items.BOXING_GLOVES_7671, "You take boxing gloves from the rack.")
                2 -> takeItem(player!!, Items.BOXING_GLOVES_7673, "You take boxing gloves from the rack.")
                3 -> takeItem(player!!, Items.WOODEN_SWORD_7675, "You take wooden sword from the rack.")
                4 -> takeItem(player!!, Items.WOODEN_SHIELD_7676, "You take wooden shield from the rack.")
                5 -> handleSpecialWeapon(player!!, r)
            }
        }

        private fun takeItem(
            player: Player,
            itemId: Int,
            message: String,
        ) {
            end()
            addItem(player, itemId)
            sendMessage(player, message)
        }

        private fun handleSpecialWeapon(
            player: Player,
            r: Node,
        ) {
            if (getItemFromEquipment(player, EquipmentSlot.WEAPON) != null &&
                getItemFromEquipment(
                    player,
                    EquipmentSlot.SHIELD,
                ) != null
            ) {
                end()
                sendMessage(player, "You cannot wield items in both hands.")
                return
            }
            if (!isBalanceBeamBuilt(player)) {
                end()
                sendMessage(player, "You must build a balance beam first!")
                return
            }
            player.equipment.replace(Item(Items.PUGEL_7679), 3)
        }

        private fun isBalanceBeamBuilt(player: Player): Boolean {
            val room = player.houseManager.getRoom(player.location)!!
            return room.isBuilt(BuildHotspot.CR_RING4)
        }
    }
}
