package content.region.morytania.port_phasmatys.quest.ahoy.plugin

import content.region.morytania.port_phasmatys.quest.ahoy.dialogue.RobinDialogueFile
import content.region.morytania.port_phasmatys.quest.ahoy.npc.GiantLobsterNPC.Companion.spawnGiantLobster
import content.region.morytania.port_phasmatys.quest.ahoy.plugin.GhostsAhoyUtils.jumpRockPath
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import shared.consts.*
import shared.consts.Scenery as Objects

class GhostsAhoyListener : InteractionListener, InterfaceListener {

    override fun defineListeners() {
        /*
        on(Objects.DOOR_5244, IntType.SCENERY, "open") { player, node ->
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
                openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        npc = NPC(NPCs.GHOST_DISCIPLE_1686)
                        when (stage) {
                            0 -> if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
                                npc("Woooo wooooo woooo").also { stage = 3 }
                            } else {
                                npc("What are you doing going in there?").also { stage++ }
                            }
                            1 -> player("Err, I was just curious...").also { stage++ }
                            2 -> npc("Inside that room is a coffin, inside which lie", "the mortal remains of our most glorious master,", "Necrovarus. None may enter.").also { stage = END_DIALOGUE }
                            3 -> sendDialogue(player, "You get the general impression that the ghost doesn't want you to open the door.").also { stage = END_DIALOGUE }
                        }
                    }
                }
                )
            }
            return@on false
        }
        */

        on(Objects.COFFIN_5278, IntType.SCENERY, "open") { player, node ->
            sendMessage(player, "The coffin creaks open...")
            replaceScenery(node.asScenery(), Objects.COFFIN_5279, -1, node.location)
            return@on true
        }

        on(Objects.COFFIN_5279, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Objects.COFFIN_5278, -1, node.location)
            return@on true
        }

        on(Objects.COFFIN_5279, IntType.SCENERY, "search") { player, _ ->
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

        on(Objects.ROCK_5269, IntType.SCENERY, "jump-to") { player, node ->
            jumpRockPath(player)
            if (!player.location.withinDistance(node.location, 1)) return@on false

            runTask(player, 2) {
                val target = GhostsAhoyUtils.rockJumpMap[player.location]
                if (target != null) teleport(player, target)
                else sendMessage(player.asPlayer(), "That's too far to jump!")
            }

            return@on true
        }

        onUseWith(IntType.ITEM, mapPieces, *mapPieces) { player, _, _ ->
            val requiredPieces = listOf(
                Items.MAP_SCRAP_4274,
                Items.MAP_SCRAP_4275,
                Items.MAP_SCRAP_4276
            )

            if (requiredPieces.any { !inInventory(player, it) }) {
                sendMessage(player, "You don't have all the pieces of the map yet.")
            } else if (requiredPieces.all { removeItem(player, it) }) {
                sendItemDialogue(player, Items.TREASURE_MAP_4277, "You piece the three map scraps together to form a complete map.")
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

        onUseWith(IntType.ITEM, intArrayOf(Items.RED_DYE_1763, Items.YELLOW_DYE_1765, Items.BLUE_DYE_1767), Items.MODEL_SHIP_4254) { player, node, _ ->
            setAttribute(player, GhostsAhoyUtils.colorMatching, 0)
            if (getAttribute(player, GhostsAhoyUtils.colorMatching, 0) == 3) {
                setAttribute(player, GhostsAhoyUtils.rightShip, 1)
            }
            openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> {
                                setTitle(player, 3)
                                sendOptions(player, "Which part of the flag do you want to dye?", "Top half", "Bottom half", "Skull emblem")
                                stage++
                            }

                            1 -> GhostsAhoyUtils.handleDyeSelection(player, buttonID)
                        }
                    }
                }, node.asItem()
            )
            return@onUseWith true
        }

        on(intArrayOf(Objects.GANGPLANK_5286, Objects.GANGPLANK_5285), IntType.SCENERY, "cross") { player, node ->
            teleport(player, if (node.id == 5285) Location(3605, 3548, 0) else Location(3605, 3545, 1))
            sendMessage(player, "You cross the gangplank.")
            return@on true
        }

        on(Objects.CLOSED_CHEST_5272, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.CHEST_KEY_2404, Objects.CLOSED_CHEST_5270) { player, _, with ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            removeItem(player, Items.CHEST_KEY_2404)
            if (removeItem(player, Items.CHEST_KEY_2404)) {
                animate(player, Animations.HUMAN_OPEN_CHEST_536)
                sendItemDialogue(player, Items.CHEST_KEY_2404, "You unlock the chest.")
                replaceScenery(with.asScenery(), with.id + 3, -1)
            }
            return@onUseWith true
        }

        on(Objects.OPEN_CHEST_5273, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), node.id - 3, -1)
            return@on true
        }

        on(Objects.OPEN_CHEST_5273, IntType.SCENERY, "search") { player, _ ->
            fun giveScrap(player: Player, hasScrap: Boolean, scrapId: Int) {
                if (hasScrap) {
                    sendDialogue(player, "You already have the map from this chest.")
                    return
                }

                if (freeSlots(player) > 1) {
                    addItem(player, scrapId, 1)
                    sendItemDialogue(player, scrapId, "You find a piece of a map inside the chest.")
                } else {
                    sendItemDialogue(player, scrapId, "You find a map inside but you don't have enough room to take it.")
                }
            }

            val hasA = hasAnItem(player, Items.MAP_SCRAP_4274).container != null
            val hasB = hasAnItem(player, Items.MAP_SCRAP_4276).container != null
            val hasC = hasAnItem(player, Items.MAP_SCRAP_4275).container != null

            val crabSpawned = getAttribute(player, "ghostsAhoyCrabSpawned", false)

            when {
                inBorders(player, 3618, 3544, 3620, 3546) ->
                    giveScrap(player, hasA, Items.MAP_SCRAP_4274)

                inBorders(player, 3604, 3563, 3607, 3565) ->
                    giveScrap(player, hasB, Items.MAP_SCRAP_4276)

                inBorders(player, 3619, 3543, 3617, 3541) -> {
                    if (crabSpawned) {
                        giveScrap(player, hasC, Items.MAP_SCRAP_4275)
                        return@on true
                    }

                    if (!getAttribute(player, GhostsAhoyUtils.lastMapScrap, false)) {
                        spawnGiantLobster(player)
                        return@on true
                    }

                    giveScrap(player, hasC, Items.MAP_SCRAP_4275)
                }
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.AHOY_BLACKOUT_7) { player, _ ->
            setMinimapState(player, 2)
            if (!inBorders(player, 3788, 3556, 3797, 3562)) {
                sendString(player, "After a long boat trip you arrive at Dragontooth Island...", Components.AHOY_BLACKOUT_7, 1)
            } else {
                sendString(player, "After a long boat trip you return to Port Phasmatys...", Components.AHOY_BLACKOUT_7, 1)
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
