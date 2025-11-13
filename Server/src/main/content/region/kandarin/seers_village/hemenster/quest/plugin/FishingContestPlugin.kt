package content.region.kandarin.seers_village.hemenster.quest.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import shared.consts.*

class FishingContestPlugin : InteractionListener {

    companion object {
        private val VINE_SCENERY = intArrayOf(Scenery.VINE_58, Scenery.VINE_2989, Scenery.VINE_2990, Scenery.VINE_2991, Scenery.VINE_2992, Scenery.VINE_2993, Scenery.VINE_2994, Scenery.VINE_2013)
        private val TUNNEL_STAIRS = intArrayOf(Scenery.STAIRS_55, Scenery.STAIRS_57)
        private val GATES = intArrayOf(Scenery.GATE_47, Scenery.GATE_48, Scenery.GATE_52, Scenery.GATE_53)
    }

    override fun defineListeners() {

        /*
         * Handles interaction with tunnel stairs (White Wolf Mountain shortcut).
         */

        on(TUNNEL_STAIRS, IntType.SCENERY, "climb-down") { player, node ->
            if (!isQuestComplete(player, Quests.FISHING_CONTEST)) {
                when (node.id) {
                    Scenery.STAIRS_55 -> {
                        player.dialogueInterpreter.open(NPCs.VESTRI_3679, Repository.findNPC(NPCs.VESTRI_3679))
                    }

                    Scenery.STAIRS_57 -> {
                        player.dialogueInterpreter.open(NPCs.AUSTRI_232, Repository.findNPC(NPCs.AUSTRI_232))
                    }
                }
            } else {
                val destination = when (node.id) {
                    Scenery.STAIRS_55 -> Location(2820, 9882, 0)
                    Scenery.STAIRS_57 -> Location(2876, 9879, 0)
                    else -> return@on true
                }
                destination?.let { teleport(player, it) }
            }
            return@on true
        }

        /*
         * Handles interaction with Hemenster fence and McGrubor's gates.
         * Varbit ID: 2053
         */

        on(GATES, IntType.SCENERY, "open") { player, node ->
            val gatePair = when (node.id) {
                Scenery.GATE_47 -> Pair(Scenery.GATE_47, Scenery.GATE_48)
                Scenery.GATE_48 -> Pair(Scenery.GATE_48, Scenery.GATE_47)
                else -> null
            }

            when (node.id) {
                Scenery.GATE_47, Scenery.GATE_48 -> {
                    if (player.location.x < 2643 && getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false)) {
                        openDialogue(player, FishingCompetitionExitDialogue(node, gatePair))
                        return@on true
                    }

                    val shownPass = getVarbit(player, Vars.VARBIT_FISHING_CONTEST_PASS_SHOWN_2053)
                    when {
                        shownPass == 0 && player.location.x > 2642 -> {
                            if (inInventory(player, Items.FISHING_PASS_27)) {
                                sendMessage(player, "You should give your pass to Morris.")
                            } else {
                                sendMessage(player, "You need a fishing pass to fish here.")
                            }
                        }
                        !inInventory(player, Items.FISHING_ROD_307) -> {
                            sendDialogueLines(player, "I should probably get a rod from", "Grandpa Jack before starting.")
                        }
                        else -> gatePair?.let {
                            DoorActionHandler.autowalkFence(player, node.asScenery(), it.first, it.second)
                        }
                    }
                }

                Scenery.GATE_52, Scenery.GATE_53 -> {
                    if (inBorders(player, 2647, 3468, 2652, 3469)) {
                        face(findNPC(NPCs.FORESTER_231)!!, player, 3)
                        sendNPCDialogue(player, NPCs.FORESTER_231, "Hey! You can't come through here! This is private land!", FaceAnim.ANGRY)
                        sendMessage(player, "There might be a gap in the fence somewhere where he wouldn't see you sneak in.")
                        sendMessage(player, "You should look around.")
                    } else {
                        sendDialogue(player, "This gate is locked.")
                    }
                }
            }
            return@on true
        }

        /*
         * Handles interaction with vines in McGrubor's Wood.
         */

        on(VINE_SCENERY, IntType.SCENERY, "check") { player, _ ->
            if (isQuestInProgress(player, Quests.FISHING_CONTEST, 1, 99)) {
                if (!inInventory(player, Items.SPADE_952, 1)) {
                    return@on true
                }

                queueScript(player, 1, QueueStrength.WEAK) {
                    sendMessage(player, "You dig in amongst the vines.")
                    animate(player, Animation(Animations.DIG_SPADE_830))
                    sendMessage(player, "You find a red vine worm.")
                    addItem(player, Items.RED_VINE_WORM_25, 1, Container.INVENTORY)
                    return@queueScript stopExecuting(player)
                }
            }
            return@on false
        }

        /*
         * Handles Bonzo NPC - pay option interaction.
         */

        on(NPCs.BONZO_225, IntType.NPC, "pay") { player, _ ->
            player.dialogueInterpreter.open(NPCs.BONZO_225, Repository.findNPC(NPCs.BONZO_225))
            return@on true
        }
    }

    /**
     * Represents dialogue for leaving the fishing competition through the gate.
     */
    inner class FishingCompetitionExitDialogue(
        private val node: Node,
        private val gatePair: Pair<Int, Int>?
    ) : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendNPCDialogue(player!!, NPCs.BONZO_225, "So you're calling it quits here for now?")
                    stage = 1
                }
                1 -> {
                    options("Yes I'll compete again another day.", "Actually I'll go back and catch some more.")
                    stage = 2
                }
                2 -> {
                    when(buttonID) {
                        1 -> {
                            removeAttribute(player!!, GameAttributes.QUEST_FISHINGCOMPO_CONTEST)
                            sendPlayerDialogue(player!!, "Yes I'll compete again another day.")
                            runTask(player!!, 3) {
                                gatePair?.let { DoorActionHandler.autowalkFence(player!!, node.asScenery(), it.first, it.second) }
                                sendNPCDialogue(player!!, NPCs.BONZO_225, "Ok, I'll see you again.")
                                end()
                            }
                        }
                        2 -> { // "Go back and catch more"
                            sendPlayerDialogue(player!!, "Actually I'll go back and catch some more.")
                            runTask(player!!, 3) {
                                sendNPCDialogue(player!!, NPCs.BONZO_225, "Good luck!")
                                end()
                            }
                        }
                    }
                }
            }
        }
    }
}