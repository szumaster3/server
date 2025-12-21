package content.region.asgarnia.falador.quest.rd

import content.region.asgarnia.falador.dialogue.SirTiffyCashienDialogue
import content.region.asgarnia.falador.quest.rd.plugin.tests.MissCheeversPlugin
import content.region.asgarnia.falador.quest.rd.plugin.tests.MissCheeversPlugin.Companion.DoorVials.Companion.doorVialsRequiredMap
import content.region.asgarnia.falador.quest.rd.plugin.tests.TestOfTactics
import core.api.*
import core.game.component.Component
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

/**
 * Utility object for the Recruitment Drive quest.
 */
object RDUtils {

    /** The varbit id for tracking the state of the fox on the east side. */
    const val VARBIT_FOX_EAST = 680
    /** The varbit id for tracking the state of the fox on the west side. */
    const val VARBIT_FOX_WEST = 681
    /** The varbit id for tracking the state of the chicken on the east side. */
    const val VARBIT_CHICKEN_EAST = 682
    /** The varbit id for tracking the state of the chicken on the west side. */
    const val VARBIT_CHICKEN_WEST = 683
    /** The varbit id for tracking the state of the grain on the east side. */
    const val VARBIT_GRAIN_EAST = 684
    /** The varbit id for tracking the state of the grain on the west side. */
    const val VARBIT_GRAIN_WEST = 685

    /** Attribute to track if the NPC has been spawned. */
    const val ATTRIBUTE_NPC_SPAWN = "rd:generatedsirleye"

    /**
     * Returns a fixed location corresponding to a specific scenery object.
     *
     * @param node The scenery node being interacted with.
     * @return The [Location] of the scenery, or (0,0) if unknown.
     */
    fun getLocationForScenery(node: Node): Location =
        when (node.asScenery().id) {
            Scenery.CRATE_7347 -> Location(2476, 4943)
            Scenery.CRATE_7348 -> Location(2476, 4937)
            Scenery.CRATE_7349 -> Location(2475, 4943)
            else -> Location(0, 0)
        }

    /**
     * Resets the player's state.
     *
     * @param player The player whose state is being reset.
     */
    fun resetPlayerState(player: Player) {
        setMinimapState(player, 0)

        listOf(
            VARBIT_FOX_EAST,
            VARBIT_FOX_WEST,
            VARBIT_CHICKEN_EAST,
            VARBIT_CHICKEN_WEST,
            VARBIT_GRAIN_EAST,
            VARBIT_GRAIN_WEST,
        ).forEach { setVarbit(player, it, 0) }

        listOf(Items.GRAIN_5607, Items.FOX_5608, Items.CHICKEN_5609)
            .forEach { removeItem(player, it, Container.EQUIPMENT) }

        player.inventory.clear()
        player.equipment.clear()

        player.interfaceManager.openDefaultTabs()

        removeAttributes(
            player,
            RecruitmentDrive.stagePass,
            RecruitmentDrive.stageFail,
            RecruitmentDrive.stage,
            RecruitmentDrive.stage0,
            RecruitmentDrive.stage1,
            RecruitmentDrive.stage2,
            RecruitmentDrive.stage3,
            RecruitmentDrive.stage4,
        )

        runTask(player, 3) { teleport(player, Location(2996, 3375)) }
    }

    /**
     * Processes the usage of one item on another, replacing them with the resulting item.
     *
     * @param player The player using the items.
     * @param used The item being used.
     * @param with The item used on.
     * @param newItem The resulting item.
     */
    fun processItemUsage(player: Player, used: Item, with: Item, newItem: Item) {
        replaceSlot(player, slot = used.index, Item(newItem.id))
        replaceSlot(player, slot = with.index, Item(Items.VIAL_229))
        animate(player, Animation(Animations.HUMAN_USE_PESTLE_AND_MORTAR_364))
        playAudio(player, Sounds.POUR_STICKY_LIQUID_2216)
        sendMessage(player, "You empty the vial into the tin.")
    }

    /**
     * Handles the player using a vial on a spade during the Miss Cheevers puzzle.
     *
     * @param player The player using the vial.
     * @param used The vial item being used.
     */
    fun handleVialUsage(player: Player, used: Item) {
        lock(player, 5)
        lockInteractions(player, 5)

        if (removeItem(player, used.id)) {
            animate(player, Animation(Animations.POUR_VIAL_2259))
            playAudio(player, Sounds.POUR_STICKY_LIQUID_2216)

            val doorVial = MissCheeversPlugin.Companion.DoorVials.doorVialsMap[used.id]
            if (doorVial != null) {
                setAttribute(player, doorVial.attribute, true)
                sendMessage(player, "You pour the vial onto the flat part of the spade.")
                addItem(player, Items.VIAL_229)
            } else {
                sendMessage(player, "The vial has no effect.")
            }
        } else {
            sendMessage(player, "You do not have the vial to use.")
        }

        if (doorVialsRequiredMap.all { getAttribute(player, it.value.attribute, false) }) {
            animate(player, Animation(2259))
            playAudio(player, Sounds.POUR_STICKY_LIQUID_2216)
            sendMessage(player, "Something caused a reaction when mixed!")
            sendMessage(player, "The spade gets hotter, and expands slightly.")
            setVarbit(player, MissCheeversPlugin.doorVarbit, 2)
        }
    }

