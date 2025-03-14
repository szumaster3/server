package content.region.morytania.quest.ahoy.handlers

import content.region.morytania.quest.ahoy.handlers.GhostsAhoyUtils.jumpRockPath
import content.region.morytania.quest.ahoy.handlers.GiantLobsterNPC.Companion.spawnGiantLobster
import content.region.morytania.quest.ahoy.dialogue.RobinDialogueFile
import core.api.*
import core.api.quest.setQuestStage
import core.api.ui.setMinimapState
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.*

class GhostsAhoyListener :
    InteractionListener,
    InterfaceListener {
    override fun defineListeners() {
        on(Scenery.DOOR_5244, IntType.SCENERY, "open") { player, node ->
            if (node.location == Location(3461, 3555, 0)) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                return@on true
            }
            if (inInventory(player, Items.BONE_KEY_4272)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                if (player.location.x == 3655) {
                    sendMessage(player, "You unlock the door.")
                }
            } else {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            npc = NPC(NPCs.GHOST_DISCIPLE_1686)
                            when (stage) {
                                0 ->
                                    if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
                                        npc("Woooo wooooo woooo").also { stage = 3 }
                                    } else {
                                        npc("What are you doing going in there?").also { stage++ }
                                    }
                                1 -> player("Err, I was just curious...").also { stage++ }
                                2 ->
                                    npc(
                                        "Inside that room is a coffin, inside which lie",
                                        "the mortal remains of our most glorious master,",
                                        "Necrovarus. None may enter.",
                                    ).also {
                                        stage =
                                            END_DIALOGUE
                                    }
                                3 ->
                                    sendDialogue(
                                        player,
                                        "You get the general impression that the ghost doesn't want you to open the door.",
                                    ).also {
                                        stage =
                                            END_DIALOGUE
                                    }
                            }
                        }
                    },
                )
            }
            return@on false
        }

        on(Scenery.COFFIN_5278, IntType.SCENERY, "open") { player, node ->
            sendMessage(player, "The coffin creaks open...")
            replaceScenery(node.asScenery(), Scenery.COFFIN_5279, 100, node.location)
            return@on true
        }

        on(Scenery.COFFIN_5279, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.COFFIN_5278, -1, node.location)
            return@on true
        }

        on(Scenery.COFFIN_5279, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, Items.MYSTICAL_ROBES_4247)) {
                sendItemDialogue(
                    player,
                    Items.MYSTICAL_ROBES_4247,
                    "You take the Robes of Necrovarus from the remains of his mortal body.",
                )
                addItemOrDrop(player, Items.MYSTICAL_ROBES_4247)
            } else {
                sendMessage(player, "You search the coffin and find nothing.")
            }
            return@on true
        }

        on(Items.PETITION_FORM_4283, IntType.ITEM, "count", "drop") { player, node ->
            val score =
                getAttribute(player, GhostsAhoyUtils.petitionsigns, 0)
            when (getUsedOption(player)) {
                "count" -> {
                    if (score == 0) {
                        sendMessage(player, "You haven't got any signatures yet.")
                    } else {
                        sendMessage(player, "You have obtained $score signatures.")
                    }
                }

                "drop" -> {
                    if (!removeItem(player, node.asItem())) {
                        sendMessage(player, "Nothing interesting happens.")
                    } else {
                        sendMessage(player, "You drop the petition form; it blows away in the wind.")
                        setAttribute(player, GhostsAhoyUtils.petitionsigns, 0)
                    }
                }
            }

            return@on true
        }

        on(Scenery.ROCK_5269, IntType.SCENERY, "jump-to") { player, node ->
            jumpRockPath(player)
            if (!player.location.withinDistance(node.location, 1)) return@on false
            runTask(player, 2) {
                when (player.location) {
                    Location(3605, 3550, 0) -> teleport(player, Location(3602, 3550, 0))
                    Location(3601, 3550, 0) -> teleport(player, Location(3604, 3550, 0))
                    Location(3600, 3552, 0) -> teleport(player, Location(3597, 3552, 0))
                    Location(3596, 3552, 0) -> teleport(player, Location(3599, 3552, 0))
                    Location(3595, 3553, 0) -> teleport(player, Location(3595, 3556, 0))
                    Location(3595, 3557, 0) -> teleport(player, Location(3595, 3554, 0))
                    Location(3597, 3558, 0) -> teleport(player, Location(3597, 3561, 0))
                    Location(3597, 3562, 0) -> teleport(player, Location(3597, 3559, 0))
                    Location(3598, 3564, 0) -> teleport(player, Location(3601, 3564, 0))
                    Location(3602, 3564, 0) -> teleport(player, Location(3599, 3564, 0))
                    else -> sendMessage(player.asPlayer(), "That's too far to jump!")
                }
            }
            return@on true
        }

        onUseWith(IntType.ITEM, mapPieces, *mapPieces) { player, _, _ ->
            if (!inInventory(player, Items.MAP_SCRAP_4274) ||
                !inInventory(player, Items.MAP_SCRAP_4275) ||
                !inInventory(player, Items.MAP_SCRAP_4276)
            ) {
                sendMessage(player, "You don't have all the pieces of the map yet.")
            } else if (removeItem(player, Items.MAP_SCRAP_4274) &&
                removeItem(player, Items.MAP_SCRAP_4275) &&
                removeItem(player, Items.MAP_SCRAP_4276)
            ) {
                sendItemDialogue(
                    player,
                    Items.TREASURE_MAP_4277,
                    "You piece the three map scraps together to form a complete map.",
                )
                addItemOrDrop(player, Items.TREASURE_MAP_4277)
            }
            return@onUseWith true
        }

        on(Items.TREASURE_MAP_4277, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.AHOY_ISLANDMAP_8)
            return@on true
        }

        onDig(Location(3803, 3530, 0)) { player ->
            val hasBook = hasAnItem(player, Items.BOOK_OF_HARICANTO_4248).container != null
            if (!hasBook) {
                sendItemDialogue(player, Items.BOOK_OF_HARICANTO_4248, "You unearth the Book of Haricanto.")
                addItemOrDrop(player, Items.BOOK_OF_HARICANTO_4248)
            } else {
                sendMessage(player, "You dig but find nothing.")
            }
            return@onDig
        }

        on(GhostsAhoyUtils.shipModel, IntType.ITEM, "repair") { player, _ ->
            sendDialogue(
                player,
                "You need some silk to replace the flag, something to sew it to the boat, and something to cut the flag to the right size.",
            )
            return@on true
        }

        on(GhostsAhoyUtils.silkShipModel, IntType.ITEM, "inspect") { player, _ ->
            val shipFlagColor = getAttribute(player, GhostsAhoyUtils.shipFlag, "")
            val shipBottomColor = getAttribute(player, GhostsAhoyUtils.shipBottom, "")
            val shipSkullColor = getAttribute(player, GhostsAhoyUtils.shipSkull, "")
            sendItemDialogue(
                player,
                Items.MODEL_SHIP_4254,
                "The top of the flag is $shipFlagColor. The skull emblem is $shipSkullColor. The bottom of the flag is $shipBottomColor.",
            )
            return@on true
        }

        onUseWith(IntType.ITEM, Items.SILK_950, Items.MODEL_SHIP_4253) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                sendItemDialogue(player, Items.MODEL_SHIP_4254, "You replace the toy boat's missing flag.")
                addItemOrDrop(player, Items.MODEL_SHIP_4254)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        onUseWith(
            IntType.ITEM,
            intArrayOf(Items.RED_DYE_1763, Items.YELLOW_DYE_1765, Items.BLUE_DYE_1767),
            Items.MODEL_SHIP_4254,
        ) { player, node, _ ->
            setAttribute(player, GhostsAhoyUtils.colorMatching, 0)
            if (getAttribute(player, GhostsAhoyUtils.colorMatching, 0) == 3) {
                setAttribute(player, GhostsAhoyUtils.rightShip, 1)
            }
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> {
                                setTitle(player, 3)
                                sendDialogueOptions(
                                    player,
                                    "Which part of the flag do you want to dye?",
                                    "Top half",
                                    "Bottom half",
                                    "Skull emblem",
                                )
                                stage++
                            }

                            1 -> GhostsAhoyUtils.handleDyeSelection(player, buttonID)
                        }
                    }
                },
                node.asItem(),
            )
            return@onUseWith true
        }

        on(intArrayOf(Scenery.GANGPLANK_5286, Scenery.GANGPLANK_5285), IntType.SCENERY, "cross") { player, node ->
            teleport(player, if (node.id == 5285) Location(3605, 3548, 0) else Location(3605, 3545, 1))
            sendMessage(player, "You cross the gangplank.")
            return@on true
        }

        on(Scenery.CLOSED_CHEST_5272, IntType.SCENERY, "open") { player, _ ->

            if (inBorders(player, 3619, 3543, 3617, 3541)) {
                animate(player, Animations.HUMAN_OPEN_CHEST_536)
                addScenery(Scenery.OPEN_CHEST_5273, Location(3618, 3542, 0), 0, 10)
                removeScenery(
                    core.game.node.scenery
                        .Scenery(Scenery.CLOSED_CHEST_5270, Location(3618, 3542, 0)),
                )
            } else {
                animate(player, Animations.HUMAN_OPEN_CHEST_536)
                addScenery(Scenery.OPEN_CHEST_5273, Location(3606, 3564, 0), 3, 10)
                removeScenery(
                    core.game.node.scenery
                        .Scenery(Scenery.CLOSED_CHEST_5272, Location(3606, 3564, 0)),
                )
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.CHEST_KEY_2404, Scenery.CLOSED_CHEST_5270) { player, _, _ ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            removeItem(player, Items.CHEST_KEY_2404)
            sendItemDialogue(player, Items.CHEST_KEY_2404, "You unlock the chest.")
            addScenery(Scenery.OPEN_CHEST_5273, Location(3619, 3545, 1), 2, 10)
            removeScenery(
                core.game.node.scenery
                    .Scenery(Scenery.CLOSED_CHEST_5270, Location(3619, 3545, 1)),
            )
            return@onUseWith true
        }

        on(Scenery.OPEN_CHEST_5273, IntType.SCENERY, "close") { player, _ ->

            if (inBorders(player, 3604, 3563, 3607, 3565)) {
                addScenery(Scenery.CLOSED_CHEST_5270, Location(3606, 3564, 0), 3, 10)
                removeScenery(
                    core.game.node.scenery
                        .Scenery(Scenery.OPEN_CHEST_5273, Location(3606, 3564, 0)),
                )
            } else if (inBorders(player, 3619, 3543, 3617, 3541)) {
                addScenery(Scenery.CLOSED_CHEST_5270, Location(3618, 3542, 0), 0, 10)
                removeScenery(
                    core.game.node.scenery
                        .Scenery(Scenery.OPEN_CHEST_5273, Location(3618, 3542, 0)),
                )
            } else {
                addScenery(Scenery.CLOSED_CHEST_5270, Location(3619, 3545, 1), 2, 10)
                removeScenery(
                    core.game.node.scenery
                        .Scenery(Scenery.OPEN_CHEST_5273, Location(3619, 3545, 1)),
                )
            }
            return@on true
        }

        on(Scenery.OPEN_CHEST_5273, IntType.SCENERY, "search") { player, _ ->
            val hasMapScrap1 = hasAnItem(player, Items.MAP_SCRAP_4274).container != null
            val hasMapScrap2 = hasAnItem(player, Items.MAP_SCRAP_4276).container != null
            val hasMapScrap3 = hasAnItem(player, Items.MAP_SCRAP_4275).container != null
            if (inBorders(player, 3618, 3544, 3620, 3546)) {
                if (!hasMapScrap1 && freeSlots(player) > 1) {
                    addItem(player, Items.MAP_SCRAP_4274, 1)
                    sendItemDialogue(player, Items.MAP_SCRAP_4274, "You find a piece of a map inside the chest.")
                } else if (!hasMapScrap1 && freeSlots(player) < 1) {
                    sendItemDialogue(
                        player,
                        Items.MAP_SCRAP_4274,
                        "You unlock the chest and find a map inside but you don't have enough room to take it.",
                    )
                } else if (hasMapScrap1 && freeSlots(player) > 1) {
                    sendDialogue(player, "You already have the map from this chest.")
                }
            } else if (inBorders(player, 3604, 3563, 3607, 3565)) {
                if (!hasMapScrap2 && freeSlots(player) > 1) {
                    addItem(player, Items.MAP_SCRAP_4276, 1)
                    sendItemDialogue(player, Items.MAP_SCRAP_4276, "You find a piece of a map inside the chest.")
                } else if (!hasMapScrap2 && freeSlots(player) < 1) {
                    sendItemDialogue(
                        player,
                        Items.MAP_SCRAP_4276,
                        "You find a map inside but you don't have enough room to take it.",
                    )
                } else if (hasMapScrap2 && freeSlots(player) > 1) {
                    sendDialogue(player, "You already have the map from this chest.")
                }
            } else if (inBorders(player, 3619, 3543, 3617, 3541)) {
                if (getAttribute(player, GhostsAhoyUtils.lastMapScrap, false)) {
                    if (!hasMapScrap3 && freeSlots(player) > 1) {
                        addItem(player, Items.MAP_SCRAP_4275, 1)
                        sendItemDialogue(player, Items.MAP_SCRAP_4275, "You find a piece of a map inside the chest.")
                    } else if (!hasMapScrap2 && freeSlots(player) < 1) {
                        sendItemDialogue(
                            player,
                            Items.MAP_SCRAP_4275,
                            "You find a map inside but you don't have enough room to take it.",
                        )
                    } else if (hasMapScrap2 && freeSlots(player) > 1) {
                        sendDialogue(player, "You already have the map from this chest.")
                    }
                } else {
                    spawnGiantLobster(player)
                }
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.AHOY_BLACKOUT_7) { player, _ ->
            setMinimapState(player, 2)
            if (!inBorders(player, 3788, 3556, 3797, 3562)) {
                sendString(
                    player,
                    "After a long boat trip you arrive at Dragontooth Island...",
                    Components.AHOY_BLACKOUT_7,
                    1,
                )
            } else {
                sendString(
                    player,
                    "After a long boat trip you return to Port Phasmatys...",
                    Components.AHOY_BLACKOUT_7,
                    1,
                )
            }
            return@onOpen true
        }

        onClose(Components.AHOY_BLACKOUT_7) { player, _ ->
            setMinimapState(player, 0)
            return@onClose true
        }

        onClose(Components.AHOY_RUNEDRAW_9) { player, _ ->
            sendMessage(player, "You've won the game of Runedraw!")
            setAttribute(player, GhostsAhoyUtils.getSignedBow, true)
            setQuestStage(player, Quests.GHOSTS_AHOY, 11)
            openDialogue(player, RobinDialogueFile())
            return@onClose true
        }
    }

    companion object {
        val mapPieces = intArrayOf(Items.MAP_SCRAP_4274, Items.MAP_SCRAP_4275, Items.MAP_SCRAP_4276)
    }
}
