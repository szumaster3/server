package content.region.kandarin.camelot.quest.arthur.plugin

import content.data.GameAttributes
import content.region.kandarin.camelot.quest.arthur.cutscene.CrateCutscene
import content.region.kandarin.camelot.quest.arthur.dialogue.*
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class MerlinCrystalPlugin : InteractionListener {

    override fun defineListeners() {
        on(intArrayOf(Scenery.LARGE_DOOR_72, Scenery.LARGE_DOOR_71), IntType.SCENERY, "open") { player, node ->
            val door = node.asScenery()
            if (door.location.x in 2763..2764 && door.location.y in 3401..3402) {
                if (player.location.x >= 2764) {
                    DoorActionHandler.handleAutowalkDoor(
                        player,
                        node.asScenery(),
                        Location.create(2763, player.location.y, 0),
                    )
                    return@on true
                }

                sendDialogue(player, "The door is securely locked. You will have to find another way in.")
                return@on true
            }

            DoorActionHandler.handleDoor(player, door)
            return@on true
        }

        on(intArrayOf(Scenery.LARGE_DOOR_72, Scenery.LARGE_DOOR_71), IntType.SCENERY, "knock-at") { player, _ ->
            if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 10) {
                sendDialogue(player, "The door is securely locked.")
                return@on true
            }
            openDialogue(player, DoorRenegadeKnight(), NPCs.RENEGADE_KNIGHT_237)
            return@on true
        }

        on(NPCs.SIR_MORDRED_247, IntType.NPC, "attack") { player, node ->
            if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 30 ||
                getQuestStage(
                    player,
                    Quests.MERLINS_CRYSTAL,
                ) == 40
            ) {
                player.attack(node.asNpc())
            }

            return@on true
        }

        on(NPCs.SIR_MORDRED_247, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, SirMordredDialogueFile(), NPCs.SIR_MORDRED_247)
            return@on true
        }

        on(NPCs.BEGGAR_252, IntType.NPC, "talk-to") { player, _ ->
            if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 40 &&
                player.getAttribute(GameAttributes.ATTR_STATE_TALK_LADY, false) == true &&
                player.getAttribute(GameAttributes.TEMP_ATTR_BEGGAR, null) != null
            ) {
                openDialogue(player, BeggarDialogueFile(null), NPCs.BEGGAR_252)
                return@on true
            }
            sendDialogue(player, "This beggar isn't interested in talking.")
            return@on true
        }

        on(Scenery.CHAOS_ALTAR_61, IntType.SCENERY, "check") { player, _ ->
            if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 40) {
                sendDialogueLines(player,
                    "You find a small inscription at the bottom of the altar. It reads:",
                    "'Snarthon Candtrick Termanto'.",
                )
                setAttribute(player, GameAttributes.ATTR_STATE_ALTAR_FINISH, true)
                return@on true
            }

            sendMessage(player, "An altar of the evil god Zamorak.")
            return@on true
        }

        on(Scenery.DOOR_59, IntType.SCENERY, "open") { player, door ->
            if (player.getAttribute(GameAttributes.ATTR_STATE_CLAIM_EXCALIBUR, false) == false) {
                if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 40 &&
                    player.getAttribute(GameAttributes.ATTR_STATE_TALK_LADY, false) == true
                ) {
                    val talkedToBeggar = player.getAttribute(GameAttributes.ATTR_STATE_TALK_BEGGAR, false)
                    val dialogueFile = BeggarDialogueFile(door.asScenery())

                    if (talkedToBeggar == true) {
                        DoorActionHandler.handleAutowalkDoor(
                            player,
                            door as core.game.node.scenery.Scenery,
                            Location.create(if (player.location.x < 3016) 3016 else 3015, door.location.y, 0),
                        )
                    } else {
                        openDialogue(player, dialogueFile, NPCs.BEGGAR_252)
                    }

                    dialogueFile.initBeggar(player)
                    return@on true
                }
            }

            DoorActionHandler.handleAutowalkDoor(
                player,
                door as core.game.node.scenery.Scenery,
                Location.create(if (player.location.x < 3016) 3016 else 3015, door.location.y, 0),
            )
            return@on true
        }

        on(NPCs.CANDLE_MAKER_562, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, CandleMakerDialogueFile(), NPCs.CANDLE_MAKER_562)
            return@on true
        }

        on(Scenery.CRATE_63, IntType.SCENERY, "hide-in") { player, _ ->
            if (getQuestStage(player, Quests.MERLINS_CRYSTAL) >= 30) {
                openDialogue(player, CrateDialogue())
            } else {
                sendMessage(player, "You have no reason to do that...")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.EXCALIBUR_35, Scenery.GIANT_CRYSTAL_62) { player, _, _ ->
            smashCrystal(player, false)
            return@onUseWith true
        }

        on(Scenery.GIANT_CRYSTAL_62, IntType.SCENERY, "smash") { player, _ ->
            smashCrystal(player, true)
            return@on true
        }

        /*
        on(intArrayOf(Scenery.CRATE_WALL_65,Scenery.CRATE_WALL_66), IntType.SCENERY, "climb-out") { player, node ->
                setTitle(player, 2)
                sendOptions(player, "Would you like to get back out of the crate?", "Yes.", "No.")
                addDialogueAction(player) { _, selected ->
                    when(selected) {
                        2 -> sendDialogue(player, "You climb out of the crate.").also {
                            teleport(player, Location.create(2778, 3401, 0), TeleportManager.TeleportType.INSTANT)
                        }
                        else -> sendDialogue(player, "You decide to stay in the crate.")
                    }
                }
                return@on true
            }
        */

        on(Items.BAT_BONES_530, IntType.ITEM, "drop") { player, node ->
                val merlinStage = getQuestStage(player, Quests.MERLINS_CRYSTAL)
                var doingQuest =
                merlinStage == 40 && player.getAttribute(GameAttributes.ATTR_STATE_ALTAR_FINISH, false) == true

            if (doingQuest) {
                var hasQuestItems =
                    inInventory(player, Items.LIT_BLACK_CANDLE_32) && inInventory(player, Items.EXCALIBUR_35)

                if (hasQuestItems && player.location == Location(2780, 3515, 0)) {
                    forceWalk(player, Location.create(2778, 3515, 0), "smart")

                    queueScript(player, 2, QueueStrength.SOFT) { _ ->
                        openDialogue(player, ThrantaxDialogue())
                        return@queueScript stopExecuting(player)
                    }
                }
            }

            if (player.location.x in 2779..2781 && player.location.y >= 3514 && player.location.y <= 3516) {
                sendMessage(player, "You can sense a powerful presence here...")
                return@on true
            }

            DropListener.drop(player, node.asItem())
            return@on true
        }
    }

    private fun smashCrystal(player: Player, wielding: Boolean) {
        if(isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            sendMessage(player, "You have already freed Merlin from the crystal.")
            return
        }
        if (getQuestStage(player, Quests.MERLINS_CRYSTAL) == 60) {
            sendMessages(
                player,
                "You have already freed Merlin from the crystal.",
                "Go and see King Arthur for your reward.",
            )
            return
        }

        player.lock()
        sendMessage(player, "You attempt to smash the crystal...")

        if (wielding) {
            player.animate(player.properties.attackAnimation)
        }

        val delay = if (wielding) 1 else 0

        queueScript(player, delay, QueueStrength.SOFT) { _ ->
            if (player.equipment.contains(Items.EXCALIBUR_35, 1) &&
                getQuestStage(player, Quests.MERLINS_CRYSTAL) == 50
            ) {
                sendMessage(player, "...and it shatters under the force of Excalibur!")
                openDialogue(player, MerlinDialogueFile(true))
            } else {
                sendMessage(player, "...but your weapon didn't do any damage at all.")
            }

            player.unlock()
            return@queueScript stopExecuting(player)
        }
    }

    inner class CrateDialogue : DialogueFile() {

        init {
            stage = 0
        }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogue(player!!, "The crate is empty. It's just about big enough to hide inside.")
                    stage = 1
                }
                1 -> {
                    setTitle(player!!, 2)
                    sendOptions(player!!, "Would you like to hide inside the crate?", "Yes.", "No.")
                    stage = 2
                }
                2 -> {
                    when(buttonID) {
                        1 -> {
                            player!!.animate(Animation.create(Animations.HUMAN_BURYING_BONES_827))
                            sendDialogue(player!!, "You climb inside the crate and wait.")
                            stage = 3
                        }
                        else -> {
                            sendDialogue(player!!, "You leave the empty crate alone.")
                            stage = 4
                        }
                    }
                }
                3 -> {
                    end().also {
                        CrateCutscene(player!!).start(true)
                    }
                }
                4 -> end()
            }
        }
    }
}
