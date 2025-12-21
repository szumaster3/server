package content.region.asgarnia.falador.quest.rd.plugin.tests

import content.region.asgarnia.falador.quest.rd.RDUtils
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery
import shared.consts.Sounds

class MissCheeversPlugin : InteractionListener {

    companion object {
        /**
         * Varbit used to track the state of the door.
         */
        const val doorVarbit = 686

        /**
         * Attributes representing various tools and items.
         */
        const val book = "rd:book"
        const val magnet = "rd:magnet"
        const val tin = "rd:tin"
        const val chisel = "rd:chisel"
        const val wire = "rd:wire"
        const val knife = "rd:knife"
        const val shears = "rd:shears"
        const val vials = "rd:3vialsofliquid"

        /**
         * Item ids for the tools that can be used by the player.
         */
        val toolIDs = intArrayOf(Items.BRONZE_WIRE_5602, Items.CHISEL_5601, Items.KNIFE_5605)

        /**
         * Item ids for potion ingredients or powders.
         */
        val potionIDs = intArrayOf(Items.TIN_ORE_POWDER_5583, Items.CUPRIC_ORE_POWDER_5584)

        /**
         * Enum representing all vials used in the Recruitment Drive quest.
         *
         * @property itemId The item id of the vial.
         * @property attribute The string key used to track the vial in player attributes.
         */
        enum class Vials(val itemId: Int, val attribute: String) {
            CUPRIC_SULPHATE_5577(Items.CUPRIC_SULPHATE_5577, "rd:cupricsulphate"),
            ACETIC_ACID_5578(Items.ACETIC_ACID_5578, "rd:aceticacid"),
            GYPSUM_5579(Items.GYPSUM_5579, "rd:gypsum"),
            SODIUM_CHLORIDE_5580(Items.SODIUM_CHLORIDE_5580, "rd:sodiumchloride"),
            NITROUS_OXIDE_5581(Items.NITROUS_OXIDE_5581, "rd:nitrousoxide"),
            VIAL_OF_LIQUID_5582(Items.VIAL_OF_LIQUID_5582, "rd:vialofliquid"),
            TIN_ORE_POWDER_5583(Items.TIN_ORE_POWDER_5583, "rd:tinorepowder"),
            CUPRIC_ORE_POWDER_5584(Items.CUPRIC_ORE_POWDER_5584, "rd:cupricorepowder");

            companion object {
                /**
                 * Map of vial item ids to their corresponding [Vials] enum instances.
                 */
                val vialMap = values().associateBy { it.itemId }
            }
        }

        /**
         * Enum representing vials required specifically for the door puzzle.
         *
         * @property itemId The item id of the vial.
         * @property attribute The attribute used to track if the vial has been applied to the door.
         *                     An empty string indicates the vial is not required for door mechanics.
         */
        enum class DoorVials(val itemId: Int, val attribute: String) {
            CUPRIC_SULPHATE_5577(Items.CUPRIC_SULPHATE_5577, "rd:doorcupricsulphate"),
            ACETIC_ACID_5578(Items.ACETIC_ACID_5578, ""),
            SODIUM_CHLORIDE_5580(Items.SODIUM_CHLORIDE_5580, ""),
            VIAL_OF_LIQUID_5582(Items.VIAL_OF_LIQUID_5582, "rd:doorvialofliquid");

            companion object {
                /**
                 * Array of item ids corresponding to all [DoorVials].
                 */
                val doorVialsArray = values().map { it.itemId }.toIntArray()

                /**
                 * Map of door vial item ids to their corresponding DoorVials enum instances.
                 */
                val doorVialsMap = values().associateBy { it.itemId }

                /**
                 * Map of door vial item ids that are actually required for the door puzzle.
                 */
                val doorVialsRequiredMap = values()
                    .associateBy { it.itemId }
                    .filter { it.value.attribute.isNotEmpty() }
            }
        }
    }

