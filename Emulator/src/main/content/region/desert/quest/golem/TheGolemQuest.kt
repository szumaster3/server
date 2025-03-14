package content.region.desert.quest.golem

import content.global.skill.thieving.PickpocketListener
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.global.action.ClimbActionHandler
import core.game.global.action.SpecialLadder
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.*

@Initializable
class TheGolemQuest : Quest(Quests.THE_GOLEM, 70, 69, 1, Vars.VARBIT_QUEST_THE_GOLEM_PROGRESS_346, 0, 1, 10) {
    override fun drawJournal(
        player: Player,
        stage: Int,
    ) {
        super.drawJournal(player, stage)
        var ln = 11
        if (stage == 0) {
            line(player, "I can start this quest by talking to !!the golem?? who is in the:", ln++, false)
            line(player, "Ruined city of !!Uzer??, which is in the desert to the east of", ln++, false)
            line(player, "the !!Shantay Pass.", ln++, false)
            line(player, "I will need to have !!level 20 crafting?? and !!level 25 thieving", ln++, false)
        }
        if (stage >= 1) {
            line(player, "I've found !!the golem??, and offered to !!repair?? it.", ln++, stage > 1)
        }
        if (stage >= 2) {
            line(player, "I've !!repaired?? the golem with some !!soft clay??.", ln++, stage > 2)
        }
        if (stage >= 3) {
            line(player, "The golem wants me to open a portal to help it defeat", ln++, stage > 3)
            line(player, "the demon that attacked its city.", ln++, stage > 3)
        }
        val readLetter = getAttribute(player, "the-golem:read-elissa-letter", false)
        val readBook = getAttribute(player, "the-golem:varmen-notes-read", false)
        if (readLetter) {
            line(player, "I've found a letter that mentions !!The Digsite", ln++, readBook)
        }
        if (readBook) {
            line(player, "I've found a !!book?? that mentions that golems are !!programmed by??", ln++, stage > 7)
            line(player, "!!writing instructions?? on !!papyrus?? with a !!phoenix quill pen??.", ln++, stage > 7)
        }
        val hasStatuette = TheGolemListeners.hasStatuette(player)
        val doorOpen = getAttribute(player, "the-golem:door-open", false)
        if (hasStatuette) {
            line(player, "I've acquired a statuette that fits a !!mechanism?? in the !!ruins??", ln++, doorOpen)
            line(player, "of !!Uzer?? from the !!Varrock museum??.", ln++, doorOpen)
        }
        val seenDemon = getAttribute(player, "the-golem:seen-demon", false)
        if (doorOpen) {
            line(player, "I've opened the portal in the !!ruins of Uzer??.", ln++, seenDemon)
        }
        if (seenDemon) {
            line(player, "It turns out that !!the demon?? is !!already dead??!", ln++, stage > 4)
            line(player, "I should tell the golem the good news.", ln++, stage > 4)
        }
        if (stage > 4) {
            line(player, "The demon doesn't think its task is complete.", ln++, stage > 7)
        }
        if (stage >= 100) {
            ln++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", ln, false)
        }
    }

    override fun hasRequirements(player: Player?): Boolean {
        return getStatLevel(player!!, Skills.CRAFTING) >= 20 && getStatLevel(player, Skills.THIEVING) >= 25
    }

    override fun finish(player: Player) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.STATUETTE_4618, 230, 277, 5)
        drawReward(player, "1 quest point", ln++)
        drawReward(player, "1,000 Crafting XP", ln++)
        drawReward(player, "1,000 Theiving XP", ln)
        rewardXP(player, Skills.CRAFTING, 1000.0)
        rewardXP(player, Skills.THIEVING, 1000.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun updateVarps(player: Player?) {
        TheGolemListeners.updateVarps(player!!)
    }
}

@Initializable
class ClayGolemNPC : AbstractNPC {
    constructor() : super(NPCs.BROKEN_CLAY_GOLEM_1908, null, true)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC {
        return ClayGolemNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.TOUGH_GUY_1907,
            NPCs.BROKEN_CLAY_GOLEM_1908,
            NPCs.DAMAGED_CLAY_GOLEM_1909,
            NPCs.CLAY_GOLEM_1910,
        )
    }
}

