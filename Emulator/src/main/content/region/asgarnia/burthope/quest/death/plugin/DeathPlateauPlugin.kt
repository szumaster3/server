package content.region.asgarnia.burthope.quest.death.plugin

import content.region.asgarnia.burthope.quest.death.dialogue.DoorPlateauDialogueFile
import core.api.*
import core.api.item.produceGroundItem
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class DeathPlateauPlugin : InteractionListener, MapArea {

    companion object {
        val stoneBalls = intArrayOf(
            Items.STONE_BALL_3109,
            Items.STONE_BALL_3110,
            Items.STONE_BALL_3111,
            Items.STONE_BALL_3112,
            Items.STONE_BALL_3113
        )
        val stoneMechanisms = intArrayOf(
            Scenery.STONE_MECHANISM_3676,
            Scenery.STONE_MECHANISM_3677
        )
        val combinationScroll = arrayOf(
            "",
            "Red is North of Blue. Yellow is South of Purple.",
            "Green is North of Purple. Blue is West of",
            "Yellow. Purple is East of Red.",
            ""
        )
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2866, 3609, 2866, 3609))

    override fun areaEnter(entity: Entity) {
        if (entity is Player && getQuestStage(entity, Quests.DEATH_PLATEAU) == 25) {
            sendPlayerDialogue(
                entity,
                "I think this is far enough, I can see Death Plateau and it looks like the trolls haven't found the path. I'd better go and tell Denulth.",
            )
            sendMessage(entity, "You can see that there are no trolls on the secret path")
            sendMessage(entity, "You should go and speak to Denulth")
            setQuestStage(entity, Quests.DEATH_PLATEAU, 26)
        }
    }

    override fun defineListeners() {
        on(Scenery.DOOR_3747, SCENERY, "open") { player, node ->
            if (player.location.x == 2906 && player.location.y == 3543) {
                openDialogue(player, DoorPlateauDialogueFile(1))
                return@on true
            } else if (player.location.y == 3542) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                DoorActionHandler.handleDoor(player, node.asScenery())
            }
            return@on true
        }

        on(Scenery.DOOR_3745, SCENERY, "open") { player, node ->
            if (node.location == location(2823, 3555, 0)) {
                openDialogue(player, DoorPlateauDialogueFile(2))
            } else if (node.location == location(2820, 3558, 0)) {
                openDialogue(player, DoorPlateauDialogueFile(3))
            }
            return@on true
        }

        on(Items.IOU_3103, ITEM, "read") { player, _ ->
            if(getQuestStage(player, Quests.DEATH_PLATEAU) in 15..16) {
                dialogue(player) {
                    player(FaceAnim.NEUTRAL, "The IOU says that Harold owes me some money.")
                    player(FaceAnim.EXTREMELY_SHOCKED, "Wait just a minute!")
                    player(FaceAnim.EXTREMELY_SHOCKED, "The IOU is written on the back of the combination! The stupid guard had it in his back pocket all the time!")
                    end {
                        if (removeItem(player, Items.IOU_3103)) {
                            addItemOrDrop(player, Items.COMBINATION_3102)
                            setQuestStage(player, Quests.DEATH_PLATEAU, 16)
                            sendItemDialogue(player, Items.COMBINATION_3102, "You have found the combination!")
                            sendMessage(player, "You have found the combination!")
                            runTask(player, 1){openInterface(player, Components.BLANK_SCROLL_222)
                                sendString(player, combinationScroll.joinToString("<br>"), Components.BLANK_SCROLL_222, 4)}
                        }
                    }
                }
            }
            return@on true
        }

        on(Items.COMBINATION_3102, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, combinationScroll.joinToString("<br>"), Components.BLANK_SCROLL_222, 4)
            return@on true
        }

        onUseWith(
            IntType.SCENERY,
            stoneBalls,
            *stoneMechanisms,
        ) { player, used, with ->
            val stoneBall = used.asItem()
            val stoneMechanism = with.asScenery()

            if (removeItem(player, stoneBall)) {
                produceGroundItem(player, stoneBall.id, 1, stoneMechanism.location)
            }

            if (GroundItemManager.get(Items.STONE_BALL_3109, location(2894, 3563, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3110, location(2894, 3562, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3111, location(2895, 3562, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3112, location(2895, 3563, 0), player) != null &&
                GroundItemManager.get(Items.STONE_BALL_3113, location(2895, 3564, 0), player) != null
            ) {
                if (getQuestStage(player, Quests.DEATH_PLATEAU) == 16) {
                    sendMessage(player, "The equipment room door has unlocked.")
                    setQuestStage(player, Quests.DEATH_PLATEAU, 19)
                }
            }
            return@onUseWith true
        }

        on(Scenery.LARGE_DOOR_3743, SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.DEATH_PLATEAU) > 16) {
                DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
    }
}
