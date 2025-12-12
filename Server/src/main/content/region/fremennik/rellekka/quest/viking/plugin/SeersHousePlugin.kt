package content.region.fremennik.rellekka.quest.viking.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*
import shared.consts.Scenery as Objects

/**
 * Handles interaction in Seers house for Fremennik trial quest.
 */
class SeersHousePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles opening the west door.
         */

        on(WEST_DOOR, IntType.SCENERY, "open") { player, node ->
            val started = getAttribute(player, GameAttributes.QUEST_VIKING_PEER_START, false)
            val voted = getAttribute(player, GameAttributes.QUEST_VIKING_PEER_VOTE, false)
            val riddleProgress = getAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE, 5)
            val riddleSolved = getAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE_SOLVED, false)

            if (!started) {
                sendDialogue(player, "You should probably talk to the owner of this home.")
                return@on true
            }

            if (voted) {
                sendDialogue(player, "I don't need to go through that again.")
                return@on true
            }

            when {
                riddleProgress < 5 -> {
                    player.dialogueInterpreter.open(DoorRiddleDialogue(player), Scenery(WEST_DOOR, node.location))
                }
                riddleSolved -> {
                    val insideHouse = player.location == Location(2631, 3666, 0)
                    if (insideHouse || (player.inventory.isEmpty && player.equipment.isEmpty)) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        if (insideHouse) player.inventory.clear()
                    } else {
                        openDialogue(player, NPCs.PEER_THE_SEER_1288, findNPC(NPCs.PEER_THE_SEER_1288)!!)
                    }
                }
            }

            return@on true
        }

        /*
         * Handles opening the west door.
         */

        on(WEST_LADDER, IntType.SCENERY, "Climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location.create(2631, 3664, 2))
            return@on true
        }

        /*
         * Handles climbing down the west trapdoor.
         */

        on(WEST_TRAP_DOOR.id, IntType.SCENERY, "Climb-down") { player, _ ->
            if (player.location.x < 2634) {
                ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location.create(2631, 3664, 0))
            } else if (player.location.x > 2634) {
                ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location.create(2636, 3664, 0))
            }
            return@on true
        }

        /*
         * Handles closing the west trapdoor.
         */

        on(WEST_TRAP_DOOR.id, IntType.SCENERY, "Close") { _, node ->
            replaceScenery(node.asScenery(), Objects.TRAPDOOR_4174, -1)
            return@on true
        }

        /*
         * Handles opening the west trapdoor.
         */

        on(WEST_TRAP_DOOR.id, IntType.SCENERY, "Open") { player, node ->
            replaceScenery(node.asScenery(), Objects.TRAPDOOR_4173, -1)
            sendMessage(player, "The trapdoor opens...")
            return@on true
        }

        /*
         * Handles opening the east trapdoor.
         */

        on(EAST_TRAP_DOOR.id, IntType.SCENERY, "Open") { player, node ->
            replaceScenery(node.asScenery(), Objects.TRAPDOOR_4173, -1)
            sendMessage(player, "The trapdoor opens...")
            return@on true
        }

        /*
         * Handles closing the east trapdoor.
         */

        on(EAST_TRAP_DOOR.id, IntType.SCENERY, "Close") { _, node ->
            replaceScenery(node.asScenery(), Objects.TRAPDOOR_4174, -1)
            return@on true
        }

        /*
         * Handles climbing down the east trapdoor.
         */

        on(EAST_TRAP_DOOR.id, IntType.SCENERY, "Climb-down") { player, _ ->
            if (player.location.x > 2634) {
                ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location.create(2636, 3664, 0))
            }
            return@on true
        }

        /*
         * Handles climbing up the east ladder.
         */

        on(EAST_LADDER, IntType.SCENERY, "Climb-Up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location.create(2636, 3664, 2))
            return@on true
        }

        /*
         * Handles searching the opened cupboard.
         */

        on(CUPBOARD_OPENED, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the cupboard...")
            if (inInventory(player, EMPTY_BUCKET, 1)) {
                sendMessage(player, "You find nothing of interest.")
            } else {
                addItem(player, EMPTY_BUCKET)
                sendMessage(player, "You find a bucket with a number five painted on it.")
            }
            return@on true
        }

        /*
         * Handles opening the closed cupboard.
         */

        on(CUPBOARD_CLOSED, IntType.SCENERY, "Open") { player, node ->
            animate(player, 542)
            playAudio(player, Sounds.CUPBOARD_OPEN_58)
            replaceScenery(node.asScenery()!!, Objects.CUPBOARD_4178, -1)
            return@on true
        }

        /*
         * Handles shutting the opened cupboard.
         */

        on(CUPBOARD_OPENED, IntType.SCENERY, "Shut") { player, node ->
            animate(player, 543)
            playAudio(player, Sounds.CUPBOARD_CLOSE_57)
            replaceScenery(node.asScenery()!!, Objects.CUPBOARD_4177, -1)
            return@on true
        }

        /*
         * Handles opening the balance chest.
         */

        on(BALANCE_CHEST, IntType.SCENERY, "Open") { player, _ ->
            sendDialogue(player, "This chest is securely locked shut. There is some kind of balance attached to the lock, and a number four is painted just above it.")
            return@on true
        }

        /*
         * Handles searching the bookcase.
         */

        on(BOOKCASE, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the bookcase...")
            if (inInventory(player, RED_HERRING, 1)) {
                sendMessage(player, "You find nothing of interest.")
            } else {
                addItem(player, RED_HERRING)
                sendMessage(player, "Hidden behind some old books, you find a red herring.")
            }
            return@on true
        }

        /*
         * Handles searching the south boxes.
         */

        on(SOUTH_BOXES, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the boxes...")

            when {
                !inInventory(player, BLUE_THREAD, 1) -> {
                    addItem(player, BLUE_THREAD)
                    sendMessage(player, "You find some thread hidden inside.")
                }
                !inInventory(player, MAGNET, 1) && inInventory(player, BLUE_THREAD, 1) -> {
                    addItem(player, MAGNET)
                    sendMessage(player, "You find a magnet hidden inside.")
                }
                else -> sendMessage(player, "You find nothing of interest.")
            }

            return@on true
        }

        /*
         * Handles opening the chest.
         */

        on(CHESTS, IntType.SCENERY, "Open") { player, node ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            playAudio(player, Sounds.CHEST_OPEN_52)
            replaceScenery(node.asScenery(), Objects.CHEST_4168, -1)
            return@on true
        }

        /*
         * Handles closing the chest.
         */

        on(CHESTS, IntType.SCENERY, "Close") { player, node ->
            animate(player, Animations.HUMAN_CLOSE_CHEST_538)
            playAudio(player, Sounds.CHEST_CLOSE_51)
            replaceScenery(node.asScenery(), Objects.CHEST_4167, -1)
            return@on true
        }

        /*
         * Handles searching the chest.
         */

        on(CHESTS, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the chest...")
            if (inInventory(player, EMPTY_JUG, 1)) {
                sendMessage(player, "You find nothing of interest.")
            } else {
                playAudio(player, Sounds.CHEST_CLOSE_51)
                animate(player, Animations.CLOSE_CHEST_539)
                sendMessage(player, "You find a jug with a number three painted on it")
                addItem(player, EMPTY_JUG)
            }
            return@on true
        }

        /*
         * Handles searching the east crates.
         */

        on(EAST_CRATES, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the crates...")
            if (inInventory(player, PICK, 1)) {
                sendMessage(player, "You find nothing of interest.")
            } else {
                addItem(player, PICK)
                sendMessage(player, "You find a small pick hidden inside.")
            }
            return@on true
        }

        /*
         * Handles searching the south crates.
         */

        on(SOUTH_CRATES, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the crates...")
            if (inInventory(player, SHIP_TOY, 1)) {
                sendMessage(player, "You find nothing of interest.")
            } else {
                addItem(player, SHIP_TOY)
                sendMessage(player, "You find a toy ship hidden inside")
            }
            return@on true
        }

        /*
         * Handles studying the bull's head.
         */

        on(BULLS_HEAD, IntType.SCENERY, "Study") { player, node ->
            if (!inInventory(player, WOODEN_DISK, 1)) {
                openDialogue(player, BullHeadDialogue(), node.asScenery())
            } else {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        /*
         * Handles studying the bull's head.
         */

        on(UNICORN_HEAD, IntType.SCENERY, "Study") { player, node ->
            if (!inInventory(player, OLD_RED_DISK, 1)) {
                openDialogue(player, UnicornHeadDialogue(), node.asScenery())
            } else {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        /*
         * Handles cooking the red herring on the cooking range.
         */

        onUseWith(IntType.SCENERY, RED_HERRING, COOKING_RANGE) { player, used, _ ->
            if(removeItem(player, used.id)) {
                playAudio(player, Sounds.FRY_2577)
                animate(player, 883)
                addItem(player, RED_GOOP)
                sendDialogue(player, "As you cook the herring on the stove, the colouring on it peels off separately as a red sticky goop...")
            }
            return@onUseWith true
        }

        /*
         * Handles coating the wooden disk with red goop.
         */

        onUseWith(IntType.ITEM, RED_GOOP, WOODEN_DISK) { player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, RED_DISK)
                sendMessage(player, "You coat the wooden coin with the sticky red goop.")
            }
            return@onUseWith true
        }

        /*
         * Handles studying the mural.
         */

        on(MURAL, IntType.SCENERY, "Study") { player, _ ->
            sendMessage(player, "The mural feels like something is missing.")
            return@on true
        }

        on(Objects.ABSTRACT_MURAL_4180, IntType.SCENERY, "Study") { player, node ->
            sendMessage(player, node.asScenery().definition.examine)
            return@on true
        }

        /*
         * Handles filling buckets from the tap.
         */

        onUseWith(IntType.SCENERY, BUCKETS, TAP) { player, bucket, _ ->
            when (bucket.id) {
                EMPTY_BUCKET,
                ONE_FIFTH_BUCKET,
                TWO_FIFTH_BUCKET,
                THREE_FIFTH_BUCKET,
                FOUR_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(bucket.id)), Item(FULL_BUCKET))
                    sendMessage(player, "You fill the bucket from the tap.")
                }
                FULL_BUCKET -> sendMessage(player, "The bucket is already full!")
                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        /*
         * Handles filling jugs from the tap.
         */

        onUseWith(IntType.SCENERY, JUGS, TAP) { player, used, _ ->
            when (used.id) {
                EMPTY_JUG, ONE_THIRD_JUG, TWO_THIRD_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(FULL_JUG))
                    sendMessage(player, "You fill the jug from the tap.")
                }
                FULL_JUG -> sendMessage(player, "The jug is already full!")
            }
            return@onUseWith true
        }

        /*
         * Handles emptying buckets into the drain.
         */

        onUseWith(IntType.SCENERY, BUCKETS, DRAIN) { player, used, _ ->
            when (used.id) {
                EMPTY_BUCKET -> sendMessage(player, "The bucket is already empty!")
                ONE_FIFTH_BUCKET, TWO_FIFTH_BUCKET, THREE_FIFTH_BUCKET, FOUR_FIFTH_BUCKET, FULL_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(EMPTY_BUCKET))
                    sendMessage(player, "You empty the bucket down the drain.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles emptying jugs into the drain.
         */

        onUseWith(IntType.SCENERY, JUGS, DRAIN) { player, used, _ ->
            when (used.id) {
                EMPTY_JUG -> sendMessage(player, "The jug is already empty!")
                ONE_THIRD_JUG, TWO_THIRD_JUG, FULL_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(EMPTY_JUG))
                    sendMessage(player, "You empty the jug down the drain.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles placing a four-fifths bucket on the balance chest.
         */

        onUseWith(IntType.SCENERY, FOUR_FIFTH_BUCKET, BALANCE_CHEST) { player, used, _ ->
            replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(VASE))
            sendMessage(player, "You place the bucket on the scale.")
            sendMessage(player, "It is a perfect counterweight and balances precisely.")
            sendMessage(player, "You take a strange looking vase out of the chest.")
            return@onUseWith true
        }

        /*
         * Handles placing disks into the mural.
         */

        onUseWith(IntType.SCENERY, DISKS, MURAL) { player, disk, with ->
            val attr = when (disk.id) {
                RED_DISK, OLD_RED_DISK -> disk.id.toString()
                WOODEN_DISK -> {
                    sendMessage(player, "You put the wooden disk into the empty hole in the mural.")
                    sendMessage(player, "It is slightly too small, and falls back out.", 1)
                    if (removeItem(player, WOODEN_DISK)) {
                        runTask(player, 1) { addItem(player, WOODEN_DISK) }
                    }
                    return@onUseWith true
                }
                else -> return@onUseWith true
            }

            val otherAttr = if (disk.id == RED_DISK) OLD_RED_DISK.toString() else RED_DISK.toString()

            when {
                getAttribute(player, otherAttr, false) -> {
                    removeItem(player, disk.id)
                    addItem(player, VASE_LID)
                    replaceScenery(with.asScenery(), Objects.ABSTRACT_MURAL_4180, 80)
                    sendMessage(player, "You put the red disk into the empty hole on the mural.")
                    sendMessage(player, "It is a perfect fit!")
                    sendMessage(player, "The center of the mural falls out!")
                }
                getAttribute(player, attr, false) -> {
                    sendMessage(player, "You already have a disk in that spot.")
                }
                else -> {
                    removeItem(player, disk.id)
                    sendMessage(player, "You put the red disk into the empty hole on the mural.")
                    sendMessage(player, "It's a perfect fit!")
                    setAttribute(player, attr, true)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles placing buckets onto the balance chest.
         */

        onUseWith(IntType.SCENERY, BUCKETS, BALANCE_CHEST) { player, bucket, _ ->
            animate(player, 883)
            sendMessage(player, "You place the bucket on the scale.")

            when (bucket.id) {
                EMPTY_BUCKET, ONE_FIFTH_BUCKET, TWO_FIFTH_BUCKET, THREE_FIFTH_BUCKET ->
                    sendMessage(player, "It is too light to balance it properly.")

                FOUR_FIFTH_BUCKET -> {
                    sendMessage(player, "It is a perfect counterweight and balances precisely.")
                    sendMessage(player, "You take a strange looking vase out of the chest.")
                    addItem(player, VASE)
                }

                FULL_BUCKET ->
                    sendMessage(player, "It is too heavy to balance it properly.")
            }

            return@onUseWith true
        }

        /*
         * Handles placing jugs onto the balance chest.
         */

        onUseWith(IntType.SCENERY, JUGS, BALANCE_CHEST) { player, _, _ ->
            animate(player, 883)
            sendMessage(player, "You place the jug on the scale.")
            sendMessage(player, "It is too light to balance it properly.")
            return@onUseWith true
        }

        /*
         * Handles placing buckets on the frozen table.
         */

        onUseWith(IntType.SCENERY, BUCKETS, FROZEN_TABLE) { player, used, _ ->
            when (used.id) {
                EMPTY_BUCKET -> sendMessage(player, "Your empty bucket gets very cold on the icy table.")
                FULL_BUCKET -> {
                    animate(player, 883)
                    replaceSlot(player, player.inventory.getSlot(Item(FULL_BUCKET)), Item(FROZEN_BUCKET))
                    sendMessage(player, "They icy table immediately freezes the water in your bucket.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles placing jugs on the frozen table.
         */

        onUseWith(IntType.SCENERY, JUGS, FROZEN_TABLE) { player, used, _ ->
            when (used.id) {
                EMPTY_JUG -> sendMessage(player, "Your empty jug gets very cold on the icy table.")
                FULL_JUG -> {
                    animate(player, 883)
                    replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(FROZEN_JUG))
                    sendMessage(player, "The icy table immediately freezes the water in your jug.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles filling a bucket from a tap.
         */

        onUseWith(IntType.SCENERY, BUCKETS, TAP) { player, bucket, _ ->
            when (bucket.id) {
                Items.EMPTY_BUCKET_3727 -> {
                    replaceSlot(player, player.inventory.getSlot(Item(EMPTY_BUCKET)), Item(FULL_BUCKET))
                    sendMessage(player, "You fill the bucket from the tap.")
                    return@onUseWith true
                }
                ONE_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(FULL_BUCKET))
                    sendMessage(player, "You fill the bucket from the tap.")
                }
                TWO_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(FULL_BUCKET))
                    sendMessage(player, "You fill the bucket from the tap.")
                }
                THREE_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(FULL_BUCKET))
                    sendMessage(player, "You fill the bucket from the tap.")
                }
                FOUR_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(FULL_BUCKET))
                    sendMessage(player, "You fill the bucket from the tap.")
                }
                FULL_BUCKET -> {
                    sendMessage(player, "The bucket is already full!")
                }
                else -> return@onUseWith false
            }
            return@onUseWith true
        }

        /*
         * Handles filling a jug from a tap.
         */

        onUseWith(IntType.SCENERY, JUGS, TAP) { player, used, _ ->
            when (used.id) {
                EMPTY_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(EMPTY_JUG)), Item(FULL_JUG))
                    sendMessage(player, "You fill the jug from the tap.")
                }
                ONE_THIRD_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(FULL_JUG))
                    sendMessage(player, "You fill the jug from the tap.")
                }
                TWO_THIRD_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(FULL_JUG))
                    sendMessage(player, "You fill the jug from the tap.")
                }
                FULL_JUG -> {
                    sendMessage(player, "The jug is already full!")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles pouring buckets contents into a drain.
         */

        onUseWith(IntType.SCENERY, BUCKETS, DRAIN) { player, used, _ ->
            when (used.id) {
                EMPTY_BUCKET -> sendMessage(player, "The bucket is already empty!")
                ONE_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                    sendMessage(player, "You empty the bucket down the drain.")
                }
                TWO_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                    sendMessage(player, "You empty the bucket down the drain.")
                }
                THREE_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                    sendMessage(player, "You empty the bucket down the drain.")
                }
                FOUR_FIFTH_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                    sendMessage(player, "You empty the bucket down the drain.")
                }
                FULL_BUCKET -> {
                    replaceSlot(player, player.inventory.getSlot(Item(FULL_BUCKET)), Item(EMPTY_BUCKET))
                    sendMessage(player, "You empty the bucket down the drain.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles pouring jug contents into a drain.
         */

        onUseWith(IntType.SCENERY, JUGS, DRAIN) { player, used, _ ->
            when (used.id) {
                EMPTY_JUG -> sendMessage(player, "The jug is already empty!")
                ONE_THIRD_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(EMPTY_JUG))
                    sendMessage(player, "You empty the jug down the drain.")
                }
                TWO_THIRD_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(EMPTY_JUG))
                    sendMessage(player, "You empty the jug down the drain.")
                }
                FULL_JUG -> {
                    replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(EMPTY_JUG))
                    sendMessage(player, "You empty the jug down the drain.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles pouring buckets contents into a jugs.
         */

        onUseWith(IntType.ITEM, BUCKETS, *JUGS) { player, used, with ->
            when (used.id) {
                ONE_FIFTH_BUCKET -> when (with.id) {
                    EMPTY_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_JUG)), Item(ONE_THIRD_JUG))
                        sendMessage(player, "You empty the bucket into the jug.")
                    }
                    ONE_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(TWO_THIRD_JUG))
                        sendMessage(player, "You empty the bucket into the jug.")
                    }
                    TWO_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    FULL_JUG -> sendMessage(player, "The jug is already full!")
                }
                TWO_FIFTH_BUCKET -> when (with.id) {
                    EMPTY_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_JUG)), Item(TWO_THIRD_JUG))
                        sendMessage(player, "You empty the bucket into the jug")
                    }
                    ONE_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    TWO_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(ONE_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    FULL_JUG -> sendMessage(player, "The jug is already full!")
                }
                THREE_FIFTH_BUCKET -> when (with.id) {
                    EMPTY_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(EMPTY_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    ONE_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(ONE_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    TWO_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(TWO_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    FULL_JUG -> sendMessage(player, "The jug is already full!")
                }
                FOUR_FIFTH_BUCKET -> when (with.id) {
                    EMPTY_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(ONE_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    ONE_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(TWO_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    TWO_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(THREE_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    FULL_JUG -> sendMessage(player, "The jug is already full!")
                }
                FULL_BUCKET -> when (with.id) {
                    EMPTY_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_BUCKET)), Item(TWO_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    ONE_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_BUCKET)), Item(THREE_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    TWO_THIRD_JUG -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_BUCKET)), Item(FOUR_FIFTH_BUCKET))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(FULL_JUG))
                        sendMessage(player, "You fill the jug to the brim.")
                    }
                    FULL_JUG -> sendMessage(player, "The jug is already full!")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles pouring jug contents into a buckets.
         */

        onUseWith(IntType.ITEM, JUGS, *BUCKETS) { player, used, with ->
            when (used.id) {
                ONE_THIRD_JUG -> when (with.id) {
                    EMPTY_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_BUCKET)), Item(ONE_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    ONE_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(TWO_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    TWO_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(THREE_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    THREE_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(FOUR_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    FOUR_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(FULL_BUCKET))
                        sendMessage(player, "You fill the bucket to the brim.")
                    }
                    FULL_BUCKET -> sendMessage(player, "The bucket is already full!")
                }
                TWO_THIRD_JUG -> when (with.id) {
                    EMPTY_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_BUCKET)), Item(TWO_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    ONE_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(THREE_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    TWO_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(FOUR_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    THREE_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(FULL_BUCKET))
                        sendMessage(player, "You fill the bucket to the brim.")
                    }
                    FOUR_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_THIRD_JUG)), Item(ONE_THIRD_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(FULL_BUCKET))
                        sendMessage(player, "You fill the bucket to the brim.")
                    }
                    FULL_BUCKET -> sendMessage(player, "The bucket is already full!")
                }
                FULL_JUG -> when (with.id) {
                    EMPTY_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(EMPTY_BUCKET)), Item(THREE_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    ONE_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(ONE_FIFTH_BUCKET)), Item(FOUR_FIFTH_BUCKET))
                        sendMessage(player, "You empty the jug into the bucket.")
                    }
                    TWO_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(EMPTY_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(TWO_FIFTH_BUCKET)), Item(FULL_BUCKET))
                        sendMessage(player, "You fill the bucket to the brim.")
                    }
                    THREE_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(ONE_THIRD_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(THREE_FIFTH_BUCKET)), Item(FULL_BUCKET))
                        sendMessage(player, "You fill the bucket to the brim.")
                    }
                    FOUR_FIFTH_BUCKET -> {
                        replaceSlot(player, player.inventory.getSlot(Item(FULL_JUG)), Item(TWO_THIRD_JUG))
                        replaceSlot(player, player.inventory.getSlot(Item(FOUR_FIFTH_BUCKET)), Item(FULL_BUCKET))
                        sendMessage(player, "You fill the bucket to the brim.")
                    }
                    FULL_BUCKET -> sendMessage(player, "The bucket is already full!")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles shaking the vase.
         */

        on(VASE, IntType.ITEM, "Shake") { player, _ ->
            sendDialogue(player, "You shake the strangely shaped Vase. From the sound of it there is something metallic inside, but the neck of th vase is too narrow for it to come out.")
            return@on true
        }

        /*
         * Handles using a pick on the vase.
         */

        onUseWith(IntType.ITEM, PICK, VASE) { player, _, _ ->
            sendMessage(player, "The pick wouldn't be strong enough to break the vase open.")
            return@onUseWith true
        }

        /*
         * Handles using a pick on the vase.
         */

        onUseWith(IntType.ITEM, MAGNET, VASE) { player, _, _ ->
            sendMessage(player, "You use the magnet on the vase. The metallic object inside moves.")
            sendMessage(player, "The neck of the vase is too thin for thee object to come out of the vase.")
            return@onUseWith true
        }

        /*
         * Handles filling the vase with water from the tap.
         */

        onUseWith(IntType.SCENERY, VASE, TAP) { player, used, _ ->
            replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(FULL_VASE))
            sendMessage(player, "You fill the strange looking vase with water.")
            return@onUseWith true
        }

        /*
         * Handles pouring a jug into the vase.
         */

        onUseWith(IntType.ITEM, FULL_JUG, VASE) { player, used, with ->
            replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(EMPTY_JUG))
            replaceSlot(player, player.inventory.getSlot(Item(with.id)), Item(FULL_VASE))
            sendMessage(player, "You fill the vase with water.")
            return@onUseWith true
        }

        /*
         * Handles shaking a full vase.
         */

        on(FULL_VASE, IntType.ITEM, "Shake") { player, _ ->
            sendDialogue(player, "You shake the strangely shaped vase. The water inside it sloshes a little. Some spills out of the neck of the vase.")
            return@on true
        }

        /*
         * Handles shaking a full vase.
         */

        onUseWith(IntType.ITEM, VASE, VASE_LID) { player, used, with ->
            if(removeItem(player, with.asItem())) {
                replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(SEALED_EMPTY_VASE))
                sendMessage(player, "You screw the lid on tightly.")
            }
            return@onUseWith true
        }

        /*
         * Handles sealing an empty vase with the lid.
         */

        onUseWith(IntType.ITEM, FULL_VASE, VASE_LID) { player, used, with ->
            if(removeItem(player, with.asItem())) {
                replaceSlot(player, player.inventory.getSlot(Item(used.id)), Item(SEALED_FULL_VASE))
                sendMessage(player, "You screw the lid on tightly.")
            }
            return@onUseWith true
        }

        /*
         * Handles removing the lid from an empty sealed vase.
         */

        on(SEALED_EMPTY_VASE, IntType.ITEM, "Remove-lid") { player, node ->
            replaceSlot(player, player.inventory.getSlot(Item(node.id)), Item(VASE))
            addItem(player, VASE_LID)
            sendMessage(player, "You unscrew the lid from the vase.")
            return@on true
        }

        /*
         * Handles removing the lid from a full sealed vase.
         */

        on(SEALED_FULL_VASE, IntType.ITEM, "Remove-lid") { player, node ->
            replaceSlot(player, player.inventory.getSlot(Item(node.id)), Item(VASE))
            addItem(player, VASE_LID)
            sendMessage(player, "You unscrew the lid from the vase.")
            return@on true
        }

        /*
         * Handles freezing the water inside a vase using the frozen table.
         */

        onUseWith(IntType.SCENERY, FULL_VASE, FROZEN_TABLE) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                addItem(player, FROZEN_VASE)
                sendMessage(player, "The icy table immediately freezes the water in your vase.")
            }
            return@onUseWith true
        }

        /*
         * Handles freezing a sealed full vase until it shatters, revealing an icy key.
         */

        onUseWith(IntType.SCENERY, SEALED_FULL_VASE, FROZEN_TABLE) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                addItem(player, FROZEN_KEY)
                sendMessage(player, "The water expands as it freezes, and shatters the vase.")
                sendMessage(player, "You are left with a key encased in ice.")
            }
            return@onUseWith true
        }

        /*
         * Handles melting a frozen bucket on the cooking range.
         */

        onUseWith(IntType.SCENERY, FROZEN_BUCKET, COOKING_RANGE) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                animate(player, 883)
                playAudio(player, Sounds.FRY_2577)
                addItem(player, EMPTY_BUCKET)
                sendMessage(player, "You place the frozen bucket on the range. The ice turns to steam.")
            }
            return@onUseWith true
        }

        /*
         * Handles melting a frozen jug on the cooking range.
         */

        onUseWith(IntType.SCENERY, FROZEN_JUG, COOKING_RANGE) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                animate(player, 883)
                playAudio(player, Sounds.FRY_2577)
                addItem(player, EMPTY_JUG)
                sendMessage(player, "You place the frozen jug on the range. The ice turns to steam.")
            }
            return@onUseWith true
        }

        /*
         * Handles melting a frozen vase on the cooking range.
         */

        onUseWith(IntType.SCENERY, FROZEN_VASE, COOKING_RANGE) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                animate(player, 883)
                playAudio(player, Sounds.FRY_2577)
                addItem(player, VASE)
                sendMessage(player, "You place the frozen vase on the range. The ice turns into steam.")
            }
            return@onUseWith true
        }

        /*
         * Handles melting the ice around a key.
         */

        onUseWith(IntType.SCENERY, FROZEN_KEY, COOKING_RANGE) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                animate(player, 883)
                playAudio(player, Sounds.FRY_2577)
                addItem(player, SEERS_KEY)
                sendMessage(player, "The heat of the range melts the ice around the key.")
            }
            return@onUseWith true
        }

        /*
         * Handles opening the east door after obtaining the Seer's Key.
         */

        on(EAST_DOOR, IntType.SCENERY, "Open") { player, node ->
            if (!inInventory(player, SEERS_KEY, 1)) {
                sendMessage(player, "This door is locked tightly shut.")
                return@on false
            }
            lock(player, 2)
            player.inventory.clear()
            sendMessage(player, "You unlock the door with your key.")
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            sendNPCDialogueLines(player, NPCs.PEER_THE_SEER_1288, FaceAnim.EXTREMELY_SHOCKED, false,"Incredible! To have solved my puzzle so quickly! I have", "no choice but to vote in your favour!")
            addItem(player, Items.HERRING_347)
            setAttribute(player, GameAttributes.QUEST_VIKING_PEER_VOTE, true)
            val currentVotes = getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0)
            setAttribute(player, GameAttributes.QUEST_VIKING_VOTES, currentVotes + 1)
            return@on true
        }

        /*
         * Handles use the Seer's key on east door.
         */

        onUseWith(IntType.SCENERY, SEERS_KEY, EAST_DOOR) { player, used, with ->
            if (!inInventory(player, used.id, 1)) {
                sendMessage(player, "This door is locked tightly shut.")
                return@onUseWith false
            }
            lock(player, 2)
            player.inventory.clear()
            sendMessage(player, "You unlock the door with your key.")
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            sendNPCDialogueLines(player, NPCs.PEER_THE_SEER_1288, FaceAnim.EXTREMELY_SHOCKED, false, "Incredible! To have solved my puzzle so quickly! I have", "no choice but to vote in your favour!")
            addItem(player, Items.HERRING_347)
            setAttribute(player, GameAttributes.QUEST_VIKING_PEER_VOTE, true)
            val currentVotes = getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0)
            setAttribute(player, GameAttributes.QUEST_VIKING_VOTES, currentVotes + 1)
            return@onUseWith true
        }
    }

    companion object {
        // Scenery location.
        private val WEST_TRAP_DOOR = getScenery(2631, 3663, 2)!!
        private val EAST_TRAP_DOOR = getScenery(2636, 3663, 2)!!

        // Scenery.
        private const val WEST_DOOR = Objects.DOOR_4165
        private const val EAST_DOOR = Objects.DOOR_4166
        private const val WEST_LADDER = Objects.LADDER_4163
        private const val EAST_LADDER = Objects.LADDER_4164
        private const val TAP = Objects.TAP_4176
        private const val COOKING_RANGE = Objects.COOKING_RANGE_4172
        private const val DRAIN = Objects.DRAIN_4175
        private const val CUPBOARD_CLOSED = Objects.CUPBOARD_4177
        private const val CUPBOARD_OPENED = Objects.CUPBOARD_4178
        private const val BALANCE_CHEST = Objects.CHEST_4170
        private const val UNICORN_HEAD = Objects.UNICORN_S_HEAD_4181
        private const val SOUTH_BOXES = Objects.BOXES_4183
        private val CHESTS = intArrayOf(Objects.CHEST_4167, Objects.CHEST_4168)
        private const val SOUTH_CRATES = Objects.CRATE_4186
        private const val EAST_CRATES = Objects.CRATE_4185
        private const val FROZEN_TABLE = Objects.FROZEN_TABLE_4169
        private const val BOOKCASE = Objects.BOOKCASE_4171
        private const val BULLS_HEAD = Objects.BULL_S_HEAD_4182
        private const val MURAL = Objects.ABSTRACT_MURAL_4179

        // Items.
        private const val OLD_RED_DISK = Items.OLD_RED_DISK_9947
        private const val WOODEN_DISK = Items.WOODEN_DISK_3744
        private const val RED_HERRING = Items.RED_HERRING_3742
        private const val BLUE_THREAD = Items.THREAD_3719
        private const val PICK = Items.PICK_3720
        private const val SHIP_TOY = Items.TOY_BOAT_3721
        private const val MAGNET = Items.MAGNET_3718
        private const val RED_GOOP = Items.STICKY_RED_GOOP_3746
        private const val RED_DISK = Items.RED_DISK_3743
        private const val VASE_LID = Items.VASE_LID_3737
        private const val VASE = Items.VASE_3734
        private const val FULL_VASE = Items.VASE_OF_WATER_3735
        private const val FROZEN_VASE = Items.FROZEN_VASE_3736
        private const val SEALED_EMPTY_VASE = Items.SEALED_VASE_3738
        private const val SEALED_FULL_VASE = Items.SEALED_VASE_3739
        private const val FROZEN_KEY = Items.FROZEN_KEY_3741
        private const val SEERS_KEY = Items.SEERS_KEY_3745
        private const val EMPTY_BUCKET = Items.EMPTY_BUCKET_3727
        private const val ONE_FIFTH_BUCKET = Items.ONE_5THS_FULL_BUCKET_3726
        private const val TWO_FIFTH_BUCKET = Items.TWO_5THS_FULL_BUCKET_3725
        private const val THREE_FIFTH_BUCKET = Items.THREE_5THS_FULL_BUCKET_3724
        private const val FOUR_FIFTH_BUCKET = Items.FOUR_5THS_FULL_BUCKET_3723
        private const val FULL_BUCKET = Items.FULL_BUCKET_3722
        private const val FROZEN_BUCKET = Items.FROZEN_BUCKET_3728
        private const val EMPTY_JUG = Items.EMPTY_JUG_3732
        private const val ONE_THIRD_JUG = Items.ONE_THIRDRDS_FULL_JUG_3731
        private const val TWO_THIRD_JUG = Items.TWO_THIRDSRDS_FULL_JUG_3730
        private const val FULL_JUG = Items.FULL_JUG_3729
        private const val FROZEN_JUG = Items.FROZEN_JUG_3733

        // Arrays.
        private val JUGS = intArrayOf(Items.EMPTY_JUG_3732, Items.ONE_THIRDRDS_FULL_JUG_3731, Items.TWO_THIRDSRDS_FULL_JUG_3730, Items.FULL_JUG_3729)
        private val BUCKETS = intArrayOf(Items.EMPTY_BUCKET_3727, Items.ONE_5THS_FULL_BUCKET_3726, Items.TWO_5THS_FULL_BUCKET_3725, Items.THREE_5THS_FULL_BUCKET_3724, Items.FOUR_5THS_FULL_BUCKET_3723, Items.FULL_BUCKET_3722)
        private val DISKS = intArrayOf(Items.OLD_RED_DISK_9947, Items.RED_DISK_3743, WOODEN_DISK)
    }

    private class BullHeadDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> sendDialogueLines(player!!, "You notice there is something unusual about the right eye of this", "bulls' head...").also { stage++ }
                1 -> sendDialogueLines(player!!, "It is not an eye at all, but some kind of disk made of wood. You", "take it from the head.").also { stage++ }
                2 -> {
                    end()
                    addItem(player!!, Items.WOODEN_DISK_3744, 1)
                }
            }
        }
    }

    private class UnicornHeadDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                0 -> sendDialogueLines(player!!, "You notice there is something unusual about the left eye of this", "unicorn head...").also { stage++ }
                1 -> sendDialogueLines(player!!, "It is not an eye at all, but some kind of red coloured disk. You take it", "from the head.").also { stage++ }
                2 -> {
                    end()
                    addItem(player!!, Items.OLD_RED_DISK_9947, 1)
                }
            }
        }
    }
    private class DoorRiddleDialogue(player: Player) : DialogueFile() {

        private data class Riddle(val lines: List<String>)

        private val riddles = listOf(
            Riddle(listOf(
                "My first is in the well, but not at sea.",
                "My second in 'I', but not in 'me'.",
                "My third is in flies, but insects not found.",
                "My last is in earth, but not in the ground.",
                "My whole when stolen from you, caused you death.",
                "What am I?"
            )),
            Riddle(listOf(
                "My first is in mage, but not in wizard.",
                "My second in goblin, and also in lizard.",
                "My third is in night, but not in the day.",
                "My last is in fields, but not in the hay.",
                "My whole is the most powerful tool you will ever possess.",
                "What am I?"
            )),
            Riddle(listOf(
                "My first is in water, and also in tea.",
                "My second in fish, but not in the sea.",
                "My third in mountains, but not underground.",
                "My last is in strike, but not in pound.",
                "My whole crushes mountains, drains rivers, and destroys civilisations.",
                "All that live fear my passing.",
                "What am I?"
            )),
            Riddle(listOf(
                "My first is in wizard, but not in a mage.",
                "My second in jail, but not in a cage.",
                "My third is in anger, but not in a rage.",
                "My last in a drawing, but not on a page.",
                "My whole helps to make bread, let birds fly and boats sail.",
                "What am I?"
            ))
        )

        private val playerRef = player

        private val riddle: Riddle
            get() = riddles[getAttribute(playerRef, GameAttributes.QUEST_VIKING_PEER_RIDDLE, 0).coerceIn(0, riddles.lastIndex)]

        private var initStage = true

        override fun handle(componentID: Int, buttonID: Int) {
            if (initStage) {
                stage = 1
                initStage = false
            }

            when (stage) {
                1 -> {
                    sendDialogue(playerRef, "There is a combination lock on this door. Above the lock you can see that there is a metal plaque with a riddle on it.")
                    stage = 5
                }
                5 -> {
                    options("Read the riddle", "Solve the riddle", "Forget it")
                    stage = 10
                }
                10 -> when (buttonID) {
                    1 -> {
                        showRiddle()
                        stage = 20
                    }
                    2 -> {
                        end()
                        openInterface(playerRef, Components.SEER_COMBOLOCK_298)
                    }
                    3 -> end()
                }
                20 -> {
                    val endLines = riddle.lines.drop(4)
                    dialogue(*endLines.toTypedArray())
                    stage = 1000
                }
                1000 -> {
                    end()
                    openInterface(playerRef, Components.SEER_COMBOLOCK_298)
                }
            }
        }

        private fun showRiddle() {
            dialogue(*riddle.lines.take(4).toTypedArray())
        }
    }
}