    override fun defineListeners() {
        val searchActions =
            mapOf(
                Scenery.OLD_BOOKSHELF_7327 to { player: Player -> RDUtils.searchingHelper(player, magnet, Items.MAGNET_5604, "You search the bookshelves...", "Hidden amongst the books you find a magnet.") },
                Scenery.OLD_BOOKSHELF_7328 to { player: Player ->
                            if (getAttribute(player, "/save:rd:help", -1) < 3) {
                                sendMessage(player, "You search the bookshelves...")
                                sendMessage(player, "You find nothing of interest.", 1)
                            } else {
                                RDUtils.searchingHelper(player, book, Items.ALCHEMICAL_NOTES_5588, "You search the bookshelves...", "You find a book that looks like it might be helpful.")
                            }
                        },
                Scenery.OLD_BOOKSHELF_7329 to { player: Player -> RDUtils.searchingHelper(player, knife, Items.KNIFE_5605, "You search the bookshelves...", "Hidden amongst the books you find a knife.") },
                Scenery.OLD_BOOKSHELF_7330 to { player: Player -> RDUtils.searchingHelper(player, "", 0, "You search the bookshelves...", "") })

        searchActions.forEach { (scenery, action) ->
            on(scenery, IntType.SCENERY, "search") { player, _ ->
                action(player)
                return@on true
            }
        }

        val vialItems =
            listOf(
                Items.ACETIC_ACID_5578,
                Items.CUPRIC_SULPHATE_5577,
                Items.GYPSUM_5579,
                Items.SODIUM_CHLORIDE_5580,
                Items.NITROUS_OXIDE_5581,
                Items.TIN_ORE_POWDER_5583,
                Items.CUPRIC_ORE_POWDER_5584
            )
        val sceneryIDs = (Scenery.SHELVES_7333..Scenery.SHELVES_7339).toList()

        vialItems.forEachIndexed { index, item ->
            on(sceneryIDs[index], IntType.SCENERY, "search") { player, _ ->
                val vialList = mutableListOf<Int>()
                if (!getAttribute(player, Vials.vialMap[item]?.attribute ?: return@on false, false)) {
                    vialList.add(item)
                }
                openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
                return@on true
            }
        }

        on(Scenery.SHELVES_7340, IntType.SCENERY, "search") { player, _ ->
            val vialCount = getAttribute(player, vials, 3)
            val vialList = List(vialCount) { Items.VIAL_OF_LIQUID_5582 }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray(), vials))
            return@on true
        }

        val crateInteractions =
            mapOf(
                Scenery.CRATE_7347 to Pair(tin, Items.TIN_5600),
                Scenery.CRATE_7348 to Pair(chisel, Items.CHISEL_5601),
                Scenery.CRATE_7349 to Pair(wire, Items.BRONZE_WIRE_5602),
            )

        crateInteractions.forEach { (scenery, attributes) ->
            on(scenery, IntType.SCENERY, "search") { player, node ->
                val (attribute, item) = attributes
                if (node.location == RDUtils.getLocationForScenery(node.asScenery())) {
                    RDUtils.searchingHelper(player, attribute, item, "You search the crate...", "Inside the crate you find a ${getItemName(item).lowercase()}.")
                } else {
                    RDUtils.searchingHelper(player, "", 0, "You search the crate...", "")
                }
                return@on true
            }
        }

        on(Scenery.CLOSED_CHEST_7350, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_7351, 100)
            return@on true
        }

        on(Scenery.OPEN_CHEST_7351, IntType.SCENERY, "search") { player, _ ->
            RDUtils.searchingHelper(player, shears, Items.SHEARS_5603, "You search the chest...", "Inside the chest you find some shears.")
            return@on true
        }

        on(Scenery.OPEN_CHEST_7351, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CLOSED_CHEST_7350, -1)
            return@on true
        }

        onUseWith(IntType.ITEM, Items.TIN_5600, Items.GYPSUM_5579) { player, used, with ->
            RDUtils.processItemUsageAndReturn(player, used.asItem(), with.asItem(), Item(Items.TIN_5592))
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.TIN_5592, Items.VIAL_OF_LIQUID_5582) { player, used, with ->
            RDUtils.processItemUsage(player, used.asItem(), with.asItem(), Item(Items.TIN_5593))
            sendMessage(player, "You notice the tin gets quite warm as you do this.")
            sendMessage(player, "A lumpy white mixture is made, that seems to be hardening.", 1)
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.TIN_5593, Scenery.KEY_7346) { player, used, _ ->
            sendMessage(player, "You make an impression of the key as the white mixture hardens.")
            replaceSlot(player, slot = used.index, Item(Items.TIN_5594))
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.TIN_5594, *potionIDs) { player, used, with ->
            RDUtils.processItemUsageAndReturn(player, used.asItem(), with.asItem(), Item(Items.TIN_5595))
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.TIN_5595, *potionIDs) { player, used, with ->
            RDUtils.processItemUsageAndReturn(player, used.asItem(), with.asItem(), Item(Items.TIN_5596))
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.TIN_5596, Scenery.BUNSEN_BURNER_7332) { player, used, _ ->
            if (removeItem(player, used.id)) {
                sendMessage(player, "You heat the two powdered ores together in the tin.")
                sendMessage(player, "You make a duplicate of the key in bronze.")
                addItemOrDrop(player, Items.TIN_5597)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.TIN_5597, *toolIDs) { player, used, _ ->
            if (removeItem(player, used.id)) {
                sendMessage(player, "You prise the duplicate key out of the tin.")
                addItemOrDrop(player, Items.TIN_5600)
                addItemOrDrop(player, Items.BRONZE_KEY_5585)
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.METAL_SPADE_5586, Scenery.BUNSEN_BURNER_7332) { player, _, _ ->
            lock(player, 3)
            sendMessage(player, "You burn the wooden handle away from the spade...")
            queueScript(player, 1, QueueStrength.WEAK) { stage: Int ->
                when (stage) {
                    0 -> {
                        visualize(player, -1, Graphics(157, 96))
                        playAudio(player, Sounds.FIREWAVE_HIT_163)
                        keepRunning(player)
                    }
                    1 -> {
                        removeItem(player, Items.METAL_SPADE_5586)
                        keepRunning(player)
                    }
                    2 -> {
                        addItem(player, Items.METAL_SPADE_5587)
                        addItem(player, Items.ASHES_592)
                        sendMessage(player, "...and are left with a metal spade with no handle.")
                        stopExecuting(player)
                    }
                    else -> stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        on(Scenery.STONE_DOOR_7343, SCENERY, "study") { player, _ ->
            sendDialogueLines(player, "There is a stone slab here obstructing the door.", "There is a small hole in the slab that looks like it might be for a handle.")
            // sendMessage(player, "It's nearly a perfect fit!")
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.METAL_SPADE_5587, Scenery.STONE_DOOR_7343) { player, used, _ ->
            if (removeItem(player, used.id)) {
                playAudio(player, Sounds.RECRUIT_SPADE_1742)
                sendMessage(player, "You slide the spade into the hole in the stone...")
                sendMessage(player, "It's nearly a perfect fit!", 1)
                setVarbit(player, doorVarbit, 1)
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, DoorVials.doorVialsArray, Scenery.STONE_DOOR_7344) { player, used, _ ->
            RDUtils.handleVialUsage(player, used.asItem())
            return@onUseWith true
        }

        on(Scenery.STONE_DOOR_7344, SCENERY, "pull-spade") { player, _ ->
            RDUtils.handleSpadePull(player)
            return@on true
        }

        on(Scenery.OPEN_DOOR_7345, SCENERY, "walk-through") { player, _ ->
            RDUtils.handleDoorWalkThrough(player)
            return@on true
        }
    }

}

private class VialShelfDialogueFile(private val flaskIdsArray: IntArray, private val specialAttribute: String? = null) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
            .branch { _ -> flaskIdsArray.size }
            .let { branch ->
                branch.onValue(3).line("There are three vials on this shelf.").options("Take the vials?").let { optionBuilder ->
                    optionBuilder.option("Take one vial.").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[0])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 3) - 1)
                            print(getAttribute(player, specialAttribute, 3))
                        }
                    }
                    optionBuilder.option("Take two vials.").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[0])
                        addItemOrDrop(player, flaskIdsArray[1])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 3) - 2)
                        }
                    }
                    optionBuilder.option("Take all three vials.").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[0])
                        addItemOrDrop(player, flaskIdsArray[1])
                        addItemOrDrop(player, flaskIdsArray[2])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 3) - 3)
                        }
                    }
                    optionBuilder.option("Don't take a vial.").end()
                }
                branch.onValue(2).line("There are two vials on this shelf.").options("Take the vials?").let { optionBuilder ->
                    optionBuilder.option("Take the first vial.").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[0])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 2) - 1)
                        } else {
                            setAttribute(player, MissCheeversPlugin.Companion.Vials.vialMap[flaskIdsArray[0]]!!.attribute, true)
                        }
                    }
                    optionBuilder.option("Take the second vial.").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[1])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 2) - 1)
                        } else {
                            setAttribute(player, MissCheeversPlugin.Companion.Vials.vialMap[flaskIdsArray[1]]!!.attribute, true)
                        }
                    }
                    optionBuilder.option("Take both vials.").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[0])
                        addItemOrDrop(player, flaskIdsArray[1])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 2) - 2)
                        } else {
                            setAttribute(player, MissCheeversPlugin.Companion.Vials.vialMap[flaskIdsArray[0]]!!.attribute, true)
                            setAttribute(player, MissCheeversPlugin.Companion.Vials.vialMap[flaskIdsArray[1]]!!.attribute, true)
                        }
                    }
                }

                branch.onValue(1).line("There is a vial on this shelf.").options("Take the vial?").let { optionBuilder ->
                    optionBuilder.option("YES").endWith { _, player ->
                        addItemOrDrop(player, flaskIdsArray[0])
                        if (specialAttribute != null) {
                            setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 1) - 1)
                        } else {
                            setAttribute(player, MissCheeversPlugin.Companion.Vials.vialMap[flaskIdsArray[0]]!!.attribute, true)
                        }
                    }
                    optionBuilder.option("NO").end()
                }

                branch.onValue(0).line("There is nothing of interest on these shelves.")
            }
    }
}

