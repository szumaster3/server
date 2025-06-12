package content.region.asgarnia.falador.quest.rd.plugin

import content.region.asgarnia.quest.rd.RecruitmentDrive
import core.api.*
import core.api.ui.setMinimapState
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Sounds

object RDUtils {
    const val VARBIT_FOX_EAST = 680
    const val VARBIT_FOX_WEST = 681
    const val VARBIT_CHICKEN_EAST = 682
    const val VARBIT_CHICKEN_WEST = 683
    const val VARBIT_GRAIN_EAST = 684
    const val VARBIT_GRAIN_WEST = 685

    const val ATTRIBUTE_NPC_SPAWN = "rd:generatedsirleye"

    fun getLocationForScenery(node: Node): Location =
        when (node.asScenery().id) {
            Scenery.CRATE_7347 -> Location(2476, 4943)
            Scenery.CRATE_7348 -> Location(2476, 4937)
            Scenery.CRATE_7349 -> Location(2475, 4943)
            else -> Location(0, 0)
        }

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

    fun processItemUsage(
        player: Player,
        used: Item,
        with: Item,
        newItem: Item,
    ) {
        replaceSlot(player, slot = used.index, Item(newItem.id))
        replaceSlot(player, slot = with.index, Item(Items.VIAL_229))
        animate(player, Animation(Animations.HUMAN_USE_PESTLE_AND_MORTAR_364))
        playAudio(player, Sounds.VIALPOUR_2613)
        sendMessage(player, "You empty the vial into the tin.")
    }

    fun handleVialUsage(
        player: Player,
        used: Item,
    ) {
        lock(player, 5)
        lockInteractions(player, 5)

        if (removeItem(player, used.id)) {
            animate(player, Animation(Animations.POUR_VIAL_2259))
            playAudio(player, Sounds.VIALPOUR_2613)

            val doorVial = MissCheeversPuzzleListener.Companion.DoorVials.doorVialsMap[used.id]
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

        if (MissCheeversPuzzleListener.Companion.DoorVials.doorVialsRequiredMap.all {
                getAttribute(player, it.value.attribute, false)
            }
        ) {
            animate(player, Animation(2259))
            playAudio(player, Sounds.VIALPOUR_2613)
            sendMessage(player, "Something caused a reaction when mixed!")
            sendMessage(player, "The spade gets hotter, and expands slightly.")
            setVarbit(player, MissCheeversPuzzleListener.doorVarbit, 2)
        }
    }

    fun handleSpadePull(player: Player) {
        lock(player, 3)
        lockInteractions(player, 3)

        if (MissCheeversPuzzleListener.Companion.DoorVials.doorVialsRequiredMap.all
                {
                    getAttribute(player, it.value.attribute, false)
                }
        ) {
            sendMessage(player, "You pull on the spade...")
            sendMessage(player, "It works as a handle, and you swing the stone door open.")
            setVarbit(player, MissCheeversPuzzleListener.doorVarbit, 3)
        } else {
            sendMessage(player, "You pull on the spade...")
            sendMessage(player, "It comes loose, and slides out of the hole in the stone.")
            addItemOrDrop(player, Items.METAL_SPADE_5587)
            setVarbit(player, MissCheeversPuzzleListener.doorVarbit, 0)
        }
    }

    fun handleDoorWalkThrough(player: Player) {
        when {
            inBorders(player, 2476, 4941, 2477, 4939) ->
                forceMove(player, player.location, Location(2478, 4940, 0), 20, 80)

            inBorders(player, 2477, 4941, 2478, 4939) ->
                forceMove(player, player.location, Location(2476, 4940, 0), 20, 80)
        }
    }

    fun searchingHelper(
        player: Player,
        attributeCheck: String,
        item: Int,
        searchingDescription: String,
        objectDescription: String,
    ) {
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

    fun processItemUsageAndReturn(
        player: Player,
        used: Item,
        with: Item,
        resultItem: Item,
    ) {
        processItemUsage(player, used, with, resultItem)
    }

    fun resetStage(player: Player) {
        setVarbit(player, VARBIT_FOX_EAST, 0)
        setVarbit(player, VARBIT_FOX_WEST, 0)
        setVarbit(player, VARBIT_CHICKEN_EAST, 0)
        setVarbit(player, VARBIT_CHICKEN_WEST, 0)
        setVarbit(player, VARBIT_GRAIN_EAST, 0)
        setVarbit(player, VARBIT_GRAIN_WEST, 0)
        removeItem(player, Items.GRAIN_5607, Container.EQUIPMENT)
        removeItem(player, Items.FOX_5608, Container.EQUIPMENT)
        removeItem(player, Items.CHICKEN_5609, Container.EQUIPMENT)
    }

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
}
