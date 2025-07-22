package content.region.desert.bedabin.quest.deserttreasure.plugin

import content.region.desert.bedabin.quest.deserttreasure.DTUtils
import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class IceDiamondPlugin : InteractionListener {
    override fun defineListeners() {
        onUseWith(
            IntType.NPC,
            Items.CHOCOLATE_CAKE_1897,
            NPCs.BANDIT_1932,
        ) { player, used, with ->
            if (removeItem(player, used)) {
                if (DTUtils.getSubStage(player, DesertTreasure.iceStage) == 0) {
                    DTUtils.setSubStage(player, DesertTreasure.iceStage, 1)
                }
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            when (stage) {
                                0 ->
                                    player(
                                        "Hey there little troll...",
                                        "Take this and dry those tears...",
                                    ).also { stage++ }

                                1 ->
                                    npc(FaceAnim.OLD_NEARLY_CRYING, "(sniff)").also {
                                        stage = END_DIALOGUE
                                    }
                            }
                        }
                    },
                    with as NPC,
                )
            }
            return@onUseWith true
        }

        on(intArrayOf(Scenery.ICE_GATE_5043, Scenery.ICE_GATE_5044), SCENERY, "go-through") { player, _ ->
            if ((
                    getQuestStage(player, Quests.DESERT_TREASURE) == 9 &&
                        DTUtils.getSubStage(player, DesertTreasure.iceStage) > 1
                ) ||
                getQuestStage(player, Quests.DESERT_TREASURE) >= 10
            ) {
                sendMessage(player, "You squeeze through the large icy bars of the gate.")

                if (player.location.x > 2838) {
                    teleport(player, Location(2837, 3739, 0))
                } else {
                    teleport(player, Location(2839, 3739, 0))
                }
            } else {
                sendDialogueLines(
                    player,
                    "The bars are frozen tightly shut and a sturdy layer of ice prevents",
                    "you from slipping through.",
                )
            }
            return@on true
        }

        on(Scenery.CAVE_ENTRANCE_6441, SCENERY, "enter") { player, _ ->
            lock(player, 3)
            animate(player, 2796)
            queueScript(player, 3, QueueStrength.SOFT) {
                teleport(player, Location(2874, 3720, 0))
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Scenery.CAVE_ENTRANCE_6446, SCENERY, "enter") { player, _ ->
            sendMessage(player, "The entrance to the cave is covered in too much ice to get through.")
            return@on true
        }

        on(Scenery.CAVE_EXIT_6447, SCENERY, "enter") { player, _ ->
            lock(player, 3)
            animate(player, 2796)
            queueScript(player, 3, QueueStrength.SOFT) {
                teleport(player, Location(2867, 3719, 0))
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Scenery.ICE_LEDGE_6455, SCENERY, "use") { player, _ ->

            if ((
                    getQuestStage(player, Quests.DESERT_TREASURE) == 9 &&
                        DTUtils.getSubStage(player, DesertTreasure.iceStage) >= 3
                ) ||
                getQuestStage(player, Quests.DESERT_TREASURE) >= 10
            ) {
                if (inEquipment(player, Items.SPIKED_BOOTS_3107)) {
                    teleport(player, Location(2838, 3803, 1))
                } else {
                    sendPlayerDialogue(
                        player,
                        "I don't think I'll make much headway along that icy slope without some spiked boots...",
                    )
                }
            } else {
                sendMessage(player, "You have not defeated Kamil yet.")
            }
            return@on true
        }

        on(intArrayOf(Scenery.ICE_GATE_6461, Scenery.ICE_GATE_6462), SCENERY, "go-through") { player, _ ->

            teleport(player, Location(2852, 3810, 2))
            return@on true
        }

        on(NPCs.ICE_BLOCK_1944, NPC, "talk-to") { player, _ ->
            sendDialogueLines(
                player,
                "There is a thick layer of ice covering this troll.",
                "You will have to find some way of shattering it.",
            )
            return@on true
        }
        on(NPCs.ICE_BLOCK_1944, NPC, "smash-ice") { player, node ->
            player.attack(node)
            return@on true
        }

        on(NPCs.ICE_BLOCK_1946, NPC, "talk-to") { player, _ ->
            sendDialogueLines(
                player,
                "There is a thick layer of ice covering this troll.",
                "You will have to find some way of shattering it.",
            )
            return@on true
        }
        on(NPCs.ICE_BLOCK_1946, NPC, "smash-ice") { player, node ->
            player.attack(node)
            return@on true
        }
    }
}

class ComicalTrippingIce : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(
            ZoneBorders(2815, 3775, 2880, 3839, 1),
            ZoneBorders(2815, 3775, 2880, 3839, 2),
        )

    override fun entityStep(
        entity: Entity,
        location: Location,
        lastLocation: Location,
    ) {
        if (entity is Player) {
            if ((1..10).random() == 1) {
                lock(entity, 2)
                stopWalk(entity)
                animate(entity, 767)
            }
        }
    }
}

class IceAttack : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2850, 3750, 2880, 3770))

    override fun areaEnter(entity: Entity) {
        if (entity is Player &&
            getQuestStage(entity, Quests.DESERT_TREASURE) == 9 &&
            DTUtils.getSubStage(entity, DesertTreasure.iceStage) == 2 &&
            getAttribute<NPC?>(entity, DesertTreasure.attributeKamilInstance, null) == null
        ) {
            sendMessage(entity, "You can feel an evil presence nearby...")
            val npc = NPC.create(NPCs.KAMIL_1913, Location(2857, 3754, 0))
            setAttribute(entity, DesertTreasure.attributeKamilInstance, npc)
            setAttribute(npc, "target", entity)
            npc.isRespawn = false
            npc.init()
            npc.attack(entity)
        }
    }
}
