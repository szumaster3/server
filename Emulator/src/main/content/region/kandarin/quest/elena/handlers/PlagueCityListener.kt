package content.region.kandarin.quest.elena.handlers

import content.region.kandarin.dialogue.plaguecity.WomanDialogue
import content.region.kandarin.quest.elena.dialogue.HeadMournerDialogue
import content.region.kandarin.quest.elena.dialogue.ManRehnisonDialogue
import content.region.kandarin.quest.elena.dialogue.MournerWestDialogue
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.tools.END_DIALOGUE
import org.rs.consts.*

class PlagueCityListener : InteractionListener {
    companion object {
        const val BUCKET_USES_ATTRIBUTE = "/save:elena:bucket"
        const val TIED_ROPE_VARBIT = Vars.VARP_QUEST_PLAGUE_CITY_TIED_ROPE_1787
        const val MUD_PATCH_VARBIT = Vars.VARP_QUEST_PLAGUE_CITY_MUD_PATCH_1785
        val MANS = intArrayOf(NPCs.MAN_728, NPCs.MAN_729, NPCs.MAN_351)
        val WOMANS = intArrayOf(NPCs.WOMAN_352, NPCs.WOMAN_353, NPCs.WOMAN_354, NPCs.WOMAN_360, NPCs.WOMAN_362, NPCs.WOMAN_363)
    }

