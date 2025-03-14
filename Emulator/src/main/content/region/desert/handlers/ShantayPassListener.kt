package content.region.desert.handlers

import content.global.handlers.iface.warning.Warnings
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.DARK_RED
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class ShantayPassListener : InteractionListener {
    companion object {
        private const val SHANTAY = NPCs.SHANTAY_836
        private const val SHANTAY_NPC = NPCs.SHANTAY_GUARD_838
        private const val SHANTAY_PASS_TICKET = Items.SHANTAY_PASS_1854
        private const val SHANTAY_CHEST = Scenery.SHANTAY_CHEST_2693
        private const val JAIL_DOOR = Scenery.JAIL_DOOR_35401
        private const val COINS = Items.COINS_995
        private val SHANTAY_SCENERY_IDS =
            intArrayOf(
                Scenery.SHANTAY_PASS_35542,
                Scenery.SHANTAY_PASS_35543,
                Scenery.SHANTAY_PASS_35544,
                Scenery.SHANTAY_PASS_35400,
            )
    }

    override fun defineListeners() {
        on(SHANTAY, IntType.NPC, "buy-pass") { player, _ ->
            if (freeSlots(player) == 0) {
                sendNPCDialogue(
                    player,
                    SHANTAY,
                    "Sorry friend, you'll need more inventory space to buy a pass.",
                    FaceAnim.NEUTRAL,
                )
                return@on true
            }
            if (!removeItem(player, Item(COINS, 5))) {
                sendNPCDialogue(
                    player,
                    SHANTAY,
                    "Sorry friend, the Shantay Pass is 5 gold coins. You don't seem to have enough money!",
                    FaceAnim.NEUTRAL,
                )
            } else {
                sendItemDialogue(player, SHANTAY_PASS_TICKET, "You purchase a Shantay Pass.")
                addItemOrDrop(player, SHANTAY_PASS_TICKET)
            }
            return@on true
        }

        on(SHANTAY_SCENERY_IDS, IntType.SCENERY, "look-at") { player, _ ->
            sendMessage(player, "You look at the huge stone gate.")
            sendDialogueLines(
                player,
                DARK_RED + "The Desert is a VERY Dangerous place. Do not enter if you are",
                DARK_RED + "afraid of dying. Beware of high temperatures, and storms, robbers,",
                DARK_RED + "and slavers. No responsibility is taken by Shantay if anything bad",
                DARK_RED + "should happen to you in any circumstances whatsoever.",
            )
            return@on true
        }

        on(SHANTAY_SCENERY_IDS, IntType.SCENERY, "go-through") { player, _ ->
            if (player.location.y < 3117) {
                sendMessage(player, "You go through the gate.")
                AgilityHandler.walk(
                    player,
                    0,
                    player.location,
                    player.location.transform(0, if (player.location.y > 3116) -2 else 2, 0),
                    null,
                    0.0,
                    null,
                )
            } else {
                if (!Warnings.SHANTAY_PASS.isDisabled) {
                    openInterface(player, Components.CWS_WARNING_10_565)
                } else {
                    if (!removeItem(player, SHANTAY_PASS_TICKET)) {
                        sendNPCDialogue(
                            player,
                            SHANTAY_NPC,
                            "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.",
                            FaceAnim.NEUTRAL,
                        )
                    } else {
                        sendMessage(player, "You go through the gate.")
                        AgilityHandler.walk(
                            player,
                            0,
                            player.location,
                            player.location.transform(0, if (player.location.y > 3116) -2 else 2, 0),
                            null,
                            0.0,
                            null,
                        )
                    }
                }
            }
            return@on true
        }

        on(SHANTAY_SCENERY_IDS, IntType.SCENERY, "quick-pass") { player, _ ->
            if (player.location.y > 3116) {
                if (!inInventory(player, SHANTAY_PASS_TICKET, 1)) {
                    sendNPCDialogue(
                        player,
                        SHANTAY_NPC,
                        "You need a Shantay pass to get through this gate. See Shantay, he will sell you one for a very reasonable price.",
                        FaceAnim.NEUTRAL,
                    )
                    return@on true
                }
                if (!removeItem(player, SHANTAY_PASS_TICKET, Container.INVENTORY)) {
                    sendMessage(player, "An error occurred while trying to remove your Shantay pass. Please try again.")
                    return@on false
                }
                sendMessage(player, "You hand your Shantay pass to the guard and pass through the gate.")
            }
            AgilityHandler.walk(
                player,
                0,
                player.location,
                player.location.transform(0, if (player.location.y > 3116) -2 else 2, 0),
                null,
                0.0,
                null,
            )
            return@on true
        }

        on(JAIL_DOOR, IntType.SCENERY, "open") { player, node ->
            if (player.getAttribute("shantay-jail", false) && player.location.x > 3299) {
                player.removeAttribute("shantay-jail")
            }
            return@on if (!player.getAttribute("shantay-jail", false)) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                return@on true
            } else {
                player.dialogueInterpreter.open(SHANTAY, null, true)
            }
        }

        on(SHANTAY_CHEST, IntType.SCENERY, "open") { player, _ ->
            player.bank.open()
            return@on true
        }

        on(SHANTAY_NPC, IntType.NPC, "bribe") { player, node ->
            player.dialogueInterpreter.open(SHANTAY_NPC, node)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, SHANTAY_SCENERY_IDS, "look-at", "go-through", "quick-pass") { player, node ->
            if (node.id in intArrayOf(35543, 35544)) {
                return@setDest node.location.transform(-1, if (player.location.y > node.location.y) 1 else -1, 0)
            } else {
                return@setDest node.location.transform(1, if (player.location.y > node.location.y) 1 else -1, 0)
            }
        }
    }
}
