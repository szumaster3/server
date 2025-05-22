package content.region.kandarin.handlers

import content.data.items.BrokenItem
import content.region.kandarin.dialogue.TindelMerchantDialogue
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import org.rs.consts.Sounds

class TindelMerchantListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles ring the bell at Port Khazard.
         */

        on(ANTIQUE_SHOP_STALL, IntType.SCENERY, "ring-bell") { player, _ ->
            playGlobalAudio(player.location, BELL_SOUND)
            sendDialogue(player, "You ring for attention.")
            runTask(player, 1) {
                openDialogue(player, TindelMerchantDialogue())
            }
            return@on true
        }

        /*
         * Handles interaction options with Tindel Merchant NPCs.
         */

        on(TINDEL, IntType.NPC, "talk-to", "Give-Sword") { player, _ ->
            when (getUsedOption(player)) {
                "talk-to" -> openDialogue(player, TindelMerchantDialogue())
                "give-sword" -> exchangeRustyWeapon(player)
                else -> sendMessage(player, "You can't reach!")
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(TINDEL), "talk-to", "give-sword") { _, _ ->
            return@setDest Location(2678, 3152, 0)
        }
    }

    companion object {
        private const val TINDEL = NPCs.TINDEL_MARCHANT_1799
        private const val BELL_SOUND = Sounds.BELL_2192
        private const val ANTIQUE_SHOP_STALL = Scenery.ANTIQUES_SHOP_STALL_5831
        private const val RUSTY_SWORD = Items.RUSTY_SWORD_686
        private const val RUSTY_SCIMITAR = Items.RUSTY_SCIMITAR_6721
        private const val FAKE_COINS = Items.COINS_8896

        /**
         * Exchanges a rusty items for a random repaired weapon.
         *
         * @param player The player.
         * @return item.
         */
        fun exchangeRustyWeapon(player: Player): Boolean {
            val rustyItemId = when {
                inInventory(player, RUSTY_SWORD) -> RUSTY_SWORD
                inInventory(player, RUSTY_SCIMITAR) -> RUSTY_SCIMITAR
                else -> {
                    sendNPCDialogue(player, TINDEL, "Sorry my friend, but you don't seem to have any swords that need to be identified.", FaceAnim.HALF_GUILTY)
                    return false
                }
            }

            if (!inInventory(player, Items.COINS_995, 100)) {
                sendNPCDialogue(player, TINDEL, "Sorry, you don't have enough coins.", FaceAnim.HALF_GUILTY)
                return false
            }

            sendDoubleItemDialogue(
                player,
                rustyItemId,
                FAKE_COINS,
                "You hand Tindel 100 coins plus the ${getItemName(rustyItemId).lowercase()}."
            )

            addDialogueAction(player) { _, _ ->
                val smithingLevel = getStatLevel(player, Skills.SMITHING)
                val chance = RandomFunction.getSkillSuccessChance(50.0, 100.0, smithingLevel)
                val success = RandomFunction.random(0.0, 100.0) < chance

                val equipmentType = when (rustyItemId) {
                    RUSTY_SWORD -> BrokenItem.EquipmentType.SWORDS
                    RUSTY_SCIMITAR -> BrokenItem.EquipmentType.SCIMITARS
                    else -> null
                }

                if (equipmentType == null) {
                    sendNPCDialogue(player, TINDEL, "Sorry my friend, but you don't seem to have any swords that need to be identified.", FaceAnim.HALF_GUILTY)
                    return@addDialogueAction
                }

                val rustyItem = Item(rustyItemId, 1)
                val repairedItem = BrokenItem.getRepair(equipmentType)
                val itemName = getItemName(repairedItem!!.id).lowercase()

                removeItem(player, Item(Items.COINS_995, 100))
                removeItem(player, rustyItem)

                if (success) {
                    sendItemDialogue(player, repairedItem.id, "Tindel gives you a $itemName.")
                    addItem(player, repairedItem.id, 1)
                } else {
                    sendNPCDialogue(player, TINDEL, "Sorry my friend, but the item wasn't worth anything. I've disposed of it for you.", FaceAnim.HALF_GUILTY)
                }
            }

            return true
        }
    }
}
