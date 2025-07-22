package content.region.asgarnia.falador.quest.fortress.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.world.map.Location
import org.rs.consts.*

class BlackKnightsFortressPlugin : InteractionListener {

    val LARGE_DOOR_ID = intArrayOf(Scenery.LARGE_DOOR_74, Scenery.LARGE_DOOR_73)
    val CABBAGE_ID = intArrayOf(Items.CABBAGE_1965, Items.CABBAGE_1967)

    override fun defineListeners() {
        on(LARGE_DOOR_ID, IntType.SCENERY, "open") { player, node ->
            if (player.location.x == 3008) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "You can't open this door.")
            }
            return@on true
        }

        on(Scenery.STURDY_DOOR_2337, IntType.SCENERY, "open") { player, node ->
            when (player.location.y) {
                3514 -> {
                    if (allInEquipment(player, Items.BRONZE_MED_HELM_1139, Items.IRON_CHAINBODY_1101)) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    } else {
                        player.dialogueInterpreter.open(4605, findNPC(4604), true)
                    }
                }

                3515 -> DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }

        on(Scenery.DOOR_2338, IntType.SCENERY, "open") { player, node ->
            if (player.location.x > 3019) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                player.dialogueInterpreter.open(4605, findNPC(4605), true, true)
            }
            return@on true
        }


        on(Scenery.WALL_2341, IntType.SCENERY, "push") { player, node ->
            sendMessage(player, "You push against the wall. You find a secret passage.")
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        onUseWith(IntType.SCENERY, CABBAGE_ID, Scenery.HOLE_2336) { player, used, _ ->
            when {
                getQuestStage(player, Quests.BLACK_KNIGHTS_FORTRESS) < 20 -> {
                    sendPlayerDialogue(player, "Why exactly would I want to do that?", FaceAnim.THINKING)
                }
                used.id == Items.CABBAGE_1967 -> {
                    dialogue(player) {
                        message("This is the wrong sort of cabbage!")
                        player("I'm not supposed to be HELPING the witch you know...")
                    }
                }
                else -> {
                    openDialogue(player, 992752973, true, true)
                }
            }
            return@onUseWith true
        }

        on(2342, IntType.SCENERY, "listen-at") { player, _ ->
            if (getQuestStage(player, Quests.BLACK_KNIGHTS_FORTRESS) < 1) {
                sendPlayerDialogue(player, "Why exactly would I want to do that?")
                return@on true
            }

            lock(player, 10)
            animate(player, Animations.ON_GROUND_LISTENING_TO_DOOR_4195)

            queueScript(player, 1, QueueStrength.SOFT) {
                animate(player, Animations.CROUCH_HIDE_4552)
                if (getQuestStage(player, Quests.BLACK_KNIGHTS_FORTRESS) >= 30) {
                    openDialogue(player, AfterSabotageDialogue())
                } else {
                    openDialogue(player, 992752973)
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Items.DOSSIER_9589, IntType.ITEM, "read") { player, node ->
            if (removeItem(player, node.asItem())) {
                lock(player, 1)
                queueScript(player, 1, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> {
                            visualize(player, Animations.BURN_NOTE_4551, 770)
                            sendDialogueLines(player, "Infiltrate fortress... sabotage secret weapon... self", "destruct in 3...2...ARG!")
                            setAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER, true)
                            return@queueScript delayScript(player, animationCycles(Animations.BURN_NOTE_4551))
                        }

                        1 -> {
                            closeChatBox(player)
                            resetAnimator(player)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.GRILL_2342) { _, _ ->
            return@setDest Location.create(3026, 3507, 0)
        }
    }
}

private class AfterSabotageDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> sendNPCDialogue(player!!, NPCs.WITCH_611, "It's RUINED! All that work, FIVE years down the drain, who was it! I'll find them and when I do....", FaceAnim.ANGRY).also { stage++ }
            1 -> sendNPCDialogue(player!!, NPCs.BLACK_KNIGHT_CAPTAIN_610, "Erm.. maybe we can save some of it?", FaceAnim.THINKING).also { stage++ }
            2 -> sendNPCDialogue(player!!, NPCs.WITCH_611, "SAVE some of it? Are you mad!? That cabbage was a normal cabbage, it spoilt the whole thing and now I have to start all over again!", FaceAnim.ANGRY).also { stage++ }
            3 -> sendNPCDialogue(player!!, NPCs.BLACK_KNIGHT_CAPTAIN_610, "No need to... get upset... you're scaring the cat.", FaceAnim.CALM).also { stage++ }
            4 -> sendNPCDialogue(player!!, NPCs.WITCH_611, "'The CAT'? She's not 'THE CAT'!", FaceAnim.SAD).also { stage++ }
            5 -> sendNPCDialogue(player!!, NPCs.WITCH_611, "She's Maemi... aren't you dear....", FaceAnim.CALM).also { stage++ }
            6 -> sendNPCDialogue(player!!, NPCs.BLACK_CAT_4607, "Purrr....", FaceAnim.CHILD_FRIENDLY).also { stage++ }
            7 -> {
                end()
                animate(player!!, Animations.GET_UP_FROM_LISTENING_TO_DOOR_4197)
            }
        }
    }
}