    override fun defineListeners() {
        on(NPCs.BILLY_REHNISON_723, IntType.NPC, "talk-to") { player, _ ->
            sendMessage(player, "Billy isn't interested in talking.")
            return@on true
        }

        on(MANS, IntType.NPC, "talk-to") { player, _ ->
            if (inBorders(player, 2496, 3280, 2557, 3336)) {
                openDialogue(player, ManRehnisonDialogue())
            }
            return@on true
        }

        on(WOMANS, IntType.NPC, "talk-to") { player, _ ->
            if (inBorders(player, 2496, 3280, 2557, 3336)) {
                openDialogue(player, WomanDialogue())
            }
            return@on true
        }

        on(
            intArrayOf(Scenery.ARDOUGNE_WALL_DOOR_9738, Scenery.ARDOUGNE_WALL_DOOR_9330),
            IntType.SCENERY,
            "open",
        ) { player, _ ->
            if (inBorders(player, 2556, 3298, 2557, 3301)) {
                sendMessage(player, "You pull on the large wooden doors...")
                sendMessageWithDelay(player, "...But they will not open.", 1)
            } else {
                sendMessage(player, "You try to open the large wooden doors...")
                sendMessageWithDelay(player, "...But they will not open.", 1)
                sendNPCDialogue(player, NPCs.MOURNER_2349, "Oi! What are you doing? Get away from there!")
            }
            return@on true
        }

        on(Scenery.MANHOLE_2543, IntType.SCENERY, "open") { player, node ->
            if (isQuestInProgress(player, Quests.BIOHAZARD, 1, 99)) {
                sendMessage(player, "The trapdoor is bolted on the other side.")
                return@on true
            }
            replaceScenery(node.asScenery(), Scenery.MANHOLE_2544, -1)
            playAudio(player, Sounds.SWINGING_SIGN_RUSTY_2970)
            addScenery(Scenery.MANHOLE_COVER_2545, Location(2529, 3302, 0), 0, 10)
            sendMessage(player, "You pull back the manhole cover.")
            return@on true
        }

        on(Scenery.MANHOLE_COVER_2545, IntType.SCENERY, "close") { player, node ->
            removeScenery(node.asScenery())
            playAudio(player, Sounds.MANHOLE_CLOSE_74)
            getScenery(location(2529, 3303, 0))?.let {
                replaceScenery(it, Scenery.MANHOLE_2543, -1)
            }
            sendMessage(player, "You close the manhole cover.")
            return@on true
        }

        on(Scenery.MANHOLE_2544, IntType.SCENERY, "climb-down") { player, _ ->
            if (isQuestInProgress(player, Quests.BIOHAZARD, 1, 100)) {
                sendMessage(player, "The trapdoor is bolted on the other side.")
            } else {
                animate(player, Animations.MULTI_BEND_OVER_827)
                queueScript(player, 2, QueueStrength.SOFT) {
                    teleport(player, Location(2514, 9739, 0))
                    sendMessage(player, "You climb down through the manhole.")
                    return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        on(Scenery.DOOR_2528, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 13) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendNPCDialogue(player, NPCs.BRAVEK_711, "Go away, I'm busy! I'm... Umm... In a meeting!")
                sendMessage(player, "The door won't open.")
            }
            return@on true
        }

        on(Scenery.MUD_PILE_2533, IntType.SCENERY, "climb") { player, _ ->
            animate(player, Animations.USE_LADDER_828)
            queueScript(player, 2, QueueStrength.SOFT) {
                teleport(player, Location(2566, 3333, 0))
                sendDialogue(player, "You climb up the mud pile.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Items.A_MAGIC_SCROLL_1505, IntType.ITEM, "read") { player, _ ->
            sendItemDialogue(player, Items.A_MAGIC_SCROLL_1505, "You memorise what is written on the scroll.")
            removeItem(player, Items.A_MAGIC_SCROLL_1505)
            sendDialogue(
                player,
                "You can now cast the Ardougne Teleport spell provided you have the required runes and magic level.",
            )
            return@on true
        }

        on(Items.A_SCRUFFY_NOTE_1508, IntType.ITEM, "read") { player, _ ->
            sendMessage(player, "You guess it really says something slightly different.")
            openInterface(player, Components.BLANK_SCROLL_222).also { scruffyNote(player) }
            return@on true
        }

        on(Scenery.DOOR_35991, IntType.SCENERY, "open") { player, node ->
            val questStage = getQuestStage(player, Quests.PLAGUE_CITY)
            if (questStage < 11) {
                sendDialogueLines(player, "The door won't open.", "You notice a black cross on the door.")
                return@on true
            }

            if (questStage == 11) {
                openDialogue(player, HeadMournerDialogue())
                return@on true
            }

            if (questStage == 16) {
                sendPlayerDialogue(player, "I have a warrant from Bravek to enter here.")
                addDialogueAction(player) { player, button ->
                    if (button > 0) {
                        sendNPCDialogue(
                            player, NPCs.MOURNER_3216, "This is highly irregular. Please wait...", FaceAnim.ANNOYED
                        )
                        addDialogueAction(player) { player, button ->
                            if (button > 0) {
                                lock(player, 6)
                                lockInteractions(player, 6)

                                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                                    when (stage) {
                                        0 -> {
                                            findLocalNPC(player, NPCs.MOURNER_717)?.apply {
                                                sendChat("Hey... I got someone here with a warrant from Bravek, what should we do?")
                                                faceLocation(location(2536, 3273, 0))
                                            }
                                            return@queueScript delayScript(player, 1)
                                        }

                                        1 -> {
                                            findLocalNPC(player, NPCs.MOURNER_3216)?.apply {
                                                sendChat("Well, you can't let them in...", 1)
                                                faceLocation(location(2537, 3273, 0))
                                            }
                                            return@queueScript delayScript(player, 1)
                                        }

                                        2 -> {
                                            getObject(location(2540, 3273, 0))?.asScenery()?.let {
                                                DoorActionHandler.handleAutowalkDoor(player, it)
                                            }
                                            return@queueScript delayScript(player, 3)
                                        }

                                        3 -> {
                                            setQuestStage(player, Quests.PLAGUE_CITY, 17)
                                            sendDialogueLines(
                                                player,
                                                "You wait until the mourner's back is turned and sneak into the building."
                                            )
                                            return@queueScript stopExecuting(player)
                                        }

                                        else -> return@queueScript stopExecuting(player)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (questStage in 17..100) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, MournerWestDialogue())
            }

            return@on true
        }


        on(Scenery.WARDROBE_2524, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_WARDROBE_545)
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        on(Scenery.WARDROBE_2525, IntType.SCENERY, "close") { player, node ->
            animate(player, Animations.CLOSE_WARDROBE_544)
            replaceScenery(node.asScenery(), node.id - 1, -1)
            return@on true
        }

        on(Scenery.WARDROBE_2525, IntType.SCENERY, "search") { player, _ ->
            if (freeSlots(player) == 0 && !inEquipmentOrInventory(player, Items.GAS_MASK_1506)) {
                sendItemDialogue(
                    player,
                    Items.GAS_MASK_1506,
                    "You find a protective mask but you don't have enough room to take it.",
                )
            } else if (inEquipmentOrInventory(player, Items.GAS_MASK_1506)) {
                sendMessage(player, "You search the wardrobe but you find nothing.")
            } else if (getQuestStage(player, Quests.PLAGUE_CITY) >= 2) {
                sendItemDialogue(player, Items.GAS_MASK_1506, "You find a protective mask.")
                addItem(player, Items.GAS_MASK_1506)
            } else {
                sendMessage(player, "You search the wardrobe but you find nothing.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.BUCKET_OF_WATER_1929, Scenery.MUD_PATCH_11418) { player, _, _ ->
            val bucketUses = getAttribute(player, BUCKET_USES_ATTRIBUTE, 0)

            if (bucketUses in 0..2 && removeItem(player, Items.BUCKET_OF_WATER_1929)) {
                animate(player, Animations.POUR_BUCKET_OVER_GROUND_2283)
                playAudio(player, Sounds.WATER_BEING_POURED_2982)
                if (bucketUses < 3) {
                    sendDialogueLines(player, "You pour water onto the soil.", "The soil softens slightly.")
                    player.incrementAttribute(BUCKET_USES_ATTRIBUTE, 1)
                    addItem(player, Items.BUCKET_1925)
                } else if (bucketUses == 3) {
                    sendDialogueLines(player, "You pour water onto the soil.", "The soil is now soft enough to dig into.")
                    setVarbit(player, MUD_PATCH_VARBIT, 1, true)
                    addItem(player, Items.BUCKET_1925)
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(Scenery.DUG_HOLE_11417, IntType.SCENERY, "climb-down") { player, _ ->
            if (getQuestStage(player, Quests.BIOHAZARD) > 1) {
                sendMessage(player, "The ground's been filled in and packed hard.")
                return@on true
            }

            lock(player, 3)
            animate(player, 827)
            queueScript(player, 1, QueueStrength.SOFT) {
                teleport(player, Location(2518, 9759))
                setQuestStage(player, Quests.PLAGUE_CITY, 4)
                sendDialogueLines(
                    player,
                    "You fall through...",
                    "...you land in the sewer.",
                    "Edmond follows you down the hole.",
                )
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Scenery.GRILL_11423, IntType.SCENERY, "open") { player, _ ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) == 4) {
                sendDialogue(player, "The grill is too secure. You can't pull it off alone.")
                animate(player, Animations.PC_SEWERS_REMOVE_GRILL_3192)
                playAudio(player, Sounds.PLAGUE_PULL_GRILL_1730)
                setQuestStage(player, Quests.PLAGUE_CITY, 5)
            } else {
                sendDialogue(player, "There is a grill blocking your way.")
            }
            return@on true
        }

        on(Scenery.PIPE_2542, IntType.SCENERY, "climb-up") { player, _ ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 7 && inEquipment(player, Items.GAS_MASK_1506)) {
                forceMove(player, player.location, Location(2514, 9737, 0), 0, 30, Direction.SOUTH, 10580)
                runTask(player, 3) {
                    teleport(player, Location(2529, 3304, 0))
                    sendDialogue(player, "You climb up through the sewer pipe.")
                    playAudio(player, Sounds.SQUEEZE_OUT_2490)
                }
                return@on true
            }

            if (!inEquipment(player, Items.GAS_MASK_1506)) {
                sendNPCDialogue(player, NPCs.EDMOND_714, "I can't let you enter the city without your gasmask on.")
            } else {
                sendDialogue(player, "There is a grill blocking your way.")
            }

            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.PIPE_2542) { player, _, _ ->
            sendPlayerDialogue(player, "Maybe I should try opening it first.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.GRILL_11423) { player, _, _ ->
            lock(player, 5)
            lockInteractions(player, 5)

            if (!removeItem(player, Items.ROPE_954)) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                lock(player, 4)
                submitIndividualPulse(
                    player,
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> forceWalk(player, Location(2514, 9740, 0), "SMART")
                                1 -> face(player, Location(2514, 9739, 0), 2)
                                2 -> {
                                    animate(player, Animations.PC_SEWERS_TIE_ROPE_3191)
                                    playAudio(player, Sounds.PLAGUE_ATTACH_1731)
                                    setVarbit(player, TIED_ROPE_VARBIT, 5, true)
                                }

                                3 -> {
                                    setQuestStage(player, Quests.PLAGUE_CITY, 6)
                                    sendItemDialogue(
                                        player,
                                        Items.ROPE_954,
                                        "You tie the end of the rope to the sewer pipe's grill.",
                                    )
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@onUseWith true
        }

        on(Scenery.DOOR_2537, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 9) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            npc = NPC(NPCs.TED_REHNISON_721)
                            when (stage) {
                                0 -> if (inInventory(player, Items.BOOK_1509)) {
                                    playerl(FaceAnim.NEUTRAL, "I'm a friend of Jethick's, I have come to return a book he borrowed.").also { stage++ }
                                } else {
                                    npcl(FaceAnim.FRIENDLY, "Go away. We don't want any.").also { stage = END_DIALOGUE }
                                }

                                1 -> npcl(FaceAnim.FRIENDLY, "Oh... why didn't you say, come in then.").also { stage++ }

                                2 -> {
                                    sendItemDialogue(player, Items.BOOK_1509, "You hand the book to Ted as you enter.")
                                    DoorActionHandler.handleAutowalkDoor(player, getScenery(2531, 3328, 0)!!)
                                    removeItem(player, Items.BOOK_1509)
                                    stage++
                                }

                                3 -> {
                                    end()
                                    npcl(FaceAnim.NEUTRAL, "Thanks, I've been missing that.")
                                    setQuestStage(player, Quests.PLAGUE_CITY, 9)
                                }
                            }
                        }
                    },
                )
            }
            return@on true
        }

        on(Scenery.BARREL_2530, IntType.SCENERY, "search") { player, _ ->
            animate(player, Animations.SEARCHING_CRATES_6840)
            if (inInventory(player, Items.KEY_423) || freeSlots(player) == 0) {
                sendMessage(player, "You don't find anything interesting.")
                return@on true
            }
            sendItemDialogue(player, Items.KEY_423, "You find a small key in the barrel.")
            addItem(player, Items.KEY_423)
            return@on true
        }

        on(
            intArrayOf(Scenery.SPOOKY_STAIRS_2522, Scenery.SPOOKY_STAIRS_2523),
            IntType.SCENERY,
            "walk-down",
            "walk-up",
        ) { player, node ->
            if (node.id == Scenery.SPOOKY_STAIRS_2522) {
                sendMessage(player, "You walk down the stairs...")
                teleport(player, Location(2537, 9671))
            } else {
                sendMessage(player, "You walk up the stairs...")
                teleport(player, Location(2536, 3271))
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.KEY_423, Scenery.DOOR_2526) { player, _, _ ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 16) {
                DoorActionHandler.handleAutowalkDoor(player, getScenery(Location(2539, 9672, 0))!!.asScenery())
                sendMessage(player, "You unlock the door.")
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@onUseWith true
        }

        on(Scenery.DOOR_2526, IntType.SCENERY, "open") { player, node ->
            if (isQuestComplete(player, Quests.PLAGUE_CITY) || player.location.x > 2539) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int, ) {
                            npc = NPC(NPCs.ELENA_3215)
                            when (stage) {
                                0 -> npcl(FaceAnim.CRYING, "Hey get me out of here please!").also { stage++ }
                                1 -> playerl(FaceAnim.FRIENDLY, "I would do but I don't have a key.").also { stage++ }
                                2 -> npcl(FaceAnim.SAD, "I think there may be one around somewhere. I'm sure I heard them stashing it somewhere.").also { stage++ }
                                3 -> options("Have you caught the plague?", "Okay, I'll look for it.").also { stage++ }
                                4 -> when (buttonID) {
                                    1 -> playerl(FaceAnim.FRIENDLY, "Have you caught the plague?").also { stage++ }
                                    2 -> playerl(FaceAnim.FRIENDLY, "Okay, I'll look for it.").also { stage = END_DIALOGUE }
                                }
                                5 -> npcl(FaceAnim.HALF_WORRIED, "No, I have none of the symptoms.").also { stage++ }
                                6 -> playerl(FaceAnim.THINKING, "Strange, I was told this house was plague infected.").also { stage++ }
                                7 -> playerl(FaceAnim.THINKING, "I suppose that was a cover up by the kidnappers.").also { stage = 3 }
                            }
                        }
                    },
                )
            }
            return@on true
        }
    }

    private fun scruffyNote(player: Player) {
        val scruffynotes = arrayOf(
            "Got a bncket of nnilk",
            "Tlen grind sorne lhoculate",
            "vnith a pestal and rnortar",
            "ald the grourd dlocolate to tho milt",
            "finales add 5cme snape gras5",
        )
        sendString(player, scruffynotes.joinToString("<br>"), Components.BLANK_SCROLL_222, 5)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.GRILL_11423) { _, _ ->
            return@setDest Location.create(
                2514,
                9739,
                0,
            )
        }
        setDest(IntType.SCENERY, Scenery.PIPE_2542) { _, _ ->
            return@setDest Location.create(
                2514,
                9739,
                0,
            )
        }
        setDest(IntType.SCENERY, Scenery.MANHOLE_2544) { _, _ ->
            return@setDest Location.create(
                2529,
                3304,
                0,
            )
        }
    }
}