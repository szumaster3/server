package content.region.morytania.port_phasmatys.quest.ahoy.plugin

import core.api.*
import core.game.component.Component
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.*

object GhostsAhoyUtils {
    const val signatures = 0
    const val shipModel = Items.MODEL_SHIP_4253
    const val silkShipModel = Items.MODEL_SHIP_4254
    const val rightShip = "/save:ghostsahoy:right-ship"
    const val colorMatching = "/save:ghostsahoy:paint-ship"
    const val lastMapScrap = "/save:ghostahoy:map-scrap"
    const val windSpeed = "/save:ghostsahoy:windspeed"
    const val logout = "/save:ghostsahoy:bedsheet-costume"
    const val petitionsigns = "/save:ghostsahoy:petition"
    const val petitionstart = "/save:ghostsahoy:petition-start"
    const val petitioncomplete = "/save:ghostsahoy:petition-complete"
    const val getSignedBow = "/save:ghostsahoy:signed-bow"
    const val shipFlag = "/save:ghostsahoy:ship-flag"
    const val shipBottom = "/save:ghostsahoy:ship-bottom"
    const val shipSkull = "/save:ghostsahoy:ship-skull"

    val rockJumpMap: Map<Location, Location> = mapOf(
        Location(3605, 3550, 0) to Location(3602, 3550, 0),
        Location(3601, 3550, 0) to Location(3604, 3550, 0),
        Location(3600, 3552, 0) to Location(3597, 3552, 0),
        Location(3596, 3552, 0) to Location(3599, 3552, 0),
        Location(3595, 3553, 0) to Location(3595, 3556, 0),
        Location(3595, 3557, 0) to Location(3595, 3554, 0),
        Location(3597, 3558, 0) to Location(3597, 3561, 0),
        Location(3597, 3562, 0) to Location(3597, 3559, 0),
        Location(3598, 3564, 0) to Location(3601, 3564, 0),
        Location(3602, 3564, 0) to Location(3599, 3564, 0)
    )

    fun jumpRockPath(player: Player) {
        queueScript(player, 0, QueueStrength.SOFT) {
            if (RandomFunction.getRandom(3) == 2) {
                animate(player, Animations.HUMAN_JUMP_FAIL_767)
                playGlobalAudio(player.location, Sounds.JUMP_AND_FALL_2463)
                player.impactHandler.manualHit(player, 1, ImpactHandler.HitsplatType.NORMAL, 1)
            } else {
                animate(player, Animations.HUMAN_JUMP_SHORT_GAP_741)
                playGlobalAudio(player.location, Sounds.JUMP2_2462)
            }
            return@queueScript stopExecuting(player)
        }
    }

    fun collectSignature(player: Player) {
        player.incrementAttribute(petitionsigns)
        sendItemDialogue(
            player,
            Items.PETITION_FORM_4283,
            "The ghost signs your petition. You have obtained ${
                getAttribute(player, petitionsigns, signatures.toString())
            } " + if (getAttribute(player, petitionsigns, 0) == 1) "signature so far." else "signature's so far.",
        )

        if (getAttribute(player, petitionsigns, 0) > 9) {
            setAttribute(player, petitioncomplete, true)
            setQuestStage(player, Quests.GHOSTS_AHOY, 50)
            removeItem(player, Items.PETITION_FORM_4283)
            removeAttributes(player, petitionstart, petitionsigns)
            sendItemDialogue(
                player,
                Items.PETITION_FORM_4283,
                "You have succeeded in obtaining 10 signatures on the petition form!",
            )
            return
        }
        if (getAttribute(player, petitionsigns, -1) == 0) {
            sendMessage(player, "You haven't got any signatures yet.")
            return
        }
    }

    fun travelToDragontoothIsland(player: Player) {
        lock(player, 6)
        queueScript(player, 1, QueueStrength.SOFT) { qstage: Int ->
            when (qstage) {
                0 -> {
                    openInterface(player, Components.AHOY_BLACKOUT_7)
                    return@queueScript delayScript(player, 5)
                }

                1 -> {
                    teleport(player, location(3792, 3559, 0))
                    player.interfaceManager.close(Component(Components.AHOY_BLACKOUT_7))
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    fun travelToPortPhasmatys(player: Player) {
        lock(player, 6)
        queueScript(player, 1, QueueStrength.SOFT) { qstage: Int ->
            when (qstage) {
                0 -> {
                    openInterface(player, Components.AHOY_BLACKOUT_7)
                    return@queueScript delayScript(player, 5)
                }

                1 -> {
                    teleport(player, location(3702, 3487, 0))
                    player.interfaceManager.close(Component(Components.AHOY_BLACKOUT_7))
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    fun handleDyeSelection(player: Player, buttonID: Int) {
        val dyeColor =
            mapOf(
                1 to { color: String ->
                    dyeFlag(player, color, shipFlag)
                },
                2 to { color: String ->
                    dyeFlag(player, color, shipBottom)
                },
                3 to { color: String ->
                    dyeFlag(player, color, shipSkull)
                },
            )

        dyeColor[buttonID]?.let { color ->
            when {
                removeItem(player, Item(Items.YELLOW_DYE_1765, 1)) -> color("yellow")
                removeItem(player, Item(Items.BLUE_DYE_1767, 1)) -> color("blue")
                removeItem(player, Item(Items.RED_DYE_1763, 1)) -> color("red")
            }
        }
    }

    private fun dyeFlag(player: Player, color: String, attribute: String) {
        sendItemDialogue(player, shipModel, "You dye the flag $color.")
        setAttribute(player, attribute, color)
    }
}
