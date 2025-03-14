package content.region.karamja.handlers.shilo

import content.region.karamja.dialogue.shilovillage.BlackPrismDialogue
import content.region.karamja.handlers.shilo.ShiloVillageListener.Companion
import core.api.*
import core.api.quest.hasRequirement
import core.api.ui.closeDialogue
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.*

class ShiloVillageListener : InteractionListener {
    override fun defineListeners() {
        on(NOTES, IntType.ITEM, "drop") { player, node ->
            removeItem(player, node.asItem())
            sendMessage(player, "As you drop the delicate scrolls onto the floor, they disintegrate immediately.")
            return@on true
        }

        on(BLACKSMITH_DOOR, IntType.SCENERY, "open") { player, node ->
            if (!getAttribute(player, "shilo-village:blacksmith-doors", false)) {
                sendNPCDialogue(
                    player,
                    NPCs.YOHNUS_513,
                    "Sorry but the blacksmiths is closed. But I can let you use the furnace at the cost of 20 gold pieces.",
                )
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        on(Scenery.BROKEN_CART_2216, IntType.SCENERY, "look-at") { player, node ->
            if (!hasRequirement(player, "Shilo Village")) return@on true
            var location = Location(0, 0)
            val playerloc = Location(player.location.x, player.location.y)
            if (node.id == 2216 && getUsedOption(player) == "look-at") {
                if (playerloc.x > 2878) {
                    location = Location(2876, 2952)
                } else if (playerloc.x < 2879) {
                    location = Location(2880, 2952)
                }
            }

            lock(player, 1)
            sendMessage(player, "You climb up onto the cart.")
            sendMessage(player, "You nimbly jump from one side of the cart...")
            player.impactHandler.disabledTicks = 4
            submitWorldPulse(
                object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        unlock(player)
                        teleport(player, location)
                        sendMessage(player, "...to the other and climb down again.")
                        resetAnimator(player)
                        return true
                    }
                },
            )
            return@on true
        }

        on(Scenery.TRAVEL_CART_2230, IntType.SCENERY, "board") { player, _ ->
            openDialogue(player, CartTravelDialogue(), NPC(NPCs.HAJEDY_510))
            return@on true
        }

        on(NPCs.HAJEDY_510, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, CartTravelDialogue(), NPC(NPCs.HAJEDY_510))
            return@on true
        }

        on(Scenery.TRAVEL_CART_2230, IntType.SCENERY, "pay-fare") { player, _ ->
            openDialogue(player, CartQuickPay(), NPC(NPCs.HAJEDY_510))
            return@on true
        }

        on(NPCs.HAJEDY_510, IntType.NPC, "pay-fare") { player, _ ->
            openDialogue(player, CartQuickPay(), NPC(NPCs.HAJEDY_510))
            return@on true
        }

        on(Scenery.TRAVEL_CART_2265, IntType.SCENERY, "board") { player, _ ->
            openDialogue(player, CartTravelDialogue(), NPC(NPCs.VIGROY_511))
            return@on true
        }

        on(Scenery.TRAVEL_CART_2265, IntType.SCENERY, "pay-fare") { player, _ ->
            openDialogue(player, CartQuickPay(), NPC(NPCs.VIGROY_511))
            return@on true
        }

