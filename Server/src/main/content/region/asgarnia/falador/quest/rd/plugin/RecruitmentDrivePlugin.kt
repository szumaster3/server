package content.region.asgarnia.falador.quest.rd.plugin

import content.region.asgarnia.falador.dialogue.KnightNotesDialogue
import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import content.region.asgarnia.falador.quest.rd.cutscene.FailCutscene
import content.region.asgarnia.falador.quest.rd.cutscene.FinishCutscene
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class RecruitmentDrivePlugin :
    InteractionListener,
    MapArea {
    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        if (entity is Player) {
            val player = entity.asPlayer()
            RDUtils.resetPlayerState(player)
        }
    }

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.CANNON,
            ZoneRestriction.FOLLOWERS,
            ZoneRestriction.TELEPORT
        )

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(9805))

    override fun defineListeners() {
        on(Rooms.statueIDs, IntType.SCENERY, "touch") { player, node ->
            val correctStatue = getAttribute(player, "rd:statues", 0)
            if (node.id == Rooms.statueIDs[correctStatue]) {
                setAttribute(player, RecruitmentDrive.stagePass, true)
                playJingle(player, 156)
                sendNPCDialogueLines(
                    player,
                    NPCs.LADY_TABLE_2283,
                    FaceAnim.NEUTRAL,
                    false,
                    "Excellent work, ${player.name}.",
                    "Please step through the portal to meet your next challenge.",
                )
            } else {
                setAttribute(player, RecruitmentDrive.stageFail, true)
                openDialogue(player, content.region.asgarnia.falador.quest.rd.plugin.LadyTablePlugin(2), NPC(NPCs.LADY_TABLE_2283))
            }
            return@on true
        }

        on(Items.NITROUS_OXIDE_5581, IntType.ITEM, "open") { player, node ->
            sendMessage(player, "You uncork the vial...")
            replaceSlot(player, node.asItem().slot, Item(Items.VIAL_229, 1))
            sendMessage(player, "You smell a strange gas as it escapes from inside the vial.")
            sendChat(player, "Hahahahahahaha!", 1)
            return@on true
        }

        val interactionWithNotes =
            mapOf(
                Items.KNIGHTS_NOTES_11734 to KnightNotesDialogue(),
                Items.KNIGHTS_NOTES_11735 to KnightNotesDialogue.BrokenKnightNotes(),
            )
        interactionWithNotes.forEach { (item, dialogue) ->
            onUseWith(IntType.NPC, item, NPCs.SIR_TIFFY_CASHIEN_2290) { player, _, with ->
                openDialogue(player, dialogue, with)
                return@onUseWith true
            }
        }

        on(Rooms.portalsIDs, IntType.SCENERY, "use") { player, _ ->
            sendOptions(player, "Quit the training grounds?", "YES.", "NO.")
            addDialogueAction(player) { player, buttonID ->
                if (buttonID == 2) {
                    FailCutscene(player).start()
                } else {
                    closeChatBox(player)
                }
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.BRONZE_KEY_5585, Scenery.DOOR_7326) { player, used, with ->
            if (with.id == Scenery.DOOR_7326 && inInventory(player, Items.BRONZE_KEY_5585)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
                sendMessage(player, "You use the duplicate key you made to unlock the door.")
                setAttribute(player, RecruitmentDrive.stagePass, true)
            }

            if (getAttribute(player, RecruitmentDrive.stagePass, false)) {
                setAttribute(player, RecruitmentDrive.stagePass, false)
                setAttribute(player, RecruitmentDrive.stage, getAttribute(player, RecruitmentDrive.stage, 0) + 1)
                val currentLevel = getAttribute(player, RecruitmentDrive.stage, 0)
                if (currentLevel >= 5) {
                    DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
                    face(player, with.asScenery())
                    FinishCutscene(player).start()
                    return@onUseWith true
                }
                val currentStage = getAttribute(player, RecruitmentDrive.stageArray[currentLevel], 0)
                val currentStageEnum = Rooms.index[currentStage]!!
                closeDialogue(player)
                clearInventory(player)
                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
                            face(player, with.asScenery())
                            return@queueScript delayScript(player, 4)
                        }

                        1 -> {
                            teleport(player, currentStageEnum.location)
                            return@queueScript delayScript(player, 2)
                        }

                        2 -> {
                            forceWalk(player, currentStageEnum.destination, "DUMB")
                            return@queueScript delayScript(player, 1)
                        }

                        3 -> {
                            initRoomStage(player, currentStageEnum.npc)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                if (with.id == Scenery.DOOR_7323) {
                    openInterface(player, Components.RD_COMBOLOCK_285)
                } else {
                    sendMessage(player, "You have not completed this room's puzzle yet.")
                }
            }
            return@onUseWith true
        }

        on(Rooms.doorIDs, IntType.SCENERY, "open") { player, node ->
            if (node.id == Scenery.DOOR_7326 && inInventory(player, Items.BRONZE_KEY_5585)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "You use the duplicate key you made to unlock the door.")
                setAttribute(player, RecruitmentDrive.stagePass, true)
            }

            if (getAttribute(player, RecruitmentDrive.stagePass, false)) {
                setAttribute(player, RecruitmentDrive.stagePass, false)
                setAttribute(player, RecruitmentDrive.stage, getAttribute(player, RecruitmentDrive.stage, 0) + 1)
                val currentLevel = getAttribute(player, RecruitmentDrive.stage, 0)
                if (currentLevel >= 5) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    face(player, node.asScenery())
                    FinishCutscene(player).start()
                    return@on true
                }
                val currentStage = getAttribute(player, RecruitmentDrive.stageArray[currentLevel], 0)
                val currentStageEnum = Rooms.index[currentStage]!!
                closeDialogue(player)
                clearInventory(player)
                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                            face(player, node.asScenery())
                            return@queueScript delayScript(player, 4)
                        }

                        1 -> {
                            teleport(player, currentStageEnum.location)
                            return@queueScript delayScript(player, 2)
                        }

                        2 -> {
                            forceWalk(player, currentStageEnum.destination, "DUMB")
                            return@queueScript delayScript(player, 1)
                        }

                        3 -> {
                            initRoomStage(player, currentStageEnum.npc)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                if (node.id == Scenery.DOOR_7323) {
                    openInterface(player, Components.RD_COMBOLOCK_285)
                } else {
                    sendMessage(player, "You have not completed this room's puzzle yet.")
                }
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.OPEN_DOOR_7345), "walk-through") { player, _ ->
            when {
                inBorders(player, 2476, 4941, 2477, 4939) -> Location(2476, 4940, 0)
                inBorders(player, 2477, 4941, 2478, 4939) -> Location(2478, 4940, 0)
                else -> Location(2478, 4940, 0)
            }
        }

        setDest(IntType.NPC, intArrayOf(NPCs.SIR_TIFFY_CASHIEN_2290), "talk-to") { _, _ ->
            Location(2997, 3374, 0)
        }
    }

    companion object {
        enum class Rooms(
            val npc: Int,
            val location: Location,
            val destination: Location,
            val portal: Int,
            val door: Int,
        ) {
            I(
                NPCs.SIR_SPISHYUS_2282,
                Location(2490, 4972),
                Location(2489, 4972),
                Scenery.PORTAL_7272,
                Scenery.DOOR_7274,
            ),
            II(
                NPCs.LADY_TABLE_2283,
                Location(2460, 4979),
                Location(2459, 4979),
                Scenery.PORTAL_7288,
                Scenery.DOOR_7302,
            ),
            III(
                NPCs.SIR_KUAM_FERENTSE_2284,
                Location(2455, 4964),
                Location(2456, 4964),
                Scenery.PORTAL_7315,
                Scenery.DOOR_7317,
            ),
            IV(
                NPCs.SIR_TINLEY_2286,
                Location(2471, 4956),
                Location(2472, 4956),
                Scenery.PORTAL_7318,
                Scenery.DOOR_7320,
            ),
            V(
                NPCs.SIR_REN_ITCHOOD_2287,
                Location(2439, 4956),
                Location(2440, 4956),
                Scenery.PORTAL_7321,
                Scenery.DOOR_7323,
            ),
            VI(
                NPCs.MISS_CHEEVERS_2288,
                Location(2467, 4940),
                Location(2468, 4940),
                Scenery.PORTAL_7324,
                Scenery.DOOR_7326,
            ),
            VII(
                NPCs.MS_HYNN_TERPRETT_2289,
                Location(2451, 4935),
                Location(2451, 4936),
                Scenery.PORTAL_7352,
                Scenery.DOOR_7354,
            ),
            ;

            companion object {
                val index = values().associateBy { it.ordinal }
                val statueIDs = intArrayOf(0, 7308, 7307, 7306, 7305, 7304, 7303, 7312, 7313, 7314, 7311, 7310, 7309)
                val portalsIDs = values().map { it.portal }.toIntArray()
                val doorIDs = values().map { it.door }.toIntArray()
            }
        }

        fun initRoomStage(
            player: Player,
            npc: Int,
        ) {
            when (npc) {
                NPCs.SIR_SPISHYUS_2282 -> openDialogue(player, SirSpishyusDialogueFile(1), NPC(npc))
                NPCs.LADY_TABLE_2283 -> openDialogue(player, LadyTablePlugin(1), NPC(npc))
                NPCs.SIR_KUAM_FERENTSE_2284 -> openDialogue(player, SirKuamPlugin(1), NPC(npc))
                NPCs.SIR_TINLEY_2286 -> openDialogue(player, SirTinleyPlugin(1), NPC(npc))
                NPCs.SIR_REN_ITCHOOD_2287 -> openDialogue(player, SirReenItchoodPlugin(1), NPC(npc))
                NPCs.MISS_CHEEVERS_2288 -> openDialogue(player, MissCheeversDialogueFile(1), NPC(npc))
                NPCs.MS_HYNN_TERPRETT_2289 -> openDialogue(player, HynnTerprettPlugin(1), NPC(npc))
            }
        }
    }
}