class TheGolemListeners :
    InteractionListener,
    InterfaceListener {
    companion object {
        @JvmStatic
        fun hasStatuette(player: Player): Boolean {
            return player.inventory.containsAtLeastOneItem(Items.STATUETTE_4618) ||
                player.bank.containsAtLeastOneItem(Items.STATUETTE_4618) ||
                player.getAttribute(
                    "the-golem:placed-statuette",
                    false,
                )
        }

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
            setVarbit(player, 348, clayUsed)
            setVarbit(player, 354, gemsTaken)
            setVarbit(player, 355, statuetteTaken)
            setVarbit(player, 349, rotation0)
            setVarbit(player, 350, rotation1)
            setVarbit(player, 351, rotation2)
            setVarbit(player, 352, statuettePlaced * (rotation3 + 1))
        }
    }

    fun checkDoor(player: Player) {
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

        onUseWith(IntType.NPC, Items.SOFT_CLAY_1761, NPCs.TOUGH_GUY_1907) { player, used, _ ->
            if (player.questRepository.getStage(Quests.THE_GOLEM) == 1) {
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

        on(org.rs.consts.Scenery.STAIRCASE_34978, IntType.SCENERY, "climb-down") { player, node ->
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

        on(org.rs.consts.Scenery.STAIRCASE_6372, IntType.SCENERY, "climb-up") { player, node ->
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

        on(org.rs.consts.Scenery.BOOKCASE_35226, IntType.SCENERY, "search") { player, _ ->
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

        on(org.rs.consts.Scenery.DOOR_6363, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door doesn't open.")
            return@on true
        }

        /*
         * Handles entering a (portal) for first time.
         */

        on(org.rs.consts.Scenery.DOOR_6364, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You step into the portal.")
            if (!player.getAttribute("the-golem:seen-demon", false)) {
                sendMessage(player, "The room is dominated by a colossal horned skeleton!")
                setAttribute(player, "/save:the-golem:seen-demon", true)
                setQuestStage(player, Quests.THE_GOLEM, 4)
                playGlobalAudio(player.location, Sounds.GOLEM_DEMONDOOR_1848)
            }
            teleport(player, Location.create(3552, 4948, 0))
            return@on true
        }

        /*
         * Handles entering portal.
         */

        on(org.rs.consts.Scenery.PORTAL_6282, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You step into the portal.")
            playGlobalAudio(player.location, Sounds.GOLEM_TP_1851)
            teleport(player, Location.create(2722, 4911, 0))
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
                sendMessage(
                    player,
                    "You crush the mushroom, but you have no vial to put the ink in and it goes everywhere!",
                )
                removeItem(player, Item(Items.BLACK_MUSHROOM_4620, 1))
                return@onUseWith true
            }
            if (removeItem(player, with.asItem(), Container.INVENTORY) &&
                removeItem(player, Items.VIAL_229, Container.INVENTORY)
            ) {
                sendItemDialogue(
                    player,
                    Items.BLACK_MUSHROOM_INK_4622,
                    "You crush the mushroom and pour the juice into a vial.",
                )
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

        onUseWith(IntType.NPC, Items.STRANGE_IMPLEMENT_4619, NPCs.TOUGH_GUY_1907) { player, _, _ ->
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

        onUseWith(IntType.NPC, Items.GOLEM_PROGRAM_4624, NPCs.TOUGH_GUY_1907) { player, _, with ->
            playGlobalAudio(player.location, Sounds.GOLEM_PROGRAM_1849)
            player.dialogueInterpreter.open(ClayGolemProgramDialogueFile(), with)
            return@onUseWith true
        }

        /*
         * Handles grabbing a feather from the Desert Phoenix.
         */

        on(NPCs.DESERT_PHOENIX_1911, IntType.NPC, "grab-feather") { player, node ->
            if (getAttribute(player, "the-golem:varmen-notes-read", false)) {
                lock(player, 1000)
                val lootTable =
                    PickpocketListener.pickpocketRoll(
                        player = player,
                        low = 90.0,
                        high = 240.0,
                        table = WeightBasedTable.create(WeightedItem(Items.PHOENIX_FEATHER_4621, 1, 1, 1.0, true)),
                    )
                if (lootTable != null) {
                    sendMessage(player, "You attempt to grab the pheonix's tail-feather.")
                    animate(player, Animations.PICK_POCKET_881)
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