class MissCheeversDialogueFile(private val state: Int = 0) : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.MISS_CHEEVERS_2288)

        when (state) {
            0 -> handleInitialDialogue()
            1 -> handleChallengeDialogue()
        }
    }

    private fun handleInitialDialogue() {
        when (stage) {
            0 -> {
                val helpStatus = getAttribute(player!!, "/save:rd:help", -1)
                if (helpStatus > 1) {
                    playerl(FaceAnim.FRIENDLY, "Please... I am REALLY stuck... Isn't there ANYTHING you can do to help me...?").also { stage = 6 }
                } else {
                    playerl(FaceAnim.FRIENDLY, "Can you give me any help?").also { stage++ }
                }
            }
            1 -> npcl(FaceAnim.FRIENDLY, "No, I am sorry, but that is forbidden by our rules.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "If you are having a particularly tough time of it, I suggest you leave and come back later when you are in a more receptive frame of mind.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Sometimes a break from concentration will yield fresh insight. Our aim is to test you, but not to the point of frustration!").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks!").also { stage++ }
            5 -> {
                end()
                setAttribute(player!!, "/save:rd:help", 2)
            }
            6 -> npcl(FaceAnim.FRIENDLY, "Well... Look, I really shouldn't say anything about this room, but...").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "When I was attempting to join the Temple Knights I myself had to do this puzzle myself.").also { stage++ }
            8 -> npcl(FaceAnim.FRIENDLY, "It was slightly different, but the idea behind it was the same, and I left the notes I had made while doing it hidden in one of the bookcases.").also { stage++ }
            9 -> npcl(FaceAnim.FRIENDLY, "If you look carefully you may find them, and they may be of some use to you.").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "I really can't be any more help than that I'm afraid, it is more than my job's worth to have given you the help I already have.").also { stage++ }
            11 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks a lot, you've been very helpful!").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "Best of luck with the test @name. I hope your application is successful.").also { stage++ }
            13 -> {
                end()
                setAttribute(player!!, "/save:rd:help", 3)
            }
        }
    }

    private fun handleChallengeDialogue() {
        clearAttributes()
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Greetings, @name. Welcome to my challenge.").also { player!!.faceLocation(location(2469, 4941, 0)) }.also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "All you need to do is leave from the opposite door to where you came in by.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "I will warn you that this is more complicated than it may at first appear.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "I should also warn you that there are limited supplies of the items in this room, so think carefully before using them, you may find yourself stuck and have to leave to start again!").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "Best of luck!").also { stage++ }
            5 -> {
                end()
                setAttribute(player!!, "/save:rd:help", 1)
            }
        }
    }

    private fun clearAttributes() {
        with(MissCheeversPlugin) {
            removeAttribute(player!!, book)
            removeAttribute(player!!, magnet)
            removeAttribute(player!!, knife)
            removeAttribute(player!!, shears)
            removeAttribute(player!!, tin)
            removeAttribute(player!!, chisel)
            removeAttribute(player!!, wire)
            removeAttribute(player!!, vials)
            MissCheeversPlugin.Companion.Vials.vialMap.forEach { removeAttribute(player!!, it.value.attribute) }
            MissCheeversPlugin.Companion.DoorVials.doorVialsRequiredMap.forEach { removeAttribute(player!!, it.value.attribute) }
        }
    }
}
