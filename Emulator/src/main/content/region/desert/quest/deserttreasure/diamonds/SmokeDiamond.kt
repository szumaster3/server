package content.region.desert.quest.deserttreasure.diamonds

import content.region.desert.quest.deserttreasure.handlers.DTUtils
import content.region.desert.quest.deserttreasure.DesertTreasure
import core.api.*
import core.api.quest.getQuestStage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.timer.RSTimer
import core.game.world.map.Location
import core.tools.secondsToTicks
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class SmokeDiamond : InteractionListener {
    companion object {
        const val timerIdentifierTorchNE = "deserttreasureNEtorch"
        const val timerIdentifierTorchSE = "deserttreasureSEtorch"
        const val timerIdentifierTorchSW = "deserttreasureSWtorch"
        const val timerIdentifierTorchNW = "deserttreasureNWtorch"
    }

    override fun defineListeners() {
        on(Scenery.BURNT_CHEST_6420, SCENERY, "open") { player, node ->
            if (DTUtils.checkAllTorchesLit(player)) {
                if (DTUtils.getSubStage(player, DesertTreasure.smokeStage) == 0) {
                    DTUtils.setSubStage(player, DesertTreasure.smokeStage, 1)
                }
            }
            if (DTUtils.getSubStage(player, DesertTreasure.smokeStage) >= 1) {
                replaceScenery(node as core.game.node.scenery.Scenery, 6421, 2)
                addItemOrDrop(player, Items.WARM_KEY_4656)
                sendMessage(player, "You open the chest and take a key.")
            } else {
                sendDialogueLines(
                    player,
                    "There seems to be no way to open this chest. Engraved where the",
                    "keyhole should be is a message:",
                    "'Light the path, and find the key...'",
                )
            }
            return@on true
        }

        on(intArrayOf(Scenery.GATE_6451, Scenery.GATE_6452), SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.DESERT_TREASURE) == 9) {
                if (!getAttribute(player, DesertTreasure.attributeUnlockedGate, false) &&
                    inInventory(
                        player,
                        Items.WARM_KEY_4656,
                    )
                ) {
                    if (removeItem(player, Items.WARM_KEY_4656)) {
                        sendMessage(player, "You unlock the gate and enter the room.")
                        setAttribute(player, DesertTreasure.attributeUnlockedGate, true)
                    }
                }

                if (getAttribute(player, DesertTreasure.attributeUnlockedGate, false)) {
                    if (DTUtils.getSubStage(player, DesertTreasure.smokeStage) == 1 &&
                        getAttribute<NPC?>(
                            player,
                            DesertTreasure.attributeFareedInstance,
                            null,
                        ) == null
                    ) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        val npc =
                            core.game.node.entity.npc.NPC
                                .create(NPCs.FAREED_1977, Location(3315, 9376, 0))
                        setAttribute(player, DesertTreasure.attributeFareedInstance, npc)
                        setAttribute(npc, "target", player)
                        npc.isRespawn = false
                        npc.init()
                        npc.attack(player)
                        sendChat(npc, "You dare trespass in my realm?")
                    } else if (DTUtils.getSubStage(player, DesertTreasure.smokeStage) >= 100) {
                        if (!inInventory(player, Items.SMOKE_DIAMOND_4672) &&
                            !inBank(
                                player,
                                Items.SMOKE_DIAMOND_4672,
                            )
                        ) {
                            sendMessage(
                                player,
                                "The Diamond of Smoke seems to have mystically found its way back here...",
                            )
                            GroundItemManager.create(Item(Items.SMOKE_DIAMOND_4672), Location(3315, 9376, 0), player)
                        }
                    }
                } else {
                    sendMessage(player, "The gate is locked.")
                }
            } else if (getQuestStage(player, Quests.DESERT_TREASURE) in 9..100) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The gate is locked.")
            }
            return@on true
        }

        on(Scenery.A_DARK_HOLE_31367, SCENERY, "enter") { player, _ ->
            sendDialogueLines(
                player,
                "This hole leads to a maze of other passages. Without knowing where",
                "you are headed, there's no chance of reaching anywhere interesting.",
            )
            return@on true
        }

        onUseWith(
            IntType.SCENERY,
            Items.TINDERBOX_590,
            Scenery.STANDING_TORCH_6406,
            Scenery.STANDING_TORCH_6413,
        ) { player, _, _ ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchNorthEast, 1)
            sendMessage(player, "You light the torch.")
            if (DTUtils.checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchNE)
            registerTimer(
                player,
                StandingTorchTimer(timerIdentifierTorchNE, DesertTreasure.varbitStandingTorchNorthEast),
            )
            return@onUseWith true
        }

        onUseWith(
            IntType.SCENERY,
            Items.TINDERBOX_590,
            Scenery.STANDING_TORCH_6408,
            Scenery.STANDING_TORCH_6414,
        ) { player, _, _ ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchSouthEast, 1)
            sendMessage(player, "You light the torch.")
            if (DTUtils.checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchSE)
            registerTimer(
                player,
                StandingTorchTimer(timerIdentifierTorchSE, DesertTreasure.varbitStandingTorchSouthEast),
            )
            return@onUseWith true
        }

        onUseWith(
            IntType.SCENERY,
            Items.TINDERBOX_590,
            Scenery.STANDING_TORCH_6410,
            Scenery.STANDING_TORCH_6415,
        ) { player, _, _ ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchSouthWest, 1)
            sendMessage(player, "You light the torch.")
            if (DTUtils.checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchSW)
            registerTimer(
                player,
                StandingTorchTimer(timerIdentifierTorchSW, DesertTreasure.varbitStandingTorchSouthWest),
            )
            return@onUseWith true
        }

        onUseWith(
            IntType.SCENERY,
            Items.TINDERBOX_590,
            Scenery.STANDING_TORCH_6412,
            Scenery.STANDING_TORCH_6416,
        ) { player, _, _ ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchNorthWest, 1)
            sendMessage(player, "You light the torch.")
            if (DTUtils.checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchNW)
            registerTimer(
                player,
                StandingTorchTimer(timerIdentifierTorchNW, DesertTreasure.varbitStandingTorchNorthWest),
            )
            return@onUseWith true
        }
    }
}

class StandingTorchTimer(
    private val timerIdentifier: String = "deserttreasureunknowntimer",
    private val torchVarbit: Int = 0,
) : RSTimer(secondsToTicks(150), timerIdentifier) {
    override fun run(entity: Entity): Boolean {
        if (entity is Player) {
            when (timerIdentifier) {
                SmokeDiamond.timerIdentifierTorchNE ->
                    sendMessage(
                        entity,
                        "The North-east torch burns out...",
                    )

                SmokeDiamond.timerIdentifierTorchSE ->
                    sendMessage(
                        entity,
                        "The South-east torch burns out...",
                    )

                SmokeDiamond.timerIdentifierTorchSW ->
                    sendMessage(
                        entity,
                        "The South-west torch burns out...",
                    )

                SmokeDiamond.timerIdentifierTorchNW ->
                    sendMessage(
                        entity,
                        "The North-west torch burns out...",
                    )

                else -> sendMessage(entity, "The torch burns out...")
            }
            setVarbit(entity, torchVarbit, 0)
        }
        entity.timers.removeTimer(this)
        return true
    }
}
