package content.region.fremennik.quest.viking.handlers

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class SeersHouseListener : InteractionListener {
    private val WESTDOOR = 4165
    private val EASTDOOR = 4166
    private val WESTLADDER = 4163
    private val EASTLADDER = 4164
    private val WESTTRAPDOOR = getScenery(2631, 3663, 2)!!
    private val EASTTRAPDOOR = getScenery(2636, 3663, 2)!!
    private val TAP = 4176
    private val COOKINGRANGE = 4172
    private val DRAIN = 4175
    private val CUPBOARD_CLOSED = 4177
    private val CUPBOARD_OPENED = 4178
    private val BALANCECHEST = 4170
    private val UNICORNHEAD = 4181
    private val SOUTHBOXES = 4183
    private val CHEST = intArrayOf(4167, 4168)
    private val SOUTHCRATES = 4186
    private val EASTCRATES = 4185
    private val EASTBOXES = 4184
    private val FROZENTABLE = 4169
    private val BOOKCASE = 4171
    private val BULLSHEAD = 4182
    private val MURAL = 4179

    private val OLDREDDISK = Items.OLD_RED_DISK_9947
    private val WOODENDISK = Items.WOODEN_DISK_3744
    private val REDHERRING = Items.RED_HERRING_3742
    private val BLUETHREAD = Items.THREAD_3719
    private val PICK = Items.PICK_3720
    private val SHIPTOY = Items.TOY_BOAT_3721
    private val MAGNET = Items.MAGNET_3718
    private val REDGOOP = Items.STICKY_RED_GOOP_3746
    private val REDDISK = Items.RED_DISK_3743
    private val VASELID = Items.VASE_LID_3737
    private val VASE = Items.VASE_3734
    private val FULLVASE = Items.VASE_OF_WATER_3735
    private val FROZENVASE = Items.FROZEN_VASE_3736
    private val SEALEDEMPTYVASE = Items.SEALED_VASE_3738
    private val SEALEDFULLVASE = Items.SEALED_VASE_3739
    private val FROZENKEY = Items.FROZEN_KEY_3741
    private val SEERSKEY = Items.SEERS_KEY_3745
    private val EMPTYBUCKET = Items.EMPTY_BUCKET_3727
    private val ONEFIFTHBUCKET = Items.ONE_5THS_FULL_BUCKET_3726
    private val TWOFIFTHBUCKET = Items.TWO_5THS_FULL_BUCKET_3725
    private val THREEFIFTHBUCKET = Items.THREE_5THS_FULL_BUCKET_3724
    private val FOURFIFTHBUCKET = Items.FOUR_5THS_FULL_BUCKET_3723
    private val FULLBUCKET = Items.FULL_BUCKET_3722
    private val FROZENBUCKET = Items.FROZEN_BUCKET_3728
    private val EMPTYJUG = Items.EMPTY_JUG_3732
    private val ONETHIRDJUG = Items.ONE_THIRDRDS_FULL_JUG_3731
    private val TWOTHIRDJUG = Items.TWO_THIRDSRDS_FULL_JUG_3730
    private val FULLJUG = Items.FULL_JUG_3729
    private val FROZENJUG = Items.FROZEN_JUG_3733

    private val JUGS =
        intArrayOf(
            Items.EMPTY_JUG_3732,
            Items.ONE_THIRDRDS_FULL_JUG_3731,
            Items.TWO_THIRDSRDS_FULL_JUG_3730,
            Items.FULL_JUG_3729,
        )
    private val BUCKETS =
        intArrayOf(
            Items.EMPTY_BUCKET_3727,
            Items.ONE_5THS_FULL_BUCKET_3726,
            Items.TWO_5THS_FULL_BUCKET_3725,
            Items.THREE_5THS_FULL_BUCKET_3724,
            Items.FOUR_5THS_FULL_BUCKET_3723,
            Items.FULL_BUCKET_3722,
        )
    private val DISKS = intArrayOf(Items.OLD_RED_DISK_9947, Items.RED_DISK_3743)
    private val EASTZONE = ZoneBorders(2635, 3662, 2637, 3664, 2)
    private val WESTZONE = ZoneBorders(2630, 3662, 2632, 3664, 2)

    override fun defineListeners() {
        on(WESTDOOR, IntType.SCENERY, "open") { player, node ->
            if (!getAttribute(player, "PeerStarted", false)) {
                sendDialogue(player, "You should probably talk to the owner of this home.")
            }
            if (getAttribute(player, "fremtrials:peer-vote", false)) {
                sendDialogue(player, "I don't need to go through that again.")
                return@on true
            } else if (getAttribute(player, "PeerRiddle", 5) < 5) {
                player.dialogueInterpreter.open(
                    DoorRiddleDialogue(player),
                    Scenery(WESTDOOR, node.location),
                )
            } else if (getAttribute(player, "riddlesolved", false)) {
                val insideHouse = (player.location == Location.create(2631, 3666, 0))
                if (insideHouse) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    player.inventory.clear()
                } else if (player.inventory.isEmpty && player.equipment.isEmpty) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                } else {
                    openDialogue(player, NPCs.PEER_THE_SEER_1288, findNPC(NPCs.PEER_THE_SEER_1288)!!)
                }
            }
            return@on true
        }

        on(WESTLADDER, IntType.SCENERY, "Climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(828), Location.create(2631, 3664, 2))
            return@on true
        }

        on(WESTTRAPDOOR.id, IntType.SCENERY, "Climb-down") { player, _ ->
            if (player.location.x < 2634) {
                ClimbActionHandler.climb(player, Animation(828), Location.create(2631, 3664, 0))
            } else if (player.location.x > 2634) {
                ClimbActionHandler.climb(player, Animation(828), Location.create(2636, 3664, 0))
            }
            return@on true
        }

        on(WESTTRAPDOOR.id, IntType.SCENERY, "Close") { _, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4174))
            return@on true
        }

        on(WESTTRAPDOOR.id, IntType.SCENERY, "Open") { _, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4173))
            return@on true
        }

        on(EASTTRAPDOOR.id, IntType.SCENERY, "Open") { _, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4173))
            return@on true
        }

        on(EASTTRAPDOOR.id, IntType.SCENERY, "Close") { _, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4174))
            return@on true
        }

        on(EASTTRAPDOOR.id, IntType.SCENERY, "Climb-down") { player, _ ->
            if (player.location.x > 2634) {
                ClimbActionHandler.climb(player, Animation(828), Location.create(2636, 3664, 0))
            }
            return@on true
        }

        on(EASTLADDER, IntType.SCENERY, "Climb-Up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(828), Location.create(2636, 3664, 2))
            return@on true
        }

        on(CUPBOARD_OPENED, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the cupboard...")
            queueScript(player, 3, QueueStrength.SOFT) {
                if (inInventory(player, EMPTYBUCKET, 1)) {
                    sendMessage(player, "You find nothing of interest.")
                } else {
                    addItem(player, EMPTYBUCKET)
                    sendMessage(player, "You find a bucket with a number five painted on it.")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(CUPBOARD_CLOSED, IntType.SCENERY, "Open") { player, node ->
            lock(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        animate(player, 542)
                        playAudio(player, Sounds.CUPBOARD_OPEN_58)
                        return@queueScript keepRunning(player)
                    }

                    1 -> {
                        replaceScenery(node.asScenery()!!, 4178, -1)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(CUPBOARD_OPENED, IntType.SCENERY, "Shut") { player, node ->
            lock(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        animate(player, 543)
                        playAudio(player, Sounds.CUPBOARD_CLOSE_57)
                        return@queueScript keepRunning(player)
                    }

                    1 -> {
                        replaceScenery(node.asScenery()!!, 4177, -1)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(BALANCECHEST, IntType.SCENERY, "Open") { player, _ ->
            sendDialogue(
                player,
                "This chest is securely locked shut. There is some kind of balance attached to the lock, and a number four is painted just above it.",
            )
            return@on true
        }

        on(BOOKCASE, IntType.SCENERY, "Search") { player, _ ->
            lock(player, 3)
            sendMessage(player, "You search the bookcase...")
            queueScript(player, 3, QueueStrength.SOFT) {
                if (inInventory(player, REDHERRING, 1)) {
                    sendMessage(player, "You find nothing of interest.")
                } else {
                    addItem(player, REDHERRING)
                    sendMessage(player, "Hidden behind some old books, you find a red herring.")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(SOUTHBOXES, IntType.SCENERY, "Search") { player, _ ->
            lock(player, 3)
            sendMessage(player, "You search the boxes...")
            queueScript(player, 3, QueueStrength.SOFT) {
                if (!inInventory(player, BLUETHREAD, 1)) {
                    addItem(player, BLUETHREAD)
                    sendMessage(player, "You find some thread hidden inside.")
                } else if (!inInventory(player, MAGNET, 1) && inInventory(player, BLUETHREAD, 1)) {
                    addItem(player, MAGNET)
                    sendMessage(player, "You find a magnet hidden inside.")
                } else {
                    sendMessage(player, "You find nothing of interest.")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(CHEST, IntType.SCENERY, "Open") { player, node ->
            lock(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        animate(player, Animations.HUMAN_OPEN_CHEST_536)
                        playAudio(player, Sounds.CHEST_OPEN_52)
                        return@queueScript keepRunning(player)
                    }

                    1 -> {
                        SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4168))
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(CHEST, IntType.SCENERY, "Close") { player, node ->
            lock(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        animate(player, Animations.HUMAN_CLOSE_CHEST_538)
                        playAudio(player, Sounds.CHEST_CLOSE_51)
                        return@queueScript keepRunning(player)
                    }

                    1 -> {
                        SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4167))
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(CHEST, IntType.SCENERY, "Search") { player, _ ->
            sendMessage(player, "You search the chest...")
            queueScript(player, 3, QueueStrength.SOFT) {
                if (inInventory(player, EMPTYJUG, 1)) {
                    sendMessage(player, "You find nothing of interest.")
                } else {
                    animate(player, Animations.HUMAN_TAKE_FROM_CHEST_539)
                    sendMessage(player, "You find a jug with a number three painted on it")
                    addItem(player, EMPTYJUG)
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(EASTCRATES, IntType.SCENERY, "Search") { player, _ ->
            lock(player, 3)
            sendMessage(player, "You search the crates...")
            queueScript(player, 3, QueueStrength.SOFT) {
                if (inInventory(player, PICK, 1)) {
                    sendMessage(player, "You find nothing of interest.")
                } else {
                    addItem(player, PICK)
                    sendMessage(player, "You find a small pick hidden inside.")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(SOUTHCRATES, IntType.SCENERY, "Search") { player, _ ->
            lock(player, 3)
            sendMessage(player, "You search the crates...")
            queueScript(player, 3, QueueStrength.SOFT) {
                if (inInventory(player, SHIPTOY, 1)) {
                    sendMessage(player, "You find nothing of interest.")
                } else {
                    addItem(player, SHIPTOY)
                    sendMessage(player, "You find a toy ship hidden inside")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(BULLSHEAD, IntType.SCENERY, "Study") { player, node ->
            if (!inInventory(player, WOODENDISK, 1)) {
                openDialogue(player, BullHeadDialogue(), node.asScenery())
            } else {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        on(UNICORNHEAD, IntType.SCENERY, "Study") { player, node ->
            if (!inInventory(player, OLDREDDISK, 1)) {
                openDialogue(player, UnicornHeadDialogue(), node.asScenery())
            } else {
                sendMessage(player, "You find nothing of interest.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, REDHERRING, COOKINGRANGE) { player, _, _ ->
            animate(player, 883)
            playAudio(player, Sounds.FRY_2577)
            removeItem(player, REDHERRING)
            addItem(player, REDGOOP)
            sendDialogue(
                player,
                "As you cook the herring on the stove, the colouring on it peels off separately as a red sticky goop...",
            )
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, REDGOOP, WOODENDISK) { player, _, _ ->
            removeItem(player, WOODENDISK)
            removeItem(player, REDGOOP)
            addItem(player, REDDISK)
            sendMessage(player, "You coat the wooden coin with the sticky red goop.")
            return@onUseWith true
        }

        on(MURAL, IntType.SCENERY, "Study") { player, _ ->
            sendMessage(player, "The mural feels like something is missing.")
            return@on true
        }

        onUseWith(IntType.SCENERY, BUCKETS, TAP) { player, bucket, _ ->
            when (bucket.id) {
                3727 -> {
                    removeItem(player, EMPTYBUCKET)
                    addItem(player, FULLBUCKET)
                    sendMessage(player, "You fill the bucket from the tap.")
                    return@onUseWith true
                }

                ONEFIFTHBUCKET -> {
                    removeItem(player, ONEFIFTHBUCKET)
                    addItem(player, FULLBUCKET)
                    sendMessage(player, "You fill the bucket from the tap.")
                }

                TWOFIFTHBUCKET -> {
                    removeItem(player, TWOFIFTHBUCKET)
                    addItem(player, FULLBUCKET)
                    sendMessage(player, "You fill the bucket from the tap.")
                }

                THREEFIFTHBUCKET -> {
                    removeItem(player, THREEFIFTHBUCKET)
                    addItem(player, FULLBUCKET)
                    sendMessage(player, "You fill the bucket from the tap.")
                }

                FOURFIFTHBUCKET -> {
                    removeItem(player, FOURFIFTHBUCKET)
                    addItem(player, FULLBUCKET)
                    sendMessage(player, "You fill the bucket from the tap.")
                }

                FULLBUCKET -> {
                    sendMessage(player, "The bucket is already full!")
                }

                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, JUGS, TAP) { player, used, _ ->
            when (used.id) {
                EMPTYJUG -> {
                    removeItem(player, EMPTYJUG)
                    addItem(player, FULLJUG)
                    sendMessage(player, "You fill the jug from the tap.")
                }

                ONETHIRDJUG -> {
                    removeItem(player, ONETHIRDJUG)
                    addItem(player, FULLJUG)
                    sendMessage(player, "You fill the jug from the tap.")
                }

                TWOTHIRDJUG -> {
                    removeItem(player, TWOTHIRDJUG)
                    addItem(player, FULLJUG)
                    sendMessage(player, "You fill the jug from the tap.")
                }

                FULLJUG -> {
                    sendMessage(player, "The jug is already full!")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, BUCKETS, DRAIN) { player, used, _ ->
            when (used.id) {
                EMPTYBUCKET -> {
                    sendMessage(player, "The bucket is already empty!")
                }

                ONEFIFTHBUCKET -> {
                    removeItem(player, ONEFIFTHBUCKET)
                    addItem(player, EMPTYBUCKET)
                    sendMessage(player, "You empty the bucket down the drain.")
                }

                TWOFIFTHBUCKET -> {
                    removeItem(player, TWOFIFTHBUCKET)
                    addItem(player, EMPTYBUCKET)
                    sendMessage(player, "You empty the bucket down the drain.")
                }

                THREEFIFTHBUCKET -> {
                    removeItem(player, THREEFIFTHBUCKET)
                    addItem(player, EMPTYBUCKET)
                    sendMessage(player, "You empty the bucket down the drain.")
                }

                FOURFIFTHBUCKET -> {
                    removeItem(player, FOURFIFTHBUCKET)
                    addItem(player, EMPTYBUCKET)
                    sendMessage(player, "You empty the bucket down the drain.")
                }

                FULLBUCKET -> {
                    removeItem(player, FULLBUCKET)
                    addItem(player, EMPTYBUCKET)
                    sendMessage(player, "You empty the bucket down the drain.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, JUGS, DRAIN) { player, used, _ ->
            when (used.id) {
                EMPTYJUG -> {
                    sendMessage(player, "The jug is already empty!")
                }

                ONETHIRDJUG -> {
                    removeItem(player, ONETHIRDJUG)
                    addItem(player, EMPTYJUG)
                    sendMessage(player, "You empty the jug down the drain.")
                }

                TWOTHIRDJUG -> {
                    removeItem(player, TWOTHIRDJUG)
                    addItem(player, EMPTYJUG)
                    sendMessage(player, "You empty the jug down the drain.")
                }

                FULLJUG -> {
                    removeItem(player, FULLJUG)
                    addItem(player, EMPTYJUG)
                    sendMessage(player, "You empty the jug down the drain.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, FOURFIFTHBUCKET, BALANCECHEST) { player, _, _ ->
            removeItem(player, FOURFIFTHBUCKET)
            addItem(player, VASE)
            sendMessage(player, "You place the bucket on the scale.")
            sendMessage(player, "It is a perfect counterweight and balances precisely.")
            sendMessage(player, "You take a strange looking vase out of the chest.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, DISKS, MURAL) { player, used, _ ->
            when (used.id) {
                REDDISK -> {
                    if (getAttribute(player, "olddiskplaced", false)) {
                        removeItem(player, REDDISK)
                        addItem(player, VASELID)
                        sendDialogue(
                            player,
                            "You put the red disk into the empty hole on the mural. It is a perfect fit! The centre of the mural appears to have become loose.",
                        )
                    } else if (getAttribute(player, "reddiskplaced", false)) {
                        sendMessage(player, "You already have a disk in that spot.")
                    } else {
                        removeItem(player, REDDISK)
                        sendMessage(player, "You put the red disk into the empty hold on the mural.")
                        sendMessage(player, "It's a perfect fit!")
                        setAttribute(player, "reddiskplaced", true)
                    }
                }

                OLDREDDISK -> {
                    if (getAttribute(player, "reddiskplaced", false)) {
                        removeItem(player, OLDREDDISK)
                        addItem(player, VASELID)
                        sendDialogue(
                            player,
                            "You put the red disk into the empty hole on the mural. It is a perfect fit! The centre of the mural appears to have become loose.",
                        )
                    } else if (getAttribute(player, "olddiskplaced", false)) {
                        sendMessage(player, "You already have a disk in that spot.")
                    } else {
                        removeItem(player, OLDREDDISK)
                        sendMessage(player, "You put the red disk into the empty hold on the mural.")
                        sendMessage(player, "It's a perfect fit!")
                        setAttribute(player, "olddiskplaced", true)
                    }
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, BUCKETS, BALANCECHEST) { player, used, _ ->
            when (used.id) {
                EMPTYBUCKET -> {
                    animate(player, 883)
                    sendMessage(player, "You place the bucket on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                ONEFIFTHBUCKET -> {
                    animate(player, 883)
                    sendMessage(player, "You place the bucket on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                TWOFIFTHBUCKET -> {
                    animate(player, 883)
                    sendMessage(player, "You place the bucket on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                THREEFIFTHBUCKET -> {
                    animate(player, 883)
                    sendMessage(player, "You place the bucket on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                FOURFIFTHBUCKET -> {
                    animate(player, 883)
                    sendMessage(player, "You place the bucket on the scale")
                    sendMessage(player, "It is a perfect counterweight and balances precisely.")
                    sendMessage(player, "You take a strange looking vase out of the chest.")
                    addItem(player, VASE)
                }

                FULLBUCKET -> {
                    animate(player, 883)
                    sendMessage(player, "You place the bucket on the scale")
                    sendMessage(player, "It is too heavy to balance it properly")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, JUGS, BALANCECHEST) { player, used, _ ->
            when (used.id) {
                EMPTYJUG -> {
                    animate(player, 883)
                    sendMessage(player, "You place the jug on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                ONETHIRDJUG -> {
                    animate(player, 883)
                    sendMessage(player, "You place the jug on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                TWOTHIRDJUG -> {
                    animate(player, 883)
                    sendMessage(player, "You place the jug on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }

                FULLJUG -> {
                    animate(player, 883)
                    sendMessage(player, "You place the jug on the scale")
                    sendMessage(player, "It is too light to balance it properly")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, BUCKETS, FROZENTABLE) { player, used, _ ->
            when (used.id) {
                EMPTYBUCKET -> sendMessage(player, "Your empty bucket gets very cold on the icy table.")
                FULLBUCKET -> {
                    animate(player, 883)
                    removeItem(player, FULLBUCKET)
                    addItem(player, FROZENBUCKET)
                    sendMessage(player, "They icy table immediately freezes the water in your bucket.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, JUGS, FROZENTABLE) { player, used, _ ->
            when (used.id) {
                EMPTYJUG -> sendMessage(player, "Your empty jug gets very cold on the icy table.")
                FULLJUG -> {
                    animate(player, 883)
                    removeItem(player, FULLJUG)
                    addItem(player, FROZENJUG)
                    sendMessage(player, "The icy table immediately freezes the water in your jug.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, BUCKETS, *JUGS) { player, used, with ->
            when (used.id) {
                ONEFIFTHBUCKET -> {
                    when (with.id) {
                        EMPTYJUG -> {
                            removeItem(player, ONEFIFTHBUCKET)
                            removeItem(player, EMPTYJUG)
                            addItem(player, EMPTYBUCKET)
                            addItem(player, ONETHIRDJUG)
                            sendMessage(player, "You empty the bucket into the jug")
                        }

                        ONETHIRDJUG -> {
                            removeItem(player, ONEFIFTHBUCKET)
                            removeItem(player, ONETHIRDJUG)
                            addItem(player, EMPTYBUCKET)
                            addItem(player, TWOTHIRDJUG)
                            sendMessage(player, "You empty the bucket into the jug")
                        }

                        TWOTHIRDJUG -> {
                            removeItem(player, ONEFIFTHBUCKET)
                            removeItem(player, TWOTHIRDJUG)
                            addItem(player, EMPTYBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        FULLJUG -> sendMessage(player, "The jug is already full!")
                    }
                }

                TWOFIFTHBUCKET -> {
                    when (with.id) {
                        EMPTYJUG -> {
                            removeItem(player, TWOFIFTHBUCKET)
                            removeItem(player, EMPTYJUG)
                            addItem(player, EMPTYBUCKET)
                            addItem(player, TWOTHIRDJUG)
                            sendMessage(player, "You empty the bucket into the jug")
                        }

                        ONETHIRDJUG -> {
                            removeItem(player, TWOFIFTHBUCKET)
                            removeItem(player, ONETHIRDJUG)
                            addItem(player, EMPTYBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        TWOTHIRDJUG -> {
                            removeItem(player, TWOFIFTHBUCKET)
                            removeItem(player, TWOTHIRDJUG)
                            addItem(player, ONEFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        FULLJUG -> sendMessage(player, "The jug is already full!")
                    }
                }

                THREEFIFTHBUCKET -> {
                    when (with.id) {
                        EMPTYJUG -> {
                            removeItem(player, THREEFIFTHBUCKET)
                            removeItem(player, EMPTYJUG)
                            addItem(player, EMPTYBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        ONETHIRDJUG -> {
                            removeItem(player, THREEFIFTHBUCKET)
                            removeItem(player, ONETHIRDJUG)
                            addItem(player, ONEFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        TWOTHIRDJUG -> {
                            removeItem(player, THREEFIFTHBUCKET)
                            removeItem(player, TWOTHIRDJUG)
                            addItem(player, TWOFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        FULLJUG -> sendMessage(player, "The jug is already full!")
                    }
                }

                FOURFIFTHBUCKET -> {
                    when (with.id) {
                        EMPTYJUG -> {
                            removeItem(player, FOURFIFTHBUCKET)
                            removeItem(player, EMPTYJUG)
                            addItem(player, ONEFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        ONETHIRDJUG -> {
                            removeItem(player, FOURFIFTHBUCKET)
                            removeItem(player, ONETHIRDJUG)
                            addItem(player, TWOFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        TWOTHIRDJUG -> {
                            removeItem(player, FOURFIFTHBUCKET)
                            removeItem(player, TWOTHIRDJUG)
                            addItem(player, THREEFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        FULLJUG -> sendMessage(player, "The jug is already full!")
                    }
                }

                FULLBUCKET -> {
                    when (with.id) {
                        EMPTYJUG -> {
                            removeItem(player, FULLBUCKET)
                            removeItem(player, EMPTYJUG)
                            addItem(player, TWOFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        ONETHIRDJUG -> {
                            removeItem(player, FULLBUCKET)
                            removeItem(player, ONETHIRDJUG)
                            addItem(player, THREEFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        TWOTHIRDJUG -> {
                            removeItem(player, FULLBUCKET)
                            removeItem(player, TWOTHIRDJUG)
                            addItem(player, FOURFIFTHBUCKET)
                            addItem(player, FULLJUG)
                            sendMessage(player, "You fill the jug to the brim.")
                        }

                        FULLJUG -> sendMessage(player, "The jug is already full!")
                    }
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, JUGS, *BUCKETS) { player, used, with ->
            when (used.id) {
                ONETHIRDJUG -> {
                    when (with.id) {
                        EMPTYBUCKET -> {
                            removeItem(player, ONETHIRDJUG)
                            removeItem(player, EMPTYBUCKET)
                            addItem(player, EMPTYJUG)
                            addItem(player, ONEFIFTHBUCKET)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        ONEFIFTHBUCKET -> {
                            removeItem(player, ONETHIRDJUG)
                            removeItem(player, ONEFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            addItem(player, TWOFIFTHBUCKET)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        TWOFIFTHBUCKET -> {
                            removeItem(player, ONETHIRDJUG)
                            removeItem(player, TWOFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            addItem(player, THREEFIFTHBUCKET)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        THREEFIFTHBUCKET -> {
                            removeItem(player, ONETHIRDJUG)
                            removeItem(player, THREEFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            addItem(player, FOURFIFTHBUCKET)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        FOURFIFTHBUCKET -> {
                            removeItem(player, ONETHIRDJUG)
                            removeItem(player, FOURFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            addItem(player, FULLBUCKET)
                            sendMessage(player, "You fill the bucket to the brim.")
                        }

                        FULLBUCKET -> sendMessage(player, "The bucket is already full!")
                    }
                }

                TWOTHIRDJUG -> {
                    when (with.id) {
                        EMPTYBUCKET -> {
                            removeItem(player, TWOTHIRDJUG)
                            removeItem(player, EMPTYBUCKET)
                            addItem(player, TWOFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        ONEFIFTHBUCKET -> {
                            removeItem(player, TWOTHIRDJUG)
                            removeItem(player, ONEFIFTHBUCKET)
                            addItem(player, THREEFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        TWOFIFTHBUCKET -> {
                            removeItem(player, TWOTHIRDJUG)
                            removeItem(player, TWOFIFTHBUCKET)
                            addItem(player, FOURFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        THREEFIFTHBUCKET -> {
                            removeItem(player, TWOTHIRDJUG)
                            removeItem(player, THREEFIFTHBUCKET)
                            addItem(player, FULLBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You fill the bucket to the brim.")
                        }

                        FOURFIFTHBUCKET -> {
                            removeItem(player, TWOTHIRDJUG)
                            removeItem(player, FOURFIFTHBUCKET)
                            addItem(player, FULLBUCKET)
                            addItem(player, ONETHIRDJUG)
                            sendMessage(player, "You fill the bucket to the brim.")
                        }

                        FULLBUCKET -> sendMessage(player, "The bucket is already full!")
                    }
                }

                FULLJUG -> {
                    when (with.id) {
                        EMPTYBUCKET -> {
                            removeItem(player, FULLJUG)
                            removeItem(player, EMPTYBUCKET)
                            addItem(player, THREEFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        ONEFIFTHBUCKET -> {
                            removeItem(player, FULLJUG)
                            removeItem(player, ONEFIFTHBUCKET)
                            addItem(player, FOURFIFTHBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You empty the jug into the bucket.")
                        }

                        TWOFIFTHBUCKET -> {
                            removeItem(player, FULLJUG)
                            removeItem(player, TWOFIFTHBUCKET)
                            addItem(player, FULLBUCKET)
                            addItem(player, EMPTYJUG)
                            sendMessage(player, "You fill the bucket to the brim.")
                        }

                        THREEFIFTHBUCKET -> {
                            removeItem(player, FULLJUG)
                            removeItem(player, THREEFIFTHBUCKET)
                            addItem(player, FULLBUCKET)
                            addItem(player, ONETHIRDJUG)
                            sendMessage(player, "You fill the bucket to the brim.")
                        }

                        FOURFIFTHBUCKET -> {
                            removeItem(player, FULLJUG)
                            removeItem(player, FOURFIFTHBUCKET)
                            addItem(player, FULLBUCKET)
                            addItem(player, TWOTHIRDJUG)
                            sendMessage(player, "You fill the bucket to the brim.")
                        }

                        FULLBUCKET -> sendMessage(player, "The bucket is already full!")
                    }
                }
            }
            return@onUseWith true
        }

        on(VASE, IntType.ITEM, "Shake") { player, _ ->
            sendDialogue(
                player,
                "You shake the strangely shaped Vase. From the sound of it there is something metallic inside, but the neck of th vase is too narrow for it to come out.",
            )
            return@on true
        }

        onUseWith(IntType.ITEM, PICK, VASE) { player, _, _ ->
            sendMessage(player, "The pick wouldn't be strong enough to break the vase open.")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, MAGNET, VASE) { player, _, _ ->
            sendMessage(player, "You use the magnet on the vase. The metallic object inside moves.")
            sendMessage(player, "The neck of the vase is too thin for thee object to come out of the vase.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, VASE, TAP) { player, _, _ ->
            removeItem(player, VASE)
            addItem(player, FULLVASE)
            sendMessage(player, "You fill the strange looking vase with water.")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, FULLJUG, VASE) { player, _, _ ->
            removeItem(player, FULLJUG)
            removeItem(player, VASE)
            addItem(player, EMPTYJUG)
            addItem(player, FULLVASE)
            sendMessage(player, "You fill the vase with water.")
            return@onUseWith true
        }

        on(FULLVASE, IntType.ITEM, "Shake") { player, _ ->
            sendDialogue(
                player,
                "You shake the strangely shaped vase. The water inside it sloshes a little. Some spills out of the neck of the vase.",
            )
            return@on true
        }

        onUseWith(IntType.ITEM, VASE, VASELID) { player, _, _ ->
            removeItem(player, VASE)
            removeItem(player, VASELID)
            addItem(player, SEALEDEMPTYVASE)
            sendMessage(player, "You screw the lid on tightly.")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, FULLVASE, VASELID) { player, _, _ ->
            removeItem(player, FULLVASE)
            removeItem(player, VASELID)
            addItem(player, SEALEDFULLVASE)
            sendMessage(player, "You screw the lid on tightly.")
            return@onUseWith true
        }

        on(SEALEDEMPTYVASE, IntType.ITEM, "Remove-lid") { player, _ ->
            removeItem(player, SEALEDEMPTYVASE)
            addItem(player, VASE)
            addItem(player, VASELID)
            sendMessage(player, "You unscrew the lid from the vase.")
            return@on true
        }

        on(SEALEDFULLVASE, IntType.ITEM, "Remove-lid") { player, _ ->
            removeItem(player, SEALEDFULLVASE)
            addItem(player, VASE)
            addItem(player, VASELID)
            sendMessage(player, "You unscrew the lid from the vase.")
            return@on true
        }

        onUseWith(IntType.SCENERY, FULLVASE, FROZENTABLE) { player, _, _ ->
            animate(player, 883)
            removeItem(player, FULLVASE)
            addItem(player, FROZENVASE)
            sendMessage(player, "The icy table immediately freezes the water in your vase.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, SEALEDFULLVASE, FROZENTABLE) { player, _, _ ->
            animate(player, 883)
            removeItem(player, SEALEDFULLVASE)
            addItem(player, FROZENKEY)
            sendMessage(player, "The water expands as it freezes, and shatters the vase.")
            sendMessage(player, "You are left with a key encased in ice.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, FROZENBUCKET, COOKINGRANGE) { player, _, _ ->
            animate(player, 883)
            playAudio(player, Sounds.FRY_2577)
            removeItem(player, FROZENBUCKET)
            addItem(player, EMPTYBUCKET)
            sendMessage(player, "You place the frozen bucket on the range. The ice turns to steam.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, FROZENJUG, COOKINGRANGE) { player, _, _ ->
            animate(player, 883)
            playAudio(player, Sounds.FRY_2577)
            removeItem(player, FROZENJUG)
            addItem(player, EMPTYJUG)
            sendMessage(player, "You place the frozen jug on the range. The ice turns to steam.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, FROZENVASE, COOKINGRANGE) { player, _, _ ->
            animate(player, 883)
            playAudio(player, Sounds.FRY_2577)
            removeItem(player, FROZENVASE)
            addItem(player, VASE)
            sendMessage(player, "You place the frozen vase on the range. The ice turns into steam.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, FROZENKEY, COOKINGRANGE) { player, _, _ ->
            animate(player, 883)
            playAudio(player, Sounds.FRY_2577)
            removeItem(player, FROZENKEY)
            addItem(player, SEERSKEY)
            sendMessage(player, "The heat of the range melts the ice around the key.")
            return@onUseWith true
        }

        on(EASTDOOR, IntType.SCENERY, "Open") { player, node ->
            if (inInventory(player, SEERSKEY, 1)) {
                setAttribute(player, "/save:housepuzzlesolved", true)
                player.inventory.clear()
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                setAttribute(player, "/save:fremtrials:peer-vote", true)
                setAttribute(player, "/save:fremtrials:votes", getAttribute(player, "fremtrials:votes", 0) + 1)
                sendNPCDialogue(
                    player,
                    1288,
                    "Incredible! To have solved my puzzle so quickly! I have no choice but to vote in your favour!",
                )
            } else {
                sendMessage(player, "This door is locked tightly shut.")
            }
            return@on true
        }
    }
}

class BullHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                player!!
                    .dialogueInterpreter
                    .sendDialogue(
                        "You notice there is something unusual about the right eye of this",
                        "bulls' head...",
                    ).also { stage++ }

            1 ->
                player!!
                    .dialogueInterpreter
                    .sendDialogue(
                        "It is not an eye at all, but some kind of disk made of wood. You",
                        "take it from the head.",
                    ).also { stage++ }

            2 -> {
                end()
                addItem(player!!, Items.WOODEN_DISK_3744, 1)
            }
        }
    }
}

class UnicornHeadDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 ->
                player!!
                    .dialogueInterpreter
                    .sendDialogue(
                        "You notice there is something unusual about the left eye of this",
                        "unicorn head...",
                    ).also { stage++ }

            1 ->
                player!!
                    .dialogueInterpreter
                    .sendDialogue(
                        "It is not an eye at all, but some kind of red coloured disk. You take it",
                        "from the head.",
                    ).also { stage++ }

            2 -> {
                end()
                addItem(player!!, Items.OLD_RED_DISK_9947)
            }
        }
    }
}

class DoorRiddleDialogue(
    player: Player,
) : DialogueFile() {
    private val RIDDLEONE =
        arrayOf(
            "My first is in the well, but not at sea.",
            "My second in 'I', but not in 'me'.",
            "My third is in flies, but insects not found.",
            "My last is in earth, but not in the ground.",
            "My whole when stolen from you, caused you death.",
            "What am I?",
        )
    private val RIDDLETWO =
        arrayOf(
            "My first is in mage, but not in wizard.",
            "My second in goblin, and also in lizard.",
            "My third is in night, but not in the day.",
            "My last is in fields, but not in the hay.",
            "My whole is the most powerful tool you will ever possess.",
            "What am I?",
        )
    private val RIDDLETHREE =
        arrayOf(
            "My first is in water, and also in tea.",
            "My second in fish, but not in the sea.",
            "My third in mountains, but not underground.",
            "My last is in strike, but not in pound.",
            "My whole crushes mountains, drains rivers, and destroys civilisations.",
            "All that live fear my passing.",
            "What am I?",
        )
    private val RIDDLEFOUR =
        arrayOf(
            "My first is in wizard, but not in a mage.",
            "My second in jail, but not in a cage.",
            "My third is in anger, but not in a rage.",
            "My last in a drawing, but not on a page.",
            "My whole helps to make bread, let birds fly and boats sail.",
            "What am I?",
        )
    private val RIDDLEANSWER = arrayOf("LIFE", "MIND", "TIME", "WIND")

    val p = player

    val riddle =
        when (getAttribute(player, "PeerRiddle", 0)) {
            0 -> RIDDLEONE
            1 -> RIDDLETWO
            2 -> RIDDLETHREE
            3 -> RIDDLEFOUR
            else -> RIDDLEONE
        }

    var init = true

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (init) {
            stage = 1
            init = false
        }
        when (stage) {
            1 ->
                sendDialogue(
                    player!!,
                    "There is a combination lock on this door. Above the lock you can see that there is a metal plaque with a riddle on it.",
                ).also { stage = 5 }

            5 -> options("Read the riddle", "Solve the riddle", "Forget it").also { stage = 10 }
            10 ->
                when (buttonID) {
                    1 -> dialogue(riddle[0], riddle[1], riddle[2], riddle[3]).also { stage = 20 }
                    2 -> {
                        openInterface(p, 298)
                        end()
                    }

                    3 -> end()
                }

            15 -> {
                dialogue(riddle[0], riddle[1], riddle[2], riddle[3])
                stage = 20
            }

            20 ->
                if (riddle.contentEquals(RIDDLETHREE)) {
                    dialogue(riddle[4], riddle[5], riddle[6]).also { stage = 1000 }
                } else {
                    dialogue(riddle[4], riddle[5]).also { stage = 1000 }
                }

            1000 -> end()
        }
    }
}
