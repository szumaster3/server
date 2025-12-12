package content.region.asgarnia.burthope.quest.troll.plugin

import content.global.skill.gather.SkillReward
import content.region.asgarnia.burthope.quest.troll.dialogue.DadDialogueFile
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery
import kotlin.math.ceil

class TrollStrongholdPlugin : InteractionListener {

    override fun defineListeners() {
        on(intArrayOf(Scenery.ARENA_ENTRANCE_3782, Scenery.ARENA_ENTRANCE_3783), IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == 1) {
                openDialogue(player, DadDialogueFile(1), findNPC(NPCs.DAD_1125)!!)
            }

            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) > 0) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "You need to start the Troll Stronghold quest.")
            }
            return@on true
        }

        on(intArrayOf(Scenery.ARENA_EXIT_3785, Scenery.ARENA_EXIT_3786), IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) < 5) {
                openDialogue(player, DadDialogueFile(1), findNPC(NPCs.DAD_1125)!!)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        on(Scenery.PRISON_DOOR_3780, IntType.SCENERY, "unlock") { player, node ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) >= 8) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                if (inInventory(player, Items.PRISON_KEY_3135)) {
                    if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == 5) {
                        setQuestStage(player, Quests.TROLL_STRONGHOLD, 8)
                    }
                    if (removeItem(player, Items.PRISON_KEY_3135)) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        sendMessage(player, "You unlock the prison door.")
                    }
                } else {
                    sendMessage(player, "The prison door is locked.")
                }
            }
            return@on true
        }

        pickpocketNPCs.forEach { data ->
            val npcId = data[0] as Int
            val failTransform = data[1] as Int
            val keyItem = data[2] as Int

            on(npcId, IntType.NPC, "pickpocket") { player, node ->
                val npc = node.asNpc()
                player.lock()
                submitWorldPulse(object : Pulse() {
                    var counter = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                if (hasLevelDyn(player, Skills.THIEVING, 30)) {
                                    animate(player, Animation(881))
                                } else {
                                    sendMessage(player, "You need to be a level 30 thief to pickpocket ${npc.name}.")
                                    return true
                                }
                            }
                            3 -> {
                                val success = SkillReward.success(player, Skills.THIEVING)
                                if (success) {
                                    if (isQuestInProgress(player, Quests.TROLL_STRONGHOLD, 8, 10)) {
                                        addItem(player, keyItem)
                                        sendMessage(player, "You find a small key on ${npc.name}.")
                                    } else {
                                        sendMessage(player, "You find nothing on ${npc.name}.")
                                    }
                                } else {
                                    sendChat(npc, "What you think you doing?")
                                    transformNpc(npc, failTransform, 50)
                                    npc.attack(player)
                                }
                                player.unlock()
                                return true
                            }
                        }
                        return false
                    }
                })
                return@on true
            }
        }

        onUseWith(IntType.SCENERY, Items.CELL_KEY_1_3136, Scenery.CELL_DOOR_3765) { player, _, with ->
            unlockCellDoor(player, with, Items.CELL_KEY_1_3136, NPCs.EADGAR_1113, mapOf(8 to 9, 10 to 11), "Thanks! I'm off back home!")
            return@onUseWith true
        }

        on(Scenery.CELL_DOOR_3765, IntType.SCENERY, "unlock") { player, node ->
            unlockCellDoor(player, node, Items.CELL_KEY_1_3136, NPCs.EADGAR_1113, mapOf(8 to 9, 10 to 11), "Thanks! I'm off back home!")
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.CELL_KEY_2_3137, Scenery.CELL_DOOR_3767) { player, _, with ->
            unlockCellDoor(player, with, Items.CELL_KEY_2_3137, NPCs.GODRIC_1114, mapOf(8 to 10, 9 to 11), "Thank you, my friend.")
            return@onUseWith true
        }
        on(Scenery.CELL_DOOR_3767, IntType.SCENERY, "unlock") { player, node ->
            unlockCellDoor(player, node, Items.CELL_KEY_2_3137, NPCs.GODRIC_1114, mapOf(8 to 10, 9 to 11), "Thank you, my friend.")
            return@on true
        }

        on(Scenery.EXIT_3761, IntType.SCENERY, "open") { player, _ ->
            player.properties.teleportLocation = Location.create(2827, 3646, 0)
            return@on true
        }

        on(Scenery.SECRET_DOOR_3762, IntType.SCENERY, "open") { player, _ ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) >= 8) {
                player.properties.teleportLocation = Location.create(2824, 10050, 0)
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
    }

    private fun unlockCellDoor(player: Player, node: Node, keyItem: Int, npcId: Int, questStageMapping: Map<Int, Int>, npcMessage: String) {
        if (!inInventory(player, keyItem)) {
            sendMessage(player, "The cell door is locked.")
            return
        }

        sendMessage(player, "You unlock the cell door.")

        val currentStage = getQuestStage(player, Quests.TROLL_STRONGHOLD)
        questStageMapping[currentStage]?.let { setQuestStage(player, Quests.TROLL_STRONGHOLD, it) }

        val npc = findNPC(npcId)!!
        sendNPCDialogue(player, npcId, npcMessage)
        npc.walkRadius = 40

        submitWorldPulse(object : Pulse(1) {
            var count = 0
            var stage = 0
            var trigger = true
            var targetLocation = Location(0, 0, 0)

            override fun pulse(): Boolean {
                if (npc.location == targetLocation) {
                    stage++
                    trigger = true
                }

                if (trigger) {
                    trigger = false
                    when (stage) {
                        0 -> {
                            forceWalk(npc, Location(2831, if (npcId == NPCs.EADGAR_1113) 10082 else 10078, 0), "DUMB")
                            npc.walkRadius = 11
                            npc.isWalks = false
                            targetLocation = Location(2831, if (npcId == NPCs.EADGAR_1113) 10082 else 10078, 0)
                        }
                        1 -> {
                            DoorActionHandler.handleAutowalkDoor(npc, node.asScenery())
                            targetLocation = Location(2832, if (npcId == NPCs.EADGAR_1113) 10082 else 10078, 0)
                        }
                        4 -> {
                            forceWalk(npc, Location(2836, if (npcId == NPCs.EADGAR_1113) 10082 else 10078, 0), "DUMB")
                            targetLocation = Location(2836, if (npcId == NPCs.EADGAR_1113) 10082 else 10078, 0)
                        }
                        5 -> {
                            forceWalk(npc, Location(2836, 10061, 0), "")
                            targetLocation = Location(2836, 10061, 0)
                        }
                        6 -> {
                            forceWalk(npc, Location(2824, 10050, 0), "")
                            targetLocation = Location(2824, 10050, 0)
                        }
                        7 -> {
                            npc.teleport(Location(2823, 10035, 0))
                            npc.startDeath(null)
                            npc.isWalks = true
                            return true
                        }
                    }
                }

                if (count++ > 100) {
                    npc.startDeath(null)
                    npc.isWalks = true
                    return true
                }
                return false
            }
        })
    }


    companion object {
        val pickpocketNPCs = listOf(listOf(NPCs.TWIG_1128, NPCs.TWIG_1126, Items.CELL_KEY_1_3136), listOf(NPCs.BERRY_1129, NPCs.BERRY_1127, Items.CELL_KEY_2_3137))
    }
}