        on(NPCs.VIGROY_511, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, CartTravelDialogue(), npc)
            return@on true
        }

        on(NPCs.VIGROY_511, IntType.NPC, "pay-fare") { player, npc ->
            openDialogue(player, CartQuickPay(), npc)
            return@on true
        }

        onUseWith(IntType.NPC, ANTIQUE_ITEMS, YANNI) { player, used, _ ->
            val item = AntiqueItem.getAntiqueItem(used.id) ?: return@onUseWith true
            if (amountInInventory(player, used.id) == 1) {
                if (!inInventory(player, used.id)) {
                    sendNPCDialogue(player, NPCs.YANNI_SALIKA_515, "Sorry Bwana, you have nothing I am interested in.")
                } else {
                    openDialogue(
                        player,
                        object : DialogueFile() {
                            override fun handle(
                                componentID: Int,
                                buttonID: Int,
                            ) {
                                npc = NPC(NPCs.YANNI_SALIKA_515)
                                when (stage) {
                                    0 -> sendNPCDialogue(player, NPCs.YANNI_SALIKA_515, item.dialogue).also { stage++ }
                                    1 -> sendNPCDialogue(player, NPCs.YANNI_SALIKA_515, item.message).also { stage++ }
                                    2 -> {
                                        setTitle(player, 2)
                                        sendDialogueOptions(
                                            player,
                                            "Sell the " + getItemName(used.id) + "?",
                                            "Yes.",
                                            "No.",
                                        ).also { stage++ }
                                    }

                                    3 ->
                                        when (buttonID) {
                                            1 -> {
                                                end()
                                                if (removeItem(player, used.id)) {
                                                    sendNPCDialogue(
                                                        player,
                                                        NPCs.YANNI_SALIKA_515,
                                                        "Here's " + item.price + " for it.",
                                                    ).also { stage++ }
                                                    sendMessage(
                                                        player,
                                                        "You sell the " + getItemName(used.id) + " for " + item.price +
                                                            " gold.",
                                                    )
                                                    addItem(player, Items.COINS_995, item.price)
                                                }
                                            }

                                            2 -> end()
                                        }
                                }
                            }
                        },
                    )
                }
                if (used.id == Items.BLACK_PRISM_4808) {
                    openDialogue(player, BlackPrismDialogue())
                }
            }
            return@onUseWith true
        }

        on(WOODEN_GATE, IntType.SCENERY, "open") { player, node ->
            if (player.location.x == 2867) {
                DoorActionHandler.autowalkFence(player, node.asScenery(), 2262, 2261)
                sendMessage(player, "You make your way out of Shilo Village.")
            } else {
                DoorActionHandler.autowalkFence(player, node.asScenery(), 2262, 2261)
            }
            return@on true
        }

        on(METAL_GATE, IntType.SCENERY, "open") { player, node ->
            if (player.location.x == 2874) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "You open the gates and make you way back out of the village.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }
    }

    companion object {
        private const val YANNI = NPCs.YANNI_SALIKA_515
        private const val NOTES = Items.BERVIRIUS_NOTES_624
        private const val BLACKSMITH_DOOR = Scenery.BLACKSMITH_S_DOOR_2266
        private val WOODEN_GATE = intArrayOf(Scenery.WOODEN_GATE_2261, Scenery.WOODEN_GATE_2262)
        private val METAL_GATE = intArrayOf(Scenery.METAL_GATE_2259, Scenery.METAL_GATE_2260)
        val ANTIQUE_ITEMS =
            intArrayOf(
                Items.BONE_KEY_605,
                Items.STONE_PLAQUE_606,
                Items.TATTERED_SCROLL_607,
                Items.CRUMPLED_SCROLL_608,
                Items.LOCATING_CRYSTAL_611,
                Items.BEADS_OF_THE_DEAD_616,
                Items.BERVIRIUS_NOTES_624,
                Items.BLACK_PRISM_4808,
            )

        class CartQuickPay : DialogueFile() {
            override fun handle(
                componentID: Int,
                buttonID: Int,
            ) {
                if (!hasRequirement(player!!, "Shilo Village", true)) return
                val shilo = npc?.id == 510
                when (stage) {
                    0 ->
                        if (inInventory(player!!, Items.COINS_995, 10)) {
                            sendDialogue(
                                player!!,
                                "You pay the fare and hand 10 gold coins to " + (npc?.name ?: "") + ".",
                            ).also { stage++ }
                        } else {
                            sendMessage(player!!, "You don't have enough coins.").also { stage = END_DIALOGUE }
                        }

                    1 ->
                        if (removeItem(player!!, Item(Items.COINS_995, 10))) {
                            closeDialogue(player!!)
                            closeOverlay(player!!)
                            openOverlay(player!!, Components.FADE_TO_BLACK_120)
                            queueScript(player!!, 3, QueueStrength.SOFT) {
                                teleport(
                                    player!!,
                                    if (shilo) Location.create(2834, 2951, 0) else Location.create(2780, 3212, 0),
                                )
                                closeOverlay(player!!)
                                openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                                sendDialogueLines(
                                    player!!,
                                    "You feel tired from the journey, but at least you didn't have to walk",
                                    "all that distance.",
                                )
                                return@queueScript stopExecuting(player!!)
                            }
                            stage = END_DIALOGUE
                        }
                }
            }
        }
    }
}

class CartTravelDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (!hasRequirement(player!!, Quests.SHILO_VILLAGE)) return
        val shilo = npc?.id == 510
        when (stage) {
            0 ->
                npcl(
                    "I am offering a cart ride to " + (if (shilo) "Shilo Village" else "Brimhaven") +
                        " if you're interested? It will cost 10 gold coins. Is that Ok?",
                ).also { stage++ }
            1 -> {
                if (!inInventory(player!!, Items.COINS_995, 10)) {
                    playerl("Sorry, I don't seem to have enough coins.").also { stage = END_DIALOGUE }
                } else {
                    playerl(
                        "Yes please, I'd like to go to " + (if (shilo) "Shilo Village" else "Brimhaven") + ".",
                    ).also { stage++ }
                }
            }
            2 -> npcl("Great! Just hop into the cart then and we'll go!").also { stage++ }
            3 ->
                sendDialogue(
                    player!!,
                    "You hop into the cart and the driver urges the horses on. You take a taxing journey through the jungle to " +
                        (if (shilo) "Shilo Village" else "Brimhaven") +
                        ".",
                ).also { stage++ }
            4 -> end().also { openDialogue(player!!, Companion.CartQuickPay(), npc!!) }
        }
    }
}