    /**
     * Handles the player pulling the spade during the Miss Cheevers puzzle.
     *
     * @param player The player pulling the spade.
     */
    fun handleSpadePull(player: Player) {
        lock(player, 3)
        lockInteractions(player, 3)

        if (doorVialsRequiredMap.all { getAttribute(player, it.value.attribute, false) }) {
            sendMessage(player, "You pull on the spade...")
            sendMessage(player, "It works as a handle, and you swing the stone door open.")
            setVarbit(player, MissCheeversPlugin.doorVarbit, 3)
        } else {
            sendMessage(player, "You pull on the spade...")
            sendMessage(player, "It comes loose, and slides out of the hole in the stone.")
            addItemOrDrop(player, Items.METAL_SPADE_5587)
            setVarbit(player, MissCheeversPlugin.doorVarbit, 0)
        }
    }

    /**
     * Handles the player walking through the door area, moving them to the correct side.
     *
     * @param player The player walking through the door.
     */
    fun handleDoorWalkThrough(player: Player) {
        when {
            inBorders(player, 2476, 4941, 2477, 4939) ->
                forceMove(player, player.location, Location(2478, 4940, 0), 20, 80)
            inBorders(player, 2477, 4941, 2478, 4939) ->
                forceMove(player, player.location, Location(2476, 4940, 0), 20, 80)
        }
    }

    /**
     * Helper method for handling a player searching an object.
     *
     * @param player The player searching.
     * @param attributeCheck Attribute to check if the item was already found.
     * @param item The item to give the player if successful.
     * @param searchingDescription Message shown when searching.
     * @param objectDescription Message shown when finding the item.
     */
    fun searchingHelper(player: Player, attributeCheck: String, item: Int, searchingDescription: String, objectDescription: String) {
        sendMessage(player, searchingDescription)
        queueScript(player, 1, QueueStrength.WEAK) {
            if (attributeCheck.isNotEmpty() && !getAttribute(player, attributeCheck, false)) {
                setAttribute(player, attributeCheck, true)
                addItem(player, item)
                sendMessage(player, objectDescription)
            } else {
                sendMessage(player, "You don't find anything interesting.")
            }
            return@queueScript stopExecuting(player)
        }
    }

    /**
     * Processes item usage and returns the resulting item.
     *
     * @param player The player using the items.
     * @param used The item being used.
     * @param with The item used on.
     * @param resultItem The resulting item.
     */
    fun processItemUsageAndReturn(player: Player, used: Item, with: Item, resultItem: Item) {
        processItemUsage(player, used, with, resultItem)
    }

    /**
     * Shuffles the puzzle stages and resets the player's quest attributes.
     *
     * @param player The player to reset and shuffle stages for.
     */
    @JvmStatic
    fun shuffleTask(player: Player) {
        val stageArray = intArrayOf(0, 1, 2, 3, 4, 5, 6)
        stageArray.shuffle()
        setAttribute(player, RecruitmentDrive.stage0, stageArray[0])
        setAttribute(player, RecruitmentDrive.stage1, stageArray[1])
        setAttribute(player, RecruitmentDrive.stage2, stageArray[2])
        setAttribute(player, RecruitmentDrive.stage3, stageArray[3])
        setAttribute(player, RecruitmentDrive.stage4, stageArray[4])
        setAttribute(player, RecruitmentDrive.stagePass, false)
        setAttribute(player, RecruitmentDrive.stageFail, false)
        setAttribute(player, RecruitmentDrive.stage, 0)
    }

    /**
     * Handles fail sequence.
     */
    @JvmStatic
    fun failSequence(player : Player) {
        player.lock()
        player.interfaceManager.closeOverlay()
        player.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
        var clearBoss = getAttribute(player, TestOfTactics.spawnSirLeye, NPC(0))
        if (clearBoss.id != 0) {
            clearBoss.clear()
        }
        hideMinimap(player)
        clearInventory(player)
        queueScript(player, 6, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    player.interfaceManager.closeOverlay()
                    player.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
                    teleport(player, Location(2996, 3375), TeleportManager.TeleportType.INSTANT)
                    return@queueScript delayScript(player, 3)
                }

                1 -> {
                    face(player, findLocalNPC(player, NPCs.SIR_TIFFY_CASHIEN_2290)!!)
                    player.unlock()
                    showMinimap(player)
                    player.interfaceManager.restoreTabs()
                    player.interfaceManager.openDefaultTabs()
                    openDialogue(
                        player,
                        SirTiffyCashienDialogue.SirTiffyCashienFailedDialogueFile(),
                        NPC(NPCs.SIR_TIFFY_CASHIEN_2290)
                    )
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    /**
     * Handles finish quest sequence.
     */
    @JvmStatic
    fun finishSequence(player : Player) {
        queueScript(player, 1, QueueStrength.SOFT) { stage ->
            when (stage) {
                0 -> {
                    if (getQuestStage(player, Quests.RECRUITMENT_DRIVE) == 2) {
                        setQuestStage(player, Quests.RECRUITMENT_DRIVE, 3)
                    }
                    player.lock()
                    closeDialogue(player)
                    player.interfaceManager.closeOverlay()
                    player.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
                    hideMinimap(player)
                    return@queueScript delayScript(player, 6)
                }
                1 -> {
                    clearInventory(player)
                    teleport(player, Location(2996, 3375), TeleportManager.TeleportType.INSTANT)
                    player.interfaceManager.closeOverlay()
                    player.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
                    return@queueScript delayScript(player, 2)

                }
                2 -> {
                    face(player, findLocalNPC(player, NPCs.SIR_TIFFY_CASHIEN_2290)!!)
                    player.interfaceManager.restoreTabs()
                    player.unlock()
                    openDialogue(player, SirTiffyCashienDialogue.SirTiffyCashienDialogueFile(), NPC(NPCs.SIR_TIFFY_CASHIEN_2290))
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }
}
