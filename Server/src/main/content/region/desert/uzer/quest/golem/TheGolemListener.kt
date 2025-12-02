package content.region.desert.uzer.quest.golem

import content.global.skill.thieving.ThievingDefinition
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.global.action.ClimbActionHandler
import core.game.global.action.SpecialLadder
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.*

class TheGolemListener : InteractionListener, InterfaceListener {

    companion object {
        val LETTER_LINES = arrayOf(
            "",
            "",
            "Dearest Varmen,",
            "I hope this finds you well. Here are the books you asked for",
            "There has been an exciting development closer to home --",
            "another city from the same period has been discovered east",
            "of Varrock, and we are starting a huge excavation project",
            "here. I don't know if the museum will be able to finance your",
            "expedition as well as this one, so I fear your current trip will be",
            "the last.",
            "May Saradomin grant you a safe journey home",
            "Your loving Elissa.",
        )

        @JvmStatic
        fun hasStatuette(player: Player): Boolean =
            player.inventory.containsAtLeastOneItem(Items.STATUETTE_4618) ||
                player.bank.containsAtLeastOneItem(Items.STATUETTE_4618) ||
                player.getAttribute(
                    "the-golem:placed-statuette",
                    false,
                )

        @JvmStatic
        fun initializeStatuettes(player: Player) {
            if (!player.getAttribute("the-golem:statuette-rotation:initialized", false)) {
                for (i in 0 until 4) {
                    setAttribute(player, "/save:the-golem:statuette-rotation:$i", RandomFunction.random(2))
                }
                setAttribute(player, "/save:the-golem:statuette-rotation:initialized", true)
            }
        }

        @JvmStatic
        fun updateVarps(player: Player) {
            val clayUsed = player.getAttribute("the-golem:clay-used", 0)
            val gemsTaken =
                if (player.getAttribute("the-golem:gems-taken", false)) {
                    1
                } else {
                    0
                }
            val statuetteTaken =
                if (hasStatuette(player)) {
                    1
                } else {
                    0
                }
            val statuettePlaced =
                if (player.getAttribute("the-golem:placed-statuette", false)) {
                    1
                } else {
                    0
                }
            initializeStatuettes(player)
            val rotation0 = player.getAttribute("the-golem:statuette-rotation:0", 0)
            val rotation1 = player.getAttribute("the-golem:statuette-rotation:1", 0)
            val rotation2 = player.getAttribute("the-golem:statuette-rotation:2", 0)
            val rotation3 = player.getAttribute("the-golem:statuette-rotation:3", 0)
            val doorOpen = player.getAttribute("the-golem:door-open", false)
            var clientStage = 0
            if (getQuestStage(player, Quests.THE_GOLEM) > 0) {
                clientStage = Math.max(clientStage, 1)
            }
            if (doorOpen) {
                clientStage = Math.max(clientStage, 5)
            }
            if (getQuestStage(player, Quests.THE_GOLEM) >= 100) {
                clientStage = Math.max(clientStage, 10)
            }
            setVarbit(player, Vars.VARBIT_QUEST_THE_GOLEM_PROGRESS_346, clientStage, true)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_CLAY_USED_348, clayUsed)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_REMOVED_GEMS_AGRITH_NAAR_THRONE_354, gemsTaken)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_STATUETTE_TAKEN_355, statuetteTaken)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_STATUETTE_ROTATION_349, rotation0)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_STATUETTE_ROTATION_350, rotation1)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_STATUETTE_ROTATION_351, rotation2)
            setVarbit(player, Vars.VARBIT_THE_GOLEM_STATUETTE_PLACED_352, statuettePlaced * (rotation3 + 1))
        }
    }

    private fun checkDoor(player: Player) {
        if (!player.getAttribute("the-golem:door-open", false)) {
            val rotation0 = player.getAttribute("the-golem:statuette-rotation:0", 0)
            val rotation1 = player.getAttribute("the-golem:statuette-rotation:1", 0)
            val rotation2 = player.getAttribute("the-golem:statuette-rotation:2", 0)
            val rotation3 = player.getAttribute("the-golem:statuette-rotation:3", 0)
            val placed = player.getAttribute("the-golem:placed-statuette", false)
            if (rotation0 == 1 && rotation1 == 1 && rotation2 == 0 && rotation3 == 0 && placed) {
                sendMessage(player, "The door grinds open.")
                setAttribute(player, "/save:the-golem:door-open", true)
            }
        }
    }

    override fun defineDestinationOverrides() {
        addClimbDest(Location.create(3492, 3089, 0), Location.create(2722, 4886, 0))
        addClimbDest(Location.create(2721, 4884, 0), Location.create(3491, 3090, 0))
        setDest(IntType.SCENERY, intArrayOf(34978), "climb-down") { _, _ ->
            return@setDest Location.create(3491, 3090, 0)
        }
        setDest(IntType.SCENERY, intArrayOf(6372), "climb-up") { _, _ -> return@setDest Location.create(2722, 4886, 0) }
    }

    override fun defineListeners() {
        /*
         * Handles using the soft clay on Golem NPC.
         */

        onUseWith(IntType.NPC, Items.SOFT_CLAY_1761, NPCs.CLAY_GOLEM_1907) { player, used, _ ->
            if (getQuestStage(player, Quests.THE_GOLEM) == 1) {
                var clayUsed = player.getAttribute("the-golem:clay-used", 0)
                val msg =
                    when (clayUsed) {
                        0 -> "You apply some clay to the golem's wounds. The clay begins to harden in the hot sun."
                        1 -> "You fix the golem's legs."
                        2 -> "The golem is nearly whole."
                        3 -> "You repair the golem with a final piece of clay."
                        else -> "Maybe you should ask the golem first!"
                    }
                if (removeItem(player, used.asItem())) {
                    playGlobalAudio(player.location, Sounds.GOLEM_REPAIRCLAY_1850)
                    if (msg != null) {
                        sendItemDialogue(player, Items.SOFT_CLAY_1761, msg)
                    }
                    clayUsed = Math.min(clayUsed + 1, 4)
                    setAttribute(player, "/save:the-golem:clay-used", clayUsed)
                    updateVarps(player)
                    if (clayUsed == 4) {
                        setQuestStage(player, Quests.THE_GOLEM, 2)
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles climbing down the staircase.
         */

        on(Scenery.STAIRCASE_34978, IntType.SCENERY, "climb-down") { player, node ->
            ClimbActionHandler.climb(
                player,
                null,
                SpecialLadder.getDestination(node.location)!!,
            )
            return@on true
        }

        /*
         * Handles climbing up the staircase.
         */

        on(Scenery.STAIRCASE_6372, IntType.SCENERY, "climb-up") { player, node ->
            ClimbActionHandler.climb(player, null, SpecialLadder.getDestination(node.location)!!)
            return@on true
        }

        /*
         * Handles reading a letter.
         */

        on(Items.LETTER_4615, IntType.ITEM, "read") { player, _ ->
            setAttribute(player, "ifaces:220:lines", LETTER_LINES)
            setAttribute(player, "/save:the-golem:read-elissa-letter", true)
            openInterface(player, Components.MESSAGESCROLL_220)
            return@on true
        }

        /*
         * Handles searching a bookcase.
         */

        on(Scenery.BOOKCASE_35226, IntType.SCENERY, "search") { player, _ ->
            val notes = hasAnItem(player, Items.VARMENS_NOTES_4616).container != null
            val readLetter = player.getAttribute("the-golem:read-elissa-letter", false)

            sendMessage(player, "You search the bookcase.")

            if (!notes && readLetter) {
                sendItemDialogue(player, Items.VARMENS_NOTES_4616, "You find Varmen's expedition notes.")
                addItemOrDrop(player, Items.VARMENS_NOTES_4616, 1)
            } else {
                sendMessage(player, "You find nothing of interest.")
            }

            return@on true
        }

        /*
         * Handles opening a door.
         */

        on(Scenery.DOOR_6363, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door doesn't open.")
            return@on true
        }

        /*
         * Handles entering a (portal) for first time.
         */

        on(Scenery.DOOR_6364, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You step into the portal.")
            if (!player.getAttribute("the-golem:seen-demon", false)) {
                sendMessage(player, "The room is dominated by a colossal horned skeleton!")
                setAttribute(player, "/save:the-golem:seen-demon", true)
                setQuestStage(player, Quests.THE_GOLEM, 4)
                playGlobalAudio(player.location, Sounds.GOLEM_DEMONDOOR_1848)
            }
            teleport(player, Location.create(3552, 4948, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles entering portal.
         */

        on(Scenery.PORTAL_6282, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You step into the portal.")
            playGlobalAudio(player.location, Sounds.GOLEM_TP_1851)
            teleport(player, Location.create(2722, 4911, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles using a chisel / hammer on a throne scenery.
         */

        onUseWith(IntType.SCENERY, intArrayOf(Items.CHISEL_1755, Items.HAMMER_2347), 6301) { player, _, _ ->
            if (player.getAttribute("the-golem:gems-taken", false)) {
                return@onUseWith true
            }
            if (!anyInInventory(player, Items.HAMMER_2347, Items.CHISEL_1755)) {
                sendMessage(player, "You'll need a chisel as well as a hammer to get the gems.")
                return@onUseWith true
            }
            if (freeSlots(player) < 6) {
                sendMessage(player, "You don't have enough free space to remove all six gems.")
                return@onUseWith true
            }

            sendItemDialogue(player, Items.RUBY_1603, "You prize the gems from the demon's throne.")
            setAttribute(player, "/save:the-golem:gems-taken", true)
            addItem(player, Items.SAPPHIRE_1607, 2)
            addItem(player, Items.EMERALD_1605, 2)
            addItem(player, Items.RUBY_1603, 2)
            updateVarps(player)
            return@onUseWith true
        }

        /*
         * Handles placing a statuette into an alcove.
         */

        onUseWith(IntType.SCENERY, 4618, 6309) { player, _, _ ->
            if (removeItem(player, Items.STATUETTE_4618)) {
                sendMessage(player, "You insert the statuette into the alcove.")
                setAttribute(player, "/save:the-golem:placed-statuette", true)
                updateVarps(player)
            }
            return@onUseWith true
        }

        /*
         * Handles turning a statue.
         */

        on(intArrayOf(6307, 6308), IntType.SCENERY, "turn") { player, node ->
            playGlobalAudio(player.location, Sounds.TURN_STATUE_1852)
            if (player.getAttribute("the-golem:door-open", false)) {
                sendMessage(player, "You've already opened the door.")
                return@on true
            }
            val index =
                when (node.asScenery().wrapper.id) {
                    6303 -> 0
                    6304 -> 1
                    6305 -> 2
                    6306 -> 3
                    else -> return@on true
                }

            initializeStatuettes(player)
            val rotation = 1 - player.getAttribute("the-golem:statuette-rotation:$index", 0)
            val dir = if (rotation == 0) "right" else "left"
            sendMessage(player, "You turn the statuette to the $dir.")
            setAttribute(player, "the-golem:statuette-rotation:$index", rotation)
            checkDoor(player)
            updateVarps(player)
            return@on true
        }

        /*
         * Handles using a pestle and mortar with a black mushroom.
         */

        onUseWith(IntType.ITEM, Items.PESTLE_AND_MORTAR_233, Items.BLACK_MUSHROOM_4620) { player, _, with ->
            if (!inInventory(player, Items.VIAL_229)) {
                sendMessage(player, "You crush the mushroom, but you have no vial to put the ink in and it goes everywhere!")
                removeItem(player, Item(Items.BLACK_MUSHROOM_4620, 1))
                return@onUseWith true
            }
            if (removeItem(player, with.asItem(), Container.INVENTORY) && removeItem(player, Items.VIAL_229, Container.INVENTORY)) {
                sendItemDialogue(player, Items.BLACK_MUSHROOM_INK_4622, "You crush the mushroom and pour the juice into a vial.")
                addItem(player, Items.BLACK_MUSHROOM_INK_4622, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles using a phoenix feather with black mushroom ink.
         */

        onUseWith(IntType.ITEM, Items.PHOENIX_FEATHER_4621, Items.BLACK_MUSHROOM_INK_4622) { player, _, _ ->
            if (removeItem(player, Item(Items.PHOENIX_FEATHER_4621, 1), Container.INVENTORY)) {
                sendItemDialogue(player, Items.PHOENIX_QUILL_PEN_4623, "You dip the phoenix feather into the ink.")
                addItem(player, Items.PHOENIX_QUILL_PEN_4623, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles using papyrus with a phoenix quill pen.
         */

        onUseWith(IntType.ITEM, Items.PAPYRUS_970, Items.PHOENIX_QUILL_PEN_4623) { player, _, _ ->
            if (!player.getAttribute("the-golem:varmen-notes-read", false)) {
                sendMessage(player, "You don't know what to write.")
                return@onUseWith true
            }
            if (removeItem(player, Item(Items.PAPYRUS_970, 1), Container.INVENTORY)) {
                sendItemDialogue(player, Items.GOLEM_PROGRAM_4624, "You write on the papyrus:<br>YOUR TASK IS DONE")
                addItem(player, Items.GOLEM_PROGRAM_4624, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles inserting the strange implement into the golem's skull.
         */

        onUseWith(IntType.NPC, Items.STRANGE_IMPLEMENT_4619, NPCs.CLAY_GOLEM_1907) { player, _, _ ->
            val questStage = getQuestStage(player, Quests.THE_GOLEM)
            if (!player.getAttribute("the-golem:varmen-notes-read", false)) {
                sendMessage(player, "You can't see a way to put the instructions in the golem's skull.")
                return@onUseWith true
            }
            if (questStage == 7) {
                sendMessage(player, "You insert the key and the golem's skull hinges open.")
                sendMessage(player, "The golem's skull shuts automatically.")
                setQuestStage(player, Quests.THE_GOLEM, 8)
            }
            return@onUseWith true
        }

        /*
         * Handles use golem program on the golem's skull.
         */

        onUseWith(IntType.NPC, Items.GOLEM_PROGRAM_4624, NPCs.CLAY_GOLEM_1907) { player, _, with ->
            playGlobalAudio(player.location, Sounds.GOLEM_PROGRAM_1849)
            openDialogue(player, with.id, with.id)
            return@onUseWith true
        }

        /*
         * Handles grabbing a feather from the Desert Phoenix.
         */

        on(NPCs.DESERT_PHOENIX_1911, IntType.NPC, "grab-feather") { player, node ->
            if (getAttribute(player, "the-golem:varmen-notes-read", false)) {
                lock(player, 1000)
                val lootTable =
                    ThievingDefinition.pickpocketRoll(player = player, low = 90.0, high = 240.0, table = WeightBasedTable.create(WeightedItem(Items.PHOENIX_FEATHER_4621, 1, 1, 1.0, true)))
                if (lootTable != null) {
                    sendMessage(player, "You attempt to grab the pheonix's tail-feather.")
                    animate(player, Animations.HUMAN_PICKPOCKETING_881)
                    runTask(player, 3) {
                        lootTable.forEach {
                            player.inventory.add(it)
                            sendMessage(player, "You grab a tail-feather.")
                        }
                        unlock(player)
                        return@runTask
                    }
                } else {
                    node.asNpc().face(player)
                    animate(node.asNpc(), Animations.PHOENIX_ATK_6811)
                    node.asNpc().sendChat("Squawk!")
                    sendMessage(player, "You fail to take the Desert Phoenix tail-feather.")
                    node.asNpc().face(null)
                }
            } else {
                sendMessage(player, "You have no reason to take the phoenix's feathers.")
            }
            return@on true
        }
    }

    override fun defineInterfaceListeners() {
        /*
         * Handles opening the message scroll interface.
         */

        onOpen(Components.MESSAGESCROLL_220) { player, _ ->
            val lines: Array<String> = player.getAttribute("ifaces:220:lines", arrayOf())
            for (i in 0 until Math.min(lines.size, 15)) {
                sendString(player, lines[i], Components.MESSAGESCROLL_220, i + 1)
            }
            return@onOpen true
        }
    }
}
