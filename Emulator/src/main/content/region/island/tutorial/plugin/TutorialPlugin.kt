package content.region.island.tutorial.plugin

import content.data.GameAttributes
import core.api.*
import core.api.ui.closeDialogue
import core.game.component.Component
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.repository.Repository
import org.rs.consts.*

class TutorialPlugin : InteractionListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(getRegionBorders(12079), getRegionBorders(12180), getRegionBorders(12592), getRegionBorders(12436), getRegionBorders(12335), getRegionBorders(12336))

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val p = entity.asPlayer()
            if (getAttribute(p, TutorialStage.TUTORIAL_STAGE, 0) != 72) {
                lockTeleport(p)
            } else {
                p.locks.unlockTeleport()
            }
            if (getAttribute(p, TutorialStage.TUTORIAL_STAGE, -1) == 72) {
                Component.setUnclosable(p, p.dialogueInterpreter.sendPlaneMessageWithBlueTitle("You have almost completed the tutorial!", "", "Just click on the first spell, Home Teleport, in your Magic", "Spellbook. This spell doesn't require any runes, but can only", "be cast once every 30 minutes."),)
            }
        }
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location, ) {
        if (entity is Player) {
            val player = entity.asPlayer()
            val stage = getAttribute(player, TutorialStage.TUTORIAL_STAGE, -1)

            if (stage == 72) {
                player.locks.unlockTeleport()
                Component.setUnclosable(player, player.dialogueInterpreter.sendPlaneMessageWithBlueTitle("You have almost completed the tutorial!", "", "Just click on the first spell, Home Teleport, in your Magic", "Spellbook. This spell doesn't require any runes, but can only", "be cast once every 30 minutes."),)
            }
        }
    }

    override fun defineListeners() {
        on(RS_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 3) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You may not pass this door yet. Try following the instructions.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 4)
            TutorialStage.load(player, 4)
            playAudio(player, Sounds.GATE_OPEN_67)
            DoorActionHandler.handleAutowalkDoor(
                player,
                node as core.game.node.scenery.Scenery,
                Location.create(3098, 3107, 0),
            )
            return@on true
        }

        on(WOODEN_GATE, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 16) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You need to finish the Survival Expert tasks first.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 17)
            TutorialStage.load(player, 17)
            DoorActionHandler.autowalkFence(
                player,
                node as core.game.node.scenery.Scenery,
                Scenery.GATE_3016,
                Scenery.GATE_3015
            )
            return@on true
        }

        on(COOK_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 17) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You may not pass this door yet. Try following the instructions.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 18)
            TutorialStage.load(player, 18)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }

        on(COOK_GUIDE_DOOR_EXIT, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) < 21) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You need to finish the Master Chef's tasks first.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            } else if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) > 22) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "Follow the path to the home of the quest guide.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 23)
            TutorialStage.load(player, 23)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }

        on(QUEST_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 26) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You may not pass this door yet. Try following the instructions.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 27)
            TutorialStage.load(player, 27)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }

        on(QUEST_LADDER_DOWN, IntType.SCENERY, "climb-down") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) < 29) {
                return@on true
            }

            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) == 29) {
                setAttribute(player, TutorialStage.TUTORIAL_STAGE, 30)
                TutorialStage.load(player, 30)
            }
            ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-down")
        }

        on(QUEST_LADDER_UP, IntType.SCENERY, "climb-up") { player, node ->
            ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-up")
            submitWorldPulse(
                object : Pulse(2) {
                    override fun pulse(): Boolean {
                        val questTutor = Repository.findNPC(NPCs.QUEST_GUIDE_949) ?: return true
                        sendChat(questTutor, "What are you doing, ${player.username}? Get back down the ladder.")
                        return true
                    }
                },
            )

            return@on true
        }

        on(COMBAT_GATE, IntType.SCENERY, "open") { player, node ->
            val stage = getAttribute(player, TutorialStage.TUTORIAL_STAGE, -1)
            if (stage < 42) {
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlainMessage(
                        false,
                        "",
                        "You may not pass this door yet. Try following the instructions.",
                        "",
                    ),
                )
                return@on true
            }
            if (stage >= 44) {
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlainMessage(
                        false,
                        "",
                        "Follow the path to the Combat Instructor.",
                        "",
                    ),
                )
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 44)
            TutorialStage.load(player, 44)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
        }

        on(GIANT_RAT_GATE, IntType.SCENERY, "open") { player, node ->
            val stage = getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)
            if (stage !in 50..53) {
                player.dialogueInterpreter
                    .sendDialogues(
                        NPCs.COMBAT_INSTRUCTOR_944,
                        FaceAnim.NEUTRAL,
                        "Oi, get away from there!",
                        "Don't enter my rat pen unless I say so!",
                    ).also {
                        closeDialogue(player)
                    }
                return@on true
            }
            if (stage == 50) {
                setAttribute(player, TutorialStage.TUTORIAL_STAGE, 51)
                TutorialStage.load(player, 51)
            }
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }

        on(COMBAT_LADDER, IntType.SCENERY, "climb-up") { player, _ ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 55) {
                TutorialStage.rollback(player)
                return@on true
            }

            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 56)
            TutorialStage.load(player, 56)
            animate(player, Animations.USE_LADDER_828)
            teleport(player, Location.create(3111, 3127, 0), TeleportManager.TeleportType.INSTANT, 2)
            return@on true
        }

        on(Scenery.LADDER_3031, IntType.SCENERY, "climb-down") { player, _ ->
            TutorialStage.rollback(player)
            return@on true
        }

        on(BANK_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 57) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You may not pass this door yet. Try following the instructions.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 58)
            TutorialStage.load(player, 58)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
        }

        on(FINANCE_GUIDE_DOOR, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 59) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You may not pass this door yet. Try following the instructions.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 60)
            TutorialStage.load(player, 60)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
        }

        on(CHURCH_DOOR_EXIT, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0) != 66) {
                Component
                    .setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlainMessage(
                            false,
                            "",
                            "You may not pass this door yet. Try following the instructions.",
                            "",
                        ),
                    ).also {
                        TutorialStage.rollback(player)
                    }
                return@on true
            }
            setAttribute(player, TutorialStage.TUTORIAL_STAGE, 67)
            TutorialStage.load(player, 67)
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
        }

        onEquip(intArrayOf(BRONZE_AXE, BRONZE_PICKAXE)) { player, _ ->
            val restriction = getAttribute(player, GameAttributes.TUTORIAL_STAGE, -1)
            if (restriction < 45) {
                sendDialogue(player, "You'll be told how to equip items later.")
                TutorialStage.rollback(player)
                return@onEquip false
            }
            return@onEquip true
        }
    }

    companion object {
        private const val RS_GUIDE_DOOR = Scenery.DOOR_3014
        private const val COOK_GUIDE_DOOR = Scenery.DOOR_3017
        private const val COOK_GUIDE_DOOR_EXIT = Scenery.DOOR_3018
        private const val QUEST_GUIDE_DOOR = Scenery.DOOR_3019
        private const val QUEST_LADDER_DOWN = Scenery.LADDER_3029
        private const val QUEST_LADDER_UP = Scenery.LADDER_3028
        private const val COMBAT_LADDER = Scenery.LADDER_3030
        private const val BANK_GUIDE_DOOR = Scenery.DOOR_3024
        private const val FINANCE_GUIDE_DOOR = Scenery.DOOR_3025
        private val CHURCH_DOORS = intArrayOf(Scenery.CHURCH_DOOR_36999, Scenery.CHURCH_DOOR_37000)
        private const val CHURCH_DOOR_EXIT = Scenery.DOOR_3026
        private const val BRONZE_AXE = Items.BRONZE_AXE_1351
        private const val BRONZE_PICKAXE = Items.BRONZE_PICKAXE_1265
        private val WOODEN_GATE = intArrayOf(Scenery.GATE_3015, Scenery.GATE_3016)
        private val COMBAT_GATE = intArrayOf(Scenery.GATE_3020, Scenery.GATE_3021)
        private val GIANT_RAT_GATE = intArrayOf(Scenery.GATE_3022, Scenery.GATE_3023)
    }

}
