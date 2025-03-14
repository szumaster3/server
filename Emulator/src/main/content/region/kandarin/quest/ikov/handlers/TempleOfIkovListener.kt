package content.region.kandarin.quest.ikov.handlers

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.global.action.DoorActionHandler
import core.game.global.action.PickupHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class TempleOfIkovListener : InteractionListener {
    companion object {
        val chestLocations =
            arrayOf(
                Location(2710, 9850, 0),
                Location(2719, 9838, 0),
                Location(2729, 9850, 0),
                Location(2747, 9848, 0),
                Location(2738, 9835, 0),
                Location(2745, 9821, 0),
            )
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.LEVER_87), "pull") { _, _ ->
            return@setDest Location.create(2671, 9805, 0)
        }
    }

    override fun defineListeners() {
        addClimbDest(Location.create(2682, 9849, 0), Location.create(2665, 9849, 0))

        on(Scenery.DOOR_99, SCENERY, "open") { player, node ->
            if (inInventory(player, Items.SHINY_KEY_85)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
        onUseWith(SCENERY, Items.SHINY_KEY_85, Scenery.DOOR_99) { player, used, with ->
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        on(intArrayOf(Scenery.GATE_94, Scenery.GATE_95), SCENERY, "open") { player, node ->
            if (inEquipment(player, Items.PENDANT_OF_LUCIEN_86)) {
                if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 1) {
                    setQuestStage(player, Quests.TEMPLE_OF_IKOV, 2)
                }
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "As you reach to open the door a great terror overcomes you!")
            }
            return@on true
        }

        on(Scenery.TRAPDOOR_100, SCENERY, "open") { player, node ->
            sendDialogueLines(
                player,
                "You try to open the trapdoor but it won't budge! It looks like the",
                "trapdoor can only be opened from the other side.",
            )
            sendMessage(player, "You try to open the trapdoor but it won't budge!")
            sendMessage(player, "It looks like the trapdoor can only be opened from the other side.")
            return@on true
        }

        on(Scenery.LEVER_91, SCENERY, "pull") { player, node ->
            replaceScenery(node.asScenery(), 88, 3)
            animate(player, Animation(2140))
            if (getAttribute(player, GameAttributes.QUEST_IKOV_DISABLED_TRAP, false)) {
                if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 2) {
                    setQuestStage(player, Quests.TEMPLE_OF_IKOV, 3)
                }
            } else {
                content.global.skill.agility.AgilityHandler.fail(
                    player,
                    2,
                    Location.create(2682, 9855, 0),
                    Animation(770),
                    20,
                    "You slip and fall to the pit below.",
                )
            }
            return@on true
        }

        on(Scenery.LEVER_91, SCENERY, "Search for traps") { player, _ ->
            if (getDynLevel(player, Skills.THIEVING) >= 42) {
                sendDialogueLines(player, "You find a trap on the lever! You disable the trap.")
                setAttribute(player, GameAttributes.QUEST_IKOV_DISABLED_TRAP, true)
            } else {
                sendDialogueLines(player, "You find nothing.")
            }
            return@on true
        }

        onUseWith(SCENERY, Items.LEVER_83, Scenery.LEVER_BRACKET_86) { player, used, with ->
            removeItem(player, used)
            replaceScenery(with.asScenery(), Scenery.LEVER_87, 20)
            sendDialogue(player, "You fit the lever into the bracket.")
            return@onUseWith true
        }

        on(Scenery.LEVER_87, SCENERY, "pull") { player, node ->
            face(player, Location.create(2671, 9803, 0))
            animate(player, Animation(2140))
            replaceScenery(node.asScenery(), 88, 3)
            setAttribute(player, GameAttributes.QUEST_IKOV_ICE_CHAMBER_ACCESS, true)
            queueScript(player, 6, QueueStrength.NORMAL) {
                replaceScenery(node.asScenery(), Scenery.LEVER_BRACKET_86, -1)
                return@queueScript stopExecuting(player)
            }
            sendDialogue(player, "You hear the clunking of some hidden machinery.")
            return@on true
        }

        on(intArrayOf(Scenery.GATE_89, Scenery.GATE_90), SCENERY, "open") { player, node ->
            if (getAttribute(player, GameAttributes.QUEST_IKOV_ICE_CHAMBER_ACCESS, false) ||
                getQuestStage(
                    player,
                    Quests.TEMPLE_OF_IKOV,
                ) >= 4
            ) {
                setAttribute(player, GameAttributes.QUEST_IKOV_CHEST_INTER, chestLocations.random())
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "The door won't open!")
            }
            return@on true
        }

        on(Scenery.CLOSED_CHEST_35122, SCENERY, "open") { player, node ->
            animate(player, 536)
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.OPEN_CHEST_35123, -1)
            return@on true
        }

        on(Scenery.OPEN_CHEST_35123, SCENERY, "shut") { player, node ->
            animate(player, 536)
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CLOSED_CHEST_35122, -1)
            return@on true
        }

        on(Scenery.OPEN_CHEST_35123, SCENERY, "search") { player, node ->
            if (getAttribute(player, GameAttributes.QUEST_IKOV_CHEST_INTER, null) == null) {
                setAttribute(player, GameAttributes.QUEST_IKOV_CHEST_INTER, chestLocations.random())
            }
            if (getAttribute(player, GameAttributes.QUEST_IKOV_CHEST_INTER, null) == node.location) {
                removeAttribute(player, GameAttributes.QUEST_IKOV_CHEST_INTER)
                val randomAmount = (1..5).random()
                addItemOrDrop(player, Items.ICE_ARROWS_78, randomAmount)
                sendItemDialogue(player, Item(Items.ICE_ARROWS_78, randomAmount), "You found some ice arrows!")
                setAttribute(player, GameAttributes.QUEST_IKOV_ICE_ARROWS, true)
            } else {
                sendMessage(player, "You search the chest, but find nothing.")
            }
            return@on true
        }

        on(Scenery.DOOR_92, SCENERY, "open") { player, node ->
            removeAttribute(player, GameAttributes.QUEST_IKOV_WARRIOR_INST)
            if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) >= 3) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door won't open.")
            }
            return@on true
        }

        on(Scenery.DOOR_93, SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) >= 4) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                if (getAttribute(player, GameAttributes.QUEST_IKOV_WARRIOR_INST, null) == null) {
                    val npc =
                        FireWarriorOfLesarkusNPC(NPCs.FIRE_WARRIOR_OF_LESARKUS_277, player, Location(2646, 9866, 0))
                    npc.init()
                    npc.isRespawn = false
                    npc.isWalks = false
                    npc.location = Location(2646, 9866, 0)
                    npc.direction = Direction.NORTH
                    submitIndividualPulse(
                        player,
                        object : Pulse() {
                            var counter = 0

                            override fun pulse(): Boolean {
                                when (counter++) {
                                    0 -> {
                                        sendChat(npc, "You will not pass!")
                                    }

                                    2 -> {
                                        sendChat(npc, "Amitus! Setitii!")
                                        spawnProjectile(npc, player, 127)
                                    }

                                    4 -> {
                                        npc.isWalks = true
                                        return true
                                    }
                                }
                                return false
                            }
                        },
                    )

                    setAttribute(player, GameAttributes.QUEST_IKOV_WARRIOR_INST, npc)
                } else {
                    sendMessage(player, "The door won't open.")
                }
            }
            return@on true
        }

        on(Items.STAFF_OF_ARMADYL_84, IntType.GROUNDITEM, "take") { player, node ->
            if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) >= 6 &&
                getAttribute(
                    player,
                    GameAttributes.QUEST_IKOV_SELECTED_END,
                    0,
                ) == 1
            ) {
                sendMessage(player, "You decide not to steal the staff as you have agreed to help the Guardians")
            }
            val npcID = findLocalNPCs(player, intArrayOf(NPCs.GUARDIAN_OF_ARMADYL_274, NPCs.GUARDIAN_OF_ARMADYL_275), 4)
            if (npcID.isNotEmpty()) {
                sendChat(npcID[0], "That is not thine to take!")
                npcID[0].attack(player)
            } else {
                if (getQuestStage(player, Quests.TEMPLE_OF_IKOV) == 5) {
                    setQuestStage(player, Quests.TEMPLE_OF_IKOV, 6)
                    setAttribute(player, GameAttributes.QUEST_IKOV_SELECTED_END, 2)
                }
                PickupHandler.take(player, node as GroundItem)
            }
            return@on true
        }
    }
}
